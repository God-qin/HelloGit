package dbaccess;

import java.util.Map;

/**
 * Created by terry.qian on 2016/7/29.
 */
public interface IBaseDataProvider {
    Map<String, Map<String, String>> getTableColumnTypeMap(String tableSchema);
}
