package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * Created by terry.qian on 2016/2/22.
 */
public class DataAccess implements IDataAccess {
    public final static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public final static String DRIVER_ORACLE = "oracle.jdbc.display.OracleDriver";

    private JdbcTemplate jdbcTemplate;

    public DataAccess() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        DataSource dataSource = (DataSource) context.getBean("dataSource-mysql");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    com.mysql.jdbc.Driver
    oracle.jdbc.display.OracleDriver
     */
    public DataAccess(String driver) {

        DataSource dataSource;
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        if (driver != null && driver.toLowerCase().equals("oracle.jdbc.display.OracleDriver".toLowerCase())) {
            dataSource = (DataSource) context.getBean("dataSource-oracle");
        } else {
            dataSource = (DataSource) context.getBean("dataSource-mysql");
        }

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
   com.mysql.jdbc.Driver
   oracle.jdbc.display.OracleDriver
    */
    public DataAccess(String driver, String source) {

        DataSource dataSource;
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        if (driver != null && driver.toLowerCase().equals("oracle.jdbc.display.OracleDriver".toLowerCase())) {
            dataSource = (DataSource) context.getBean(source);
        } else {
            dataSource = (DataSource) context.getBean(source);
        }

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public <T extends IEntity> T get(Class<T> entityClass, String sql) {

        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(entityClass);
        rowMapper.setPrimitivesDefaultedForNullValue(true);

        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    @Override
    public <T extends IEntity> List<T> getList(Class<T> entityClass, String sql) {

        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(entityClass);
        rowMapper.setPrimitivesDefaultedForNullValue(true);

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public <T extends IEntity> int delete(T entity) {
        return 0;
    }

    @Override
    public <T extends IEntity> int insert(T entity) {
        return 0;
    }

    @Override
    public <T extends IEntity> int update(T entity) {
        return 0;
    }

    @Override
    public void execSql(String sql) {
        jdbcTemplate.execute(sql);
    }

    @Override
    public <T extends IEntity> int[][] batchInsert(String sql, Collection<T> batchArgs, int batchSize,
                                                   ParameterizedPreparedStatementSetter<T> pss) {
        return jdbcTemplate.batchUpdate(sql, batchArgs, batchSize, pss);
    }

    @Override
    public SqlRowSet getNativeRowSet(String sql) {
        return jdbcTemplate.queryForRowSet(sql);
    }
}
