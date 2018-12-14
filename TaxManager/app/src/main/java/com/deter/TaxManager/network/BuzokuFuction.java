package com.deter.TaxManager.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DownloadUtil;
import com.deter.TaxManager.utils.Log;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.utils.JsonUtils;
import com.deter.TaxManager.utils.RandomUtils;
import com.deter.TaxManager.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yuxinz on 2017/7/20.
 */

public class BuzokuFuction {
//
//    private Task currTask;
//    private long startTime;
//    private boolean startFlag;
//
//    private static BuzokuFuction instance;
//    private Context context;
//
//    public static final int FUCTION_LISTENER_START = 0;
//    public static final int FUCTION_LISTENER_STOP = 1;
//    public static final int FUCTION_LISTENER_STATE = 2;
//    public static final int FUCTION_LISTEN_RESTART = 3;
//    public static final int FUCTION_SYSTEM_REBOOT = 4;
//    public static final int FUCTION_SYSTEM_SHUTDOWN = 5;
//    public static final int FUCTION_QUERY_CURR_DEVICE = 6;
//
//    public static final int FUCTION_WHITE_LIST_ADD = 8;
//    public static final int FUCTION_WHITE_LIST_DEL = 9;
//    public static final int FUCTION_WHITE_LIST_QUERY = 10;
//    public static final int FUCTION_BLACK_LIST_ADD = 11;
//    public static final int FUCTION_BLACK_LIST_DEL = 12;
//    public static final int FUCTION_BLACK_LIST_QUERY = 13;
//
//    public static final int FUCTION_SSID_LIB_ADD = 14;
//    public static final int FUCTION_SSID_LIB_DEL = 15;
//    public static final int FUCTION_SSID_LIB_QUERY = 16;
//
//    public static final int FUCTION_SYSTEM_STATE = 17;
//
//    public static final int FUCTION_DEAUTH_START = 18;
//    public static final int FUCTION_DEAUTH_STOP = 19;
//    public static final int FUCTION_DEAUTH_STATE = 20;
//    public static final int FUCTION_DEAUTH_RESTART = 21;
//
//    public static final int FUCTION_SSID_START = 22;
//    public static final int FUCTION_SSID_STOP = 23;
//    public static final int FUCTION_SSID_RESTART = 24;
//    public static final int FUCTION_SSID_STATE = 25;
//
//
//    public static final int FUCTION_MITM_START = 26;
//    public static final int FUCTION_MITM_STOP = 27;
//    public static final int FUCTION_MTIM_STATE = 28;
//
//    public static final int FUCTION_MAC_IDENTITY = 29;
//    public static final int FUCTION_MAC_URL = 30;
//
//    public static final int FUCTION_TOP_AP_ADD = 31;
//    public static final int FUCTION_TOP_AP_DEL = 32;
//    public static final int FUCTION_TOP_AP_QUERY = 33;
//
//    public static final int FUCTION_CHECK_APP_UPDATE = 34;
//    public static final int FUCTION_SYN_TIME = 35;
//    public static final int FUCTION_PI_SW_UPDATE = 36;
//    public static final int FUCTION_PI_SW_VERSION = 37;
//    public static final int FUCTION_CHECK_PI_UPDATE = 38;
//    public static final int FUCTION_EXTEND_FILE_SYS = 39;
//
//    public static final int MSG_CURR_DEVICE_DATA_BACK = 200;
//    public static final long GET_CURR_DEVICE_TIME = 480L;
//    public static final float DEVICE_MAX_DISTANCE = 50f;
//    private boolean isSystemRunning = false;
//    private int isListenningRunning = 1;
//    private int isMitmRunning =1;
//    private List<Device> deauthlist = new ArrayList<>();
//    private List<Device> mitmList = new ArrayList<>();
//    private List<Device> tempMitmList = new ArrayList<>();
//    private Thread getMacDetailThread = null;
//    private boolean isGettingMacDetail = false;
//    public static final String DETER_DIR_NAME = "deter";
//
//    private static Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_CURR_DEVICE_DATA_BACK:
//                    if(msg.obj!=null)
//                    {
//                        MessageContent mc = (MessageContent)msg.obj;
//                        FuctionListener listener = mc.listener;
//                        List<Object> dataList = mc.datalist;
//                        if(listener!=null)
//                        {
//                            listener.onResult(FUCTION_QUERY_CURR_DEVICE, dataList);
//                        }
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    private BuzokuFuction(Context context) {
//
//        this.context = context;
//
//    }
//
//    public static BuzokuFuction getInstance(Context context){
//        if(instance == null){
//            synchronized (BuzokuFuction.class){
//                if(instance == null){
//                    instance = new BuzokuFuction(context.getApplicationContext());
//                }
//            }
//        }
//
//        return instance;
//    }
//
//
//
//    public long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(long startTime) {
//        this.startTime = startTime;
//    }
//
//    public boolean isStartFlag() {
//        return startFlag;
//    }
//
//    public void setStartFlag(boolean startFlag) {
//        this.startFlag = startFlag;
//    }
//
//
//
//    public void reboot(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SYSTEM_REBOOT,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SYSTEM_REBOOT);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                listener.onPostExecute(FUCTION_SYSTEM_REBOOT, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        listener.onPostExecute(FUCTION_SYSTEM_REBOOT, 0);
//                                    }else{
//                                        listener.onPostExecute(FUCTION_SYSTEM_REBOOT, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_SYSTEM_REBOOT, 1);
//                                }
//
//                            }
//                        }
//                    }
//                });
//    }
//
//
//
//    public void shutdown(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SYSTEM_SHUTDOWN,
//                new RestApiListener() {
//            @Override
//            public void onPreExecute() {
//                if(listener!=null)
//                    listener.onPreExecute(FUCTION_SYSTEM_SHUTDOWN);
//            }
//
//            @Override
//            public void onPostExecute(String result) {
//                if(listener!=null)
//                {
//                    if(DtConstant.DEBUG_MODE)
//                    {
//                        listener.onPostExecute(FUCTION_SYSTEM_SHUTDOWN, 0);
//                    }else{
//                        if(result!=null)
//                        {
//                            if(result.contains("SUCCESS"))
//                            {
//                                listener.onPostExecute(FUCTION_SYSTEM_SHUTDOWN, 0);
//                            }else{
//                                listener.onPostExecute(FUCTION_SYSTEM_SHUTDOWN, 1);
//                            }
//                        }else{
//                            listener.onPostExecute(FUCTION_SYSTEM_SHUTDOWN, 1);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//
//    //获取 ap mac， 返回值
//
//    public void queryCurrDevice(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_GET_CURR_DEVICE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_QUERY_CURR_DEVICE);
//                    }
//
//                    @Override
//                    public void onPostExecute(final String result) {
//                        Log.i(DtConstant.DT_TAG, ">>>queryCurrDevice>>>onPostExecute ");
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(!DtConstant.DEBUG_MODE) {
//                                    if (result != null) {
//
//                                        JSONObject jo = null;
//                                        String jarrayStr = null;
//
//                                        try {
//                                            jo = new JSONObject(result);
//                                            jarrayStr = jo.getString("data");
//                                            //Log.i(DtConstant.DT_TAG, ">>>queryCurrAp>>>onPostExecute data:"+jarrayStr);
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                            Log.i(DtConstant.DT_TAG, ">>>queryCurrDevice>>>onPostExecute " +
//                                                    "JSONException excption:"+e.toString());
//                                        }
//
//                                        final List<Device> devices = parseJSonArrayToDeviceList(currTask,jarrayStr);
//                                        saveCurrDeviceToDB(devices);
//                                        long taskId = -1;
//                                        if(currTask!=null) {
//                                            taskId = currTask.getId();
//                                        }
//                                        sendCurrDeviceDataBackMsg(listener, getDevicesToUiByTaskIdAndTimeFromDb(taskId));
//
//                                    } else {
//                                        if (listener != null) {
//                                            sendCurrDeviceDataBackMsg(listener, null);
//                                        }
//                                    }
//                                }else{
//                                    if (listener != null) {
//                                        sendCurrDeviceDataBackMsg(listener, getDevicesToUiByTaskIdAndTimeFromDb(currTask.getId()));
//                                    }
//                                }
//                            }
//                        }).start();
//                    }
//                });
//    }
//
//    private synchronized void saveCurrDeviceToDB(final List<Device> currdevices ) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(DtConstant.DT_TAG2, "saveCurrDeviceToDB>>>thread start");
//                saveCurrDeviceToDBExt(currdevices);
//                Log.i(DtConstant.DT_TAG2, "saveCurrDeviceToDB>>>thread end");
//            }
//        }).start();
//    }
//
//    private synchronized void saveCurrDeviceToDBExt(List<Device> currdevices )
//    {
//        if(currdevices == null)
//            return;
//        if(currdevices.size()==0)
//            return;
//
//        DeviceDao deviceDao = new DeviceDao(context);
//        for(Device d:currdevices)
//        {
//            if(d.getMac()==null)
//            { continue; }
//            List<Device> deviceList = null;
//            try {
////              long nowTime = System.currentTimeMillis();
////              long queryTime = nowTime - GET_CURR_DEVICE_TIME*1000;
//                deviceList = deviceDao.getDao().queryBuilder().where().eq("mac", d.getMac()).
//                        and().eq("task_id", d.getTaskId()).query();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                Log.i(DtConstant.DT_TAG2, "saveCurrDeviceToDB>>>excepton:"+e.toString());
//            }
//
//            if(deviceList!=null)
//            {
//                if(deviceList.size()==0)
//                {
//                    Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>1 addDeviceToDB: "+d.toString());
//                    DataManager.getInstance(context).addDeviceToDB(d);
//                }else{
//                    Log.i(DtConstant.DT_TAG, "saveCurrDeviceToDB>>>deviceList size:"+deviceList.size());
//                    Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>> device exist, updat device");
//                    String existConectState = null;
//                    String newConectState = d.getConectState();
//                    if(newConectState.equalsIgnoreCase("CONNECTING")){
//                        newConectState = "PROBE";
//                    }
//                    int count = 0;
//                    for(Device device:deviceList)
//                    {
//                        if(count!=0){
//                            DataManager.getInstance(context).deleteDeviceFromDb(device);
//                            Log.i(DtConstant.DT_TAG, "saveCurrDeviceToDB>>>delete duplicate device mac:"+device.getMac());
//                            Log.i(DtConstant.DT_TAG, "saveCurrDeviceToDB>>>delete duplicate device count:"+count);
//                            continue;
//                        }
//                        existConectState = device.getConectState();
//                        if(existConectState.equalsIgnoreCase("CONNECTING")){
//                            existConectState = "PROBE";
//                        }
//                        int deauthState = device.getDeauthState();
//                        int mitmState = device.getMitmState();
//                        d.setDeauthState(deauthState);
//                        d.setMitmState(mitmState);
//                        if(existConectState!=null && newConectState!=null)
//                        {
//                            Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>1");
//                            if(existConectState.equalsIgnoreCase("PROBE") && newConectState.equalsIgnoreCase("PROBE"))
//                            {
//                                DataManager.getInstance(context).deleteDeviceFromDb(device);
//                                Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>delete old probe and add new probe");
//                                DataManager.getInstance(context).addDeviceToDB(d);
//                            }else if(!existConectState.equalsIgnoreCase("PROBE") && newConectState.equalsIgnoreCase("PROBE")){
//                                Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>has old conected sta and dont add new probe");
//                            }else if(!existConectState.equalsIgnoreCase("PROBE") && !newConectState.equalsIgnoreCase("PROBE")){
//                                DataManager.getInstance(context).deleteDeviceFromDb(device);
//                                Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>delete old conected sta and add new conected sta");
//                                DataManager.getInstance(context).addDeviceToDB(d);
//                            }else if(existConectState.equalsIgnoreCase("PROBE") && !newConectState.equalsIgnoreCase("PROBE")){
//                                DataManager.getInstance(context).deleteDeviceFromDb(device);
//                                Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>delete old probe sta and add new conected sta");
//                                DataManager.getInstance(context).addDeviceToDB(d);
//                            }else{
//                                Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>others");
//                            }
//                        }else{
//                            Log.i(DtConstant.DT_TAG, ">>>saveCurrDeviceToDB>>>2");
//                        }
//                        count ++;
//                    }
//                }
//            }else{
//                Log.i(DtConstant.DT_TAG, ">>>2 saveCurrDeviceToDB>>> addDeviceToDB: "+d.toString());
//                DataManager.getInstance(context).addDeviceToDB(d);
//            }
//        }
//    }
//
//
//    void collectStationsOfAp(List<Device> deviceList)
//    {
//        Log.i(DtConstant.DT_TAG, ">>>collectStationsOfAp>>>enter");
//        if(deviceList!=null)
//        {
//            if(deviceList.size()>0)
//            {
//                for(Device d:deviceList)
//                {
//                    if(d.getRole()!=null)
//                    {
//                        if(d.getRole().equalsIgnoreCase("AP"))
//                        {
//                            String apSsid = d.getSsid();
//                            String mac = d.getMac();
//                            d.setStations(new ArrayList<Device>());
//                            if(apSsid!=null && mac!=null)
//                            {
//                                for(Device device:deviceList)
//                                {
//                                    if(device.getRole().equalsIgnoreCase("STATION")
//                                            && apSsid.equals(device.getSsid())
//                                            && mac.equalsIgnoreCase(device.getSsidMac()))
//                                    {
//                                        d.getStations().add(device);
//                                    }
//                                }
//                            }
//                            Log.i(DtConstant.DT_TAG, ">>>collectStationsOfAp>>>stations: "+JsonUtils.objectToString(d.getStations()));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    private List<Object> getDevicesToUiByTaskIdAndTimeFromDb(long taskId)
//    {
//        List<Device> devices = null;
//        List<Object> resultList = new ArrayList<Object>();
//        if(DtConstant.DEBUG_MODE)
//        {
//            //devices = DataManager.getInstance(context).getAllDeviceFromDb();
//            //for()
//            //devices = DataManager.getInstance(context).getAllDeviceFromDb();
//            devices = DataManager.getInstance(context).queryDeviceByTaskIdAndTime(taskId, GET_CURR_DEVICE_TIME);
//        }else{
//            devices = DataManager.getInstance(context).queryDeviceByTaskIdAndTime(taskId, GET_CURR_DEVICE_TIME);
//        }
//
//        collectStationsOfAp(devices);
//        checkDeviceListInWhiteOrBlackList(devices);
//        try {
//            if(devices!=null)
//            {
//                for(Device d:devices)
//                {
//                    if(d!=null)
//                    {
//
//                        d.setDeauthState(Device.DEAUTH_STATE_NONE);
//                        d.setMitmState(Device.MITM_STATE_NONE);
//                        if(d.getDistance()>DEVICE_MAX_DISTANCE)
//                            d.setDistance(DEVICE_MAX_DISTANCE);
//                        if(d.getRole().equalsIgnoreCase("station"))
//                            Log.i(DtConstant.DT_TAG3, "getDevices "+d.toString());
//                        d.setFetchTime(TimeUtils.getCurrentTimeInLong());
//                        resultList.add(d);
//                    }
//                }
//            }
//
//        }catch (Exception e) {
//            Log.i(DtConstant.DT_TAG, ">>>getDevicesToUiByTaskIdFromDb>>>exception:"+e.toString());
//        }
//        Log.i(DtConstant.TAG, ">>>getDevicesToUiByTaskIdAndTimeFromDb>>>taskId:"+taskId
//                +"  resultList size:"+resultList.size());
//        return resultList;
//    }
//
//
//    class MessageContent{
//        FuctionListener listener;
//        List<Object> datalist;
//    }
//
//
//    void sendCurrDeviceDataBackMsg(FuctionListener listener , List<Object> datalist)
//    {
//        MessageContent msgContent = new  MessageContent();
//        msgContent.listener = listener;
//        msgContent.datalist = datalist;
//        Message message = new Message();
//        message.what = MSG_CURR_DEVICE_DATA_BACK;
//        message.obj = msgContent;
//        mHandler.sendMessage(message);
//    }
//
//    void checkDeviceListInWhiteOrBlackList(List<Device> deviceList)
//    {
//        if(deviceList==null)
//            return;
//        if(deviceList.size()==0)
//            return;
//        List<SpecialDevice> whiteList = DataManager.getInstance(context).getWhiteListFromDb();
//        List<SpecialDevice> blackList = DataManager.getInstance(context).getBlackListFromDb();
//
//        for(Device d:deviceList)
//        {
//            d.setInwhitelist(false);
//            if(whiteList!=null)
//            {
//                for(SpecialDevice s:whiteList)
//                {
//                    if(d.getMac().equals(s.getMac()))
//                        d.setInwhitelist(true);
//                }
//            }
//            d.setInblacklist(false);
//            if(blackList!=null)
//            {
//                for(SpecialDevice s:blackList)
//                {
//                    if(d.getMac().equals(s.getMac()))
//                        d.setInblacklist(true);
//                }
//            }
//        }
//    }
//
//    List<Device> parseJSonArrayToDeviceList(Task task, String jarrayStr)
//    {
//        Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>start");
//        if(jarrayStr==null)
//        {
//            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>start null");
//            return null;
//        }
//
//        List<Device> devices = null;
//        JSONArray jarray = null;
//        try {
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    devices = new ArrayList<Device>();
//                    String address = "";//DataManager.getInstance(context).getCurrListenAddress();
//                    Long time = System.currentTimeMillis();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>1");
//                        Device d = new Device();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        d.setMac(jo.getString("mac"));
//                        d.setRole(jo.getString("role"));
//                        d.setDclass(jo.getString("class"));
//                        d.setName(jo.getString("name"));
//                        try {
//                            d.setTime_seen(Long.parseLong(jo.getString("time_seen")));
//                        }catch (Exception e){
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> time_seen e:"+e.toString());
//                        }
//                        d.setRssi(jo.getInt("rssi"));
//                        d.setCompass(jo.getInt("compass"));
//                        d.setVender(jo.getString("vendor"));
//                        d.setConectState(jo.getString("state"));// "state": "PROBE"
//                        Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>conect state:"+d.getConectState());
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>2");
//                        String [] vendors = DataManager.getInstance(context).queryVendor(d.getMac());
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>3");
//                        if(vendors!=null)
//                        {
//                            d.setVendorcn(vendors[0]);
//                            d.setVendoren(vendors[1]);
//                        }
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>4");
//                        if(Locale.getDefault().getLanguage().contains("zh"))
//                        {
//                            if(vendors!=null)
//                                d.setVender(vendors[0]);
//                            else
//                                d.setVender("");
//                        }else{
//                            if(vendors!=null)
//                                d.setVender(vendors[1]);
//                            else
//                                d.setVender("");
//                        }
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>5");
//                        JSONObject jssid = jo.getJSONObject("ssid");
//                        String tmpName = jssid.getString("name");
//                        if(tmpName==null || tmpName.equalsIgnoreCase("null"))
//                        {
//                            d.setSsid("");
//                        }else{
//                            if(d.getConectState()!=null)
//                            {
//                                if(d.getConectState().equalsIgnoreCase("PROBE"))
//                                {
//                                    Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>PROBE ssid set null");
//                                    d.setSsid("");
//                                }else{
//                                    d.setSsid(tmpName);
//                                }
//                            }else{
//                                d.setSsid("");
//                            }
//                        }
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>6");
//                        try{
//                            String encryption = jssid.getString("encryption");
//                            if(encryption!=null)
//                            {
//                                if(encryption.equalsIgnoreCase("null"))
//                                    d.setEncryption("");
//                                else
//                                    d.setEncryption(encryption);
//                            }else{
//                                d.setEncryption("");
//                            }
//                            d.setSsidMac(jssid.getString("mac"));
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>ssidMac:"+d.getSsidMac());
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>channel:"+jssid.get("channel"));
//                            d.setChannel(Integer.parseInt(jssid.getString("channel")));
//                        }catch (Exception e){
//                            d.setChannel(RandomUtils.getRandom(1,13));
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> mac channel e:"+e.toString());
//                        }
//
//                        JSONObject jaddr = jo.getJSONObject("address");
//                        try {
//                            d.setLatitude(jaddr.getDouble("latitude"));
//                            d.setLongitude(jaddr.getDouble("longitude"));
//                        }catch (Exception e){
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> longtitude e:"+e.toString());
//                        }
//
//                        d.setHeading(jaddr.getString("heading"));
//
//                        try {
//                            JSONObject ipinfo = jo.getJSONObject("ipinfo");
//                            d.setIpaddr(ipinfo.getString("ipaddr"));
//                            d.setIpInfoName(ipinfo.getString("name"));
//                        }catch (Exception e)
//                        {
//                            d.setIpaddr("");
//                            d.setIpInfoName("");
//                            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> ipinfo e:"+e.toString());
//                        }
//
//                        d.setAngle(RandomUtils.getRandom(1,359));
//                        d.setDistance(APPUtils.rssiToDistance(d.getRssi(), d.getRole()));
//                        //Log.i(DtConstant.TAG, ">>>parseJSonArrayToDeviceList>>>9");
//                        d.setState(RandomUtils.getRandom(0,3));
//                        d.setAddress(address);
//                        Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> setAddress :"+address);
//                        d.setTime(time);
//                        Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>> setTime :"+time);
//                        if(task!=null)
//                        {
//                            d.setTaskId(task.getId());
//                        }else{
//                            d.setTaskId(-1);
//                        }
//                        boolean shouldAdd = true;
//                        for(Device device:devices)
//                        {
//                            if(device==null)
//                                break;
//                            if(d.getMac()==null || device.getMac()==null)
//                                break;
//                            if(d.getMac().equalsIgnoreCase(device.getMac()))
//                            {
//                                Long dTime = d.getTime_seen();
//                                Long deviceTime = device.getTime_seen();
//                                if(dTime>deviceTime)
//                                {
//                                    devices.remove(device);
//                                    devices.add(d);
//                                }
//                                shouldAdd = false;
//                                break;
//                            }
//                        }
//                        if(shouldAdd)
//                            devices.add(d);
//                    }
//                }
//
//            }
//            if(devices!=null)
//            {
//                Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>success size:"+devices.size());
//            }else{
//                Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.DT_TAG, ">>>parseJSonArrayToDeviceList>>>fail exception:"+e.toString());
//        }
//        if(devices!=null)
//        {
//            Log.i(DtConstant.DT_TAG2, ">>>parseJSonArrayToDeviceList>>>devices:");
//            for (Device d:devices)
//            {
//                Log.i(DtConstant.DT_TAG2, ">>>"+d.toString());
//            }
//        }
//        return devices;
//    }
//
//
//    public Task getCurrTask() {
//        return currTask;
//    }
//
//    public void setCurrTask(Task currTask) {
//        this.currTask = currTask;
//    }
//
////开启监听， 返回值result 0：成功，1：失败
//
//    public void startListen( final String address, final double latitude, final double longitude,
//                             boolean fullDirectional, final FuctionListener listener)
//    {
//        Log.i(DtConstant.TAG, "startListen>>>fullDirectional:"+fullDirectional);
//        String url = "";
//        if(fullDirectional)
//        {
//            url = BuzokuRestApi.API_START_LISTEN;
//        }else{
//            url = BuzokuRestApi.API_START_LISTEN_DIRECTIONAL;
//        }
//
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_LISTENER_START);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                    if(currTask==null)
//                                    {
//                                        currTask = new Task();
//                                        currTask.setAddress(address);
//                                        currTask.setLatitude(latitude);
//                                        currTask.setLongitude(longitude);
//                                        currTask.setStartTime(System.currentTimeMillis());
//                                        DataManager.getInstance(context).addTaskToDb(currTask);
//                                        currTask.setId(10000);
//                                        Log.i("idtest", "addTaskToDb>>>id:"+currTask.getId());
//                                        isListenningRunning = 0;
//                                    }
//                                    }
//                                }).start();
//                                listener.onPostExecute(FUCTION_LISTENER_START, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                            if(currTask==null)
//                                            {
////                                                    currTask.setStopTime(System.currentTimeMillis());
////                                                    currTask.setDescription("异常退出 "+TimeUtils.getCurrentTimeInString());
////                                                    DataManager.getInstance(context).updateTaskToDb(currTask);
////                                                    currTask = null;
//                                                currTask = new Task();
//                                                currTask.setAddress(address);
//                                                currTask.setLatitude(latitude);
//                                                currTask.setLongitude(longitude);
//                                                currTask.setStartTime(System.currentTimeMillis());
//                                                DataManager.getInstance(context).addTaskToDb(currTask);
//                                            }
//                                            }
//                                        }).start();
//                                        listener.onPostExecute(FUCTION_LISTENER_START, 0);
//                                    }else{
//                                        listener.onPostExecute(FUCTION_LISTENER_START, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_LISTENER_START, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
//
//    //查询监听状态， 返回值result 0：成功，1：失败
//    public void queryListenState(final FuctionListener listener)
//    {
//
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_LISTEN_STATE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_LISTENER_STATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(isListenningRunning==1)
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        currTask.setStopTime(System.currentTimeMillis());
//                                        currTask.setDescription("debug mod 异常退出 "+TimeUtils.getCurrentTimeInString());
//                                        DataManager.getInstance(context).updateTaskToDb(currTask);
//                                        currTask = null;
//                                    }
//                                }
//                                listener.onPostExecute(FUCTION_LISTENER_STATE, isListenningRunning);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("YES"))
//                                    {
//                                        if(currTask==null)
//                                        {
//                                            currTask = new Task();
//                                            currTask.setAddress(null);
//                                            currTask.setLatitude(0);
//                                            currTask.setLongitude(0);
//                                            currTask.setStartTime(System.currentTimeMillis());
//                                            DataManager.getInstance(context).addTaskToDb(currTask);
//                                            Log.i(DtConstant.DT_TAG, ">>>queryCurrDevice>>>currTask == null !");
//                                        }else{
//
//                                        }
//                                        listener.onPostExecute(FUCTION_LISTENER_STATE, 0);
//                                    }else{
//                                        if(currTask!=null)
//                                        {
//                                            currTask.setStopTime(System.currentTimeMillis());
//                                            currTask.setDescription("异常退出 "+TimeUtils.getCurrentTimeInString());
//                                            DataManager.getInstance(context).updateTaskToDb(currTask);
//                                            currTask = null;
//                                        }
//                                        listener.onPostExecute(FUCTION_LISTENER_STATE, 1);
//                                    }
//                                }else{
//                                    if(currTask!=null)
//                                    {
//                                        currTask.setStopTime(System.currentTimeMillis());
//                                        DataManager.getInstance(context).updateTaskToDb(currTask);
//                                        currTask = null;
//                                    }
//                                    listener.onPostExecute(FUCTION_LISTENER_STATE, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    //停止监听， 返回值result 0：成功，1：失败
//    public void stopListen(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_STOP_LISTEN,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_LISTENER_STOP);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(currTask!=null)
//                                {
//                                    currTask.setStopTime(System.currentTimeMillis());
//                                    currTask.setDescription("debug mod "+TimeUtils.getCurrentTimeInString());
//                                    DataManager.getInstance(context).updateTaskToDb(currTask);
//                                    currTask = null;
//                                }
//                                isListenningRunning = 1;
//                                listener.onPostExecute(FUCTION_LISTENER_STOP, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        if(currTask!=null)
//                                        {
//                                            currTask.setStopTime(System.currentTimeMillis());
//                                            currTask.setDescription("正常操作"+TimeUtils.getCurrentTimeInString());
//                                            DataManager.getInstance(context).updateTaskToDb(currTask);
//                                            currTask = null;
//                                        }
//                                        listener.onPostExecute(FUCTION_LISTENER_STOP, 0);
//                                    }else{
//                                        listener.onPostExecute(FUCTION_LISTENER_STOP, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_LISTENER_STOP, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    //重启监听， 返回值result 0：成功，1：失败
//    public void restartListen(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_RESTART_LISTEN,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_LISTEN_RESTART);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                listener.onPostExecute(FUCTION_LISTEN_RESTART, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        listener.onPostExecute(FUCTION_LISTEN_RESTART, 0);
//                                    }else{
//                                        listener.onPostExecute(FUCTION_LISTEN_RESTART, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_LISTEN_RESTART, 1);
//                                }
//
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    //获取系统状态， 返回值result 0：成功，1：失败
//    public void querySystemState(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SYSTEM_STATE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SYSTEM_STATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            isSystemRunning = true;
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_SYSTEM_STATE, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("YES"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SYSTEM_STATE, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SYSTEM_STATE, 1);
//                                }
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_SYSTEM_STATE, 1);
//                            }
//
//                        }
//                    }
//                });
//    }
//
//////////////////////////////deauth api start///////////////////////////////////////
//    //开启断网， 返回值result 0：成功，1：失败 2:已在运行
//
//    public void startDeauth(final FuctionListener listener, final List<Device> devices, int rssiAbs, boolean isAll)
//    {
//        String url = BuzokuRestApi.API_START_DEAUTH+"&rssi=";
//        //rssiAbs = 60;//APPUtils.getKeyFromHashMap(APPUtils.rssiDistanceMap, distance);
//        if(rssiAbs<30 || rssiAbs>120)
//            rssiAbs = 60;
//        url += rssiAbs;
//        String postString = buildDeauthPostString(devices, isAll);
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>startDeauth>>>postString:"+postString);
//        BuzokuRestApi.doApiPostExec(url, postString,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(currTask!=null)
//                        {
//                            DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                    devices, null, Device.DEAUTH_STATE_RUNNING);
//                        }
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_DEAUTH_START);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            deauthlist.clear();
//                            for(Device device :devices){
//                                device.setState(1);
//                            }
//                            deauthlist.addAll(devices);
//                            android.util.Log.d("xiaolu","startDeauth  deauthlist size ="+deauthlist.size()+", devices size ="+devices.size());
//                            if(currTask!=null)
//                            {
//                                DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                        devices, null, Device.DEAUTH_STATE_RUNNING);
//                            }
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_DEAUTH_START, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_RUNNING);
//                                    }
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_START, 0);
//                                }else{
//                                    if(currTask!=null)
//                                    {
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_FAIL);
//                                    }
//                                    if(result.contains("ALREADY RUNNING"))
//                                    {
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_DEAUTH_START, 2);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_DEAUTH_START, 1);
//                                    }
//                                }
//                            }else{
//                                if(currTask!=null)
//                                {
//                                    DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                            devices, null, Device.DEAUTH_STATE_FAIL);
//                                }
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_DEAUTH_START, 1);
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    private String buildDeauthPostString(List<Device> devices, boolean isAll)
//    {
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>buildDeauthPostString>>>devices:"+devices);
//        String postString = "";
//        List tmpObjectList = new ArrayList<Object> ();
//
//        if(isAll)
//        {
//            postString = "[{\"mac\":\"all\"}, {\"ssid\":\"all\"}]";
//        }else{
//            if(devices!=null)
//            {
//                if(devices.size()>0)
//                {
//                    for(int i=0;i<devices.size();i++)
//                    {
//                        Device d = devices.get(i);
//                        if(d.getRole().equalsIgnoreCase("station"))
//                        {
//                            Mac m = new Mac();
//                            m.mac = d.getMac().toLowerCase();
//                            Ssid s = new Ssid();
//                            s.ssid = d.getSsid();
//                            tmpObjectList.add(m);
//                            tmpObjectList.add(s);
//
//                        }else{
//                            Ssid s = new Ssid();
//                            s.ssid = d.getSsid();
//                            Mac m = new Mac();
//                            m.mac = "all";
//                            tmpObjectList.add(m);
//                            tmpObjectList.add(s);
//                        }
//                    }
//                    postString = JsonUtils.objectToString(tmpObjectList);
//                }else{
//                    postString = "";
//                }
//            }else{
//                postString = "";
//            }
//        }
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>buildDeauthPostString>>>postString:"+postString);
//        return postString;
//    }
//
//
//    class Mac{
//        String mac;
//    }
//
//    class Ssid{
//        String ssid;
//    }
//
//    public void stopDeauth(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_STOP_DEAUTH,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_DEAUTH_STOP);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            deauthlist.clear();
//                            if(currTask!=null)
//                            {
//                                List<Device> devices= DataManager.getInstance(context).getCurrDeauthDevice(currTask.getId());
//                                DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                        devices, null, Device.DEAUTH_STATE_NONE);
//                            }
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_DEAUTH_STOP, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        List<Device> devices= DataManager.getInstance(context).getCurrDeauthDevice(currTask.getId());
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_NONE);
//                                    }
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_STOP, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_STOP, 1);
//                                }
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_DEAUTH_STOP, 1);
//                            }
//
//                        }
//                    }
//                });
//    }
//
//
//    public void restartDeauth(final FuctionListener listener, final List<Device> devices, int rssiAbs, boolean isAll)
//    {
//        String url = BuzokuRestApi.API_RESTART_DEAUTH+"&rssi=";
//        //rssiAbs = 60;//APPUtils.getKeyFromHashMap(APPUtils.rssiDistanceMap, distance);
//        if(rssiAbs<20 || rssiAbs>120)
//            rssiAbs = 60;
//        url += rssiAbs;
//        String postString = buildDeauthPostString(devices, isAll);
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>restartDeauth>>>postString:"+postString);
//        BuzokuRestApi.doApiPostExec(url, postString,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(currTask!=null)
//                        {
//                            List<Device> oldDeviceList = DataManager.getInstance(context).getCurrDeauthDevice(currTask.getId());
//                            DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                    devices,oldDeviceList,Device.DEAUTH_STATE_RUNNING);
//                        }
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_DEAUTH_RESTART);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            deauthlist.clear();
//                            for(Device device:devices){
//                                device.setState(1);
//                            }
//                            deauthlist.addAll(devices);
//
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_DEAUTH_RESTART, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_RUNNING);
//                                    }
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_RESTART, 0);
//                                }else{
//                                    if(currTask!=null)
//                                    {
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_FAIL);
//                                    }
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_RESTART, 1);
//                                }
//                            }else{
//                                if(currTask!=null)
//                                {
//                                    DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                            devices, null, Device.DEAUTH_STATE_FAIL);
//                                }
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_DEAUTH_RESTART, 1);
//                            }
//
//                        }
//                    }
//                });
//    }
//
//
//    public void queryDeauthState(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_DEAUTH_STATE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_DEAUTH_STATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            android.util.Log.d("xiaolu","deauthlist size ="+deauthlist.size());
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_DEAUTH_STATE, deauthlist.size()>0 ? 0:1);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("YES"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_STATE, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_DEAUTH_STATE, 1);
//                                }
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_DEAUTH_STATE, 2);
//                            }
//
//                        }
//                    }
//                });
//    }
//////////////////////////////deauth api start///////////////////////////////////////
//
//
///////////////////// ssid api start////////////////////////
////开启监听， 返回值result 0：成功，1：失败
//
//    public void startSsid(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_START_SSID,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SSID_START);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                listener.onPostExecute(FUCTION_SSID_START, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        listener.onPostExecute(FUCTION_SSID_START, 0);
//
//                                    }else{
//                                        listener.onPostExecute(FUCTION_SSID_START, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_SSID_START, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void stopSsid(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_STOP_SSID,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SSID_STOP);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                listener.onPostExecute(FUCTION_SSID_STOP, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        listener.onPostExecute(FUCTION_SSID_STOP, 0);
//
//                                    }else{
//                                        listener.onPostExecute(FUCTION_SSID_STOP, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_SSID_STOP, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    public void querySsidState(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SSID_STATE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SSID_STATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(listener!=null)
//                        {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                listener.onPostExecute(FUCTION_SSID_STATE, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        listener.onPostExecute(FUCTION_SSID_STATE, 0);
//
//                                    }else{
//                                        listener.onPostExecute(FUCTION_SSID_STATE, 1);
//                                    }
//                                }else{
//                                    listener.onPostExecute(FUCTION_SSID_STATE, 1);
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
///////////////////// ssid api end////////////////////////
//
//
//
//
//////////////////////////dnd list/white list black list api start//////////////////////////////////////
//
//
//    public void queryDndList(final FuctionListener listener)
//    {
//
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_WHITELIST,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_WHITE_LIST_QUERY);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onResult(FUCTION_WHITE_LIST_QUERY, null);
//                        }else{
//                            if(result!=null)
//                            {
//                                List<DndMac> dndMacs = parseJSonArrayToDndList(result);
//                                List<Object> resultList = new ArrayList<Object>() ;
//                                if(dndMacs!=null)
//                                {
//                                    if(dndMacs.size()>0)
//                                    {
//                                        for(DndMac s:dndMacs)
//                                            resultList.add(s);
//                                    }
//                                }
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_WHITE_LIST_QUERY, resultList);
//                            }else{
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_WHITE_LIST_QUERY, null);
//                            }
//
//                        }
//                    }
//                });
//
//    }
//
//
//
//    List<DndMac> parseJSonArrayToDndList(String jsonStr)
//    {
//        Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>start");
//        if(jsonStr==null)
//        {
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>start null");
//            return null;
//        }
//
//        List<DndMac> dndMacs = null;
//        JSONArray jarray = null;
//
//
//        JSONObject jsonObject = null;
//        String jarrayStr = null;
//
//        try {
//            jsonObject = new JSONObject(jsonStr);
//            jarrayStr = jsonObject.getString("whitelist");
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>dndlist:"+jarrayStr);
//            if(jarrayStr==null)
//                return null;
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    dndMacs = new ArrayList<DndMac>();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        DndMac d = new DndMac();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        try {
//                            d.setMac(jo.getString("mac"));
//                            d.setDescription(jo.getString("description"));
//
//                        }catch (Exception e){
//                            Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>parsing excepton:"+e.toString());
//                        }
//                        dndMacs.add(d);
//                    }
//                }
//            }
//            if(dndMacs!=null)
//            {
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>success size:"+dndMacs.size());
//            }else{
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToDndList>>>fail exception:"+e.toString());
//        }
//        return dndMacs;
//    }
//
//
//////
//
//
//
//    public void addToDndList(final DndMac dndMac, final FuctionListener listener)
//    {
//        if(dndMac!=null)
//        {
//            String postString = JsonUtils.objectToString(dndMac);
//            String url = BuzokuRestApi.API_POST_WHITELIST+dndMac.getMac().toLowerCase();
//            BuzokuRestApi.doApiPostExec(url, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_WHITE_LIST_ADD);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_WHITE_LIST_ADD, 0);
//
////                                dndMac.setInwhitelist(true);
////                                DataManager.getInstance(context).updateDevice(dndMac);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
////                                        dndMac.setInwhitelist(true);
////                                        DataManager.getInstance(context).updateDevice(dndMac);
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_WHITE_LIST_ADD, 0);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_WHITE_LIST_ADD, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_WHITE_LIST_ADD, 1);
//                                }
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//    public void addToBlackList(final Device device , final FuctionListener listener)
//    {
//        if(device!=null)
//        {
//            String postString = JsonUtils.objectToString(device);
//            BuzokuRestApi.doApiPostExec(BuzokuRestApi.API_BLACKLIST, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_BLACK_LIST_ADD);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                device.setInblacklist(true);
//                                DataManager.getInstance(context).updateDevice(device);
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_BLACK_LIST_ADD, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        device.setInblacklist(true);
//                                        DataManager.getInstance(context).updateDevice(device);
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_BLACK_LIST_ADD, 0);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_BLACK_LIST_ADD, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_BLACK_LIST_ADD, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void removeDndDeviceFromWhiteList(final DndMac dndMac, final FuctionListener listener)
//    {
//        if(dndMac!=null && dndMac.getMac()!=null)
//        {
//            String mac = dndMac.getMac();
//            dndMac.setMac(mac.toLowerCase());
//            String postString = JsonUtils.objectToString(dndMac);
//            String url = BuzokuRestApi.API_DELETE_WHITELIST + dndMac.getMac();
//            BuzokuRestApi.doApiDeleteExec(url,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_WHITE_LIST_DEL);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 0);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void removeDeviceFromBlackList(final Device device, final FuctionListener listener)
//    {
//        if(device!=null)
//        {
//            device.setInblacklist(false);
//            DataManager.getInstance(context).updateDevice(device);
////            String postString = JsonUtils.objectToString(device);
////            BuzokuRestApi.doApiPostExec(BuzokuRestApi.API_BLACKLIST, postString,
////                    new RestApiListener() {
////                        @Override
////                        public void onPreExecute() {
////                            if(listener!=null)
////                                listener.onPreExecute(FUCTION_WHITE_LIST_DEL);
////                        }
////
////                        @Override
////                        public void onPostExecute(String result) {
////                            if(DtConstant.DEBUG_MODE)
////                            {
////                                device.setInblacklist(false);
////                                DataManager.getInstance(context).updateDevice(device);
////                                listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 0);
////                            }else{
////                                if(result.contains("SUCCESS"))
////                                {
////                                    device.setInblacklist(false);
////                                    DataManager.getInstance(context).updateDevice(device);
////                                    listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 0);
////                                }else{
////                                    listener.onPostExecute(FUCTION_WHITE_LIST_DEL, 1);
////                                }
////                            }
////                        }
////                    });
//
//        }
//
//    }
//
//////////////////////////white list black list api end//////////////////////////////////////
//
//
//
//////////////////////////////////ssid lib api start/////////////////////////////////
//
//    public void addToSsidLib(final List<SSID> ssidList , final FuctionListener listener)
//    {
//        if(ssidList!=null)
//        {
//            for(SSID ssid:ssidList)
//            {
//                String mac = ssid.getMac();
//                if(mac!=null)
//                    ssid.setMac(mac.toLowerCase());
//            }
//            String postString = JsonUtils.objectToString(ssidList);
//            BuzokuRestApi.doApiPostExec(BuzokuRestApi.API_SSID_LIB_ADD, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_SSID_LIB_ADD);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_SSID_LIB_ADD, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_SSID_LIB_ADD, 0);
//                                    }else{
//
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_SSID_LIB_ADD, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SSID_LIB_ADD, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void addToSsidLib(final SSID ssid , final FuctionListener listener)
//    {
//        if(ssid!=null)
//        {
//            String mac = ssid.getMac();
//            if(mac!=null)
//                ssid.setMac(mac.toLowerCase());
//            String postString = JsonUtils.objectToString(ssid);
//            String url = BuzokuRestApi.API_SSID_LIB_ADD + toURLEncoded(ssid.getSsid());
//            BuzokuRestApi.doApiPostExec(url, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_SSID_LIB_ADD);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_SSID_LIB_ADD, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_SSID_LIB_ADD, 0);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_SSID_LIB_ADD, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SSID_LIB_ADD, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void delFromSsidLib(final List<SSID> ssidList, final FuctionListener listener)
//    {
//        if(ssidList!=null)
//        {
//            for(SSID ssid:ssidList)
//            {
//                if(ssid == null)
//                    continue;
//                String url = BuzokuRestApi.API_SSID_LIB_DEL + toURLEncoded(ssid.getSsid());
//                BuzokuRestApi.doApiDeleteExec(url,
//                        new RestApiListener() {
//                            @Override
//                            public void onPreExecute() {
//                                if(listener!=null)
//                                    listener.onPreExecute(FUCTION_SSID_LIB_DEL);
//                            }
//
//                            @Override
//                            public void onPostExecute(String result) {
//                                if(DtConstant.DEBUG_MODE)
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SSID_LIB_DEL, 0);
//                                }else{
//                                    if(result!=null)
//                                    {
//                                        if(result.contains("SUCCESS"))
//                                        {
//                                            if(listener!=null)
//                                                listener.onPostExecute(FUCTION_SSID_LIB_DEL, 0);
//                                        }else{
//                                            if(listener!=null)
//                                                listener.onPostExecute(FUCTION_SSID_LIB_DEL, 1);
//                                        }
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_SSID_LIB_DEL, 1);
//                                    }
//
//                                }
//                            }
//                        });
//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//
//    public void queryFromSsidLib(final FuctionListener listener)
//    {
//
//            BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_SSID_LIB_QUERY,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_SSID_LIB_QUERY);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_SSID_LIB_QUERY, null);
//                            }else{
//                                if(result!=null)
//                                {
//                                    List<SSID> ssids = parseJSonArrayToSsidList(result);
//                                    List<Object> resultList = new ArrayList<Object>() ;
//                                    if(ssids!=null)
//                                    {
//                                        if(ssids.size()>0)
//                                        {
//                                            for(SSID s:ssids)
//                                                resultList.add(s);
//                                        }
//                                    }
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_SSID_LIB_QUERY, resultList);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_SSID_LIB_QUERY, null);
//                                }
//
//                            }
//                        }
//                    });
//
//    }
//
//
//
//    List<SSID> parseJSonArrayToSsidList(String jsonStr)
//    {
//        Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>start");
//        if(jsonStr==null)
//        {
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>start null");
//            return null;
//        }
//
//        List<SSID> ssids = null;
//        JSONArray jarray = null;
//
//
//        JSONObject jsonObject = null;
//        String jarrayStr = null;
//
//        try {
//            jsonObject = new JSONObject(jsonStr);
//            jarrayStr = jsonObject.getString("ssidlib");
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>topaplist:"+jarrayStr);
//            if(jarrayStr==null)
//                return null;
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    ssids = new ArrayList<SSID>();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        SSID d = new SSID();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        try {
//                            //d.setMac(jo.getString("mac"));
//                            d.setSsid(jo.getString("ssid"));
//                            d.setPassword(jo.getString("password"));
//                            //d.setTime(jo.getString("time_inserted"));
//                            d.setEncryption(jo.getString("encryption"));
//                            d.setAreadescription(jo.getString("area_desciption"));
//                            d.setDescription(jo.getString("description"));
//
//                        }catch (Exception e){
//                            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>parsing excepton:"+e.toString());
//                        }
//                        ssids.add(d);
//                    }
//                }
//            }
//            if(ssids!=null)
//            {
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>success size:"+ssids.size());
//            }else{
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>fail exception:"+e.toString());
//        }
//        return ssids;
//    }
//
//
//////////////////////////////////ssid lib api end/////////////////////////////////
//
//
//////////////////////////////////top ap api start/////////////////////////////////
//
//    public void addToTopApList(final TopAP topAP , final FuctionListener listener)
//    {
//        if(topAP!=null)
//        {
//            String mac = topAP.getMac();
//            if(mac!=null)
//                topAP.setMac(mac.toLowerCase());
//            String postString = JsonUtils.objectToString(topAP);
//            String url = BuzokuRestApi.API_TOP_AP_ADD + toURLEncoded(topAP.getSsid());
//            Log.i(DtConstant.TAG, ">>>addToTopApList>>>postString:"+postString);
//            BuzokuRestApi.doApiPostExec(url, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_TOP_AP_ADD);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                //DataManager.getInstance(context).addSsidListToDb(topAPList);
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_TOP_AP_ADD, 0);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        //DataManager.getInstance(context).addSsidListToDb(topAPList);
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_TOP_AP_ADD, 0);
//                                    }else{
//
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_TOP_AP_ADD, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_TOP_AP_ADD, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void delFromTopAPList(final TopAP topAP, final FuctionListener listener)
//    {
//        if(topAP!=null)
//        {
//            String postString = JsonUtils.objectToString(topAP);
//            String url = BuzokuRestApi.API_TOP_AP_DEL + toURLEncoded(topAP.getSsid());
//            BuzokuRestApi.doApiPostExec(url, postString,
//                    new RestApiListener() {
//                        @Override
//                        public void onPreExecute() {
//                            if(listener!=null)
//                                listener.onPreExecute(FUCTION_TOP_AP_DEL);
//                        }
//
//                        @Override
//                        public void onPostExecute(String result) {
//                            if(DtConstant.DEBUG_MODE)
//                            {
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_TOP_AP_DEL, 0);
//                                //DataManager.getInstance(context).removeSsidListFromDb(ssidList);
//                            }else{
//                                if(result!=null)
//                                {
//                                    if(result.contains("SUCCESS"))
//                                    {
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_TOP_AP_DEL, 0);
//                                        //DataManager.getInstance(context).removeSsidListFromDb(ssidList);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_TOP_AP_DEL, 1);
//                                    }
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_TOP_AP_DEL, 1);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//
//    public void queryFromTopAPList(final FuctionListener listener)
//    {
//
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_TOP_AP_QUERY,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_TOP_AP_QUERY);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onResult(FUCTION_TOP_AP_QUERY, null);
//                        }else{
//                            if(result!=null)
//                            {
//                                List<TopAP> topAPList = parseJSonArrayToTopApList(result);
//                                List<Object> resultList = new ArrayList<Object>() ;
//                                if(topAPList!=null)
//                                {
//                                    if(topAPList.size()>0)
//                                    {
//                                        for(TopAP s:topAPList)
//                                            resultList.add(s);
//                                    }
//                                }
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_TOP_AP_QUERY, resultList);
//                            }else{
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_TOP_AP_QUERY, null);
//                            }
//
//                        }
//                    }
//                });
//
//    }
//
//
//
//    List<TopAP> parseJSonArrayToTopApList(String jsonStr)
//    {
//        Log.i(DtConstant.TAG, ">>>parseJSonArrayToTopApList>>>start");
//        if(jsonStr==null)
//        {
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToTopApList>>>start null");
//            return null;
//        }
//
//        List<TopAP> topAPList = null;
//        JSONArray jarray = null;
//
//
//        JSONObject jsonObject = null;
//        String jarrayStr = null;
//
//        try {
//            jsonObject = new JSONObject(jsonStr);
//            jarrayStr = jsonObject.getString("topaplist");
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>topaplist:"+jarrayStr);
//            if(jarrayStr==null)
//                return null;
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    topAPList = new ArrayList<TopAP>();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        TopAP d = new TopAP();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        try {
//                            //d.setMac(jo.getString("mac"));
//                            d.setSsid(jo.getString("ssid"));
//                            d.setPassword(jo.getString("password"));
//                            //d.setTime(jo.getString("time_inserted"));
//                            d.setEncryption(jo.getString("encryption"));
//                            d.setDescription(jo.getString("description"));
//                            d.setAreadescription(jo.getString("area_desciption"));
//                        }catch (Exception e){
//                            Log.i(DtConstant.TAG, ">>>parseJSonArrayToTopApList>>>parsing excepton:"+e.toString());
//                        }
//                        topAPList.add(d);
//                    }
//                }
//            }
//            if(topAPList!=null)
//            {
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToTopApList>>>success size:"+topAPList.size());
//            }else{
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToTopApList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>fail exception:"+e.toString());
//        }
//        return topAPList;
//    }
//
//
//////////////////////////////////top ap api end/////////////////////////////////
//
//
//////////////////////////////mitm api start////////////////////////////////////
//
//    //开启断网， 返回值result 0：成功，1：失败 2:已在运行
//
//    public void startMitm(final List<Device> devices, String ssid, String password, String encryption,
//                          final FuctionListener listener)
//    {
//        ////for test/////
////        if(devices!=null)
////        {
////            devices.get(0).setSsid(ssid);
////            startDeauth(null , devices, 80, false);
////            try {
////                Thread.sleep(3000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
//
//        ////for test/////
//        String postString = buildMitmPostString(devices, ssid, password, encryption);
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>postString:"+postString);
//        BuzokuRestApi.doApiPostExec(BuzokuRestApi.API_START_MITM, postString,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_MITM_START);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>debug mode listener onPostExecute ");
//                            isMitmRunning = 0;
//                            if(devices!=null)
//                            {
//                                if(devices.size()>0)
//                                {
//                                    //mitmList.clear();
//                                    //mitmList.add(devices.get(0));
//                                    startGettingMacDetail(devices.get(0));
//                                }
//                            }
//                            if(currTask!=null)
//                            {
//                                DataManager.getInstance(context).updateDeviceListMitmState(currTask.getId(),
//                                        devices, Device.MITM_STATE_RUNNING);
//                                DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                        devices, null, Device.DEAUTH_STATE_NONE);
//                            }
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_MITM_START, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>listener onPostExecute");
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        DataManager.getInstance(context).updateDeviceListMitmState(currTask.getId(),
//                                                devices, Device.MITM_STATE_RUNNING);
//                                        DataManager.getInstance(context).updateDeviceListDeauthState(currTask.getId(),
//                                                devices, null, Device.DEAUTH_STATE_NONE);
//                                    }
//                                    if(listener!=null)
//                                    {
//                                        Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>listener onPostExecute success");
//                                        listener.onPostExecute(FUCTION_MITM_START, 0);
//                                    }
//                                    if(devices!=null)
//                                    {
//                                        if(devices.size()>0)
//                                        {
////                                            mitmList.clear();
////                                            mitmList.add(devices.get(0));
//                                            startGettingMacDetail(devices.get(0));
//                                        }
//                                    }
//
//                                }else{
//                                    if(result.contains("ALREADY RUNNING"))
//                                    {
//                                        Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>listener onPostExecute already running");
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_MITM_START, 2);
//                                    }else{
//                                        Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>listener onPostExecute failed 0");
//                                        if(listener!=null)
//                                            listener.onPostExecute(FUCTION_MITM_START, 1);
//                                    }
//                                }
//                            }else{
//                                Log.i(DtConstant.TAG, "BuzokuFuction>>>startMitm>>>listener onPostExecute failed 1");
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_MITM_START, 1);
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    public String buildMitmPostString(List<Device> devices, String ssid, String password, String encryption)
//    {
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>buildMitmPostString>>>devices:"+devices);
//        String postString = "";
//
//        if(devices!=null)
//        {
//            if(devices.size()>0)
//            {
//                postString = "{";
//                postString+= "\"target\":";
//                postString+="\"";
//                for(int i=0;i<devices.size();i++)
//                {
//                    Device d = devices.get(i);
//                    String temp = "";
//                    if(d.getRole()==null || d.getMac()==null)
//                    {
//                        continue;
//                    }
//                    if(d.getRole().equalsIgnoreCase("station"))
//                    {
//                        temp+= d.getMac().toLowerCase();
//                    }
//                    if(i!=devices.size()-1)
//                    {
//                        temp += ",";
//                    }
//                    postString +=temp;
//                }
//                postString+="\"";
//                postString+=",";
//                /////
//                postString+="\"interface\": \"monitor0\",";
//                postString+="\"runtime\" : \"0\",";
//                postString+="\"rssi\": \"50\",";
//                postString+="\"pcapmode\":\"2\",";
//                postString+="\"pcapinterval\":\"200\",";
//                //////
//                if(ssid!=null)
//                {
//                    if(!ssid.equals("")){
//                        postString+="\"ssid\": ";//\"star\",";
//                        postString+="\"";
//                        postString+= ssid;
//                        postString+="\",";
//                        //////
//                        if(encryption!=null)
//                        {
//                            if(!encryption.equals(""))
//                            {
//                                postString+="\"encryption\": ";
//                                postString+="\"";
//                                postString+= encryption;
//                                postString+="\",";
//                            }
//                        }
//                        //////
//                        if(password!=null)
//                        {
//                            if(!password.equals("")) {
//                                postString += "\"password\": ";
//                                postString += "\"";
//                                postString += password;
//                                postString += "\"";
//                            }
//                        }
//                    }
//                }
//                postString += "}";
//            }else{
//                postString = "";
//            }
//        }else{
//            postString = "";
//        }
//
//        Log.i(DtConstant.TAG, "BuzokuFuction>>>buildMitmPostString>>>postString:"+postString);
//        return postString;
//    }
//
//
//    void startGettingMacDetail(final Device device)
//    {
//        if(device==null)
//            return;
//        if(device.getMac()==null)
//            return;
//        if(getMacDetailThread==null || isGettingMacDetail==false)
//        {
//            isGettingMacDetail = true;
//            getMacDetailThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    while(isGettingMacDetail)
//                    {
//                        startGettingIdentityInfo(device.getMac());
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        startGettingUrlInfo(device.getMac());
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    getMacDetailThread = null;
//                }
//            });
//            getMacDetailThread.start();
//        }
//
//    }
//
//
//    void startGettingUrlInfo(String mac)
//    {
//        if(mac==null)
//            return;
//        queryMacUrls(mac, new FuctionListener() {
//            @Override
//            public void onPreExecute(int fuctionId) {
//                Log.i(DtConstant.DT_TAG1, "startGettingUrlInfo>>>onPreExecute>>>");
//            }
//
//            @Override
//            public void onPostExecute(int fuctionId, int result) {
//                Log.i(DtConstant.DT_TAG1, "startGettingUrlInfo>>>onPostExecute>>>result:"+result);
//            }
//
//            @Override
//            public void onResult(int fuctionId, List<Object> resultList) {
//                if(resultList!=null)
//                {
//                    Log.i(DtConstant.DT_TAG1, "startGettingUrlInfo>>>onResult>>>resultList :"+resultList.size());
//                }
//            }
//        });
//    }
//
//    void startGettingIdentityInfo(String mac)
//    {
//        if(mac==null)
//            return;
//        queryMacIdentitys(mac.toLowerCase(), new FuctionListener() {
//            @Override
//            public void onPreExecute(int fuctionId) {
//                Log.i(DtConstant.DT_TAG1, "startGettingIdentityInfo>>>onPreExecute>>>");
//            }
//
//            @Override
//            public void onPostExecute(int fuctionId, int result) {
//                Log.i(DtConstant.DT_TAG1, "startGettingIdentityInfo>>>onPostExecute>>>result:"+result);
//            }
//
//            @Override
//            public void onResult(int fuctionId, List<Object> resultList) {
//                if(resultList!=null)
//                {
//                    Log.i(DtConstant.DT_TAG1, "startGettingIdentityInfo>>>onResult>>>resultList :"+resultList.size());
//                }
//
//            }
//        });
//    }
//
//    public void stopMitm(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_STOP_MITM,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_MITM_STOP);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            //mitmList.clear();
//                            isMitmRunning = 1;
//                            isGettingMacDetail = false;
//                            if(currTask!=null)
//                            {
//                                List<Device> devices= DataManager.getInstance(context).getCurrMitmDevice(currTask.getId());
//                                DataManager.getInstance(context).updateDeviceListMitmState(currTask.getId(),
//                                        devices, Device.MITM_STATE_NONE);
//                            }
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_MITM_STOP, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(currTask!=null)
//                                    {
//                                        List<Device> devices= DataManager.getInstance(context).getCurrMitmDevice(currTask.getId());
//                                        DataManager.getInstance(context).updateDeviceListMitmState(currTask.getId(),
//                                                devices, Device.MITM_STATE_NONE);
//                                    }
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_MITM_STOP, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_MITM_STOP, 1);
//                                }
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_MITM_STOP, 2);
//                            }
//                            //mitmList.clear();
//                            isGettingMacDetail = false;
//                        }
//                    }
//                });
//    }
//
//
//
//    public void queryMitmState(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_MITM_STATE,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_MTIM_STATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_MTIM_STATE, isMitmRunning);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("YES"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_MTIM_STATE, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_MTIM_STATE, 1);
//                                }
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_MTIM_STATE, 2);
//                            }
//
//                        }
//                    }
//                });
//    }
//
//
//
//
//////////////////////////////mitm api end////////////////////////////////////
//
//    public String toURLEncoded(String paramString) {
//        if (paramString == null || paramString.equals("")) {
//            Log.i(DtConstant.TAG,">>>toURLEncoded error:"+paramString);
//            return "";
//        }
//
//        try
//        {
//            String str = new String(paramString.getBytes(), "UTF-8");
//            str = URLEncoder.encode(str, "UTF-8");
//            return str;
//        }
//        catch (Exception localException)
//        {
//            Log.i(DtConstant.TAG, ">>>toURLEncoded error:"+paramString+" "+localException.toString());
//        }
//
//        return "";
//    }
//
//    ////////////////////////////////同步SSID密码库和DND mac库和topaplist  start//////////////////////////////
//
//    public void startSynPhoneDataToPi()
//    {
//        Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi");
//        queryFromSsidLib(new FuctionListener() {
//            @Override
//            public void onPreExecute(int fuctionId) {
//            }
//
//            @Override
//            public void onPostExecute(int fuctionId, int result) {
//            }
//            @Override
//            public void onResult(int fuctionId, List<Object> resultList) {
//                final List<Object> tmpResultList = resultList;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<SSID> piDbSsidlist = new ArrayList<SSID>();
//                        if(tmpResultList!=null)
//                        {
//                            for(Object o:tmpResultList)
//                            {
//                                SSID ssid = (SSID)o;
//                                piDbSsidlist.add(ssid);
//                            }
//                            delFromSsidLib(piDbSsidlist, null);
//                        }
//
//                        List<SSID> ssidList = DataManager.getInstance(context).getAllSsidFromDb();
//                        if(ssidList!=null)
//                        {
//                            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add ssid size:"+ssidList.size());
//                            for(int i=0;i<ssidList.size();i++)
//                            {
//                                Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add ssid :"+ssidList.get(i).getSsid());
//                                addToSsidLib(ssidList.get(i), null);
//                                try {
//                                    Thread.sleep(600);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }else{
//                            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add ssid null");
//                        }
//                    }
//                }).start();
//            }
//        });
//
//        queryDndList(new FuctionListener() {
//            @Override
//            public void onPreExecute(int fuctionId) {
//            }
//
//            @Override
//            public void onPostExecute(int fuctionId, int result) {
//            }
//
//            @Override
//            public void onResult(int fuctionId, List<Object> resultList) {
//                final List<Object> tmpResultList = resultList;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(tmpResultList!=null)
//                        {
//                            for(Object o:tmpResultList)
//                            {
//                                DndMac d = (DndMac) o;
//                                removeDndDeviceFromWhiteList(d, null);
//                                try {
//                                    Thread.sleep(400);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        List<DndMac> dndMacList = DataManager.getInstance(context).getDndMacListFromDb();
//
//                        if(dndMacList!=null)
//                        {
//                            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add dnd size:"+dndMacList.size());
//                            for(int i=0;i<dndMacList.size();i++)
//                            {
//                                Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add dnd mac :"+dndMacList.get(i).getMac());
//                                addToDndList(dndMacList.get(i), null);
//                                try {
//                                    Thread.sleep(400);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }else{
//                            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add dnd null");
//                        }
//
//                    }
//                }).start();
//
//            }
//        });
//
//        List<TopAP> topAPList = DataManager.getInstance(context).getAllTopApFromDb();
//
//        if(topAPList!=null)
//        {
//            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add top ap size:"+topAPList.size());
//            for(int i=0;i<topAPList.size();i++)
//            {
//                Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add top ap :"+topAPList.get(i).getSsid());
//                addToTopApList(topAPList.get(i), null);
//                try {
//                    Thread.sleep(400);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }else{
//            Log.i(DtConstant.TAG,">>>startSynPhoneDataToPi add top ap list null");
//        }
//    }
//
//    ////////////////////////////////同步SSID密码库和DND mac库和topaplist  end//////////////////////////////
//
//    ////////////////////////////////mac 虚拟身份和URL  start//////////////////////////////
//
//    public void queryMacIdentitys(final String mac , final FuctionListener listener)
//    {
//        if(mac==null)
//        {
//            return;
//        }
//        String url = BuzokuRestApi.API_GET_IDENTITY + mac.toLowerCase();
//        BuzokuRestApi.doApiGetExec(url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_MAC_IDENTITY);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            List<Identity> ids = new ArrayList<Identity>() ;
//                            List<Object> resultList = new ArrayList<Object>() ;
//                            Identity id1 = new Identity();
//                            id1.setMac(mac);
//                            id1.setTag("qq");
//                            id1.setValue("138756888");
//                            ids.add(id1);
//                            Identity id2 = new Identity();
//                            id2.setMac(mac);
//                            id2.setTag("weixinid");
//                            id2.setValue("xiaohua");
//                            ids.add(id2);
//                            addIdentitysTODb(ids);
//                            List<Identity> reslist = DataManager.getInstance(context).queryIdentity(mac);
//                            if(reslist!=null)
//                            {
//                                for(Identity d:reslist)
//                                {
//                                    resultList.add(d);
//                                }
//                            }
//                            if(listener!=null)
//                                listener.onResult(FUCTION_MAC_IDENTITY, resultList);
//                        }else{
//                            if(result!=null)
//                            {
//                                List<Identity> identities = parseJSonArrayToIdList(mac , result);
//                                addIdentitysTODb(identities);
//                                identities = DataManager.getInstance(context).queryIdentity(mac);
//                                List<Object> resultList = new ArrayList<Object>() ;
//                                if(identities!=null)
//                                {
//                                    if(identities.size()>0)
//                                    {
//                                        for(Identity s:identities)
//                                            resultList.add(s);
//                                    }
//                                }
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_MAC_IDENTITY, resultList);
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_MAC_IDENTITY, null);
//                            }
//                        }
//                    }
//                });
//
//    }
//
//    void addIdentitysTODb(List<Identity> identityList)
//    {
//        if(identityList==null)
//            return;
//        if(identityList.size()==0)
//            return;
//        DataManager.getInstance(context).addIdentityListToDb(identityList);
//    }
//
//
//    List<Identity> parseJSonArrayToIdList(String mac, String jsonStr)
//    {
//        Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>start");
//        if(jsonStr==null)
//        {
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>start null");
//            return null;
//        }
//
//        List<Identity> ids = null;
//        JSONArray jarray = null;
//
//        JSONObject jsonObject = null;
//        String jarrayStr = null;
//
//        try {
//            jsonObject = new JSONObject(jsonStr);
//            jarrayStr = jsonObject.getString("data");
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>jarrayStr:"+jarrayStr);
//            if(jarrayStr==null)
//                return null;
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    ids = new ArrayList<Identity>();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        Identity d = new Identity();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        try {
//                            d.setTag(jo.getString("tag"));
//                            d.setValue(jo.getString("value"));
//                            d.setTime(TimeUtils.getCurrentTimeInLong());
//                            d.setMac(mac);
//                        }catch (Exception e){
//                            Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>parsing excepton:"+e.toString());
//                        }
//                        ids.add(d);
//                    }
//                }
//            }
//            if(ids!=null)
//            {
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>success size:"+ids.size());
//            }else{
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToIdList>>>fail exception:"+e.toString());
//        }
//        return ids;
//    }
//
//
//    public void queryMacUrls(final String mac , final FuctionListener listener)
//    {
//        if(mac==null)
//        {
//            return;
//        }
//        String url = BuzokuRestApi.API_GET_URL + mac.toLowerCase();
//        BuzokuRestApi.doApiGetExec(url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_MAC_URL);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            List<Object> resultList = new ArrayList<Object>() ;
//                            List<URL> urlList = new ArrayList<URL>() ;
//                            URL id1 = new URL();
//                            id1.setMac(mac);
//                            id1.setUrl("www.baidu.com");
//                            id1.setTag("url");
//                            id1.setTime(1502504598);
//                            urlList.add(id1);
//
//                            URL id2 = new URL();
//                            id2.setMac(mac);
//                            id2.setUrl("www.163.com");
//                            id2.setTag("url");
//                            id2.setTime(1502850145);
//                            urlList.add(id2);
//
//                            URL id3 = new URL();
//                            id3.setMac(mac);
//                            id3.setUrl("www.qq.com");
//                            id3.setTag("url");
//                            id3.setTime(1502850198);
//                            urlList.add(id3);
//
//                            URL id4 = new URL();
//                            id4.setMac(mac);
//                            id4.setUrl("www.sohu.com");
//                            id4.setTag("url");
//                            id4.setTime(1502936500);
//                            urlList.add(id4);
//
//                            URL id5 = new URL();
//                            id5.setMac(mac);
//                            id5.setUrl("www.jd.com");
//                            id5.setTag("url");
//                            id5.setTime(1502936598);
//                            urlList.add(id5);
//
//                            URL id6 = new URL();
//                            id6.setMac(mac);
//                            id6.setUrl("www.taobao.com");
//                            id6.setTag("url");
//                            id6.setTime(1503022949);
//                            urlList.add(id6);
//
//                            URL id7 = new URL();
//                            id7.setMac(mac);
//                            id7.setUrl("www.163.com");
//                            id7.setTag("url");
//                            id7.setTime(1503022998);
//                            urlList.add(id7);
//
//                            addUrlsTODb(urlList);
//                            urlList = DataManager.getInstance(context).queryUrl(mac);
//                            if(urlList!=null)
//                            {
//                                for(URL u:urlList)
//                                    resultList.add(u);
//                            }
//                            if(listener!=null)
//                                listener.onResult(FUCTION_MAC_URL, resultList);
//                        }else{
//                            if(result!=null)
//                            {
//                                List<URL> urls = parseJSonArrayToUrlList(mac , result);
//                                addUrlsTODb(urls);
//                                urls = DataManager.getInstance(context).queryUrl(mac);
//                                List<Object> resultList = new ArrayList<Object>() ;
//                                if(urls!=null)
//                                {
//                                    if(urls.size()>0)
//                                    {
//                                        for(URL s:urls)
//                                            resultList.add(s);
//                                    }
//                                }
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_MAC_URL, resultList);
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_MAC_URL, null);
//                            }
//
//                        }
//                    }
//                });
//
//    }
//
//    void addUrlsTODb(List<URL> urlList)
//    {
//        if(urlList==null)
//            return;
//        if(urlList.size()==0)
//            return;
//        DataManager.getInstance(context).addUrlListToDb(urlList);
//    }
//
//    List<URL> parseJSonArrayToUrlList(String mac, String jsonStr)
//    {
//        Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>start");
//        if(jsonStr==null)
//        {
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>start null");
//            return null;
//        }
//
//        List<URL> urls = null;
//        JSONArray jarray = null;
//
//
//        JSONObject jsonObject = null;
//        String jarrayStr = null;
//
//        try {
//            jsonObject = new JSONObject(jsonStr);
//            jarrayStr = jsonObject.getString("data");
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>data:"+jarrayStr);
//            if(jarrayStr==null)
//                return null;
//            jarray = new JSONArray(jarrayStr);
//            if(jarray!=null)
//            {
//                if(jarray.length()>0)
//                {
//                    urls = new ArrayList<URL>();
//                    for(int i=0;i<jarray.length();i++)
//                    {
//                        URL d = new URL();
//                        JSONObject jo = jarray.getJSONObject(i);
//                        try {
//                            d.setMac(mac);
//                            d.setUrl(jo.getString("value"));
//                            d.setTag(jo.getString("tag"));
//                            d.setTime(Long.parseLong(jo.getString("time_seen")));
//                        }catch (Exception e){
//                            Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>parsing excepton:"+e.toString());
//                        }
//                        urls.add(d);
//                    }
//                }
//            }
//            if(urls!=null)
//            {
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>success size:"+urls.size());
//            }else{
//                Log.i(DtConstant.TAG, ">>>parseJSonArrayToUrlList>>>success null");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i(DtConstant.TAG, ">>>parseJSonArrayToSsidList>>>fail exception:"+e.toString());
//        }
//        return urls;
//    }
//
//    ////////////////////////////////mac 虚拟身份和URL  end//////////////////////////////
//    ////////////////////////////////APP update start//////////////////////////////
//
//    public void queryCheckAppUpdate(final FuctionListener listener)
//    {
//        String url = BuzokuRestApi.API_CHECK_APP_UPDATE;
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.DETER_API_HOST, url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_CHECK_APP_UPDATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(result!=null)
//                        {
//                            Log.i(DtConstant.TAG, ">>>queryCheckAppUpdate>>>onPostExecute result:"+result);
//                            try {
//                                JSONObject jsonObject = new JSONObject(result);
//                                if(jsonObject != null)
//                                {
//                                    List<Object> resultList = new ArrayList<Object>() ;
//                                    resultList.add(jsonObject.getString("version"));
//                                    resultList.add(""+jsonObject.getInt("versionCode"));
//                                    resultList.add(jsonObject.getString("filePath"));
//                                    resultList.add(jsonObject.getString("fileMD5"));
//                                    resultList.add(jsonObject.getString("versionNote"));
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_CHECK_APP_UPDATE, resultList);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_CHECK_APP_UPDATE, null);
//                                }
//                            }catch (Exception e){
//                                Log.i(DtConstant.TAG, ">>>queryCheckAppUpdate>>>onPostExecute exception:"+e.toString());
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_CHECK_APP_UPDATE, null);
//                            }
//
//                        }else{
//                            if(listener!=null)
//                                listener.onResult(FUCTION_CHECK_APP_UPDATE, null);
//                        }
//
//                    }
//                });
//
//    }
//
//    public void startDownloadFile(final String fileUrl, final DownloadUtil.OnDownloadListener listener)
//    {
//        final String saveDir = DETER_DIR_NAME;
//        final String url = BuzokuRestApi.DETER_API_HOST + fileUrl;
//        DownloadUtil.get().download(url, saveDir, listener);
//    }
//
//
//    ////////////////////////////////APP update end//////////////////////////////
//
//    ////////////////////////////////APP Pi syn time start//////////////////////////////
//
//    public void synAppTimeToPi(final FuctionListener listener)
//    {
//        String time = TimeUtils.getCurrentTimeInString();
//        String url = BuzokuRestApi.API_SYN_TIME + toURLEncoded(TimeUtils.getCurrentUtcTimeInString());
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_SYN_TIME);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_SYN_TIME, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                Log.i(DtConstant.TAG, ">>>synAppTimeToPi>>>onPostExecute result:"+result);
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SYN_TIME, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_SYN_TIME, 1);
//                                }
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_SYN_TIME, 1);
//                            }
//                        }
//
//                    }
//                });
//
//    }
//
//    ////////////////////////////////APP Pi syn time end//////////////////////////////
//
//
//    ////////////////////////////////Pi system update start//////////////////////////////
//    //versionNum为0时，更新最新版本
//    public void startPiSystemUpdate(String versionNum, final FuctionListener listener)
//    {
//        String url = BuzokuRestApi.API_SYSTEM_UPDATE ;
//        if(versionNum != null)
//        {
//            String param = "?version="+versionNum;
//            url += param;
//        }
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_PI_SW_UPDATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_PI_SW_UPDATE, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_PI_SW_UPDATE, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_PI_SW_UPDATE, 1);
//                                }
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_PI_SW_UPDATE, 1);
//                            }
//                        }
//
//                    }
//                });
//
//    }
//
//    public void queryPiSystemVersion(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SYSTEM_VERSION,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_PI_SW_VERSION);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onResult(FUCTION_PI_SW_VERSION, null);
//                        }else{
//                            if(result!=null)
//                            {
//                                Log.i(DtConstant.TAG, ">>>queryPiSystemVersion>>>onPostExecute result:"+result);
//                                try {
//                                    JSONObject jsonObject = new JSONObject(result);
//                                    if(jsonObject != null)
//                                    {
//                                        List<Object> resultList = new ArrayList<Object>() ;
//                                        resultList.add(jsonObject.getString("Version"));
//                                        resultList.add(jsonObject.getString("Build"));
//                                        if(listener!=null)
//                                            listener.onResult(FUCTION_PI_SW_VERSION, resultList);
//                                    }else{
//                                        if(listener!=null)
//                                            listener.onResult(FUCTION_PI_SW_VERSION, null);
//                                    }
//                                }catch (Exception e){
//                                    Log.i(DtConstant.TAG, ">>>queryPiSystemVersion>>>onPostExecute exception:"+e.toString());
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_PI_SW_VERSION, null);
//                                }
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_PI_SW_VERSION, null);
//                            }
//                        }
//
//                    }
//                });
//
//    }
//
//
//    public void queryCheckPiUpdate(final FuctionListener listener)
//    {
//        String url = BuzokuRestApi.API_CHECK_PI_UPDATE;
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.DETER_API_HOST, url,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_CHECK_PI_UPDATE);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(result!=null)
//                        {
//                            Log.i(DtConstant.TAG, ">>>queryCheckPiUpdate>>>onPostExecute result:"+result);
//                            try {
//                                JSONObject jsonObject = new JSONObject(result);
//                                if(jsonObject != null)
//                                {
//                                    List<Object> resultList = new ArrayList<Object>() ;
//                                    resultList.add(jsonObject.getString("version"));
//                                    resultList.add(jsonObject.getString("build"));
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_CHECK_PI_UPDATE, resultList);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onResult(FUCTION_CHECK_PI_UPDATE, null);
//                                }
//                            }catch (Exception e){
//                                Log.i(DtConstant.TAG, ">>>queryCheckPiUpdate>>>onPostExecute exception:"+e.toString());
//                                if(listener!=null)
//                                    listener.onResult(FUCTION_CHECK_PI_UPDATE, null);
//                            }
//
//                        }else{
//                            if(listener!=null)
//                                listener.onResult(FUCTION_CHECK_PI_UPDATE, null);
//                        }
//
//                    }
//                });
//
//    }
//
//    ////////////////////////////////Pi system update end//////////////////////////////
//
//    ////////////////////////////////extend Pi file system start//////////////////////////////
//
//    public void startExtendFileSystem(final FuctionListener listener)
//    {
//        BuzokuRestApi.doApiGetExec(BuzokuRestApi.API_HOST, BuzokuRestApi.API_SYSTEM_EXTFSYS,
//                new RestApiListener() {
//                    @Override
//                    public void onPreExecute() {
//                        if(listener!=null)
//                            listener.onPreExecute(FUCTION_EXTEND_FILE_SYS);
//                    }
//
//                    @Override
//                    public void onPostExecute(String result) {
//                        if(DtConstant.DEBUG_MODE)
//                        {
//                            if(listener!=null)
//                                listener.onPostExecute(FUCTION_EXTEND_FILE_SYS, 0);
//                        }else{
//                            if(result!=null)
//                            {
//                                Log.i(DtConstant.TAG, ">>>startExtendFileSystem>>>onPostExecute result:"+result);
//                                if(result.contains("SUCCESS"))
//                                {
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_EXTEND_FILE_SYS, 0);
//                                }else{
//                                    if(listener!=null)
//                                        listener.onPostExecute(FUCTION_EXTEND_FILE_SYS, 1);
//                                }
//
//                            }else{
//                                if(listener!=null)
//                                    listener.onPostExecute(FUCTION_EXTEND_FILE_SYS, 1);
//                            }
//                        }
//
//                    }
//                });
//
//    }
//
//    ////////////////////////////////extend Pi file system end//////////////////////////////
//
//

}
