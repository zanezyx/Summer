package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = StrikeAnalysis.TABLE_NAME)
public class StrikeAnalysis {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "status")
    private int status;

    @DatabaseField(columnName = "start_time")
    private long startTime;

    @DatabaseField(columnName = "stop_time")
    private long stopTime;

    public static final String TABLE_NAME = "t_strike_analysis";

    //空的构造方法一定要有，否则数据库会创建失败
    public StrikeAnalysis() {
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

    @Override
    public String toString() {
        String str = ""+id+" "+name+" "+status+" "+startTime+" "+stopTime;
        return str;
    }


}
