package dbaccess;

import com.ad.youlan.IEntity;

/**
 * Created by terry.qian on 2016/7/1.
 */
public class CreativeModel implements IEntity {
    private int id;
    private int group_id;
    private int ad_format;
    private String size_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getAd_format() {
        return ad_format;
    }

    public void setAd_format(int ad_format) {
        this.ad_format = ad_format;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }
}
