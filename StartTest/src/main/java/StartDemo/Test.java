package StartDemo;

import com.ad.youlan.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
    private BaseParamsModel paramsModel;
    static Calendar settledTime = DateUtil.getCalFromStr("2016-11-16 00:00:00", DateUtil.format_yyyy_MM_dd_HH_mm_ss);

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时 mm 分 ss 秒");
        try {
            Date parse = simpleDateFormat.parse("2020 年 01 月 11 日 16 时 24 分 43 秒");

            System.out.println("--------------------simpleDateFormat.parse-------------------");
            System.out.println(parse);
            Date date = new Date(1l);

            System.out.println("----------------date-----------------------");
            System.out.println(date);
            Calendar calFromDate1 = StartDemo.DateUtil.getCalFromDate(date);
            Date time = calFromDate1.getTime();

            System.out.println("-----------------time----------------------");
            System.out.println(time);

            System.out.println("----------------calFromDate1-----------------------");
            System.out.println(calFromDate1);
            Calendar calFromDate = StartDemo.DateUtil.getCalFromDate(parse);

            System.out.println("----------------calFromDate-----------------------");
            System.out.println(calFromDate);

            Calendar _result = Calendar.getInstance();

            System.out.println("----------------_result-----------------------");
            System.out.println(_result);
            _result.setTime(date);

            String strFromCal = DateUtil.getStrFromCal(_result, DateUtil.format_yyyy_MM_dd_HH_mm_ss);
            System.out.println("----------------strFromCal-----------------------");
            System.out.println(strFromCal);

            Calendar startTime = Calendar.getInstance();
            startTime.set(2019, 05, 25, 0, 0);
            Calendar endTime = Calendar.getInstance();
//        endTime.set(2018, 12, 31, 0, 0);
            endTime.set(2019, 05, 30, 0, 0);


            String format = "yyyyMMdd,HH,mm";
            //while (startTime.compareTo(endTime) < 0){
                String currentDate = DateUtil.getStrFromCal(startTime, format);
                String dt = currentDate.split(",")[0];
                String hh = currentDate.split(",")[1];
                String mm = currentDate.split(",")[2];
            System.out.println("------------------------------------------------");
            System.out.println(startTime.toString());
            System.out.println("-------------------MINUTE-----------------------------");
            startTime.add(Calendar.MINUTE, 5);
            System.out.println(startTime.toString());
            System.out.println(currentDate);
                System.out.println(dt);
                System.out.println(hh);
                System.out.println(mm);

               // String dropPartitionSql = String.format(dropSql, dt, hh, mm);
               // String addPartitionsSql = String.format(addSql, dt, hh,mm, dt, hh,mm);
                //System.out.println(addPartitionsSql);
                startTime.add(Calendar.MINUTE, 5);
           // }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Date date = new Date();
    //
        //Calendar c1 = Calendar.getInstance();
        //Date time = c1.getTime();
        //Calendar Time = StartDemo.DateUtil.getCalFromDate(date);
        //System.out.println(time);
        //System.out.println(date);
    }





}
