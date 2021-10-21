//package report;
//
//import com.ad.youlan.DataAccess;
//import com.ad.youlan.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import taskscheduler.core.InterruptableTask;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MergeRealTimeDataTask extends InterruptableTask{
//    private final static Logger LOGGER = LoggerFactory.getLogger(MergeRealTimeDataTask.class.getName());
//    @Override
//    public void execute(Map<String, Object> map) throws Exception {
//        Calendar settledTime = (Calendar) map.get("settledTime");
//        Calendar endTime = (Calendar) map.get("endTime");
//        String delay = (String) map.get("delay");
//
//        LOGGER.info("delay: " + delay);
//
//        Calendar startTime = (Calendar) settledTime.clone();
//        Calendar endTime_ = (Calendar) settledTime.clone();
//
//        if(delay.endsWith("d")){
//            startTime.set(Calendar.DATE, settledTime.get(Calendar.DATE) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//            endTime_.set(Calendar.DATE, endTime.get(Calendar.DATE) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//        }else if(delay.endsWith("h")){
//            startTime.set(Calendar.HOUR, settledTime.get(Calendar.HOUR) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//            endTime_.set(Calendar.HOUR, endTime.get(Calendar.HOUR) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//        }else if(delay.endsWith("m")){
//            startTime.set(Calendar.MINUTE, settledTime.get(Calendar.MINUTE) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//            endTime_.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE) - Integer.parseInt(delay.substring(0,delay.length()-1)));
//        }
//
//
//        String _startTime = DateUtil.getStrFromCal(startTime, DateUtil.format_yyyy_MM_dd_HH_mm_ss);
//        String _endTime = DateUtil.getStrFromCal(endTime_, DateUtil.format_yyyy_MM_dd_HH_mm_ss);
//
//        LOGGER.info("_startTime: " + _startTime);
//        LOGGER.info("_endTime: " + _endTime);
//
//        DataAccess dataAccess = new DataAccess(null, "dataSource-mysql-report");
//        String mergeSql = "select date_format(`settled_time`, '%Y-%m-%d %H') as settled_time, `adx_id`, `media_buy_id`, " +
//                "`creative_id`, flag, platform, sum(`pv`) as pv, sum(`clicks`) as clicks, sum(`bid_costs`) as bid_costs, " +
//                "sum(`bid_settlementcosts`) as bid_settlementcosts, " +
//                "sum(`bid_taxpaidcosts`) as bid_taxpaidcosts from test_m_report_rtb_hourly_b where settled_time >= '" + _startTime + "' and " +
//                "`settled_time` < '" + _endTime + "' and merge_flag = 0 group by date_format(`settled_time`, '%Y-%m-%d %H'), `adx_id`, `media_buy_id`, `creative_id`, " +
//                "`flag`, `platform`";
//
//        LOGGER.info("mergeSql: " + mergeSql);
//
//        List<RealTimeDataModel> list = dataAccess.getList(RealTimeDataModel.class, mergeSql);
//
//        LOGGER.info("list.size : " + list.size());
//
//        String insertSql = "insert into test_m_report_rtb_hourly_b(settled_time,adx_id,media_buy_id,creative_id,flag," +
//                "platform,pv,clicks,bid_costs,bid_settlementcosts,bid_taxpaidcosts,merge_flag) values('%s',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,1)";
//
//        for(RealTimeDataModel dataModel : list){
//            String is = String.format(insertSql, dataModel.getSettledTime()+":00:00", dataModel.getAdxId(),dataModel.getMediaBuyId(),
//                    dataModel.getCreativeId(),dataModel.getFlag(),dataModel.getPlatform(),dataModel.getPv(),dataModel.getClicks(),
//                    dataModel.getBidCosts(),dataModel.getBidSettlementcosts(),dataModel.getBidTaxpaidcosts());
//            LOGGER.info("insertSql: " + is);
//            dataAccess.execSql(is);
//        }
//
//        String deleteSql = "delete from test_m_report_rtb_hourly_b where settled_time >= '" + _startTime + "' and " +
//                "settled_time < '" + _endTime + "' and merge_flag = 0";
//        LOGGER.info("deleteSql: " + deleteSql);
//
//        dataAccess.execSql(deleteSql);
//
//    }
//
//    @Override
//    public double getProgress() {
//        return 0;
//    }
//
//    public static void main(String[] args) throws Exception{
//        Map map = new HashMap();
//        map.put("settledTime", DateUtil.getCalFromStr("2018-01-13 00:00:00", DateUtil.format_yyyy_MM_dd_HH_mm_ss));
//        map.put("endTime", DateUtil.getCalFromStr("2018-01-14 00:00:00", DateUtil.format_yyyy_MM_dd_HH_mm_ss));
//        map.put("delay","1d");
//
//        new MergeRealTimeDataTask().execute(map);
//    }
//}
