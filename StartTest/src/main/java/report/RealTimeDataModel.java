package report;

import com.ad.youlan.IEntity;

public class RealTimeDataModel implements IEntity {
    private String settledTime;
    private int adxId;
    private int mediaBuyId;
    private int creativeId;
    private int platform;
    private int flag;
    private int pv;
    private int clicks;
    private double bidCosts;
    private double bidTaxpaidcosts;
    private double bidSettlementcosts;

    public String getSettledTime() {
        return settledTime;
    }

    public RealTimeDataModel setSettledTime(String settledTime) {
        this.settledTime = settledTime;
        return this;
    }

    public int getAdxId() {
        return adxId;
    }

    public RealTimeDataModel setAdxId(int adxId) {
        this.adxId = adxId;
        return this;
    }

    public int getMediaBuyId() {
        return mediaBuyId;
    }

    public RealTimeDataModel setMediaBuyId(int mediaBuyId) {
        this.mediaBuyId = mediaBuyId;
        return this;
    }

    public int getCreativeId() {
        return creativeId;
    }

    public RealTimeDataModel setCreativeId(int creativeId) {
        this.creativeId = creativeId;
        return this;
    }

    public int getPlatform() {
        return platform;
    }

    public RealTimeDataModel setPlatform(int platform) {
        this.platform = platform;
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public RealTimeDataModel setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getPv() {
        return pv;
    }

    public RealTimeDataModel setPv(int pv) {
        this.pv = pv;
        return this;
    }

    public int getClicks() {
        return clicks;
    }

    public RealTimeDataModel setClicks(int clicks) {
        this.clicks = clicks;
        return this;
    }

    public double getBidCosts() {
        return bidCosts;
    }

    public RealTimeDataModel setBidCosts(double bidCosts) {
        this.bidCosts = bidCosts;
        return this;
    }

    public double getBidTaxpaidcosts() {
        return bidTaxpaidcosts;
    }

    public RealTimeDataModel setBidTaxpaidcosts(double bidTaxpaidcosts) {
        this.bidTaxpaidcosts = bidTaxpaidcosts;
        return this;
    }

    public double getBidSettlementcosts() {
        return bidSettlementcosts;
    }

    public RealTimeDataModel setBidSettlementcosts(double bidSettlementcosts) {
        this.bidSettlementcosts = bidSettlementcosts;
        return this;
    }
}
