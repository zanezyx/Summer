package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = URL.TABLE_NAME)
public class URL {

    public static final String TABLE_NAME = "t_url";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "deviceid")
    private long deviceid;

    @DatabaseField(columnName = "mac")
    private String mac;


    @DatabaseField(columnName = "tag")
    String tag;

    @DatabaseField(columnName = "url")
    String url;

    @DatabaseField(columnName = "time")
    long time;

    public URL() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+url ;
        str += "\n";
        return str;
    }
}
