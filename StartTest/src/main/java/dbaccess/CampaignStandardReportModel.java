package dbaccess;

import com.IEntity;

import java.util.Date;

/**
 * Created by terry.qian on 2016/6/28.
 */
public class CampaignStandardReportModel implements IEntity{

    private int id;
    private String dimension;
    private String result_table_name;
    private String quotas;
    private Date create_time;
    private Date last_changed;
    private int status;
    private String note;
    private int task_group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getResult_table_name() {
        return result_table_name;
    }

    public void setResult_table_name(String result_table_name) {
        this.result_table_name = result_table_name;
    }

    public String getQuotas() {
        return quotas;
    }

    public void setQuotas(String quotas) {
        this.quotas = quotas;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTask_group() {
        return task_group;
    }

    public void setTask_group(int task_group) {
        this.task_group = task_group;
    }
}
