package utils

import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer

/**
  * Created by hugh on 2017/8/3.
  */
object RDDUtil {
    def unionToOneRdd(rdds: ListBuffer[RDD[String]]): RDD[String] ={
        var list2 = new ListBuffer[RDD[String]]
        var list1 = new ListBuffer[RDD[String]]
        list1.++=(rdds)
        while (list1.size != 1){
            var i = 0
            while (i+1 <= list1.size-1){
                list2 += list1(i).union(list1(i+1))
                i += 2
            }
            if(i == list1.size-1){
                list2 += list1(i)
            }
            list1 = list2
            list2 = new ListBuffer[RDD[String]]
        }
        list1(0)
    }
}
