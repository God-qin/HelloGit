package dbaccess;

import com.ad.youlan.IEntity;

/**
 * Created by terry.qian on 2016/7/1.
 */
public class DspAdspaceModel implements IEntity {
    private int ad_space_id;
    private int supplier_id;
    private int site_id;
    private int media_category;
    private String site_name;

    public int getAd_space_id() {
        return ad_space_id;
    }

    public void setAd_space_id(int ad_space_id) {
        this.ad_space_id = ad_space_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public int getMedia_category() {
        return media_category;
    }

    public void setMedia_category(int media_category) {
        this.media_category = media_category;
    }

    public String getSite_name() {
        return site_name;
    }

    public DspAdspaceModel setSite_name(String site_name) {
        this.site_name = site_name;
        return this;
    }
}
