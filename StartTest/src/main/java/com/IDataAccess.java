package com;

import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Collection;
import java.util.List;

/**
 * Created by terry.qian on 2016/2/19.
 */
public interface IDataAccess {

    public <T extends IEntity> T get(Class<T> entityClass, String sql);

    public <T extends IEntity> List<T> getList(Class<T> entityClass, String sql);

    public <T extends IEntity> int delete(T entity);

    public <T extends IEntity> int insert(T entity);

    public <T extends IEntity> int update(T entity);

    public void execSql(String sql);

    public <T extends IEntity> int[][] batchInsert(String sql, final Collection<T> batchArgs, final int batchSize,
                                                   final ParameterizedPreparedStatementSetter<T> pss);

    public SqlRowSet getNativeRowSet(String sql);
}
