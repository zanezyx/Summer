package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = Identity.TABLE_NAME)
public class Identity {

    public static final String TABLE_NAME = "t_identity";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "deviceid")
    private long deviceid;

    @DatabaseField(columnName = "mac")
    private String mac;


    @DatabaseField(columnName = "time")
    private long time;


    @DatabaseField(columnName = "tag")
    private String tag;

    @DatabaseField(columnName = "value")
    private String value;


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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Identity() {
    }

    @Override
    public String toString() {
        String str = ""+id+" "+tag+" "+value ;
        str += "\n";
        return str;
    }
}
