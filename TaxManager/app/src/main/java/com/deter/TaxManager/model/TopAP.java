package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = TopAP.TABLE_NAME)
public class TopAP {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "ssid")
    private String ssid;

    @DatabaseField(columnName = "type")
    private String type;

    @DatabaseField(columnName = "encryption")
    private String encryption;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "areadescription")
    private String areadescription;

    @DatabaseField(columnName = "time")
    private long time;

    public static final String TABLE_NAME = "t_topap";

    //空的构造方法一定要有，否则数据库会创建失败
    public TopAP() {
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAreadescription() {
        return areadescription;
    }

    public void setAreadescription(String areadescription) {
        this.areadescription = areadescription;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+ssid+" "+type+" "+encryption+" "+password+" "+description ;
        return str;
    }


}
