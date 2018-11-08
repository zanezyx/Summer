package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/8/10.
 */
@DatabaseTable(tableName = Task.TABLE_NAME)
public class Task {

    public static final String TABLE_NAME = "t_task";

    //空的构造方法一定要有，否则数据库会创建失败
    public Task() {
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "latitude")
    private double latitude;

    @DatabaseField(columnName = "longitude")
    private double longitude;

    @DatabaseField(columnName = "start_time")
    private long startTime;

    @DatabaseField(columnName = "stop_time")
    private long stopTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        String str = ""+id+" "+startTime+" "+stopTime+" "+address;
        return str;
    }


}
