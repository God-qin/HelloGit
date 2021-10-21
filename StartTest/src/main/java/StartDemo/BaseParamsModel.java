package StartDemo;

import com.ad.youlan.DateUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by terry.qian on 2016/3/3.
 */
public class BaseParamsModel {

    public BaseParamsModel(Map<String, Object> params) {
        this.startTime = (Calendar) params.get("startTime");
        this.startTimeStr = DateUtil.getStrFromCal(this.startTime, DateUtil.format_yyyyMMddHHmmss);
        this.endTime = (Calendar) params.get("endTime");
        this.endTimeStr = DateUtil.getStrFromCal(this.endTime, DateUtil.format_yyyyMMddHHmmss);
        this.settledTime = (Calendar) params.get("settledTime");

    }

    private Calendar startTime;
    private String startTimeStr;
    private Calendar endTime;
    private String endTimeStr;
    private Calendar settledTime;
    private String settledTimeStr;
    private String jobJarDir;
    private String mailTo;
    private String jobName;
    private Boolean isLocalModel;
    private String mapMemoryMb;
    private String reduceMemoryMb;


    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Calendar getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(Calendar settledTime) {
        this.settledTime = settledTime;
    }

    public String getSettledTimeStr() {
        return settledTimeStr;
    }

    public void setSettledTimeStr(String settledTimeStr) {
        this.settledTimeStr = settledTimeStr;
    }

    public String getJobJarDir() {
        return jobJarDir;
    }

    public void setJobJarDir(String jobJarDir) {
        this.jobJarDir = jobJarDir;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Boolean getLocalModel() {
        return isLocalModel;
    }

    public void setLocalModel(Boolean localModel) {
        isLocalModel = localModel;
    }

    public String getMapMemoryMb() {
        return mapMemoryMb;
    }

    public void setMapMemoryMb(String mapMemoryMb) {
        this.mapMemoryMb = mapMemoryMb;
    }

    public String getReduceMemoryMb() {
        return reduceMemoryMb;
    }

    public void setReduceMemoryMb(String reduceMemoryMb) {
        this.reduceMemoryMb = reduceMemoryMb;
    }

    public int getFilterCampaignIds() {
        return 1;
    }



}
