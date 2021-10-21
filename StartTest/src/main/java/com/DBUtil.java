package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by hugh on 2016/3/18.
 */
public class DBUtil implements Serializable{
    private JdbcTemplate jdbcTemplate;
    public DBUtil(String source){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        DataSource dataSource = (DataSource) context.getBean(source);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public <T> T get(Class<T> entityClass, String sql) {
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<T>(entityClass));
    }

    public <T> List<T> getList(Class<T> entityClass, String sql) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entityClass));
    }

    public void delete(String table, String condition) {
        String sql = "delete from " + table;
        if(!condition.equals("")){
            sql = "delete from " + table + " where " + condition;
        }
        jdbcTemplate.update(sql);
    }

    public void insert(String table, String[] fields, String[] values) {
        String sql = "";
        sql += "insert into " + table + "(";
        for(int i=0; i<fields.length; i++){
            sql += fields[i] + ",";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ") values(";
        for(int j=0; j<values.length; j++){
            sql += "'"+(values[j].equals("")?0:values[j])+"',";
        }
        sql = sql.substring(0,sql.length()-1) + ")";
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }

    public void insertOrUpdateAdd(String table, String[] fields, String[] values, String[] updateFields){
        String sql = "";
        sql += "insert into " + table + "(";
        for(int i=0; i<fields.length; i++){
            sql += fields[i] + ",";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ") values(";
        for(int j=0; j<values.length; j++){
            sql += "'"+(values[j].equals("")?0:values[j])+"',";
        }
        sql = sql.substring(0,sql.length()-1) + ")";
        sql += " on duplicate key update ";
        for(int k=0; k<updateFields.length; k++){
            sql += updateFields[k] + " = " + updateFields[k] + " + " + "values(" + updateFields[k] + "),";
        }
        sql = sql.substring(0,sql.length()-1);

        //System.out.println(sql);
        jdbcTemplate.update(sql);
//        int isSuccess = jdbcTemplate.update(sql);
//        System.out.println("sql isSuccess: " + isSuccess);
    }

    public String insertOrUpdateAddBatch(String table, String[] fields, List<String[]> values, String[] updateFields, boolean isExecute) {
        long startTime = System.currentTimeMillis();

        String sql = "";
        sql += "insert into " + table + "(";
        for (int i = 0; i < fields.length; i++) {
            sql += fields[i] + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ") values";

        StringBuilder sb = new StringBuilder(sql);

        for (int i = 0; i < values.size(); i++) {
            String[] value = values.get(i);

            sb.append("(");
            for (int j = 0; j < value.length; j++) {

                sb.append("'");
                sb.append((value[j].equals("") ? 0 : value[j]));
                sb.append("'");

                if (j != value.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");

            if (i != values.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(" on duplicate key update ");
        for (int k = 0; k < updateFields.length; k++) {
            sb.append(updateFields[k] + " = " + updateFields[k] + " + " + "values(" + updateFields[k] + "),");
        }
        sql = sb.substring(0, sb.length() - 1);

        if (isExecute) {
            int isSuccess = jdbcTemplate.update(sql);
            System.out.println("sql isSuccess: " + isSuccess);
        }

        long cost = System.currentTimeMillis() - startTime;
        System.out.println("cost:" + cost + "mseconds,count:" + values.size() + ",table:" + table);

        return sql;
    }

    public void replaceInto(String table, String[] fields, String[] values,String[] updateFields) {

        StringBuilder sb = new StringBuilder("");
        sb.append("insert into ");
        sb.append(table);
        sb.append("(");
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]);

            if (i != fields.length - 1) {
                sb.append(",");
            }
        }

        sb.append(") values(");
        for (int j = 0; j < values.length; j++) {
            sb.append("'");
            sb.append((values[j].equals("") ? 0 : values[j]));

            if (j != values.length - 1) {
                sb.append("',");
            }
        }
        sb.append(")");
        sb.append(" on duplicate key update ");
        for (int k = 0; k < updateFields.length; k++) {
            sb.append(updateFields[k]);
            sb.append("=");
            sb.append("values(");
            sb.append(updateFields[k]);
            sb.append(")");

            if (k != updateFields.length - 1) {
                sb.append(",");
            }
        }

        String sql = sb.toString();

        int isSuccess = jdbcTemplate.update(sql);
        System.out.println("sql isSuccess: " + isSuccess);
    }

    public void update(String table, String[] fields, Objects[] values, String[] paramNames, Objects[] paramValues) {

    }

    public SqlRowSet getNativeRowSet(String sql) {
        return jdbcTemplate.queryForRowSet(sql);
    }

    public void batchInsert(String table, String[] fields, List<Object[]> parameters) {
        String sql = "insert into " + table + "(";
        String values ="";
        for(int i=0; i<fields.length; i++){
            sql += fields[i] + ",";
            values += "?,";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ") values(";
        values = values.substring(0,values.length()-1) + ")";
        sql += values;
        jdbcTemplate.batchUpdate(sql, parameters);
    }

    public List<String> getColumns(String table, String schema) {
        try {
            String sql = "select column_name from information_schema.columns where table_name = '" + table + "' and table_schema = '" + schema + "'";
            return jdbcTemplate.queryForList(sql, String.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void executeSql(String sql) throws Exception{
        int rows = jdbcTemplate.update(sql);
        System.out.println();
        if(rows <= 0){
            throw new Exception("Affect the number of rows 0");
        }
    }
}
