package com;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRConfig;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.DateUtil;
import com.ad.youlan.StdPathFilter;

import java.io.*;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by terry.qian on 2016/2/18.
 */
public class HDFSFileUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HDFSFileUtil.class);
    private static final String CONFIG_FILE1 = "hadoop.config";
    private static final String CONFIG_FILE2 = "config" + File.separator
            + "hadoop.config";
    private static final HashSet<String> hadoopConfigurationResourceUrl = new HashSet<String>();
    private static final String FIlE_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmm";

    static {

        /*File hadoopConfigFile = null;
        try {
            File hadoopConfogFile1;
            String appPath = System.getProperty("user.dir");
            LOGGER.info("app work path:" + appPath);
            hadoopConfogFile1 = new File(appPath + File.separator + CONFIG_FILE1);
            if (hadoopConfogFile1.exists()) {
                hadoopConfigFile = hadoopConfogFile1;
            } else {
                LOGGER.info("try load hadoop config file 1 not exit:" + hadoopConfogFile1.getPath());
                File hadoopConfigFile2 = new File(appPath + File.separator + CONFIG_FILE2);
                if (hadoopConfigFile2.exists()) {
                    hadoopConfigFile = hadoopConfigFile2;
                }
                LOGGER.info("try load hadoop config file 2 not exit:" + hadoopConfigFile2.getPath());
            }

            if (hadoopConfigFile != null && hadoopConfigFile.exists()) {
                FileInputStream inputStream = new FileInputStream(
                        hadoopConfigFile);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line = "";
                while ((line = reader.readLine()) != null && !line.equals("")) {
                    hadoopConfigurationResourceUrl.add(line);
                    LOGGER.info("hadoop resource config:" + line);
                }
                reader.close();
                inputStream.close();
            } else {
                LOGGER.info("hadoop configuration "
                        + hadoopConfigFile.getAbsolutePath()
                        + " does not exist,use default configuration:");
                hadoopConfigurationResourceUrl
                        .add("http://192.168.1.183:8080/yarn-conf/core-site.xml");
                hadoopConfigurationResourceUrl
                        .add("http://192.168.1.183:8080/yarn-conf/hdfs-site.xml");
                hadoopConfigurationResourceUrl
                        .add("http://192.168.1.183:8080/yarn-conf/yarn-site.xml");
                hadoopConfigurationResourceUrl
                        .add("http://192.168.1.183:8080/yarn-conf/mapred-site.xml");
                for (String resourceUrl : hadoopConfigurationResourceUrl) {
                    LOGGER.info(resourceUrl);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in loading hadoop configuration file:"
                    + hadoopConfigFile != null ? hadoopConfigFile
                    .getAbsolutePath() : "" + "," + e.toString());
        }*/
    }

    public static List<Path> listFile(FileSystem fileSystem, Path root)
            throws FileNotFoundException, IOException {
        return HDFSFileUtil.listFile(fileSystem, root, null);
    }

    public static List<Path> listFile(FileSystem fileSystem, Path root,
                                      PathFilter pathFilter) throws FileNotFoundException, IOException {
        List<Path> fileList = new ArrayList<Path>();

        if (!fileSystem.exists(root)) {
            return fileList;
        }

        if (fileSystem.isFile(root)) {
            fileList.add(root);
            return fileList;
        } else {
            FileStatus[] statusList = new FileStatus[0];
            if (pathFilter == null) {
                statusList = fileSystem.listStatus(root);
            } else {
                statusList = fileSystem.listStatus(root, pathFilter);
            }

            for (FileStatus fileStatus : statusList) {

                Path subfile = fileStatus.getPath();

                if (fileStatus.isDirectory()) {
                    fileList.addAll(listFile(fileSystem, subfile));
                } else {
                    fileList.add(subfile);
                }
            }
            return fileList;
        }
    }

    /**
     * override remoteFilePath if exist
     *
     * @param fileSystem
     * @param localFilePath
     * @param remoteFilePath
     * @return
     * @throws IOException
     */
    public static void uploadFile(FileSystem fileSystem, String localFilePath,
                                  String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);
        if (!localFile.exists()) {
            throw new FileNotFoundException("File "
                    + localFile.getAbsolutePath());
        } else if (!localFile.isFile()) {
            throw new IOException("File " + localFile.getAbsolutePath()
                    + " is not a file");
        }

        Path remoteFile = new Path(remoteFilePath);

        if (fileSystem.exists(remoteFile)) {
            fileSystem.delete(remoteFile, false);
        }
        fileSystem.copyFromLocalFile(new Path(localFile.getPath()), remoteFile);
    }

    public static void uploadDir(FileSystem fileSystem, String localDir,
                                 String remoteDir) throws IOException {
        File localDirFile = new File(localDir);
        if (!localDirFile.exists()) {
            throw new FileNotFoundException("Dir "
                    + localDirFile.getAbsolutePath());
        } else if (!localDirFile.isDirectory()) {
            throw new IOException("Path " + localDirFile.getAbsolutePath()
                    + " is not a dir");
        }
        for (File localFilePath : localDirFile.listFiles()) {
            if (localFilePath.isFile()) {
                String remoteFilePath = remoteDir + File.separator
                        + localFilePath.getName();
                HDFSFileUtil.uploadFile(fileSystem,
                        localFilePath.getAbsolutePath(), remoteFilePath);
            }
        }
    }

    public static List<Path> listOutPutFile(FileSystem fileSystem, Path root)
            throws FileNotFoundException, IOException {
        List<Path> paths = HDFSFileUtil.listFile(fileSystem, root);
        List<Path> ret = new ArrayList<Path>();
        for (Path path : paths) {
            if (path.getName().toLowerCase() != "_success") {
                ret.add(path);
            }
        }
        return ret;
    }

    public static String getRemoteJobJarBaseDir(String jobName)
            throws Exception {
        if (jobName == null || jobName.trim().equals("")) {
            throw new Exception("jobName cannot be empty or null");
        }
        return "/lib/" + jobName;
    }

    public static void uploadAndBindJobJar(Job job, FileSystem fs,
                                           String localJobJarBaseDir) throws Exception {
        long startMillis = System.currentTimeMillis();
        String remoteJobBaseDir = HDFSFileUtil.getRemoteJobJarBaseDir(job
                .getJobName());
        File localDirFile = new File(localJobJarBaseDir);
        if (!localDirFile.exists()) {
            throw new FileNotFoundException("Dir "
                    + localDirFile.getAbsolutePath());
        } else if (!localDirFile.isDirectory()) {
            throw new IOException("Path " + localDirFile.getAbsolutePath()
                    + " is not a dir");
        }
        for (File localFilePath : localDirFile.listFiles()) {
            if (localFilePath.isFile()
                    && localFilePath.getName().endsWith(".jar")) {
                String remoteFilePath = remoteJobBaseDir + File.separator
                        + localFilePath.getName();
                HDFSFileUtil.uploadFile(fs, localFilePath.getAbsolutePath(),
                        remoteFilePath);
                job.addFileToClassPath(new Path(remoteFilePath));
            }
        }
        LOGGER.info("uploadAndBindJobJar cost: " + (System.currentTimeMillis() - startMillis) + " 毫秒");
    }

    public static void deleteJobJar(Job job, FileSystem fs,
                                           String localJobJarBaseDir) throws Exception {
        long startMillis = System.currentTimeMillis();
        String remoteJobBaseDir = HDFSFileUtil.getRemoteJobJarBaseDir(job
                .getJobName());

        File localDirFile = new File(localJobJarBaseDir);
        if (!localDirFile.exists()) {
            throw new FileNotFoundException("Dir "
                    + localDirFile.getAbsolutePath());
        } else if (!localDirFile.isDirectory()) {
            throw new IOException("Path " + localDirFile.getAbsolutePath()
                    + " is not a dir");
        }

        for (File localFilePath : localDirFile.listFiles()) {
            if (localFilePath.isFile()
                    && localFilePath.getName().endsWith(".jar")) {
                String remoteFilePath = remoteJobBaseDir + File.separator
                        + localFilePath.getName();

                Path path = new Path(remoteFilePath);
                if (fs.exists(path)) {
                    fs.delete(path, true);
                }
            }
        }
        LOGGER.info("deleteJobJar cost: " + (System.currentTimeMillis() - startMillis) + " 毫秒");
    }

    public static void uploadAndBindJobJar(Job job, FileSystem fs)
            throws Exception {
        String workDir = System.getProperty("user.dir");
        LOGGER.info("job work dir:" + workDir);
        uploadAndBindJobJar(job, fs, workDir);
    }

    public static List<Path> getInputPath(FileSystem fs,
                                          Path tableDataBasePath, List<String> subTableNames, Calendar startTime,
                                          Calendar endTime) throws IOException {

        List<Path> retList = new ArrayList<Path>();
        Calendar st = (Calendar) startTime.clone();
        while (st.before(endTime)) {

            String dateStr = DateUtil.getStrFromCal(st, DateUtil.format_yyyyMMdd);
            String timeStr = DateUtil.getStrFromCal(st, DateUtil.format_yyyyMMddHHmm);

            for (String tableName : subTableNames) {

                String baseFolder = String.format("/%s/%s/%s/%s", tableDataBasePath, dateStr, timeStr, tableName);
                Path baseFolderPath = new Path(baseFolder);
                //LOGGER.info("baseFolder: " + baseFolder);

                if (!fs.exists(baseFolderPath)) {
                    continue;
                }

                FileStatus[] fileStatusList = fs.listStatus(baseFolderPath);

                if (fileStatusList != null) {

                    for (FileStatus fileStatus : fileStatusList) {

                        if (fileStatus.isFile() && fileStatus.getLen() > 0) {
                            retList.add(fileStatus.getPath());
                        }
                    }
                }
            }

            st.add(Calendar.MINUTE, 30);
        }

        return retList;
    }

    /**
     * Filter file name like activity_text.201403220000.adbjsd29.2.dat
     *
     * @param fileNamePrefix
     * @param startTime
     * @param endTime
     * @return
     */
    public static PathFilter getStdPathFilter(String fileNamePrefix,
                                              Calendar startTime, Calendar endTime) {
        return new StdPathFilter(fileNamePrefix.toLowerCase(), startTime,
                endTime);
    }

    public static List<Path> getStdInputPath(FileSystem fs,
                                             Path tableDataBasePath, String tableName, Calendar startTime,
                                             Calendar endTime) throws Exception {
        List<Path> retList = new ArrayList<Path>();
        List<Path> datePaths = new ArrayList<Path>();
        Calendar st = (Calendar) startTime.clone();
        while (st.before(endTime)) {
            datePaths.add(new Path(tableDataBasePath + "/"
                    + DateUtil.getStrFromDate(st.getTime(), "yyyyMMdd")));
            st.add(Calendar.DATE, 1);
        }
        PathFilter pathFilter = HDFSFileUtil.getStdPathFilter(tableName,
                startTime, endTime);
        for (Path path : datePaths) {
            retList.addAll(HDFSFileUtil.listFile(fs, path, pathFilter));
        }

        return retList;
    }

    public static List<Path> getParquetInputPath(FileSystem fs,
                                             Path tableDataBasePath, String tableName, Calendar startTime,
                                             Calendar endTime) throws Exception {
        List<Path> retList = new ArrayList<Path>();
        List<Path> datePaths = new ArrayList<Path>();
        Calendar st = (Calendar) startTime.clone();
        while (st.before(endTime)) {
            datePaths.add(new Path(tableDataBasePath + "/"
                    + DateUtil.getStrFromDate(st.getTime(), "yyyyMMdd")));
            st.add(Calendar.DATE, 1);
        }


        for (Path path : datePaths) {

            List<Path> pathList = HDFSFileUtil.listFile(fs, path);

            for (Path item : pathList) {
                if (fs.getFileStatus(item).getLen() > 0 && item.toString().endsWith("parquet")) {
                    retList.add(item);
                }
            }
        }

        return retList;
    }


    public static boolean validate(String path, Calendar startTime,
                                   Calendar endTime) {
        boolean ret = false;

        String[] pathArray = path.split("/");
        // menlo_tos_pc_display_text.20150528015500.adbjsd124.0.lzo
        String fileName = pathArray[pathArray.length - 1];

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        String[] fileNameArray = fileName.split(".");
        String fileTime = fileNameArray[fileNameArray.length - 4];

        try {

            Date fileDate = (Date) timeFormat.parse(fileTime);

            Calendar fileCal = Calendar.getInstance();

            fileCal.setTime(fileDate);

            if (fileCal.compareTo(startTime) > 0
                    && fileCal.compareTo(endTime) < 0) {
                ret = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static boolean isValidFile(Calendar startTime, Calendar endTime,
                                       String fileName, String fileNamePrefix) {
        int minLen = fileNamePrefix.length() + 1
                + FIlE_DATE_TIME_FORMAT_STRING.length();
        if (fileName.length() < minLen) {
            return false;
        }
        if (!fileName.toLowerCase().startsWith(fileNamePrefix.toLowerCase())) {
            return false;
        }

        String fileTime = fileName.substring(fileNamePrefix.length() + 1,
                fileNamePrefix.length() + 1 + 12);
        if (fileTime.compareTo(DateUtil.getStrFromDate(startTime.getTime(),
                FIlE_DATE_TIME_FORMAT_STRING)) >= 0
                && fileTime.compareTo(DateUtil.getStrFromDate(endTime.getTime(),
                FIlE_DATE_TIME_FORMAT_STRING)) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void addStdInputPath(Job job, FileSystem fs,
                                       Path tableDataBasePath, String tableName, Calendar startTime,
                                       Calendar endTime) throws Exception {
        for (Path path : HDFSFileUtil.getStdInputPath(fs, tableDataBasePath,
                tableName, startTime, endTime)) {
            FileInputFormat.addInputPath(job, path);
        }
    }

    public static void addLzoStdInputPath(Job job, FileSystem fs,
                                          Path tableDataBasePath, String tableName, Calendar startTime,
                                          Calendar endTime) throws Exception {
        for (Path path : HDFSFileUtil.getStdInputPath(fs, tableDataBasePath,
                tableName, startTime, endTime)) {
            if (fs.getFileStatus(path).getLen() > 0) {
                //LzoTextInputFormat.addInputPath(job, path);
            } else {
                LOGGER.info("skip empty file:" + path.getName());
            }
        }
    }

    public static Configuration getConfiguration(String cluster)
            throws MalformedURLException {
        return getConfiguration();
    }

    public static Configuration getConfiguration() throws MalformedURLException {
        Configuration conf = new Configuration();

        /*
        for (String resource : hadoopConfigurationResourceUrl) {
            conf.addResource(new URL(resource));
        }
        */

        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf/core-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf/hdfs-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf/yarn-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf/mapred-site.xml"));

        conf.set("mapred.remote.os", "Linux");
        conf.setBoolean(MRConfig.MAPREDUCE_APP_SUBMISSION_CROSS_PLATFORM, true);

        return conf;
    }

    public static Configuration getLocalConfiguration() throws MalformedURLException {
        Configuration conf = new Configuration();

        /*
        for (String resource : hadoopConfigurationResourceUrl) {
            conf.addResource(new URL(resource));
        }
        */

        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.local/core-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.local/hdfs-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.local/yarn-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.local/mapred-site.xml"));

        conf.set("mapred.remote.os", "Linux");
        conf.setBoolean(MRConfig.MAPREDUCE_APP_SUBMISSION_CROSS_PLATFORM, true);

        return conf;
    }

    public static Configuration getRemoteConfiguration() throws MalformedURLException {
        Configuration conf = new Configuration();

        /*
        for (String resource : hadoopConfigurationResourceUrl) {
            conf.addResource(new URL(resource));
        }
        */

        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.remote/core-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.remote/hdfs-site.xml"));
//        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.remote/yarn-site.xml"));
        conf.addResource(new HDFSFileUtil().getClass().getResourceAsStream("/yarn-conf.remote/mapred-site.xml"));

        conf.set("mapred.remote.os", "Linux");
        conf.setBoolean(MRConfig.MAPREDUCE_APP_SUBMISSION_CROSS_PLATFORM, true);

        return conf;
    }
}