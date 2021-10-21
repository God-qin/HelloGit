package com.ad.youlan;

import com.HDFSFileUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.util.Calendar;

/**
 * Created by terry.qian on 2016/2/18.
 */
public class StdPathFilter implements PathFilter {

    private static final String FIlE_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmm";

    private String fileNamePrefix;
    private Calendar startTime;
    private Calendar endTime;
    private FileSystem fs;

    public StdPathFilter(String tableName, Calendar startTime, Calendar endTime) {
        try {
            Configuration conf = HDFSFileUtil.getConfiguration();
            fs = FileSystem.get(conf);
            this.fileNamePrefix = tableName;
            this.startTime = startTime;
            this.endTime = endTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean accept(Path path) {
        String fileName = path.getName();
        try {
            if (this.fs.getFileStatus(path).getLen() == 0) {
                System.out.println("skip empty file:" + fileName);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.isValidFile(fileName, fileNamePrefix)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidFile(String fileName, String fileNamePrefix) {
        int minLen = fileNamePrefix.length() + 1 + FIlE_DATE_TIME_FORMAT_STRING.length();
        if (fileName.length() < minLen) {
            return false;
        }
        if (!fileName.toLowerCase().startsWith(fileNamePrefix.toLowerCase())) {
            return false;
        }

        String fileTime = fileName.substring(fileNamePrefix.length() + 1, fileNamePrefix.length() + 1 + 12);
        if (fileTime.compareTo(DateUtil.getStrFromDate(startTime.getTime(), FIlE_DATE_TIME_FORMAT_STRING)) >= 0
                && fileTime.compareTo(DateUtil.getStrFromDate(endTime.getTime(), FIlE_DATE_TIME_FORMAT_STRING)) < 0) {
            return true;
        } else {
            return false;
        }
    }
}