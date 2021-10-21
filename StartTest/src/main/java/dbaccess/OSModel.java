package dbaccess;

import com.ad.youlan.IEntity;

/**
 * Created by hugh on 2016/11/23.
 */
public class OSModel implements IEntity {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public OSModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public OSModel setValue(String value) {
        this.value = value;
        return this;
    }
}
