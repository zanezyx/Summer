package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = Station.TABLE_NAME)
public class Station {

    public static final String TABLE_NAME = "t_station";

    //空的构造方法一定要有，否则数据库会创建失败
    public Station() {
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "deviceid")
    private long deviceid;

    @DatabaseField(columnName = "mac")
    private String mac;


    @DatabaseField(columnName = "ssid")
    private String ssid;

    @DatabaseField(columnName = "apid")
    private long apid;

    @DatabaseField(columnName = "apmac")
    private String apmac;

    @DatabaseField(columnName = "encryption")
    private String encryption;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "time")
    private String time;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "channel")
    private int channel;

    @DatabaseField(columnName = "state")
    private int state; //0:正常 1:断网 2:强连

    @DatabaseField(columnName = "distance")
    private int distance;

    @DatabaseField(columnName = "rssi")
    private int rssi;

    @DatabaseField(columnName = "speed")
    private int speed;

    @DatabaseField(columnName = "vender")
    private String vender;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "angle")
    private int angle;//角度


    @DatabaseField(columnName = "compassValue")
    private int compassValue;

    private Location location;

    private List<Identity> idList;//虚拟身份

    private List<URL> urlList;

    public static String getTableName() {
        return TABLE_NAME;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Identity> getIdList() {
        return idList;
    }

    public void setIdList(List<Identity> idList) {
        this.idList = idList;
    }

    public List<URL> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<URL> urlList) {
        this.urlList = urlList;
    }

    public int getCompassValue() {
        return compassValue;
    }

    public void setCompassValue(int compassValue) {
        this.compassValue = compassValue;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getApid() {
        return apid;
    }

    public void setApid(long apid) {
        this.apid = apid;
    }

    public String getApmac() {
        return apmac;
    }

    public void setApmac(String apmac) {
        this.apmac = apmac;
    }

    public long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(long deviceid) {
        this.deviceid = deviceid;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+distance+" "+angle+" "+compassValue+" "+vender ;
        return str;
    }
}



