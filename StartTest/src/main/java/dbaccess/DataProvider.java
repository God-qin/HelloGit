//package dbaccess;
//
//import com.DataAccess;
//import com.DateUtil;
//import com.IDataAccess;
//import com.youlan.common.ConvertUtils;
//import com.youlan.common.ParamsModel;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//
///**
// * Created by terry.qian on 2016/6/28.
// */
//public class DataProvider extends BaseDataProvider implements IDataProvider {
//    private final static Logger LOGGER = LoggerFactory.getLogger(DataProvider.class.getName());
//
//    @Override
//    public List<CampaignStandardReportModel> getCampaignStandardReportConfig(int taskGroup) {
//
//        String sql = String.format(" SELECT \n" +
//                " ID,DIMENSION,RESULT_TABLE_NAME,QUOTAS,CREATE_TIME,LAST_CHANGED,STATUS,NOTE,TASK_GROUP  \n" +
//                " FROM DSP_CAMPAIGN_STANDARD_REPORT WHERE STATUS = 1 AND TASK_GROUP in(%d)", taskGroup);
//        LOGGER.info("sql: " + sql);
//        IDataAccess dataAccess = new DataAccess("com.mysql.jdbc.Driver", "dataSource-mysql-report-config");
//        return dataAccess.getList(CampaignStandardReportModel.class, sql);
//    }
//
//    @Override
//    public Map<Integer, Integer> getCreativeGroupMap() {
//
//        String sql = "SELECT ID,GROUP_ID FROM CREATIVE WHERE STATUS>=0";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<CreativeModel> creativeList = dataAccess.getList(CreativeModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (CreativeModel item : creativeList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getGroup_id());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, String> getCreativeSizeMap() {
//
//        String sql = "SELECT C.ID,C.AD_FORMAT,C.GROUP_ID,S.SIZE_NAME  FROM CREATIVE C JOIN DSP_AD_SIZE S ON C.SIZE_ID = S.SIZE_ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<CreativeModel> creativeList = dataAccess.getList(CreativeModel.class, sql);
//
//        Map<Integer, String> _result = new HashMap<Integer, String>();
//
//        for (CreativeModel item : creativeList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getSize_name());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getCreativeAdFormatMap() {
//
//        String sql = "SELECT C.ID,C.AD_FORMAT,C.GROUP_ID,S.SIZE_NAME  FROM CREATIVE C JOIN DSP_AD_SIZE S ON C.SIZE_ID = S.SIZE_ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<CreativeModel> creativeList = dataAccess.getList(CreativeModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (CreativeModel item : creativeList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getAd_format());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, String> getPubsiteMap() {
//
//        String sql = "SELECT SPACE.AD_SPACE_ID, SPACE.SITE_ID, SITE.SITE_NAME ,SPACE.SUPPLIER_ID " +
//                "FROM `DSP_AD_SPACE` SPACE , `DSP_AD_SITE` SITE WHERE SPACE.STATUS>=0 AND SPACE.SITE_ID = SITE.SITE_ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<DspAdspaceModel> adspaceList = dataAccess.getList(DspAdspaceModel.class, sql);
//
//        Map<Integer, String> _result = new HashMap<Integer, String>();
//
//        for (DspAdspaceModel item : adspaceList) {
//            if (!_result.containsKey(item.getAd_space_id())) {
//                _result.put(item.getAd_space_id(), item.getSite_id() + "|" +item.getSite_name());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getSupplierMap() {
//
//        String sql = "SELECT AD_SPACE_ID,SITE_ID,SUPPLIER_ID FROM `DSP_AD_SPACE`  WHERE STATUS>=0";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<DspAdspaceModel> adspaceList = dataAccess.getList(DspAdspaceModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (DspAdspaceModel item : adspaceList) {
//            if (!_result.containsKey(item.getAd_space_id())) {
//                _result.put(item.getAd_space_id(), item.getSupplier_id());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getAdspaceMediaTypeMap() {
//
//        String sql = "SELECT AD_SPACE_ID,SITE_ID,SUPPLIER_ID,MEDIA_CATEGORY FROM `DSP_AD_SPACE`  WHERE STATUS>=0";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<DspAdspaceModel> adspaceList = dataAccess.getList(DspAdspaceModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (DspAdspaceModel item : adspaceList) {
//            if (!_result.containsKey(item.getAd_space_id())) {
//                _result.put(item.getAd_space_id(), item.getMedia_category());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getCityStateMap() {
//
//        String sql = "SELECT C.ID,C.STATE,C.CITY_LEVEL,S.AREA FROM GEO_CITY C JOIN GEO_STATE S ON C.STATE = S.ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<GeoCity> cityList = dataAccess.getList(GeoCity.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (GeoCity item : cityList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getState());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getCityAreaMap() {
//
//        String sql = "SELECT C.ID,C.STATE,C.CITY_LEVEL,S.AREA FROM GEO_CITY C JOIN GEO_STATE S ON C.STATE = S.ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<GeoCity> cityList = dataAccess.getList(GeoCity.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (GeoCity item : cityList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getArea());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getCityLevelMap() {
//
//        String sql = "SELECT C.ID,C.STATE,C.CITY_LEVEL,S.AREA FROM GEO_CITY C JOIN GEO_STATE S ON C.STATE = S.ID";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<GeoCity> cityList = dataAccess.getList(GeoCity.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (GeoCity item : cityList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getCity_level());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getMediaBuyCampaignIdMap() {
//
//        String sql = "SELECT ID,CAMPAIGN_ID FROM MEDIA_BUY";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<MediaBuyModel> mediaBuyList = dataAccess.getList(MediaBuyModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (MediaBuyModel item : mediaBuyList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getCampaign_id());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Date getToDateStartTime(Calendar settledTime,List<Integer> filterCampaignIds) {
//
//        String campaignIdsStr = StringUtils.join(filterCampaignIds,",");
//
//        String sql = String.format("SELECT MIN(M.START_TIME) AS START_TIME FROM  CAMPAIGN C JOIN MEDIA_BUY M ON C.CAMPAIGN_ID=M.CAMPAIGN_ID \n" +
//                        "WHERE C.STATUS>=-100 AND C.START_TIME<='%s' AND C.END_TIME>='%s' AND C.START_TIME<=M.START_TIME AND C.CAMPAIGN_ID NOT IN(%s) "
//                , DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyy_MM_dd_HH_mm_ss)
//                , DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyy_MM_dd_HH_mm_ss),campaignIdsStr);
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess(DataAccess.DRIVER_MYSQL, "dataSource-mysql-core");
//        MediaBuyModel mediaBuy = dataAccess.get(MediaBuyModel.class, sql);
//
//        return mediaBuy.getStart_time();
//    }
//
//    @Override
//    public Map<Integer, Integer> getToDateValidMediaBuy(Calendar settledTime) {
//
//        String sql = String.format("SELECT DISTINCT M.ID,M.CAMPAIGN_ID FROM  CAMPAIGN C JOIN MEDIA_BUY M ON C.CAMPAIGN_ID=M.CAMPAIGN_ID \n" +
//                        "WHERE C.STATUS>=-100 AND C.START_TIME<='%s' AND C.END_TIME>='%s'"
//                , DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyy_MM_dd_HH_mm_ss)
//                , DateUtil.getStrFromCal(settledTime, DateUtil.format_yyyy_MM_dd_HH_mm_ss)
//        );
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<MediaBuyModel> mediaBuyList = dataAccess.getList(MediaBuyModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (MediaBuyModel item : mediaBuyList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getCampaign_id());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public List<ParamsModel> getParams(String target) {
//        String sql = "select * from dataops_params_transfer_emr_task where status = 1 and target = '" + target + "'";
//        IDataAccess dataAccess = new DataAccess();
//        List<ParamsModel> paramsModels = dataAccess.getList(ParamsModel.class, sql);
//        return paramsModels;
//    }
//
//    @Override
//    public Map<Integer, String> getAdspaceSizeMap() {
//
//        String sql = "SELECT AD_SPACE_ID AS ID,CONCAT(`WIDTH`,\"*\",`HEIGHT` ) AS SIZE_NAME  FROM `DSP_AD_SPACE` WHERE STATUS >=0";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<CreativeModel> creativeList = dataAccess.getList(CreativeModel.class, sql);
//
//        Map<Integer, String> _result = new HashMap<Integer, String>();
//
//        for (CreativeModel item : creativeList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), item.getSize_name());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<Integer, Integer> getAdspaceAdFormatMap() {
//
//        String sql = "SELECT AD_SPACE_ID AS ID,AD_FORMAT AS SIZE_NAME FROM DSP_AD_SPACE WHERE STATUS >=0";
//        LOGGER.info("sql: " + sql);
//
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<CreativeModel> creativeList = dataAccess.getList(CreativeModel.class, sql);
//
//        Map<Integer, Integer> _result = new HashMap<Integer, Integer>();
//
//        for (CreativeModel item : creativeList) {
//            if (!_result.containsKey(item.getId())) {
//                _result.put(item.getId(), ConvertUtils.parseToInt(item.getSize_name()));
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<String, String> getAppIdAppNameMap() {
//        String sql = "SELECT APP_ID,APP_NAME FROM APP_INFOMATION";
//        LOGGER.info("sql: " + sql);
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<AppModel> appList = dataAccess.getList(AppModel.class, sql);
//
//        Map<String, String> _result = new HashMap<String, String>();
//
//        for (AppModel item : appList) {
//            if (!_result.containsKey(item.getAppId())) {
//                _result.put(item.getAppId(), item.getAppName());
//            }
//        }
//
//        return _result;
//    }
//
//    @Override
//    public Map<String, String> getOsNameValueMap() {
//        String sql = "SELECT NAME,VALUE FROM OS_DICTIONARY";
//        LOGGER.info("sql: " + sql);
//        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-core");
//        List<OSModel> osList = dataAccess.getList(OSModel.class, sql);
//
//        Map<String, String> _result = new HashMap<String, String>();
//
//        for (OSModel item : osList) {
//            if (!_result.containsKey(item.getName())) {
//                _result.put(item.getName(), item.getValue());
//            }
//        }
//        return _result;
//    }
//}
