package com.youlan.stream.report.realtime.configs

import java.util

import com.HDFSFileUtil
import configs.{GroupModelbuilder, QueryEngine}
import model.GroupModel
import org.apache.hadoop.conf.Configuration
import org.apache.kafka.clients.consumer.{OffsetAndMetadata, OffsetCommitCallback}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}
//import utils.oss.OSSUtil

import scala.collection.mutable

object Start {
    val LOGGER: Logger = LoggerFactory.getLogger(Start.getClass.getName)

    def main(args: Array[String]): Unit = {

        LOGGER.info("into spark streaming!")

        //val Array(groupid, servers, offsetsConfig, checkpointDir,reportGroupId) = args
        val Array(offsetsConfig, checkpointDir,reportGroupId) = args


        //LOGGER.info("groupid: " + groupid)
        //LOGGER.info("servers: " + servers)
        LOGGER.info("offsetsConfig: " + offsetsConfig)
        LOGGER.info("checkpointDir: " + checkpointDir)//"datas/cp"
        val reportGroupIdInt = Integer.parseInt(reportGroupId);
        LOGGER.info("reportGroupId: " + reportGroupId)

        //val hadoopConf = new Configuration()
        //HDFSFileUtil.getConfiguration
        //hadoopConf.set("fs.defaultFS", "hdfs://192.168.1.181:8020")
        //hadoopConf.set("fs.defaultFS", "hdfs://node1.itcast.cn:8020")
        //                hadoopConf.set("fs.oss.sdk.dependency.path", "/path/to/lib/dependency.jar")
        //hadoopConf.set("fs.oss.impl", "com.aliyun.fs.oss.nat.NativeOssFileSystem")

        //val cp = OSSUtil.OSS.combinPath + checkpointDir

        val checkPointDir = "/datas/cp"
        val cp = checkPointDir

        val ssc = StreamingContext.getOrCreate(cp,
            () => createContext(offsetsConfig, checkpointDir, GroupModelbuilder.builder(reportGroupIdInt)))

        ssc.start()
        ssc.awaitTermination()
    }

   // def createContext(groupid: String, servers: String, offsetsConfig: String, checkpointDir: String, groupModel: GroupModel): StreamingContext ={
    def createContext( offsetsConfig: String, checkpointDir: String, groupModel: GroupModel): StreamingContext ={
        val sparkConf = new SparkConf()
          .setAppName("realtime_report_for_configs")
          .setMaster("local[4]")
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//        sparkConf.set("spark.hadoop.fs.oss.impl", "com.aliyun.fs.oss.nat.NativeOssFileSystem")
//        sparkConf.set("spark.hadoop.job.runlocal", "true")
        sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "2000")
        sparkConf.set("spark.streaming.backpressure.enabled", "true")
        sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true")
        sparkConf.set("spark.streaming.kafka.consumer.poll.ms", "9000")
        sparkConf.set("spark.streaming.kafka.consumer.cache.enabled", "false")


        val ssc = new StreamingContext(sparkConf, Seconds(5))
        ssc.checkpoint(checkpointDir)

        /** KAFKA */
        LOGGER.info("kafka connectiom config!")
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "node1.itcast.cn:9092",
            "key.deserializer" -> classOf[LongDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "tz_001",
            "auto.offset.reset" -> "latest",
            //            "auto.offset.reset" -> "earliest",
            "max.partition.fetch.bytes" -> "3145728",
            "max.poll.records" -> "1000",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        //val topicSet = groupModel.sourceTopics
        val topicSet = Array{"source_classification_logs"}
        LOGGER.info("start streming")
        LOGGER.info(offsetsConfig)
        val offsets: mutable.Map[TopicPartition, Long] = mutable.Map()
        if (!offsetsConfig.equals("Nil")) {
            val offsetsArr = offsetsConfig.split(",", -1)
            offsetsArr.map(x => {
                val p2o = x.split(":", -1)
                val topicPartition: TopicPartition = new TopicPartition(p2o(0), p2o(1).toInt)
                offsets.put(topicPartition, p2o(2).toLong)
            })
        }

        var cs: ConsumerStrategy[Long, String] = null
        if (offsets.size > 0) {
            LOGGER.info("offsets info:")
            offsets.map(x => LOGGER.info("partition index: " + x._1.partition() + " offset: " + x._2))
            cs = ConsumerStrategies.Subscribe[Long, String](topicSet, kafkaParams, offsets)
        } else {
            cs = ConsumerStrategies.Subscribe[Long, String](topicSet, kafkaParams)
        }

        val stream = KafkaUtils.createDirectStream[Long, String](
            ssc,
            LocationStrategies.PreferConsistent,
            cs
        )

        val queryEngine = new QueryEngine(groupModel)

        stream.foreachRDD(rdd => {
            val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
            for(or <- offsetRanges){
                LOGGER.info(or.topic + " : " + or.partition + " : " + or.fromOffset + " -> " + or.untilOffset)
                println(or.topic + " : " + or.partition + " : " + or.fromOffset + " -> " + or.untilOffset)
            }
            val topic2Record = rdd.map(x => {
                val line = x.value()
                val rowData = line.split("\\|", -1)
                (x.topic(), rowData)
            })

            queryEngine.work(topic2Record, rdd.id)

            stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges, new MyCallback())
        })

        ssc
    }


    class MyCallback extends OffsetCommitCallback with Serializable{
        override def onComplete(map: util.Map[TopicPartition, OffsetAndMetadata], e: Exception): Unit = {
            if(null != e){
                for(key <- map.keySet().toArray()){
                    val om = map.get(key)
                    val offset = om.asInstanceOf[OffsetAndMetadata].offset()
                    println(key + " ==> " + offset)
                }
                e.printStackTrace()
            }
        }
    }
}
