package configs

import java.util
import java.util.LinkedHashMap
import java.util.logging.Logger

import model.GroupModel
import org.slf4j.{Logger, LoggerFactory}
import java.util
import java.util.LinkedHashMap

import com.DBUtil
import com.youlan.stream.report.realtime.configs.Start
import model.{GroupModel, SqlConstant}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StringType, StructField}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.mutable.ArrayBuffer


class QueryEngine extends Serializable{
//    val LOGGER: Logger = LoggerFactory.getLogger(Start.getClass.getName)
    var groupModel: GroupModel = null
    var javaDBUtil: DBUtil = null
    
    def this(groupModel: GroupModel = null){
        this()
        this.groupModel = groupModel
    }
    
    def work(topic2Record: RDD[(String, Array[String])], inder: Int): Unit ={
        val sparkSession = SparkSession
            .builder()
            .appName("realtime_report_query_engine")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            //.config("spark.hadoop.fs.oss.impl", "com.aliyun.fs.oss.nat.NativeOssFileSystem")
            .config("spark.sql.crossJoin.enabled", "true")
            .config("spark.sql.warehouse.dir", "file:///")
            .master("local")
            .getOrCreate
    
        UdfRegister.registerFunction(sparkSession)
    
        val topic2df = groupModel.sourceTopics.map(topic => {
            val structFields = new util.ArrayList[StructField]()
            val sourceTopicFields = groupModel.sourceTopicFields.get(topic)
            for(field <- sourceTopicFields){
                structFields.add(DataTypes.createStructField(field.toString, StringType, true))
            }
            val schema = DataTypes.createStructType(structFields)
            val rowRdd = topic2Record.filter(x => x._1.equals(topic))
                .filter(x => x._2.length >= schema.size)
                .map(x => {
                    Row(x._2: _*)
                })
        
            //LOGGER.info("rowRdd.count: " + rowRdd.count())
        
            val df = sparkSession.createDataFrame(rowRdd, schema)
            df.registerTempTable(topic)
            (topic, df, df.count())
        })
    
        
        for (item <- groupModel.tableIdMap.asScala.mapValues(value => value)) {
            val tableId = item._1
            val tableName = item._2
            val topic2Dimension = groupModel.tableDimension.get(tableId)
            val topic2Quota = groupModel.tableQuota.get(tableId)
        
            groupModel.sourceTopics.foreach(topic => {
                val col2dim = topic2Dimension.get(topic)
                val df = topic2df.filter(x => x._1.equals(topic))
                val count = df(0)._3
                val dims = topic2Dimension.get(topic)
                val quos = topic2Quota.get(topic)
//                LOGGER.info(topic + " : dims => " + dims)
//                LOGGER.info(quos + " : quos => " + quos)

                if(count > 0){
                    val sql  = buiderSql(topic, dims, quos)
                    val result = sparkSession.sql(sql)
                    val dims_ = groupModel.dimension.asScala.mapValues(x => x).map(_._1).toArray
                    val quos_ = topic2Quota.get(topic).asScala.mapValues(x => x).map(_._1).toArray
                    val fields = dims_ ++ quos_
                    val updateFields = quos_
                    result.foreachPartition(iter => {
                        if(null == this.javaDBUtil){
                            this.javaDBUtil = new DBUtil("dataSource-mysql-report")
                        }
                        iter.foreach(f => {
                            val values = new ArrayBuffer[String]()
                            for(i <- 0 to fields.length-1){
                                if(null != f.get(i)){
                                    values += f.get(i) + ""
                                }else{
                                    values += "0"
                                }
                            }

                            try {
                                javaDBUtil.insertOrUpdateAdd(tableName, fields, values.toArray, updateFields)
                            }
                            catch {

                                case e: Exception => {

                                    println("javaDBUtil Error");
                                }
                            }
                        })
                    })
                }
                
                
            })
        
        }
    }
    
    
    def DataFrameJoin(leftDf: DataFrame, rightDf: DataFrame, array: Array[String]): DataFrame ={
        val joinDf = leftDf.join(rightDf,array.toSeq, "full")
        joinDf
    }
    
    def buiderSql(topic: String, dims: LinkedHashMap[String,String], quos: LinkedHashMap[String,String]): String = {
        
        var selectBuilder: StringBuilder = new StringBuilder();
        var groupByBuilder: StringBuilder = new StringBuilder();
        var explodeView = ""
        
        val dimension = dims.asScala.mapValues(value => value)
        val quota = quos.asScala.mapValues(value => value)
//
//        LOGGER.info("dimension: " + dimension)
//        LOGGER.info("quota: " + quota)
        for (item <- dimension) {
            if(item._2.contains("explode(")){
                val view_alias = item._2.replace("(","_").replace(")","")
                explodeView = " LATERAL VIEW OUTER "+item._2+" explode_view AS " + view_alias + " "
                selectBuilder.append("%s as %s,".format(view_alias, item._1))
                groupByBuilder.append("%s,".format(view_alias))
            }else{
                selectBuilder.append("%s as %s,".format(item._2, item._1))
                if(!item._2.equals("0")){
                    groupByBuilder.append("%s,".format(item._2))
                }
                
            }
        }
        
        for (item <- quota) {
            selectBuilder.append("%s as %s,".format(item._2, item._1))
        }
        
//        LOGGER.info("selectBuilder: " + selectBuilder.toString())
//        LOGGER.info("groupByBuilder: " + groupByBuilder.toString())
//
        selectBuilder = new StringBuilder(selectBuilder.substring(0, selectBuilder.lastIndexOf(SqlConstant.COMMA.trim)))
        groupByBuilder = new StringBuilder(groupByBuilder.substring(0, groupByBuilder.lastIndexOf(SqlConstant.COMMA.trim)))
        
        val sqlBuilder = new StringBuilder()
        sqlBuilder.append(SqlConstant.SELECT)
        sqlBuilder.append(selectBuilder.toString())
        sqlBuilder.append(SqlConstant.FROM)
        sqlBuilder.append(topic)
        if(!explodeView.equals("")){
            sqlBuilder.append(explodeView)
        }
        sqlBuilder.append(SqlConstant.WHERE)
        sqlBuilder.append("1 = 1")

        if(topic.equals("dsp_adaptor_report_log")) {
            sqlBuilder.append(SqlConstant.AND);
            sqlBuilder.append("isoutsidetracking = 1");
            sqlBuilder.append(SqlConstant.AND)
            sqlBuilder.append("shieldtypes = 0")
        }

        sqlBuilder.append(SqlConstant.GROUP_BY)
        sqlBuilder.append(groupByBuilder.toString())
        
        val sql = sqlBuilder.toString()
        
//        LOGGER.info("sourceSql: " + sql)
        sql
    }
}
