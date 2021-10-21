//package utils.oss
//
//import java.io.ByteArrayInputStream
//import java.text.{ParseException, SimpleDateFormat}
//import java.util
//import java.util.{Calendar, Date}
//
//import com.DateUtil
//import com.aliyun.oss.model.{AppendObjectRequest, ListObjectsRequest, OSSObjectSummary, ObjectListing}
//import com.aliyun.oss.{OSSClient, OSSException}
////import com.youlan.utils.oss.OSSUtil.OSS.bucketName
//import org.apache.commons.lang.StringUtils
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.{FileSystem, Path}
//import org.slf4j.{Logger, LoggerFactory}
//
///**
//  * Created by hugh on 2016/10/24.
//  */
//object OSSUtil {
//    val LOGGER: Logger = LoggerFactory.getLogger(OSSUtil.getClass.getName)
//
//    object OSS{
//        def accessKeyId = "ngO26Z1f8kTm2T8J"
//        def accessKeySecret = "ZePDGtdTZ2qpuU3SYWcBA5lgu78QGL"
//        def endPoint = "http://vpc100-oss-cn-hangzhou.aliyuncs.com" //内网
////        def endPoint = "http://oss-cn-hangzhou.aliyuncs.com" //外网
//        //def bucketName = "data-archives"
//        def bucketName = "mq-data-archives"
//
//        /*def endPoint = "http://vpc100-oss-us-west-1.aliyuncs.com" //内网
//        def endPoint = "http://oss-us-west-1.aliyuncs.com" //外网
//        def bucketName = "us-west-data-archives"*/
//
//        def combinPath = s"oss://${accessKeyId}:${accessKeySecret}@${bucketName}.${endPoint.substring(7,endPoint.length)}/"
//
//        var ossClient: OSSClient = null
//    }
//
//
//    def appendFile(key: String, content: String): Unit ={
//        if(null == OSS.ossClient){
//            OSS.ossClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret)
//        }
//        var preContentLength = 0L
//        val bucketName = "testTopic"
//        val appendObjectRequest = new AppendObjectRequest(bucketName, key, new ByteArrayInputStream(content.getBytes()))
//        if(!OSS.ossClient.doesObjectExist(OSS.bucketName, key)){
//            appendObjectRequest.setPosition(0L)
//        }else{
//            val objectMetadata = OSS.ossClient.getObjectMetadata(OSS.bucketName, key)
//            preContentLength = objectMetadata.getRawMetadata.get("x-oss-next-append-position").toString.toLong
////            LOGGER.info("key: " + key)
////            LOGGER.info("preContentLength: " + preContentLength)
////            LOGGER.info("content.length: " + content.length)
//            appendObjectRequest.setPosition(preContentLength)
//        }
//
//        try{
//            OSS.ossClient.appendObject(appendObjectRequest)
//        }catch {
//            case e: OSSException =>
//                val objectMetadata_ = OSS.ossClient.getObjectMetadata(OSS.bucketName, key)
//                val preContentLength_ = objectMetadata_.getContentLength
//                LOGGER.info("key: " + key)
//                LOGGER.info("preContentLength ggyy: " + preContentLength)
//                LOGGER.info("content.length: " + content.length)
//                LOGGER.info("preContentLength_ ggyy: " + preContentLength_)
//                e.printStackTrace()
//                throw e
//        }
//
//
//    }
//
//    def putFile(key: String, content: String): Unit ={
//        if(null == OSS.ossClient){
//            OSS.ossClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret)
//        }
//        OSS.ossClient.putObject(OSS.bucketName, key, new ByteArrayInputStream(content.getBytes))
//    }
//
//    def statDirDataSize(dir: String): Unit ={
//        val ossClient: OSSClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret)
//        val maxKeys: Int = 1000
//        val listObjectsRequest: ListObjectsRequest = new ListObjectsRequest(OSS.bucketName)
//        listObjectsRequest.setDelimiter("/")
//        listObjectsRequest.setPrefix(dir)
//        listObjectsRequest.withMaxKeys(maxKeys)
//        var objectListing: ObjectListing = null
//        var nextMarker: String = null
//        var tatolSize = 0L
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker)
//            }
//            listObjectsRequest.setMarker(nextMarker)
//            objectListing = ossClient.listObjects(listObjectsRequest)
//            val sums: util.List[OSSObjectSummary] = objectListing.getObjectSummaries
//            var total = 0
//            for (i <- 0 to sums.size()-1) {
//                val o = sums.get(i)
//                if (o.getSize > 0) {
////                    LOGGER.info(o.getKey)
//                    var count = 0;
//                    val ossObject = ossClient.getObject(OSS.bucketName, o.getKey)
//                    import java.io.{BufferedReader, InputStreamReader}
//                    val reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent))
//                    var line = reader.readLine()
//                    while (line != null) {
//                        count += 1
//                        line = reader.readLine
//                    }
//                    reader.close
//                    println(o.getKey + " => " + count)
//                    total += count
//                }
//            }
//            println("total : " + total)
//            nextMarker = objectListing.getNextMarker
//        } while (objectListing.isTruncated)
//    }
//
//    def statDirSize(dir: String): Long ={
//        val ossClient: OSSClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret)
//        val maxKeys: Int = 1000
//        val listObjectsRequest: ListObjectsRequest = new ListObjectsRequest(OSS.bucketName)
//        listObjectsRequest.setDelimiter("/")
//        listObjectsRequest.setPrefix(dir)
//        listObjectsRequest.withMaxKeys(maxKeys)
//        var objectListing: ObjectListing = null
//        var nextMarker: String = null
//        var tatolSize = 0L
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker)
//            }
//            listObjectsRequest.setMarker(nextMarker)
//            objectListing = ossClient.listObjects(listObjectsRequest)
//            val sums: util.List[OSSObjectSummary] = objectListing.getObjectSummaries
//            for (i <- 0 to sums.size()-1) {
//                val o = sums.get(i)
//                if (o.getSize > 0) {
//                    tatolSize = tatolSize + o.getSize
//                }
//            }
//            nextMarker = objectListing.getNextMarker
//        } while (objectListing.isTruncated)
//
//        return tatolSize
//    }
//
//    def getStdInputPath(basePrefix: String, startTime: Calendar, endTime: Calendar): util.List[String] = {
//        val ossClient: OSSClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret)
//        val _result: util.List[String] = new util.ArrayList[String]()
//        var count: Int = 0
//        var validCount: Int = 0
//        val maxKeys: Int = 1000
//        val listObjectsRequest: ListObjectsRequest = new ListObjectsRequest(OSS.bucketName)
//        listObjectsRequest.setDelimiter("/")
//        val prefix: String = String.format("%s/%s/", basePrefix, DateUtil.getStrFromCal(startTime, DateUtil.format_yyyyMMdd))
//        System.out.println(prefix)
//        listObjectsRequest.setPrefix(prefix)
//        listObjectsRequest.withMaxKeys(maxKeys)
//        var objectListing: ObjectListing = null
//        var nextMarker: String = null
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker)
//            }
//            listObjectsRequest.setMarker(nextMarker)
//            objectListing = ossClient.listObjects(listObjectsRequest)
//            val sums: util.List[OSSObjectSummary] = objectListing.getObjectSummaries
//            for (i <- 0 to sums.size()-1) {
//                val o = sums.get(i)
//                if (o.getSize > 0) {
//                    if (validate(o.getKey, startTime, endTime)) {
//                        _result.add(o.getKey)
//                        validCount += 1
//                        LOGGER.info(o.getKey)
//                    }
//                    count += 1
//                }
//            }
//            nextMarker = objectListing.getNextMarker
//        } while (objectListing.isTruncated)
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount)
//        return _result
//    }
//
//    @throws(classOf[Exception])
//    def getStdInputPathForMergeLog(conf: Configuration, basePrefix: String, startTime: Calendar, endTime: Calendar): util.List[String] = {
//        val startCostTime: Long = System.currentTimeMillis
//        val _result: util.List[String] = new util.ArrayList[String]
//        val startTimeCurrent: Calendar = startTime.clone.asInstanceOf[Calendar]
//        while (startTimeCurrent.compareTo(endTime) < 0) {
//            val currentResult: util.List[String] = getStdInputPath_(conf, basePrefix, startTimeCurrent, endTime)
//            for (i <- 0 to currentResult.size - 1) {
//                _result.add(currentResult.get(i))
//            }
//            startTimeCurrent.add(Calendar.MINUTE, 30)
//        }
//        LOGGER.info("getStdInputPath_ cost: " + (System.currentTimeMillis - startCostTime) + " 毫秒")
//        return _result
//    }
//
//    def getStdInputPath_(conf: Configuration, basePrefix: String, startTime: Calendar, endTime: Calendar): util.List[String] = {
//        val _result: util.List[String] = new util.ArrayList[String]()
//        var count: Int = 0
//        var validCount: Int = 0
//        val prefix: String = String.format("%s/%s/", basePrefix, DateUtil.getStrFromCal(startTime, "yyyyMMdd/HH/mm"))
////        val prefix: String = String.format("%s/", basePrefix)
//        val path = new Path(prefix)
//        val fs = FileSystem.get(path.toUri, conf)
//
//        if(fs.exists(path)){
//            val fileList = fs.listStatus(path)
//            for(f <- fileList){
//                if(f.getBlockSize > 0){
//                    if(validate(f.getPath.getName, startTime, endTime)){
//                        _result.add(f.getPath.toUri.toString)
//                        validCount += 1
//                        LOGGER.info(f.getPath.getName)
//                    }
//                    count += 1
//                }
//            }
//        }else{
//            LOGGER.info(prefix + " not exists!")
//        }
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount)
//        return _result
//    }
//
//    def getStdInputPath(conf: Configuration, basePrefix: String, startTime: Calendar, endTime: Calendar): util.List[String] = {
//        val _result: util.List[String] = new util.ArrayList[String]()
//        var count: Int = 0
//        var validCount: Int = 0
//        val prefix: String = String.format("%s/%s/", basePrefix, DateUtil.getStrFromCal(startTime, DateUtil.format_yyyyMMdd))
//        //        val prefix: String = String.format("%s/", basePrefix)
//        val path = new Path(prefix)
//        val fs = FileSystem.get(path.toUri, conf)
//
//        if(fs.exists(path)){
//            val fileList = fs.listStatus(path)
//            for(f <- fileList){
//                if(f.getBlockSize > 0){
//                    LOGGER.info("validate:"+f.getPath.getName)
//                    if(validate(f.getPath.getName, startTime, endTime)){
//                        _result.add(f.getPath.toUri.toString)
//                        validCount += 1
//                        LOGGER.info(f.getPath.getName)
//                    }
//                    count += 1
//                }
//            }
//        }else{
//            LOGGER.info(prefix + " not exists!")
//        }
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount)
//        return _result
//    }
//
//    def validate(path: String, startTime: Calendar, endTime: Calendar): Boolean = {
//        var ret: Boolean = false
//        val pathArray: Array[String] = path.split("/")
//        val fileName: String = pathArray(pathArray.length - 1)
//        val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm")
//        val fileNameArray: Array[String] = fileName.split("\\.")
//        val fileTime: String = fileNameArray(fileNameArray.length - 7)
//        try {
//            val fileDate: Date = timeFormat.parse(fileTime).asInstanceOf[Date]
//            val fileCal: Calendar = Calendar.getInstance
//            fileCal.setTime(fileDate)
//            if (fileCal.compareTo(startTime) >= 0 && fileCal.compareTo(endTime) < 0) {
//                ret = true
//            }
//        }
//        catch {
//            case e: ParseException => {
//                e.printStackTrace
//                throw e
//            }
//        }
//        return ret
//    }
//
//    @throws(classOf[Exception])
//    def getStdInputPath(conf: Configuration, basePrefix: String, lastFolder: util.List[String], startTime: Calendar, endTime: Calendar): util.List[String] = {
//        val startCostTime: Long = System.currentTimeMillis
//        val _result: util.List[String] = new util.ArrayList[String]
//        val startTimeCurrent: Calendar = startTime.clone.asInstanceOf[Calendar]
//        while (startTimeCurrent.compareTo(endTime) < 0) {
//            val currentResult: util.List[String] = getStdInputPathBySettledTime(conf, basePrefix, lastFolder, startTimeCurrent)
//            import scala.collection.JavaConversions._
//            for (path <- currentResult) {
//                _result.add(path)
//            }
//            startTimeCurrent.add(Calendar.MINUTE, 30)
//        }
//        LOGGER.info("getStdInputPath cost: " + (System.currentTimeMillis - startCostTime) + " 毫秒")
//        return _result
//    }
//
//    private def getStdInputPathBySettledTime(conf: Configuration, basePrefix: String, lastFolder: util.List[String], settledTime: Calendar): util.List[String] = {
//        val _result: util.List[String] = new util.ArrayList[String]
//        var count: Int = 0
//        var validCount: Int = 0
//        LOGGER.info("lastFolder.size: " + lastFolder.size)
//        for (item <- lastFolder.toArray()) {
//            val prefix: String = String.format("%s/%s/%s/%s/", basePrefix, DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyyMMdd),
//                DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyyMMddHHmm), item)
//            LOGGER.info("prefix: " + prefix)
//            val path = new Path(prefix)
//            val fs = FileSystem.get(path.toUri, conf)
//
//            if(fs.exists(path)){
//                val fileList = fs.listStatus(path)
//                for(f <- fileList){
//                    if(f.getLen > 0){
//                        _result.add(f.getPath.toUri.toString)
//                        validCount += 1
//                        LOGGER.info(f.getPath.getName)
//                        count += 1
//                    }
//                }
//            }else{
//                LOGGER.info(prefix + " not exists!")
//            }
//        }
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount)
//        return _result
//    }
//
//
//    def getOSSFileContent(path: String): Array[String] = {
//        try{
//            val ossClient = new OSSClient(OSS.endPoint, OSS.accessKeyId, OSS.accessKeySecret);
//            val _result = new util.ArrayList[String]
//            var count = 0
//            var validCount = 0
//            val maxKeys = 1000
//            // 构造ListObjectsRequest请求
//            val listObjectsRequest = new ListObjectsRequest(OSS.bucketName)
//            // "/" 为文件夹的分隔符
//            listObjectsRequest.setDelimiter("/")
//            // 列出fun目录下的所有文件和文件夹
//            listObjectsRequest.setPrefix(path)
//            listObjectsRequest.withMaxKeys(maxKeys)
//            var objectListing: ObjectListing  = null
//            var nextMarker: String = null
//            do {
//                if (StringUtils.isNotEmpty(nextMarker)) {
//                    listObjectsRequest.setMarker(nextMarker)
//                }
//                listObjectsRequest.setMarker(nextMarker)
//                objectListing = ossClient.listObjects(listObjectsRequest)
//                val sums: util.List[OSSObjectSummary] = objectListing.getObjectSummaries
//                for (i <- 0 to sums.size()-1) {
//                    val o = sums.get(i)
//                    count += 1
//                    if (o.getSize > 0) {
//                        LOGGER.info(o.getKey)
//                        val ossObject = ossClient.getObject(OSS.bucketName, o.getKey)
//                        import java.io.{BufferedReader, InputStreamReader}
//                        val reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent))
//                        var line = reader.readLine()
//                        while (line != null) {
//                            _result.add(line)
//                            line = reader.readLine
//                        }
//                        reader.close
//                    }
//                }
//                nextMarker = objectListing.getNextMarker
//            } while ( {
//                objectListing.isTruncated
//            })
//            LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount + " path: " + path)
//            _result.toArray.map(_.toString)
//        }catch {
//            case e: Exception =>
//                throw e
//        }
//
//    }
//}
