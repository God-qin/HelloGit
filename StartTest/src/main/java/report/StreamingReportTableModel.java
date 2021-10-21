package report;

import com.IEntity;

import java.util.Date;

/**
 * Created by terry.qian on 2017/11/8.
 */
public class StreamingReportTableModel implements IEntity {
    private int id;
    private String table_name;
    private String topics;
    private int task_group;
    private int status;
    private Date create_time;
    private Date last_changed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTopics() {
        return topics;
    }

    public StreamingReportTableModel setTopics(String topics) {
        this.topics = topics;
        return this;
    }

    public int getTask_group() {
        return task_group;
    }

    public void setTask_group(int task_group) {
        this.task_group = task_group;
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
}
