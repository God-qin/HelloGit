package dbaccess;

import com.ad.youlan.IEntity;

/**
 * Created by hugh on 2016/11/21.
 */
public class AppModel implements IEntity {
    private String appId;
    private String appName;

    public String getAppId() {
        return appId;
    }

    public AppModel setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public AppModel setAppName(String appName) {
        this.appName = appName;
        return this;
    }
}
