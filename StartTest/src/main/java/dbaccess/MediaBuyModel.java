package dbaccess;

import com.ad.youlan.IEntity;

import java.util.Date;

/**
 * Created by terry.qian on 2016/7/1.
 */
public class MediaBuyModel implements IEntity {
    private int id;
    private int campaign_id;
    private Date start_time;
    private Date end_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
