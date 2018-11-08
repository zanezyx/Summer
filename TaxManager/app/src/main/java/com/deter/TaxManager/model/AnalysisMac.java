package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = AnalysisMac.TABLE_NAME)
public class AnalysisMac {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "analysis_id")
    private long analysisId;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "times")
    private int times;

    @DatabaseField(columnName = "task_ids")
    private String taskIds;

    public static final String TABLE_NAME = "t_analysis_mac";

    private List<Integer> taskIdList;

    //空的构造方法一定要有，否则数据库会创建失败
    public AnalysisMac() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(long analysisId) {
        this.analysisId = analysisId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public List<Integer> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(List<Integer> taskIdList) {
        this.taskIdList = taskIdList;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+analysisId+" "+mac+" "+taskIds+times;
        return str;
    }


}
