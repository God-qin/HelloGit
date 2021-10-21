package dbaccess;

import com.ad.youlan.IEntity;

/**
 * Created by terry.qian on 2016/7/5.
 */
public class GeoCity implements IEntity {
    private int id;
    private int state;
    private int city_level;
    private int area;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCity_level() {
        return city_level;
    }

    public void setCity_level(int city_level) {
        this.city_level = city_level;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
