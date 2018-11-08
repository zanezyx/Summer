package com.deter.TaxManager.model;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.List;

/**
 * Created by yuxinz on 2017/7/12.
 */

@DatabaseTable(tableName = Device.TABLE_NAME)
public class Device {

    public static final String TABLE_NAME = "t_device";

    public static final int DEAUTH_STATE_NONE = 0;
    public static final int DEAUTH_STATE_RUNNING = 1;
    public static final int DEAUTH_STATE_SUCCEEDED = 2;
    public static final int DEAUTH_STATE_FAIL = 3;

    public static final int MITM_STATE_NONE = 0;
    public static final int MITM_STATE_RUNNING = 1;
    public static final int MITM_STATE_SUCCEEDED = 2;
    public static final int MITM_STATE_FAIL = 3;

    //空的构造方法一定要有，否则数据库会创建失败
    public Device() {
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "task_id")
    private long taskId;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "role")
    private String role;//ap or station

    @DatabaseField(columnName = "ssid")
    private String ssid;

    @DatabaseField(columnName = "encryption")
    private String encryption;

    @DatabaseField(columnName = "class")
    private String dclass;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "time")
    private long time;

    @DatabaseField(columnName = "time_seen")
    private long time_seen;

    @DatabaseField(columnName = "fetch_time")
    private long fetchTime;

    @DatabaseField(columnName = "name")
    private String name;


    @DatabaseField(columnName = "description")
    private String description;


    @DatabaseField(columnName = "channel")
    private int channel;


    @DatabaseField(columnName = "state")
    private int state; //0:正常 1:断网 2:强连

    @DatabaseField(columnName = "deauth_state")
    private int deauthState; //0:无 1:正在deauth 2:deauth成功

    @DatabaseField(columnName = "mitm_state")
    private int mitmState; //0:无 1:正在mitm 2:mitm成功

    @DatabaseField(columnName = "distance")
    private float distance;

    @DatabaseField(columnName = "rssi")
    private int rssi;

    @DatabaseField(columnName = "speed")
    private int speed;

    @DatabaseField(columnName = "vender")
    private String vender;

    @DatabaseField(columnName = "heading")
    private String heading;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "angle")
    private int angle;//角度

    @DatabaseField(columnName = "inblacklist")
    private boolean inblacklist;

    @DatabaseField(columnName = "inwhitelist")
    private boolean inwhitelist;

    @DatabaseField(columnName = "compass")
    private int compass;

    @DatabaseField(columnName = "altitude")
    private int altitude;

    @DatabaseField(columnName = "latitude")
    private double latitude;

    @DatabaseField(columnName = "longitude")
    private double longitude;

    @DatabaseField(columnName = "phonetime")
    private double phonetime;

    @DatabaseField(columnName = "vendor_cn")
    private String vendorcn;

    @DatabaseField(columnName = "vendor_en")
    private String vendoren;

    @DatabaseField(columnName = "ip_addr")
    private String ipaddr;

    @DatabaseField(columnName = "conect_state")
    private String conectState;

    @DatabaseField(columnName = "ssid_mac")
    private String ssidMac;

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getIpInfoName() {
        return ipInfoName;
    }

    public void setIpInfoName(String ipInfoName) {
        this.ipInfoName = ipInfoName;
    }

    @DatabaseField(columnName = "ip_name")
    private String ipInfoName;


    private List<Identity> idList;//虚拟身份

    private List<URL> urlList;

    private List<Device> stations;//AP下的station

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }


    public List<Device> getStations() {
        return stations;
    }

    public void setStations(List<Device> stations) {
        this.stations = stations;
    }

    public boolean isInblacklist() {
        return inblacklist;
    }

    public void setInblacklist(boolean inblacklist) {
        this.inblacklist = inblacklist;
    }

    public boolean isInwhitelist() {
        return inwhitelist;
    }

    public void setInwhitelist(boolean inwhitelist) {
        this.inwhitelist = inwhitelist;
    }


    public long getTime_seen() {
        return time_seen;
    }

    public void setTime_seen(long time_seen) {
        this.time_seen = time_seen;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
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

    public void setLongitude(double longtitude) {
        this.longitude = longtitude;
    }

    public String getDclass() {
        return dclass;
    }

    public void setDclass(String dclass) {
        this.dclass = dclass;
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

    public int getCompass() {
        return compass;
    }

    public void setCompass(int compass) {
        this.compass = compass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPhonetime() {
        return phonetime;
    }

    public void setPhonetime(double phonetime) {
        this.phonetime = phonetime;
    }

    public String getVendorcn() {
        return vendorcn;
    }

    public void setVendorcn(String vendorcn) {
        this.vendorcn = vendorcn;
    }

    public String getVendoren() {
        return vendoren;
    }

    public void setVendoren(String vendoren) {
        this.vendoren = vendoren;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getDeauthState() {
        return deauthState;
    }

    public void setDeauthState(int deauthState) {
        this.deauthState = deauthState;
    }

    public int getMitmState() {
        return mitmState;
    }

    public void setMitmState(int mitmState) {
        this.mitmState = mitmState;
    }

    public long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }

    public String getConectState() {
        return conectState;
    }

    public void setConectState(String conectState) {
        this.conectState = conectState;
    }

    public String getSsidMac() {
        return ssidMac;
    }

    public void setSsidMac(String ssidMac) {
        this.ssidMac = ssidMac;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+role+" tid:"+taskId+" " ;
        str += "\n";
        //str += " stations: "+ JsonUtils.objectToString(stations);
        return str;
    }

    public Device clone() throws CloneNotSupportedException {
        return (Device) super.clone();
    }
}



