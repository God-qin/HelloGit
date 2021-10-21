package com.ad.youlan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by terry.qian on 2016/2/18.
 */
public class DateUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DateUtil.class.getName());

    public static String format_yyyy_MM_dd="yyyy-MM-dd";
    public static String format_yyyyMMdd="yyyyMMdd";
    public static String format_yyyyMMddHHmm="yyyyMMddHHmm";
    public static String format_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static String format_MM_dd_yyyy_HH_mm_ss = "MM/dd/yyyy HH:mm:ss";
    public static String format_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static String format_HH = "HH";
    public static String format_mm = "mm";

    public static Calendar getCalFromDate(Date date) {
        Calendar _result = Calendar.getInstance();

        _result.setTime(date);

        return _result;
    }

    public static Date getDateFromCal(Calendar calendar) {
        return calendar.getTime();
    }

    public static String getStrFromCal(Calendar calendar, String format) {
        String _result;

        _result = getStrFromDate(calendar.getTime(), format);

        return _result;
    }

    public static synchronized String getStrFromDate(Date date, String format) {
        String _result;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        _result = simpleDateFormat.format(date);

        return _result;
    }

    public static Date getDateFromStr(String str, String format) {

        Date _result = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            simpleDateFormat.setLenient(false);
            _result = simpleDateFormat.parse(str);

        } catch (Exception e) {
            LOGGER.error("getDateFromStr Exception:" + e.toString());
        }

        return _result;
    }

    public static Calendar getCalFromStr(String str, String format) {

        Date date = getDateFromStr(str, format);

        Calendar _result = Calendar.getInstance();
        _result.setTime(date);

        return _result;
    }

    /*
    yyyyMMddHHmmss
     */
    public static String getCurrentTime() {
        return getStrFromCal(Calendar.getInstance(), format_yyyyMMddHHmmss);
    }

    public static Calendar getCalFromDate(Calendar toDateStartTime) {
     return null;
    }
}
