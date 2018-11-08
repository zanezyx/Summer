package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = AnalysisTask.TABLE_NAME)
public class AnalysisTask {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "analysis_id")
    private long analysisId;

    @DatabaseField(columnName = "task_id")
    private long taskId;

    @DatabaseField(columnName = "taskName")
    private String taskName;

    @DatabaseField(columnName = "task_start_time")
    private long taskStartTime;

    @DatabaseField(columnName = "task_stop_time")
    private long taskStopTime;

    @DatabaseField(columnName = "status")
    private int status;

    public static final String TABLE_NAME = "t_analysis_task";

    //空的构造方法一定要有，否则数据库会创建失败
    public AnalysisTask() {
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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public long getTaskStopTime() {
        return taskStopTime;
    }

    public void setTaskStopTime(long taskStopTime) {
        this.taskStopTime = taskStopTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+analysisId+" "+taskId+" "+taskName;
        return str;
    }


}
