package configs

import org.apache.spark.sql.SparkSession

object UdfRegister extends Serializable {
  def registerFunction(spark: SparkSession): Unit = {
    //decryptwinningprice


    spark.udf.register("format_minute", (now: String) => {
      if (null == now) {
        ""
      } else {
        now.substring(0, 17) + "00"
      }
    })

    spark.udf.register("format_terminal", (isMobile: Int) => {

      if (null != isMobile && isMobile == 1) {
        2;
      }
      else {
        1;
      }
    })

    spark.udf.register("pv", (advfeature: Long) => {
      if (null == advfeature) {
        0
      } else {
        if ((advfeature & 1) > 0) 1 else 0
      }
    })

    spark.udf.register("click", (advfeature: Long) => {
      if (null == advfeature)
        0
      else {
        if ((advfeature & 4) > 0) 1 else 0
      }
    })

    spark.udf.register("cost", (advfeature: Int, price: Long) => {
      if (null == advfeature)
        0
      else {
        if ((advfeature & 2) > 0) {
          if ((advfeature & 1) > 0) price
          else if ((advfeature & 4) > 0) price * 1000
          else 0
        } else {
          0
        }
      }
    })

    spark.udf.register("pv_event", (eventType: Int) => {
      if (null == eventType) {
        0
      } else {
        if (eventType == 2) 1 else 0
      }
    })

    spark.udf.register("click_event", (eventType: Int) => {
      if (null == eventType)
        0
      else {
        if (eventType == 6) 1 else 0
      }
    })

    spark.udf.register("cost_event", (eventType: Int, price: Long) => {
      if (null == eventType)
        0
      else {

        if (eventType == 2) price
        else if (eventType == 6) price * 1000
        else 0
      }
    })
  }
}
