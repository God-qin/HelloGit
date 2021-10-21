package report;

import com.IEntity;

import java.util.Date;

/**
 * Created by terry.qian on 2017/11/8.
 */
public class StreamingReportColumnModel implements IEntity{

    private int id;
    private int position;
    private String column_name;
    private int column_category;
    private int column_type;
    private int column_length;
    private String map_column_name;
    private int status;
    private Date create_time;
    private Date last_changed;
    private int table_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public int getColumn_category() {
        return column_category;
    }

    public void setColumn_category(int column_category) {
        this.column_category = column_category;
    }

    public int getColumn_type() {
        return column_type;
    }

    public void setColumn_type(int column_type) {
        this.column_type = column_type;
    }

    public int getColumn_length() {
        return column_length;
    }

    public void setColumn_length(int column_length) {
        this.column_length = column_length;
    }

    public String getMap_column_name() {
        return map_column_name;
    }

    public void setMap_column_name(String map_column_name) {
        this.map_column_name = map_column_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getLast_changed() {
        return last_changed;
    }

    public void setLast_changed(Date last_changed) {
        this.last_changed = last_changed;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }
}
