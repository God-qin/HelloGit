package model

import java.util.{HashMap, LinkedHashMap}

import scala.collection.mutable.ArrayBuffer

class GroupModel extends Serializable {
    
    var settledTimeStr: String = null
    var sourceTopics: Array[String] = null
    var dimension: LinkedHashMap[String,String] = null
    var quota: LinkedHashMap[String,String] = null
    
    var tableDimension: HashMap[Integer,HashMap[String, LinkedHashMap[String,String]]] = null // Map<tableid, Map<topic, Map<tableField,logField>>>
    var tableQuota: HashMap[Integer,HashMap[String, LinkedHashMap[String,String]]] = null
    
    var tableIdMap: HashMap[Integer, String] = null
    var mapDimension: HashMap[String,String] = null
    var sourceTopicFields: HashMap[String,ArrayBuffer[String]] = null
    
    def this(dimension: LinkedHashMap[String,String] = null,
             quota: LinkedHashMap[String,String] = null,
             tableDimension:HashMap[Integer,HashMap[String, LinkedHashMap[String,String]]] = null,
             tableQuota: HashMap[Integer,HashMap[String, LinkedHashMap[String,String]]] = null,
             tableIdMap: HashMap[Integer, String] = null,
             sourceTopics: Array[String] = null,
             mapDimension:HashMap[String,String] = null,
             sourceTopicFields: HashMap[String,ArrayBuffer[String]] = null) {
        this()
        
        this.dimension = dimension
        this.quota = quota
        this.tableDimension = tableDimension
        this.tableQuota = tableQuota
        this.tableIdMap = tableIdMap
        this.sourceTopics = sourceTopics
        this.mapDimension = mapDimension
        this.sourceTopicFields = sourceTopicFields
    }
}
