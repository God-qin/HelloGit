//package com;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.model.ListObjectsRequest;
//import com.aliyun.oss.model.OSSObject;
//import com.aliyun.oss.model.OSSObjectSummary;
//import com.aliyun.oss.model.ObjectListing;
//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.mapreduce.Job;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by terry.qian on 2016/5/24.
// */
//public class OSSFileUtil {
//    private final static Logger LOGGER = LoggerFactory.getLogger(OSSFileUtil.class.getName());
//    private final static String SEPARATOR = File.separator;
//    private OSSClient ossClient;
//    private String bucketName;
//    private String endPoint;
//    private String accessKeyId;
//    private String accessKeySecret;
//
//    public static void main(String[] args) throws Exception {
//
//        String accessKeyId = "ngO26Z1f8kTm2T8J";
//        String accessKeySecret = "ZePDGtdTZ2qpuU3SYWcBA5lgu78QGL";
//        String endPoint = "http://oss-cn-hangzhou.aliyuncs.com";
//        String bucketName = "data-archives";
//
////        String accessKeyId = "ngO26Z1f8kTm2T8J";
////        String accessKeySecret = "ZePDGtdTZ2qpuU3SYWcBA5lgu78QGL";
////        String endPoint = "http://oss-us-west-1.aliyuncs.com";
////        String bucketName = "us-west-data-archives";
//
//        OSSFileUtil util = new OSSFileUtil(accessKeyId, accessKeySecret, endPoint, bucketName);
//
//        String prefix = "data/text/ssp_media_log/20171227/16/00/";
//        List<String> list = util.getAllFiles(prefix);
//
//
//        //String sourceKey = "test/ssp/parquet_test/20171130/part-00000-621306d1-3a8d-4a4b-845e-3ce80345c785.snappy.parquet";
//        //String sourceKey = "data/parquet/dsp_activity_log/20171008/part-r-00000.snappy.parquet";
//        //String targetKey = "test/ssp/parquet_test_new/20171130/parquet_test.201711301125.snappy.parquet";
//
//        //long start = System.currentTimeMillis();
//        //util.copy(sourceKey,targetKey);
//        //System.out.println((System.currentTimeMillis()-start));
//
//        /*Job job=Job.getInstance();
//        job.setJobName("oss-upload-lib");
//        util.uploadAndBindJobJar(job,"M:\\hadooplib\\report-osstest");
//        Thread.sleep(30000);
//        util.deleteJobJar(job,"M:\\hadooplib\\report-osstest");*/
//
//
//
//        /*
//        Calendar startTime = Calendar.getInstance();
//        startTime.clear();
//        startTime.set(2017, 4, 23, 00, 00, 00);
//        Calendar endTime = Calendar.getInstance();
//        endTime.clear();
//        endTime.set(2017, 4, 24, 00, 00, 00);
//
//        List<String> lastFolder = new ArrayList<String>();
//        lastFolder.add("dsp_activity_log");
//        lastFolder.add("iped_activity_log");
//        lastFolder.add("iped_mob_activity_log");
//
//        List<String> list = util.getStdInputPathForHalfHourly("data/text/dsp_bid_log",startTime,endTime);
//
//        System.out.println(list.size());
//
//       /* List<String> list = util.getAllFiles(prefix);
//
//        int count = 0;
//        for (String path : list) {
//            System.out.println(path);
//            count++;
//        }
//
//        System.out.println(count);*/
//
//
//        /*String tableName = "dsp_activity_report_log";
//        String basePrefix = String.format("data/text/%s", tableName.toLowerCase());
//
//        List<String> lastFolder=new ArrayList<>();
//        lastFolder.add("dsp_activity_log");
//        lastFolder.add("iped_activity_log");
//        lastFolder.add("iped_mob_activity_log");
//        util.getStdInputPath(basePrefix,lastFolder,startTime,endTime);*/
//
//
//
//        /*String basePrefix ="output/dsp_hourly_report_emr/201607010000/merge/RTB_DATA_DAILY_MEDIA_REPORT";
//        List<String> list = util.getAllFiles(basePrefix);
//
//        for (String path :list){
//            LOGGER.info("path:  "+path);
//            LOGGER.info("tableName:  "+path.split("/")[4].split("-")[0]);
//
//            OSSObject ossObject = util.getOssObject(path);
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) break;
//
//                System.out.println(line);
//            }
//            reader.close();
//        }*/
//
//    }
//
//    public OSSFileUtil(String accessKeyId, String accessKeySecret, String endPoint, String bucketName) throws Exception {
//        this.accessKeyId = accessKeyId;
//        this.accessKeySecret = accessKeySecret;
//        this.endPoint = endPoint;
//        this.bucketName = bucketName;
//
//        LOGGER.info("accessKeyId:" + accessKeyId);
//        LOGGER.info("accessKeySecret:" + accessKeySecret);
//        LOGGER.info("endPoint:" + endPoint);
//        LOGGER.info("bucketName:" + bucketName);
//
//        try {
//            this.ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
//        }
//        catch (Exception e){
//            LOGGER.error("init OSSFileUtil error: "+e.toString());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    //上传jar
//    public void uploadAndBindJobJar(Job job, String localJobJarBaseDir)
//            throws Exception {
//
//        long startMillis = System.currentTimeMillis();
//        String remoteJobBaseDir = getRemoteJobJarBaseDir(job.getJobName());
//        File localDirFile = new File(localJobJarBaseDir);
//        if (!localDirFile.exists()) {
//            throw new FileNotFoundException("Dir "
//                    + localDirFile.getAbsolutePath());
//        } else if (!localDirFile.isDirectory()) {
//            throw new IOException("Path " + localDirFile.getAbsolutePath()
//                    + " is not a dir");
//        }
//
//        for (File localFilePath : localDirFile.listFiles()) {
//            if (localFilePath.isFile()
//                    && localFilePath.getName().endsWith(".jar")) {
//                String remoteFilePath = remoteJobBaseDir + SEPARATOR
//                        + localFilePath.getName();
//
//                ossClient.putObject(bucketName, remoteFilePath, localFilePath);
//                LOGGER.info("upload: "+remoteFilePath);
//                job.addFileToClassPath(new Path(remoteFilePath));
//            }
//        }
//        LOGGER.info("uploadAndBindJobJar cost: " + (System.currentTimeMillis() - startMillis) + " 毫秒");
//    }
//
//    //删除jar
//    public void deleteJobJar(Job job, String localJobJarBaseDir)
//            throws Exception {
//
//        long startMillis = System.currentTimeMillis();
//        String remoteJobBaseDir = getRemoteJobJarBaseDir(job.getJobName());
//        File localDirFile = new File(localJobJarBaseDir);
//        if (!localDirFile.exists()) {
//            throw new FileNotFoundException("Dir "
//                    + localDirFile.getAbsolutePath());
//        } else if (!localDirFile.isDirectory()) {
//            throw new IOException("Path " + localDirFile.getAbsolutePath()
//                    + " is not a dir");
//        }
//
//        for (File localFilePath : localDirFile.listFiles()) {
//            if (localFilePath.isFile()
//                    && localFilePath.getName().endsWith(".jar")) {
//                String remoteFilePath = remoteJobBaseDir + SEPARATOR
//                        + localFilePath.getName();
//
//                ossClient.deleteObject(bucketName, remoteFilePath);
//                LOGGER.info("delete: " + remoteFilePath);
//                job.addFileToClassPath(new Path(remoteFilePath));
//            }
//        }
//        LOGGER.info("deleteJobJar cost: " + (System.currentTimeMillis() - startMillis) + " 毫秒");
//    }
//
//    public String getRemoteJobJarBaseDir(String jobName)
//            throws Exception {
//        if (jobName == null || jobName.trim().equals("")) {
//            throw new Exception("jobName cannot be empty or null");
//        }
//        return "lib/" + jobName;
//    }
//
//    public OSSObject getOssObject(String key) {
//        return this.ossClient.getObject(this.bucketName, key);
//    }
//
//    public List<String> getAllFiles(String basePrefix) {
//        List<String> _result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        // 列出fun目录下的所有文件和文件夹
//        String prefix = basePrefix;
//        listObjectsRequest.setPrefix(prefix);
//        listObjectsRequest.withMaxKeys(maxKeys);
//
//        ObjectListing objectListing = null;
//        String nextMarker = null;
//
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker);
//            }
//
//            listObjectsRequest.setMarker(nextMarker);
//            objectListing = ossClient.listObjects(listObjectsRequest);
//
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary o : sums) {
//                count++;
//                if (o.getSize() <= 0) {
//                    continue;
//                }
//
//                _result.add(o.getKey());
//                validCount++;
//                LOGGER.info(o.getKey());
//            }
//
//            nextMarker = objectListing.getNextMarker();
//        }
//        while (objectListing.isTruncated());
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount + " BASEPREFIX: " + basePrefix);
//
//        return _result;
//    }
//
//    public List<String> getStdInputPath(String basePrefix, Calendar startTime,
//                                             Calendar endTime) throws Exception {
//        List<String> _result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        // 列出fun目录下的所有文件和文件夹
//        String prefix = String.format("%s/%s/", basePrefix, DateUtil.getStrFromCal(startTime, DateUtil.format_yyyyMMdd));
//        LOGGER.info("getStdInputPath  prefix === " + prefix);
//        listObjectsRequest.setPrefix(prefix);
//        listObjectsRequest.withMaxKeys(maxKeys);
//
//        ObjectListing objectListing = null;
//        String nextMarker = null;
//
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker);
//            }
//
//            listObjectsRequest.setMarker(nextMarker);
//            objectListing = ossClient.listObjects(listObjectsRequest);
//
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary o : sums) {
//                if(o.getSize()<=0){
//                    continue;
//                }
//
//                if (validate(o.getKey(), startTime, endTime)) {
//
//                    _result.add(o.getKey());
//                    validCount++;
//                    LOGGER.info("getStdInputPath  o.getKey() === " + o.getKey());
//                }
//                count++;
//            }
//
//            nextMarker = objectListing.getNextMarker();
//        }
//        while (objectListing.isTruncated());
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount);
//
//        return _result;
//    }
//
//    public List<String> getStdInputPath_(String basePrefix, Calendar startTime,
//                                        Calendar endTime) throws Exception {
//        List<String> result = new ArrayList<String>();
//
//        // 列出fun目录下的所有文件和文件夹
//        String prefix = String.format("%s/%s/", basePrefix, DateUtil.getStrFromCal(startTime, "yyyyMMdd/HH/mm"));
//        // 区分出dsp_activity_report_log和其他
//        if (basePrefix.endsWith("dsp_activity_report_log")){
//            String tb = "dsp_track_log";
//                prefix = String.format("%s/%s/%s/%s/",
//                        basePrefix,
//                        DateUtil.getStrFromCal(startTime, "yyyyMMdd"),
//                        DateUtil.getStrFromCal(startTime, "yyyyMMddHHmm"),
//                        tb);
//            LOGGER.info("getStdInputPath_  prefix === " + prefix);
//            result.addAll(listObjectsInPath(prefix, startTime, endTime, false));
//        }else{
//            LOGGER.info("getStdInputPath_  prefix === " + prefix);
//            result.addAll(listObjectsInPath(prefix, startTime, endTime, true));
//        }
//
//        LOGGER.info("getStdInputPath_  COUNT: " + result.size());
//
//        return result;
//    }
//
//    /**
//     * 列出路径下的所有对象
//     * @param prefix
//     * @param startTime
//     * @param endTime
//     * @param isValidate
//     * @return
//     */
//    private List<String> listObjectsInPath(String prefix, Calendar startTime,
//                                           Calendar endTime, boolean isValidate){
//        List<String> result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        listObjectsRequest.setPrefix(prefix);
//        listObjectsRequest.withMaxKeys(maxKeys);
//
//        ObjectListing objectListing = null;
//        String nextMarker = null;
//
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker);
//            }
//
//            listObjectsRequest.setMarker(nextMarker);
//            objectListing = ossClient.listObjects(listObjectsRequest);
//
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary o : sums) {
//                if(o.getSize()<=0){
//                    continue;
//                }
//
//                // 是否校验文件
//                if (isValidate){
//                    if (validate(o.getKey(), startTime, endTime)) {
//                        result.add(o.getKey());
//                        validCount++;
//                        LOGGER.info(o.getKey());
//                    }
//                } else {
//                    result.add(o.getKey());
//                    validCount++;
//                    LOGGER.info(o.getKey());
//                }
//                count++;
//            }
//
//            nextMarker = objectListing.getNextMarker();
//        }
//        while (objectListing.isTruncated());
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount);
//
//        return result;
//    }
//
//    public List<String> _getStdInputPath_(String basePrefix, Calendar startTime,
//                                         Calendar endTime) throws Exception {
//        LOGGER.info("into _getStdInputPath_");
//        List<String> _result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        // 列出fun目录下的所有文件和文件夹
//        String dateDir = DateUtil.getStrFromCal(startTime, "yyyyMMdd/HH/");
//        String[] tmp = DateUtil.getStrFromCal(startTime, "yyyyMMdd|HH|mm").split("\\|");
//        int m = Integer.parseInt(tmp[2]);
//        if(0 <= m && m < 30){
//            dateDir = dateDir + "00";
//        }else {
//            dateDir = dateDir + "30";
//        }
//
//        String prefix = String.format("%s/%s/", basePrefix, dateDir);
//        LOGGER.info("prefix: " + prefix);
//        listObjectsRequest.setPrefix(prefix);
//        listObjectsRequest.withMaxKeys(maxKeys);
//
//        ObjectListing objectListing = null;
//        String nextMarker = null;
//
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker);
//            }
//
//            listObjectsRequest.setMarker(nextMarker);
//            objectListing = ossClient.listObjects(listObjectsRequest);
//
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary o : sums) {
//                if(o.getSize()<=0){
//                    continue;
//                }
//
//                if (validate(o.getKey(), startTime, endTime)) {
//
//                    _result.add(o.getKey());
//                    validCount++;
//                    LOGGER.info(o.getKey());
//                }
//                count++;
//            }
//
//            nextMarker = objectListing.getNextMarker();
//        }
//        while (objectListing.isTruncated());
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount);
//
//        return _result;
//    }
//
//    public List<String> getStdInputPathForHalfHourly(String basePrefix, Calendar startTime,
//                                        Calendar endTime) throws Exception {
//        long startCostTime = System.currentTimeMillis();
//        List<String> _result = new ArrayList<>();
//
//        Calendar startTimeCurrent = (Calendar) startTime.clone();
//
//        while (startTimeCurrent.compareTo(endTime) < 0) {
//
//            List<String> currentResult = getStdInputPath_(basePrefix, startTimeCurrent, endTime);
//
//            for (String path : currentResult) {
//                _result.add(path);
//            }
//
//            startTimeCurrent.add(Calendar.MINUTE, 30);
//        }
//
//
//        LOGGER.info("getStdInputPath_ cost: " + (System.currentTimeMillis() - startCostTime) + " 毫秒");
//        return _result;
//    }
//
//    public List<String> getStdInputPath(String basePrefix,List<String> lastFolder, Calendar startTime,
//                                        Calendar endTime) throws Exception {
//        long startCostTime = System.currentTimeMillis();
//        List<String> _result = new ArrayList<>();
//
//        Calendar startTimeCurrent = (Calendar) startTime.clone();
//
//        while (startTimeCurrent.compareTo(endTime) < 0) {
//
//            List<String> currentResult = getStdInputPathBySettledTime(basePrefix, lastFolder, startTimeCurrent);
//
//            for (String path : currentResult) {
//                _result.add(path);
//            }
//
//            startTimeCurrent.add(Calendar.MINUTE, 30);
//        }
//
//
//        LOGGER.info("getStdInputPath cost: " + (System.currentTimeMillis() - startCostTime) + " 毫秒");
//        return _result;
//    }
//
//    public List<String> getStdInputPathForSite(String basePrefix, Calendar startTime,
//                                        Calendar endTime) throws Exception {
//        long startCostTime = System.currentTimeMillis();
//        List<String> _result = new ArrayList<>();
//
//        Calendar startTimeCurrent = (Calendar) startTime.clone();
//
//        while (startTimeCurrent.compareTo(endTime) < 0) {
//
//            List<String> currentResult = getStdInputPathBySettledTimeForSite(basePrefix, startTimeCurrent);
//
//            for (String path : currentResult) {
//                _result.add(path);
//            }
//
//            startTimeCurrent.add(Calendar.DATE, 1);
//        }
//
//
//        LOGGER.info("getStdInputPath cost: " + (System.currentTimeMillis() - startCostTime) + " 毫秒");
//        return _result;
//    }
//
//    public List<String> getStdInputPathBySettledTimeForSite(String basePrefix,
//                                                      Calendar settledTime) {
//        List<String> _result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        // 列出fun目录下的所有文件和文件夹
//        String prefix = String.format("%s/%s/", basePrefix,
//                DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyyMMdd));
//
//        listObjectsRequest.setPrefix(prefix);
//        listObjectsRequest.withMaxKeys(maxKeys);
//
//        ObjectListing objectListing = null;
//        String nextMarker = null;
//
//        do {
//            if (StringUtils.isNotEmpty(nextMarker)) {
//                listObjectsRequest.setMarker(nextMarker);
//            }
//
//            listObjectsRequest.setMarker(nextMarker);
//            objectListing = ossClient.listObjects(listObjectsRequest);
//
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary o : sums) {
//                if (o.getSize() <= 0) {
//                    continue;
//                }
//
//                if (o.getKey().getBytes().length > 0) {
//                    _result.add(o.getKey());
//                    validCount++;
//                    //LOGGER.info(o.getKey());
//                }
//
//                count++;
//            }
//
//            nextMarker = objectListing.getNextMarker();
//        }
//        while (objectListing.isTruncated());
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount);
//
//        return _result;
//    }
//
//    private List<String> getStdInputPathBySettledTime(String basePrefix,List<String> lastFolder,
//                                                      Calendar settledTime) {
//        List<String> _result = new ArrayList<String>();
//        int count = 0;
//        int validCount = 0;
//        final int maxKeys = 1000;
//
//        // 构造ListObjectsRequest请求
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
//
//        // "/" 为文件夹的分隔符
//        listObjectsRequest.setDelimiter("/");
//
//        // 列出fun目录下的所有文件和文件夹
//        LOGGER.info("lastFolder.size: " + lastFolder.size());
//        for (String item : lastFolder) {
//
//            String prefix = String.format("%s/%s/%s/%s/", basePrefix,
//                    DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyyMMdd),
//                    DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyyMMddHHmm),
//                    item);
//            LOGGER.info("prefix: " + prefix);
//            listObjectsRequest.setPrefix(prefix);
//            listObjectsRequest.withMaxKeys(maxKeys);
//
//            ObjectListing objectListing = null;
//            String nextMarker = null;
//
//            do {
//                if (StringUtils.isNotEmpty(nextMarker)) {
//                    listObjectsRequest.setMarker(nextMarker);
//                }
//
//                listObjectsRequest.setMarker(nextMarker);
//                objectListing = ossClient.listObjects(listObjectsRequest);
//
//                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//                for (OSSObjectSummary o : sums) {
//                    if (o.getSize() <= 0) {
//                        continue;
//                    }
//
//                    if (o.getKey().getBytes().length > 0) {
//                        _result.add(o.getKey());
//                        validCount++;
//                        //LOGGER.info(o.getKey());
//                    }
//
//                    count++;
//                }
//
//                nextMarker = objectListing.getNextMarker();
//            }
//            while (objectListing.isTruncated());
//        }
//
//        LOGGER.info("COUNT: " + count + " VALID COUNT: " + validCount);
//
//        return _result;
//    }
//
//    public boolean validate(String path, Calendar startTime,
//                                   Calendar endTime) {
//        boolean ret = false;
//
//        String[] pathArray = path.split("/");
//        //data/text/dsp_bid_log/20160530/dsp_bid_log.201605302355.iZ238pq4qdwZ.192.168.8.1.dat
//        String fileName = pathArray[pathArray.length - 1];
//
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmm");
//
//        String[] fileNameArray = fileName.split("\\.");
//        String fileTime = fileNameArray[fileNameArray.length - 7];
//
//        try {
//
//            Date fileDate = (Date) timeFormat.parse(fileTime);
//
//            Calendar fileCal = Calendar.getInstance();
//
//            fileCal.setTime(fileDate);
//
//            if (fileCal.compareTo(startTime) >= 0
//                    && fileCal.compareTo(endTime) < 0) {
//                ret = true;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return ret;
//    }
//
//    public OSSClient getOssClient() {
//        return ossClient;
//    }
//
//    public OSSFileUtil setOssClient(OSSClient ossClient) {
//        this.ossClient = ossClient;
//        return this;
//    }
//
//    public void copy(String sourceKey,String targetKey){
//
//        this.ossClient.copyObject(this.bucketName,sourceKey,this.bucketName,targetKey);
//    }
//}
