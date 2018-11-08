package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = SpecialDevice.TABLE_NAME)
public class SpecialDevice {

    public static final String TABLE_NAME = "t_special_device";

    //空的构造方法一定要有，否则数据库会创建失败
    public SpecialDevice() {
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "role")
    private String role;//ap or station

    @DatabaseField(columnName = "ssid")
    private String ssid;

    @DatabaseField(columnName = "encryption")
    private String encryption;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "time_inserted")
    private long timeInserted;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "vendercn")
    private String vendercn;

    @DatabaseField(columnName = "venderen")
    private String venderen;

    @DatabaseField(columnName = "special_flag")
    private int specialFlag;//0:白名单 1：黑名单

    private boolean isOnLine;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeInserted() {
        return timeInserted;
    }

    public void setTimeInserted(long timeInserted) {
        this.timeInserted = timeInserted;
    }

    public int getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(int specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getVendercn() {
        return vendercn;
    }

    public void setVendercn(String vendercn) {
        this.vendercn = vendercn;
    }

    public String getVenderen() {
        return venderen;
    }

    public void setVenderen(String venderen) {
        this.venderen = venderen;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+role+" "+specialFlag;
        return str;
    }
}



