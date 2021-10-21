package dbaccess;

import com.DataAccess;
import com.IDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by terry.qian on 2016/7/29.
 */
public abstract class BaseDataProvider implements IBaseDataProvider {
    protected final static Logger LOGGER = LoggerFactory.getLogger(BaseDataProvider.class.getName());

    @Override
    public Map<String, Map<String, String>> getTableColumnTypeMap(String tableSchema) {

        String sql = String.format("SELECT TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='%s'", tableSchema);
        LOGGER.info("sql: " + sql);

        IDataAccess dataAccess = new DataAccess("", "dataSource-mysql-report");
        List<TableSchema> schemaList = dataAccess.getList(TableSchema.class, sql);

        Map<String, Map<String, String>> _result = new HashMap<>();

        for (TableSchema item : schemaList) {

            if (!_result.containsKey(item.getTable_name().toUpperCase())) {

                Map<String, String> columnMap = new HashMap<>();
                _result.put(item.getTable_name().toUpperCase(), columnMap);
            }

            _result.get(item.getTable_name().toUpperCase()).put(item.getColumn_name().toUpperCase(), item.getData_type().toUpperCase());
        }

        return _result;
    }
}
