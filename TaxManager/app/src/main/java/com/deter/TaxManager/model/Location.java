package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = Location.TABLE_NAME)

public class Location {

    public static final String TABLE_NAME = "t_location";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "deviceid")
    private long deviceid;

    @DatabaseField(columnName = "time")
    private long time;

    @DatabaseField(columnName = "latitude")
    private double latitude;//纬度

    @DatabaseField(columnName = "longitude")
    private double longitude;//经度

    @DatabaseField(columnName = "address_cn")
    private String addresscn;

    @DatabaseField(columnName = "address_en")
    private String addressen;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(long deviceid) {
        this.deviceid = deviceid;
    }

    public String getAddresscn() {
        return addresscn;
    }

    public void setAddresscn(String addresscn) {
        this.addresscn = addresscn;
    }

    public String getAddressen() {
        return addressen;
    }

    public void setAddressen(String addressen) {
        this.addressen = addressen;
    }

    public Location() {
    }
}
