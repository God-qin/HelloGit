package configs

import java.util
import java.util.HashMap

import com.DataAccess
import model.GroupModel
import org.slf4j.{Logger, LoggerFactory}
import report.{ColumnCategory, StreamingReportColumnModel, StreamingReportTableModel}

import scala.collection.mutable.ArrayBuffer

object GroupModelbuilder extends Serializable{
    val LOGGER: Logger = LoggerFactory.getLogger(GroupModelbuilder.getClass.getName)
    def builder(groupId:Int): GroupModel = {
        try {
            val dataAccess = new DataAccess(null, "dataSource-mysql-report")
            LOGGER.info("query ####################################################")
            val tableSql = "select * from web_streamingreporttable where status = 1 and task_group = " + groupId
            LOGGER.info("tableSql:"+tableSql)
            val columnSql = "select * from web_streamingreportcolumn where status = 1 order by table_id asc,position asc"
            val tableList = dataAccess.getList(classOf[StreamingReportTableModel], tableSql)
            val columnList = dataAccess.getList(classOf[StreamingReportColumnModel], columnSql)
            //            LOGGER.info("tableList:" + JSON.toJSONString(tableList))
            //            LOGGER.info("columnList:" + JSON.toJSONString(columnList))
            //all dimension+quota
            val dimension = new util.LinkedHashMap[String, String]
            val quota = new util.LinkedHashMap[String, String]
            //table map dimension+quota
            val tableDimension = new util.HashMap[Integer, util.HashMap[String, util.LinkedHashMap[String, String]]]
            val tableQuota = new util.HashMap[Integer, util.HashMap[String, util.LinkedHashMap[String, String]]]
            //tableIdMap
            val tableIdMap = new util.HashMap[Integer, String]
            //sourceTableMap
            val table2TopicsMap = new util.HashMap[Integer, Array[String]]
            import scala.collection.JavaConversions._
            for (table <- tableList) {
                if (!tableIdMap.containsKey(table.getId))
                    tableIdMap.put(table.getId, table.getTable_name)
                
                val array = table.getTopics.split(",")
                table2TopicsMap.put(table.getId, array)
                
            }
            for (column <- columnList) {
                if (tableIdMap.containsKey(column.getTable_id)){
                    if (!tableDimension.containsKey(column.getTable_id)) {
                        val tmpDimension = new util.HashMap[String, util.LinkedHashMap[String, String]]
                        tableDimension.put(column.getTable_id, tmpDimension)
                    }
                    
                    if (!tableQuota.containsKey(column.getTable_id)) {
                        val tmpQuota = new util.HashMap[String, util.LinkedHashMap[String, String]]
                        tableQuota.put(column.getTable_id, tmpQuota)
                    }
                    
                    if (column.getColumn_category == ColumnCategory.DIMENSION.getValue) {
                        val topic2dimMap = new util.HashMap[String, String]()
                        val topics = table2TopicsMap.get(column.getTable_id)
                        if(column.getMap_column_name.contains(";")){
                            val topic2dim = column.getMap_column_name.trim.split(";")
                            topic2dim.map(t => {
                                val topic = t.trim.split("\\.")(0)
                                val dim = t.trim.split("\\.")(1)
                                topic2dimMap.put(topic, dim)
                            })
                        }else{
                            topics.map(x => {
                                topic2dimMap.put(x, column.getMap_column_name.trim)
                            })
                        }
                        
                        topics.map(topic => {
                            val dim = topic2dimMap.get(topic)
                            if(tableDimension.get(column.getTable_id).containsKey(topic)){
                                tableDimension.get(column.getTable_id).get(topic).put(column.getColumn_name.trim, dim)
                            }else{
                                val tmpDimension_ = new util.LinkedHashMap[String, String]
                                tmpDimension_.put(column.getColumn_name.trim, dim)
                                tableDimension.get(column.getTable_id).put(topic, tmpDimension_)
                            }
                        })
                        
                        dimension.put(column.getColumn_name.trim, column.getMap_column_name.trim)
                    }else if (column.getColumn_category == ColumnCategory.QUOTA.getValue) {
                        quota.put(column.getColumn_name.trim, column.getMap_column_name.trim)
                        val topic2quotaMap = new util.HashMap[String, String]()
                        if(column.getMap_column_name.contains(".")){
                            val topic2quota = column.getMap_column_name.trim.split("\\.")
                            topic2quotaMap.put(topic2quota(0), topic2quota(1))
                            
                            if(tableQuota.get(column.getTable_id).containsKey(topic2quota(0))){
                                tableQuota.get(column.getTable_id).get(topic2quota(0)).put(column.getColumn_name.trim, topic2quota(1))
                            }else{
                                val tmpQuota_ = new util.LinkedHashMap[String, String]
                                tmpQuota_.put(column.getColumn_name.trim, topic2quota(1))
                                tableQuota.get(column.getTable_id).put(topic2quota(0), tmpQuota_)
                            }
                        }
                        
                    }else if (column.getColumn_category == ColumnCategory.MAP_DIMENSION.getValue) {
                        //                        val hiveColumn = column.getMap_column_name.split("\\|")(0)
                        //                        dimension.put(column.getColumn_name.trim, hiveColumn.trim)
                        val topic2dimMap = new util.HashMap[String, String]()
                        val topics = table2TopicsMap.get(column.getTable_id)
                        if(column.getMap_column_name.contains(";")){
                            val topic2dim = column.getMap_column_name.trim.split(";")
                            topic2dim.map(t => {
                                val g = t.split("\\|")(0)
                                val topic = g.trim.split("\\.")(0)
                                val dim = g.trim.split("\\.")(1)
                                topic2dimMap.put(topic, dim)
                            })
                        }else{
                            topics.map(x => {
                                topic2dimMap.put(x, column.getMap_column_name.trim)
                            })
                        }
                        topics.map(topic => {
                            val dim = topic2dimMap.get(topic)
                            if(tableDimension.get(column.getTable_id).containsKey(topic)){
                                tableDimension.get(column.getTable_id).get(topic).put(column.getColumn_name.trim, dim)
                            }else{
                                val tmpDimension_ = new util.LinkedHashMap[String, String]
                                tmpDimension_.put(column.getColumn_name.trim, dim)
                                tableDimension.get(column.getTable_id).put(topic, tmpDimension_)
                            }
                        })
                    }
                }
            }
            
            val sourceTopicFields = new HashMap[String,ArrayBuffer[String]]
            table2TopicsMap.values().map(x =>{
                x.map(t => {
                    if(!sourceTopicFields.containsKey(t)){
                        val array = new ArrayBuffer[String]()
                        sourceTopicFields.put(t, array)
                    }
                })
            })
            
            val fieldsSql = "select t.file_name_prefix, f.field_name from data_table_field f, data_table t where t.id = f.table_id and t.status = 1 order by f.table_id, f.position"
            //val dataAccess_ = new DataAccess(null, "dataSource-mysql-ops")
            //val rows = dataAccess_.getNativeRowSet(fieldsSql)
            
            //while (rows.next()){
            //    val table = rows.getString(1)
            //    if(sourceTopicFields.contains(table)){
            //        sourceTopicFields.get(table) += rows.getString(2)
            //    }
            //}
            
            val sourceTopics = sourceTopicFields.keySet().toArray().map(_.toString)
            
            val model = new GroupModel(dimension, quota, tableDimension, tableQuota, tableIdMap, sourceTopics, null, sourceTopicFields)
            
            /*LOGGER.info("dimension: " + JSON.toJSONString(dimension))
            LOGGER.info("quota: " + JSON.toJSONString(quota))
            LOGGER.info("tableDimension: " + JSON.toJSONString(tableDimension))
            LOGGER.info("tableQuota: " + JSON.toJSONString(tableQuota))
            LOGGER.info("tableIdMap: " + JSON.toJSONString(tableIdMap))
            LOGGER.info("sourceTableName: " + JSON.toJSONString(sourceTopics))*/
            
            model
            
        } catch {
            case e: Exception =>
                e.printStackTrace()
                LOGGER.error(e.toString)
                throw e
        }
    }
}
