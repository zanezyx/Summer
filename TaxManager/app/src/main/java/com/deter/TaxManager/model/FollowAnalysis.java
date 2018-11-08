package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = FollowAnalysis.TABLE_NAME)
public class FollowAnalysis {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "status")
    private int status;

    @DatabaseField(columnName = "start_time")
    private long startTime;

    @DatabaseField(columnName = "stop_time")
    private long stopTime;

    @DatabaseField(columnName = "task_ids")
    private String taskIds;

    public static final String TABLE_NAME = "t_follow_analysis";

    //空的构造方法一定要有，否则数据库会创建失败
    public FollowAnalysis() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+taskIds+" "+startTime+" "+stopTime;
        return str;
    }


}
