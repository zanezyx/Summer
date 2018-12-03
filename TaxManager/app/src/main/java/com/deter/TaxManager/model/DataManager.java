package com.deter.TaxManager.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.deter.TaxManager.R;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.ExcelUtils;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.utils.RandomUtils;
import com.deter.TaxManager.utils.TimeUtils;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuxinz on 2017/7/18.
 */

public class DataManager {

//    private String currListenAddress = null;
//    private double currLatitude;
//    private double currLongtitude;
//    private long currTaskId;

    private Context context;
    private static DataManager instance;
    private BaseInfo mBaseInfo;
    private ParentInfo mFatherInfo;
    private ParentInfo mMontherInfo;
    private ChildrenInfo mFirstChildInfo;
    private ChildrenInfo mSecondChildInfo;

    public static String CACHE_FILE_DIRECTORY = Environment.getExternalStorageDirectory().toString()+"/";
    public final static String BASE_INFO_CACHE_FILE = "base_info.bin";
    public final static String FATHER_INFO_CACHE_FILE = "father_info.bin";
    public final static String MONTHER_INFO_CACHE_FILE = "monther_info.bin";
    public final static String FIRST_CHILD_INFO_CACHE_FILE = "first_child_info.bin";
    public final static String SECOND_CHILD_INFO_CACHE_FILE = "second_child_info.bin";

    public final static int SPECIAL_FLAG_WHITE_NAME = 0;
    public final static int SPECIAL_FLAG_BLACK_NAME = 1;
    public final int MSG_DB_DONE_BACK = 500;
    public static final int MSG_TASK_ANALYSE_OK = 200;
    public static final int MSG_STRIKE_ANALYSE_OVER = 201;
    public static final int MSG_FOLLOW_ANALYSE_PERCENT = 300;
    public static final int MSG_FOLLOW_ANALYSE_SUCCESSS = 301;
    public static final int MSG_FOLLOW_ANALYSE_NO_TASK = 302;
    public static final int MSG_FOLLOW_ANALYSE_NO_MAC = 303;


    private DataManager(Context context) {

        this.context = context;
    }


    public static DataManager getInstance(Context context){
        if(instance == null){
            synchronized (DataManager.class){
                if(instance == null){
                    instance = new DataManager(context.getApplicationContext());
                }
            }
        }

        return instance;
    }

    public void initAllInfo()
    {
        mBaseInfo = (BaseInfo) CacheManager.readObject(context,BASE_INFO_CACHE_FILE);
        mFatherInfo = (ParentInfo) CacheManager.readObject(context,FATHER_INFO_CACHE_FILE);
        mMontherInfo = (ParentInfo) CacheManager.readObject(context,MONTHER_INFO_CACHE_FILE);
        mFirstChildInfo = (ChildrenInfo) CacheManager.readObject(context,FIRST_CHILD_INFO_CACHE_FILE);
        mSecondChildInfo = (ChildrenInfo) CacheManager.readObject(context,SECOND_CHILD_INFO_CACHE_FILE);
        Log.i("tax", "DataManager>>>initAllInfo "+BASE_INFO_CACHE_FILE);
        if(mBaseInfo==null){
            Log.i("tax", "DataManager>>>initAllInfo>>>mBaseInfo:null");
        }else{
            Log.i("tax", "DataManager>>>initAllInfo>>>mBaseInfo getname:"+mBaseInfo.getName());
        }
    }

    public void saveAllInfo()
    {
        Log.i("tax", "DataManager>>>saveAllInfo "+BASE_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mBaseInfo,BASE_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mFatherInfo,FATHER_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mMontherInfo,MONTHER_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mFirstChildInfo,FIRST_CHILD_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mSecondChildInfo,SECOND_CHILD_INFO_CACHE_FILE);
    }


    public BaseInfo getmBaseInfo() {
        return mBaseInfo;
    }

    public void setmBaseInfo(BaseInfo mBaseInfo) {
        this.mBaseInfo = mBaseInfo;
    }

    public ParentInfo getmFatherInfo() {
        return mFatherInfo;
    }

    public void setmFatherInfo(ParentInfo mFatherInfo) {
        this.mFatherInfo = mFatherInfo;
    }

    public ParentInfo getmMontherInfo() {
        return mMontherInfo;
    }

    public void setmMontherInfo(ParentInfo mMontherInfo) {
        this.mMontherInfo = mMontherInfo;
    }

    public ChildrenInfo getmFirstChildInfo() {
        return mFirstChildInfo;
    }

    public void setmFirstChildInfo(ChildrenInfo mFirstChildInfo) {
        this.mFirstChildInfo = mFirstChildInfo;
    }

    public ChildrenInfo getmSecondChildInfo() {
        return mSecondChildInfo;
    }

    public void setmSecondChildInfo(ChildrenInfo mSecondChildInfo) {
        this.mSecondChildInfo = mSecondChildInfo;
    }


////////////////////Device db operation start//////////////////////

    public List<Device> getAllDeviceFromDb()
    {
        List<Device> devices = null;
        final DeviceDao deviceDao = new DeviceDao(context);

        try{
            devices = deviceDao.getDao().queryForAll();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllDeviceFromDb>>>exception:"+e.toString());
        }

        return devices;
    }

    public void addDebugModeDeviceToDb()
    {
        List<Device> devices = null;
        final DeviceDao deviceDao = new DeviceDao(context);

        try{
            devices = deviceDao.getDao().queryBuilder().where().eq("task_id", 10000).query();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllDeviceFromDb>>>exception:"+e.toString());
        }

        if(devices!=null)
        {
            if(devices.size()==0)
            {
                addTempDeviceToDb();
            }
        }else{
            addTempDeviceToDb();
        }
    }


    public  void updateDevice(Device device)
    {
        DeviceDao deviceDao = new DeviceDao(context);
        try{
            deviceDao.getDao().update(device);
            Log.i(DtConstant.TAG, "updateDevice>>>");
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "updateDevice>>>exception:"+e.toString());
        }
    }


    public  void addDeviceToDB(Device device)
    {
        Log.i(DtConstant.DT_TAG2, "addDeviceToDB>>>");
        if(device!=null)
        {
            device.setMac(device.getMac().toLowerCase());
            DeviceDao deviceDao = new DeviceDao(context);
            deviceDao.add(device);
            Log.i(DtConstant.DT_TAG2, "addDeviceToDB>>>"+device.toString());
        }
    }


    public  void deleteDeviceFromDb(Device device)
    {
        if(device==null)
            return;
        DeviceDao deviceDao = new DeviceDao(context);
        deviceDao.delete((int)device.getId());
        Log.i(DtConstant.DT_TAG, "deleteDeviceFromDb>>>");
    }


    public  List<Device> queryDeviceByMac(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        Log.i(DtConstant.DT_TAG, "queryDeviceByMac>>>");
        List<Device> devices = null;
        if(mac!=null)
        {
            DeviceDao deviceDao = new DeviceDao(context);
            try {
                devices = deviceDao.getDao().queryBuilder().where().eq("mac", mac).query();

            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(DtConstant.TAG, "queryDeviceByMac>>>excepton:"+e.toString());
            }
        }
        return devices;
    }


    public  List<Device> queryDeviceByMac(long taskId, String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        Log.i(DtConstant.DT_TAG, "queryDeviceByMac>>>");
        List<Device> devices = null;
        if(mac!=null)
        {
            DeviceDao deviceDao = new DeviceDao(context);
            try {
                devices = deviceDao.getDao().queryBuilder().where().eq("mac", mac).and().
                            eq("task_id", taskId).query();

            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(DtConstant.TAG, "queryDeviceByMac>>>excepton:"+e.toString());
            }
        }
        return devices;
    }


    public  Device queryDeviceById(long id)
    {
        Log.i(DtConstant.TAG, "queryDeviceById>>>");
        Device device = null;

        DeviceDao deviceDao = new DeviceDao(context);
        try {
            device = deviceDao.getDao().queryBuilder().where().eq("id", id).queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "queryDeviceById>>>excepton:"+e.toString());
        }
        return device;
    }

    //time : 查询时间范围 秒
    public List<Device> queryDeviceByTaskIdAndTime(long taskId, long time)
    {
//        SELECT * FROM Persons WHERE (FirstName='Thomas' OR FirstName='William')
//        AND LastName='Carter'
        Log.i(DtConstant.DT_TAG2, "queryDeviceByTaskIdAndTime>>>");
        List<Device> devices = null;
        List<Device> deauthDevices = null;
        List<Device> mitmDevices = null;
        long nowTime = System.currentTimeMillis();
        long queryTime = nowTime - time*1000;
        DeviceDao deviceDao = new DeviceDao(context);
        try {
            if(DtConstant.DEBUG_MODE)
            {
                devices = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).query();
            }else{
                //Log.i(DtConstant.TAG, "queryDeviceByTaskIdAndTime>>>taskId:"+taskId);
                devices = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).
                        and().ge("time",queryTime).and().eq("deauth_state", Device.DEAUTH_STATE_NONE).
                        and().eq("mitm_state", Device.MITM_STATE_NONE).query();
                deauthDevices = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).
                        and().ne("deauth_state",Device.DEAUTH_STATE_NONE).query();
                mitmDevices = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).
                        and().ne("mitm_state",Device.MITM_STATE_NONE).and().
                        eq("deauth_state", Device.DEAUTH_STATE_NONE).query();
                if(deauthDevices!=null)
                    devices.addAll(deauthDevices);
                if(mitmDevices!=null)
                    devices.addAll(mitmDevices);
            }
            //if(devices!=null)
                //Log.i(DtConstant.TAG, "queryDeviceByTaskIdAndTime>>>devices size:"+devices.size());

        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.DT_TAG2, "queryDeviceByTaskIdAndTime>>>excepton:"+e.toString());
        }
        return devices;
    }

    public List<Device> getDeviceListByTaskId(long taskId)
    {
        Log.i(DtConstant.DT_TAG2, "queryDeviceByTaskId>>>");
        List<Device> devices = null;
        DeviceDao deviceDao = new DeviceDao(context);
        try {
            devices = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).query();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.DT_TAG2, "queryDeviceByTaskId>>>excepton:"+e.toString());
        }
        return devices;
    }


    public  void addDeviceListToDB(final List<Device> devices)
    {
        Log.i(DtConstant.TAG, "addDeviceListToDB>>>");
        if(devices!=null)
        {
            if(devices.size()>0)
            {
                DeviceDao deviceDao = new DeviceDao(context);
                for(Device d:devices)
                {
                    deviceDao.add(d);
                }
            }
        }
    }


    //获取数据库断网设备
    public List<Device> getCurrDeauthDevice(long taskId)
    {
        DeviceDao deviceDao = new DeviceDao(context);
        List<Device> devices = new ArrayList<Device>();
        try{
            devices = deviceDao.getDao().queryBuilder().where().eq("deauth_state", 1)
                    .or().eq("deauth_state", 2).and().eq("task_id",taskId).query();

            String temp = "";
            for(Device d:devices)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getCurrDeauthDevice>>>db:devices:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getCurrDeauthDevice>>>exception:"+e.toString());
        }

        return devices;
    }

    //获取数据库强连设备
    public List<Device> getCurrMitmDevice(long taskId)
    {
        DeviceDao deviceDao = new DeviceDao(context);
        List<Device> devices = new ArrayList<Device>();
        try{
            devices = deviceDao.getDao().queryBuilder().where().eq("mitm_state", 1)
                    .or().eq("mitm_state", 2).and().eq("task_id",taskId).query();

            String temp = "";
            for(Device d:devices)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getCurrMitmDevice>>>db:devices:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getCurrMitmDevice>>>exception:"+e.toString());
        }

        return devices;
    }

    public void updateDeviceDeauthState(long taskId, String mac, int deauthState)
    {
        Log.i(DtConstant.TAG, "updateDeviceDeauthState>>>mac:"+mac+" deauthState:"+deauthState);
        DeviceDao deviceDao = new DeviceDao(context);
        List<Device> devices = null;
        try{
            devices = deviceDao.getDao().queryBuilder().where().eq("mac", mac)
                    .and().eq("task_id",taskId).query();
            if(devices!=null)
            {
                if(devices.size()>0){
                    for(Device d:devices){
                        d.setDeauthState(deauthState);
                        deviceDao.update(d);
                    }
                }
            }

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "updateDeviceDeauthState>>>exception:"+e.toString());
        }

    }

    public void updateDeviceMitmState(long taskId, String mac, int mitmState)
    {
        Log.i(DtConstant.TAG, "updateDeviceMitmState>>>mac:"+mac+" mitmState:"+mitmState);
        DeviceDao deviceDao = new DeviceDao(context);
        List<Device> devices = null;
        try{
            devices = deviceDao.getDao().queryBuilder().where().eq("mac", mac)
                    .and().eq("task_id",taskId).query();
            if(devices!=null)
            {
                if(devices.size()>0){
                    for(Device d:devices){
                        d.setMitmState(mitmState);
                        deviceDao.update(d);
                    }
                }
            }

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "updateDeviceMitmState>>>exception:"+e.toString());
        }
    }

    public void updateDeviceListDeauthState(final long taskId, final List<Device> deviceList,
                                            final List<Device> oldDeviceList, final int deauthState)
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (this){
//
//                    if(deviceList!=null)
//                    {
//                        for(Device d:deviceList)
//                        {
//                            if(d!=null)
//                                updateDeviceDeauthState(taskId, d.getMac(), deauthState);
//                        }
//                    }
//                }
//            }
//        }).start();
    }

    public void updateDeviceListMitmState(final long taskId, final List<Device> deviceList, final int mitmState)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(deviceList!=null)
                    {
                        for(Device d:deviceList)
                        {
                            if(d!=null)
                                updateDeviceMitmState(taskId, d.getMac(), mitmState);
                        }
                    }
                }
            }
        }).start();
    }

////////////////////Device db operation end//////////////////////

//    void sendDbDoneMsg(DbOperationListener listener , List<Object> datalist ,
//                                   int res, int functionId)
//    {
//        if(listener!=null)
//        {
//            MessageContent msgContent = new MessageContent();
//            msgContent.listener = listener;
//            msgContent.resultList = datalist;
//            msgContent.fuctionId = functionId;
//            msgContent.result = res;
//            Message message = new Message();
//            message.what = MSG_DB_DONE_BACK;
//            message.obj = msgContent;
//            mHandler.sendMessage(message);
//        }
//
//    }


////////////////////white name list and black name list  db operation start/////////////


    public List<SpecialDevice> getAllSpecialDeviceListFromDb()
    {
        final SpecialDeviceDao specialDeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = new ArrayList<SpecialDevice>();
        try {
            specialDevices = specialDeviceDao.getDao().queryForAll();
            String temp = "";
            if (null != specialDevices) {
                for (SpecialDevice d : specialDevices) {
                    d.setMac(d.getMac().toLowerCase());
                    temp += d.toString() + "\n";
                }
            }
            Log.i(DtConstant.TAG, "getAllSpecialDeviceListFromDb>>>db:specialDevice name devices:" + temp);
        } catch (SQLException e) {
            Log.i(DtConstant.TAG, "getWhiteListFromDb>>>exception:" + e.toString());
        }
        return specialDevices;
    }


    public List<SpecialDevice> getWhiteListFromDb()
    {
        final SpecialDeviceDao specialDeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;
        try{
            specialDevices = specialDeviceDao.getDao().queryBuilder().where().eq("special_flag", 0).query();
            String temp = "";
            if(specialDevices!=null)
            {
                for(SpecialDevice d:specialDevices)
                {
                    d.setMac(d.getMac().toLowerCase());
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getWhiteListFromDb>>>db:white name devices:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getWhiteListFromDb>>>exception:"+e.toString());
        }
        return specialDevices;
    }


    public List<SpecialDevice> getBlackListFromDb()
    {
        final SpecialDeviceDao specialdeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;

        try{
            specialDevices = specialdeviceDao.getDao()
                    .queryBuilder().where().eq("special_flag", 1).query();
            String temp = "";
            if(specialDevices!=null)
            {
                for(SpecialDevice d:specialDevices)
                {
                    d.setMac(d.getMac().toLowerCase());
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getBlackListFromDb>>>db: black name devices:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getBlackListFromDb>>>exception:"+e.toString());
        }
        return specialDevices;
    }



    public void addWhiteListToDb(final List<Device> devicelist)
    {
        if(devicelist!=null)
        {
            for(Device d:devicelist)
            {
                addWhiteListOrBlackListToDb(d, SPECIAL_FLAG_WHITE_NAME);
            }
        }
    }

    public void addBlackListToDb(final List<Device> devicelist)
    {
        if(devicelist!=null)
        {
            for(Device d:devicelist)
            {
                addWhiteListOrBlackListToDb(d, SPECIAL_FLAG_BLACK_NAME);
            }
        }

    }

    public void addWhiteListToDb(final Device device)
    {
        addWhiteListOrBlackListToDb(device, SPECIAL_FLAG_WHITE_NAME);
    }

    public void addBlackListToDb(final Device device)
    {
        addWhiteListOrBlackListToDb(device, SPECIAL_FLAG_BLACK_NAME);
    }


    public void addWhiteListOrBlackListToDb(Device device, int specialFlag)
    {
        if(device==null)
            return;
        SpecialDeviceDao specialDeviceDao = new SpecialDeviceDao(context);
        SpecialDevice sd = new SpecialDevice();
        sd.setEncryption(device.getEncryption());
        sd.setPassword(device.getPassword());
        sd.setDescription(device.getDescription());
        sd.setSsid(device.getSsid());
        sd.setMac(device.getMac().toLowerCase());
        sd.setRole(device.getRole());
        sd.setTimeInserted(TimeUtils.getCurrentTimeInLong());
        sd.setSpecialFlag(specialFlag);
        String[] vendors = queryVendor(device.getMac());
        if(vendors!=null)
        {
            sd.setVendercn(vendors[0]);
            sd.setVenderen(vendors[1]);
        }
        specialDeviceDao.add(sd);
    }


    public void removeWhiteListOrBlackListFromDb(String mac, int specialFlag)
    {
        Log.i(DtConstant.TAG, "removeWhiteListOrBlackListToDb>>"+specialFlag);
        if(mac==null)
            return;
        String destMac = mac.toLowerCase();
        SpecialDeviceDao specialDeviceDao = new SpecialDeviceDao(context);
        DeleteBuilder<SpecialDevice, Integer> deleteBuilder = specialDeviceDao.getDao().deleteBuilder();
        try {
            deleteBuilder.where().eq("special_flag", specialFlag).and().eq("mac", destMac);
            deleteBuilder.delete();
            Log.i(DtConstant.TAG, "removeWhiteListOrBlackListToDb>>"+specialFlag+" success");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "removeWhiteListOrBlackListToDb>>"+specialFlag+" fail"+e.toString());
        }
    }

    public void removeWhiteListFromDb(String mac)
    {
        removeWhiteListOrBlackListFromDb(mac, SPECIAL_FLAG_WHITE_NAME);
    }

    public void removeBlackListFromDb(String mac)
    {
        removeWhiteListOrBlackListFromDb(mac, SPECIAL_FLAG_BLACK_NAME);
    }

    public void removeWhiteListFromDb(final List<Device> deviceList)
    {
        if(deviceList==null)
            return;

        for (Device d:deviceList)
        {
            removeWhiteListOrBlackListFromDb(d.getMac().toLowerCase(), SPECIAL_FLAG_WHITE_NAME);
        }

    }

    public void removeBlackListFromDb(final List<Device> deviceList)
    {
        if(deviceList==null)
            return;

        for (Device d:deviceList)
        {
            removeWhiteListOrBlackListFromDb(d.getMac().toLowerCase(), SPECIAL_FLAG_BLACK_NAME);
        }

    }


    public boolean queryMacIsInBlacklist(String mac)
    {
        if(mac==null)
            return false;
        mac = mac.toLowerCase();
        final SpecialDeviceDao specialdeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;
        boolean result = false;
        try{
            specialDevices = specialdeviceDao.getDao()
                    .queryBuilder().where().eq("special_flag", SPECIAL_FLAG_BLACK_NAME).and().eq("mac",mac).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryMacIsInBlacklist>>>exception:"+e.toString());
        }
        if(specialDevices!=null)
        {
            if(specialDevices.size()>0)
            {
                result = true;
            }
        }
        return result;
    }

    public boolean queryMacIsInWhitelist(String mac)
    {
        if(mac==null)
            return false;
        mac = mac.toLowerCase();
        final SpecialDeviceDao specialdeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;
        boolean result = false;
        try{
            specialDevices = specialdeviceDao.getDao()
                    .queryBuilder().where().eq("special_flag", SPECIAL_FLAG_WHITE_NAME).and().eq("mac",mac).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryMacIsInWhilelist>>>exception:"+e.toString());
        }
        if(specialDevices!=null)
        {
            if(specialDevices.size()>0)
            {
                result = true;
            }
        }
        return result;
    }


    public List<SpecialDevice> queryMacInWhitelist(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        final SpecialDeviceDao specialdeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;
        try{
//            specialDevices = specialdeviceDao.getDao()
//                    .queryBuilder().where().eq("special_flag", 0).and().eq("mac",mac).query();
            specialDevices = specialdeviceDao.getDao().queryBuilder().where().eq("special_flag", SPECIAL_FLAG_WHITE_NAME).and()
                    .like("mac", "%"+mac+"%").query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryMacInWhilelist>>>exception:"+e.toString());
        }
        return specialDevices;
    }

    public List<SpecialDevice> queryMacInBlacklist(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        final SpecialDeviceDao specialdeviceDao = new SpecialDeviceDao(context);
        List<SpecialDevice> specialDevices = null;
        try{
//            specialDevices = specialdeviceDao.getDao()
//                    .queryBuilder().where().eq("special_flag", 1).and().eq("mac",mac).query();
            specialDevices = specialdeviceDao.getDao().queryBuilder().where().eq("special_flag", SPECIAL_FLAG_BLACK_NAME).and()
                    .like("mac", "%"+mac+"%").query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryMacInBlacklist>>>exception:"+e.toString());
        }
        return specialDevices;
    }


////////////////////white name list and black name list  db operation start/////////////



//////////////////////Station db operation start////////////////////

    public List<Station> getAllStationFromDb()
    {
        StationDao stationDao = new StationDao(context);
        List<Station> stations = null;
        try{
            stations = stationDao.getDao().queryForAll();

            String temp = "";
            for(Station d:stations)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getAllStationFromDb>>>db:stations:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllStationFromDb>>>exception:"+e.toString());
        }

        return stations;
    }

    public  void addStationListToDB(final List<Station> stations)
    {
        Log.i(DtConstant.TAG, "addStationListToDB>>>");
        if(stations!=null)
        {
            StationDao stationDao = new StationDao(context);
            if(stations.size()>0)
            {
                for(Station d:stations)
                {
                    stationDao.add(d);
                }
            }
        }
    }


    public  void updateStation(Station station)
    {
        StationDao stationDao = new StationDao(context);
        try{
            stationDao.getDao().update(station);
            Log.i(DtConstant.TAG, "updateStation>>>");
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "updateStation>>>exception:"+e.toString());
        }
    }


    public  void addStationToDB(Station station)
    {
        Log.i(DtConstant.TAG, "addStationToDB>>>");
        if(station!=null)
        {
            StationDao stationDao = new StationDao(context);
            stationDao.add(station);
        }
    }

    public  List<Station> queryStationByMac(String mac)
    {
        Log.i(DtConstant.TAG, "queryStationByMac>>>");
        List<Station> stations = null;
        if(mac!=null)
        {
            StationDao stationDao = new StationDao(context);
            try {
                stations = stationDao.getDao().queryBuilder().where().eq("mac", mac).query();

            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(DtConstant.TAG, "queryStationByMac>>>excepton:"+e.toString());
            }
        }
        return stations;
    }


    public Station queryStationById(long id)
    {
        Log.i(DtConstant.TAG, "queryStationById>>>");
        Station station = null;

        StationDao stationDao = new StationDao(context);
        try {
            station = stationDao.getDao().queryBuilder().where().eq("id", id).queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "queryStationById>>>excepton:"+e.toString());
        }
        return station;
    }


    //获取AP下的station
    public List<Station> getStationsOfAp(long deviceId)
    {
        StationDao stationDao = new StationDao(context);
        List<Station> stations = new ArrayList<Station>();
        try{
            stations = stationDao.getDao().queryBuilder().where().eq("deviceid", deviceId).query();

            String temp = "";
            if(stations!=null)
            {
                for(Station d:stations)
                {
                    temp+=d.toString()+"\n";

                }
            }
            Log.i(DtConstant.TAG, "getAllStationOfAp>>>db:stations:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllStationOfAp>>>exception:"+e.toString());
        }

        return stations;
    }

////////////////////Station db operation end////////////////////

////////////////////SSID db operation start////////////////////



    //获取ssid 密码库
    public List<SSID> getAllSsidFromDb()
    {
        SsidDao ssidDao = new SsidDao(context);
        List<SSID> ssids = new ArrayList<SSID>();
        try{
            ssids = ssidDao.getDao().queryForAll();

            String temp = "";
            for(SSID d:ssids)
            {
                temp+=d.toString()+"\n";

            }
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>db:ssids:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>exception:"+e.toString());
        }

        return ssids;
    }


    //查找ssid 密码库 通过mac
    public List<SSID> getSsidFromDbByMac(String mac)
    {

        SsidDao ssidDao = new SsidDao(context);
        List<SSID> ssids = new ArrayList<SSID>();
        try{
            ssids = ssidDao.getDao().queryBuilder().where().eq("mac", mac).query();
            String temp = "";
            for(SSID d:ssids)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>db:ssids:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>exception:"+e.toString());
        }

        return ssids;
    }


    //查找ssid 密码库 通过ssid
    public List<SSID> getSsidFromDbBySsid(String ssid)
    {

        SsidDao ssidDao = new SsidDao(context);
        List<SSID> ssids = new ArrayList<SSID>();
        try{
            ssids = ssidDao.getDao().queryBuilder().where().eq("ssid", ssid).query();
            String temp = "";
            for(SSID d:ssids)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>db:ssids:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllSsidFromDb>>>exception:"+e.toString());
        }
        return ssids;
    }


    //查找ssid 密码库 通过ssid和password
    public List<SSID> getSsidFromDbBySsidAndPassword(String ssid , String password)
    {
        if(ssid==null || password==null)
            return null;
        SsidDao ssidDao = new SsidDao(context);
        List<SSID> ssids = new ArrayList<SSID>();
        try{
            ssids = ssidDao.getDao().queryBuilder().where().eq("ssid", ssid).and().eq("password", password).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getSsidFromDbBySsidAndPassword>>>exception:"+e.toString());
        }
        return ssids;
    }


    public void addSsidToDb(SSID ssid)
    {
        if(ssid!=null)
        {
            ssid.setTime(TimeUtils.getCurrentTimeInLong());
            SsidDao ssidDao = new SsidDao(context);
            ssidDao.add(ssid);
            Log.i(DtConstant.TAG, "addSsidToDb>>>");
        }
    }

    public void removeSsidFromDb(long id)
    {
        SsidDao ssidDao = new SsidDao(context);
        ssidDao.delete(id);
        Log.i(DtConstant.TAG, "removeSsidListFromDb>>>");
    }

    public void removeSsidFromDb(SSID ssid) {
        SsidDao ssidDao = new SsidDao(context);
        ssidDao.delete(ssid);
        Log.i(DtConstant.TAG, "removeSsidListFromDb>>>");
    }

    public void addSsidListToDb(List<SSID> ssidList)
    {
        if(ssidList!=null)
        {
            if(ssidList.size()>0)
            {
                SsidDao ssidDao = new SsidDao(context);
                for(SSID ssid:ssidList)
                {
                    ssid.setTime(TimeUtils.getCurrentTimeInLong());
                    ssidDao.add(ssid);
                }
                Log.i(DtConstant.TAG, "addSsidListToDb>>>");
            }
        }
    }

    public void removeSsidListFromDb(List<SSID> ssidList)
    {
        if(ssidList!=null)
        {
            if(ssidList.size()>0)
            {
                SsidDao ssidDao = new SsidDao(context);
                for(SSID ssid:ssidList)
                {
                    ssidDao.delete((int)ssid.getId());
                }
                Log.i(DtConstant.TAG, "removeSsidListFromDb>>>");
            }
        }
    }



////////////////////SSID db operation end////////////////////


////////////////////TopAP db operation start////////////////////

    //获取top ap list
    public List<TopAP> getAllTopApFromDb()
    {
        TopAPDao dao = new TopAPDao(context);
        List<TopAP> topAPs = new ArrayList<TopAP>();
        try{
            topAPs = dao.getDao().queryForAll();

            String temp = "";
            for(TopAP d:topAPs)
            {
                temp+=d.toString()+"\n";

            }
            Log.i(DtConstant.TAG, "getAllTopApFromDb>>>db:topAPs:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllTopApFromDb>>>exception:"+e.toString());
        }
        return topAPs;
    }


    //查找top ap 通过mac
    public List<TopAP> getTopApFromDbByMac(String mac)
    {

        TopAPDao dao = new TopAPDao(context);
        List<TopAP> topAPs = new ArrayList<TopAP>();
        try{
            topAPs = dao.getDao().queryBuilder().where().eq("mac", mac).query();
            String temp = "";
            for(TopAP d:topAPs)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getTopAPFromDbByMac>>>db:topAPs:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getTopAPFromDbByMac>>>exception:"+e.toString());
        }

        return topAPs;
    }


    //查找top ap 通过ssid
    public List<TopAP> getTopApFromDbBySsid(String ssid)
    {
        TopAPDao dao = new TopAPDao(context);
        List<TopAP> topAPs = new ArrayList<TopAP>();
        try{
            topAPs = dao.getDao().queryBuilder().where().eq("ssid", ssid).query();
            String temp = "";
            for(TopAP d:topAPs)
            {
                temp+=d.toString()+"\n";
            }
            Log.i(DtConstant.TAG, "getTopApFromDbBySsid>>>db:topAPs:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getTopApFromDbBySsid>>>exception:"+e.toString());
        }
        return topAPs;
    }

    //查找top ap 通过ssid和password
    public List<TopAP> getTopApFromDbBySsidAndPassword(String ssid, String password)
    {
        TopAPDao dao = new TopAPDao(context);
        List<TopAP> topAPs = new ArrayList<TopAP>();
        try{
            topAPs = dao.getDao().queryBuilder().where().eq("ssid", ssid).and().eq("password", password).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getTopApFromDbBySsidAndPassword>>>exception:"+e.toString());
        }
        return topAPs;
    }

    public void addTopApToDb(TopAP topAP)
    {
        if(topAP!=null)
        {
            topAP.setTime(TimeUtils.getCurrentTimeInLong());
            TopAPDao dao = new TopAPDao(context);
            dao.add(topAP);
            Log.i(DtConstant.TAG, "addTopApToDb>>>");
        }
    }

    public void removeTopApFromDb(long id)
    {
        TopAPDao dao = new TopAPDao(context);
        dao.delete(id);
        Log.i(DtConstant.TAG, "removeTopApFromDb>>>");
    }

    public void removeTopApFromDb(TopAP topAP) {
        TopAPDao dao = new TopAPDao(context);
        dao.delete(topAP);
        Log.i(DtConstant.TAG, "removeTopApFromDb>>>");
    }

    public void addTopApListToDb(List<TopAP> topAPList)
    {
        if(topAPList!=null)
        {
            if(topAPList.size()>0)
            {
                TopAPDao dao = new TopAPDao(context);
                for(TopAP topAP:topAPList)
                {
                    topAP.setTime(TimeUtils.getCurrentTimeInLong());
                    dao.add(topAP);
                }
                Log.i(DtConstant.TAG, "addTopApListToDb>>>");
            }
        }
    }

    public void removeTopApListFromDb(List<TopAP> topAPList)
    {
        if(topAPList!=null)
        {
            if(topAPList.size()>0)
            {
                TopAPDao dao = new TopAPDao(context);
                for(TopAP topAP:topAPList)
                {
                    dao.delete((int)topAP.getId());
                }
                Log.i(DtConstant.TAG, "removeTopApListFromDb>>>");
            }
        }
    }


////////////////////TopAP db operation end////////////////////


////////////////////////////添加临时数据 start/////////////////////////////////////////

    synchronized void addTempDeviceToDb()
    {
        DeviceDao deviceDao = new DeviceDao(context);

        for(int i=0;i<30;i++)
        {
            Device loc = new Device();
            loc.setTime(System.currentTimeMillis());
            loc.setMac("aabbccddee"+(10+i));
            loc.setRssi(RandomUtils.getRandom(-100,-70));
            loc.setCompass(RandomUtils.getRandom(200));
            loc.setAngle(RandomUtils.getRandom(360));
            loc.setState(0);
            loc.setIpInfoName("DT");
            loc.setIpaddr("192.168.2.1");
            loc.setChannel(RandomUtils.getRandom(1,14));
            loc.setTaskId(10000);
            if(i%2==1)
            {
                loc.setRole("ap");
                loc.setInblacklist(true);
                loc.setInwhitelist(true);
                loc.setVendoren("TPLink");
                loc.setVendorcn("普联科技");
                loc.setSsid("DT"+i);
            }else{
                loc.setSsid("DT1");
                loc.setRole("station");
                loc.setInblacklist(false);
                loc.setInwhitelist(false);
                int index = RandomUtils.getRandom(0,4);
                switch (index)
                {
                    case 0:
                        loc.setSsid("DT1");
                        loc.setVendorcn("小米");
                        loc.setVendoren("XIAOMI");
                        loc.setSsidMac("aabbccddee11");
                        break;
                    case 1:
                        loc.setSsid("DT3");
                        loc.setVendorcn("华为");
                        loc.setVendoren("Huawei");
                        loc.setSsidMac("aabbccddee13");
                        break;
                    case 2:
                        loc.setSsid("DT5");
                        loc.setVendorcn("Apple");
                        loc.setVendoren("苹果");
                        loc.setSsidMac("aabbccddee15");
                        break;
                    case 3:
                        loc.setSsid("DT7");
                        loc.setVendorcn("OPPO");
                        loc.setVendoren("OPPO");
                        loc.setSsidMac("aabbccddee17");
                        break;
                }

            }
            loc.setVender(loc.getVendorcn());
            if(i%7==4)
            {
                loc.setInwhitelist(true);

            }else{
                loc.setInwhitelist(false);
            }

            if(i%5==4)
            {
                loc.setInblacklist(true);

            }else{
                loc.setInblacklist(false);
            }

            //loc.setAddress(RandomUtils.getRandomLetters(20));
            loc.setPassword(RandomUtils.getRandomLetters(6));
            loc.setEncryption("WPA");
            loc.setDistance(APPUtils.rssiToDistance(loc.getRssi(),loc.getRole()));
            deviceDao.add(loc);
        }
    }

    void addTempSSIDToDb()
    {
        SsidDao ssidDao = new SsidDao(context);
        for(int i=0;i<10;i++)
        {
            SSID ssid = new SSID();
            ssid.setMac("e3a6d6c88990f"+i);
            ssid.setDescription("shenzhen "+1);
            ssid.setEncryption("WPA");
            ssid.setPassword("45678"+ i);
            ssid.setTime(TimeUtils.getCurrentTimeInLong());
            ssid.setType("airport");
            ssid.setSsid("dttest"+i);
            ssidDao.add(ssid);
        }
    }


    void addTempURLToDb()
    {
        UrlDao urlDao = new UrlDao(context);
        for(int i=0;i<5;i++)
        {
            URL url = new URL();
            url.setMac("e3a6d6c88990f"+i);
            url.setUrl("www.baidu.com?id="+i);
            url.setTime(1503022949);
            url.setTag("search");
            url.setDeviceid(RandomUtils.getRandom(49));
            urlDao.add(url);
        }
    }


    void addTempDndMacToDb()
    {
        DndMacDao dao = new DndMacDao(context);
        for(int i=0;i<10;i++)
        {
            DndMac ssid = new DndMac();
            ssid.setMac("e3a6d6c88990f"+i);
            ssid.setDescription("shenzhen "+1);
            dao.add(ssid);
        }
    }


    void addTempIdentityToDb()
    {
        IdentityDao identityDao = new IdentityDao(context);
        for(int i=0;i<10;i++)
        {
            Identity iden = new Identity();
            iden.setMac("e3a6d6c88990f"+i);
            iden.setValue("7925556"+i);
            iden.setTime(TimeUtils.getCurrentTimeInLong());
            iden.setTag("qq");
            iden.setDeviceid(RandomUtils.getRandom(49));
            identityDao.add(iden);
        }
    }


    void addTempLocationToDb()
    {
        LocationDao locationDao = new LocationDao(context);
        for(int i=0;i<200;i++)
        {
            Location loc = new Location();
            loc.setTime(TimeUtils.getCurrentTimeInLong());
            loc.setLatitude(133.55);
            loc.setLongitude(255.88);
            loc.setDeviceid(RandomUtils.getRandom(49));
            locationDao.add(loc);
        }
    }


    void addTempStationToDb()
    {
        StationDao stationDao = new StationDao(context);
        for(int i=0;i<100;i++)
        {
            Station loc = new Station();
            loc.setDeviceid(RandomUtils.getRandom(10));
            loc.setTime("2017-07-22");
            loc.setMac("8aa95b6e6a9a6"+i);
            loc.setDistance(RandomUtils.getRandom(50));
            loc.setCompassValue(RandomUtils.getRandom(200));
            loc.setAngle(RandomUtils.getRandom(360));
            loc.setState(0);
            loc.setVender(RandomUtils.getRandomLetters(8));
            stationDao.add(loc);
        }
    }


    void addTempSpecialToDb()
    {
        SpecialDeviceDao stationDao = new SpecialDeviceDao(context);
        for(int i=0;i<10;i++)
        {
            SpecialDevice loc = new SpecialDevice();


            if(i%2==0)
            {
                loc.setMac("abcd123456d"+i);
                loc.setRole("ap");
                loc.setSpecialFlag(0);
            }else{
                loc.setMac("efab123456d"+i);
                loc.setRole("station");
                loc.setSpecialFlag(1);
            }
            stationDao.add(loc);
        }
    }

    public void addTempTaskToDb()
    {
        List<Task> taskList = new ArrayList<Task>();
        for(int i=0;i<5;i++)
        {
            Task loc = new Task();
            loc.setAddress("address"+RandomUtils.getRandom(50));
            loc.setStartTime(RandomUtils.getRandom(10000,20000));
            loc.setStopTime(RandomUtils.getRandom(30000,40000));
            taskList.add(loc);
        }
        addTaskListToDb(taskList);
    }

////////////////////////////添加临时数据 end/////////////////////////////////////////

/////////////////////////导出数据库到文件 start////////////////////////////
    public static final String FILE_EXPORT_DIR = "/deter";
    public static final String FILE_MAC = "/mac_data.xls";
    public static final String FILE_DND = "/dnd_data.xls";
    public static final String FILE_IDENTITY = "/identity_data.xls";
    public static final String FILE_WHITE_BLACK = "/white_black_data.xls";
    public static final String FILE_SSID = "/ssid_data.xls";
    public static final String FILE_TASK = "/task_data.xls";
    public static final String FILE_URL = "/url_data.xls";
    public static final String FILE_TOPAP = "/topap_data.xls";

    public void exportMacDataToFile(List<Device> deviceList) {
        Log.i(DtConstant.TAG, ">>>exportMacDataToFile>>>start");
        String[] title = new String[]{"id", "ip_name", "ip_addr", "mac", "role",
                "rssi", "task_id", "deauth_state","mitm_state","conect_state","name", "ssid", "password",
                "encryption", "state", "time", "distance", "address",
                "vender", "vendor_cn", "vendor_en", "angle", "compass", "channel", "time_seen"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_device_list);
        ExcelUtils.initExcel(file.toString() + FILE_MAC, workbookName, title);

        if(deviceList!=null)
        {
            if(deviceList.size()>0)
            {
                Log.i(DtConstant.TAG, ">>>exportMacDataToFile>>>deviceList size:"+deviceList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (Device d:deviceList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(d.getIpInfoName());
                    beanList.add(d.getIpaddr());
                    beanList.add(d.getMac());
                    beanList.add(d.getRole());
                    beanList.add(String.valueOf(d.getRssi()));
                    beanList.add(String.valueOf(d.getTaskId()));
                    beanList.add(String.valueOf(d.getDeauthState()));
                    beanList.add(String.valueOf(d.getMitmState()));
                    beanList.add(d.getConectState());
                    beanList.add(d.getName());
                    beanList.add(d.getSsid());
                    beanList.add(d.getPassword());
                    beanList.add(d.getEncryption());
                    beanList.add(String.valueOf(d.getState()));
                    beanList.add(TimeUtils.getTime(d.getTime()));
                    beanList.add(""+d.getDistance());
                    beanList.add(d.getAddress());
                    beanList.add(d.getVender());
                    beanList.add(d.getVendorcn());
                    beanList.add(d.getVendoren());
                    beanList.add(""+d.getAngle());
                    beanList.add(""+d.getCompass());
                    beanList.add(""+d.getChannel());
                    if (d.getTime_seen() > 0) {
                        beanList.add(TimeUtils.getTime(d.getTime_seen() * 1000L));
                    } else {
                        beanList.add("" + d.getTime_seen());
                    }
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_MAC, context);
                Log.i(DtConstant.TAG, ">>>exportMacDataToFile>>>write over");
            }else{
                Log.i(DtConstant.TAG, ">>>exportMacDataToFile>>>deviceList size:0");
            }
        }else{
            Log.i(DtConstant.TAG, ">>>exportMacDataToFile>>>deviceList:null");
        }

    }


    public void exportDndDataToFile(List<DndMac> dndMacList) {
        Log.i(DtConstant.TAG, ">>>exportDndDataToFile>>>start");
        String[] title = new String[]{"id", "mac", "vender"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_dnd_list);
        ExcelUtils.initExcel(file.toString() + FILE_DND, workbookName, title);

        if(dndMacList!=null)
        {
            if(dndMacList.size()>0)
            {
                Log.i(DtConstant.TAG, ">>>exportDndDataToFile>>>DndMacList size:"+dndMacList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (DndMac d:dndMacList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(d.getMac());
                    beanList.add(d.getVender());
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_DND, context);
                Log.i(DtConstant.TAG, ">>>exportDndDataToFile>>>write over");
            }else{
                Log.i(DtConstant.TAG, ">>>exportDndDataToFile>>>DndMacList size:0");
            }
        }else{
            Log.i(DtConstant.TAG, ">>>exportDndDataToFile>>>DndMacList:null");
        }

    }



    public void exportIdentityDataToFile(List<Identity> identityList) {
        Log.i(DtConstant.TAG, ">>>exportIdentityDataToFile>>>start");
        String[] title = new String[]{"id", "deviceid", "mac", "tag", "time", "value"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_identity_list);
        ExcelUtils.initExcel(file.toString() + FILE_IDENTITY, workbookName, title);

        if(identityList!=null)
        {
            if(identityList.size()>0)
            {
                Log.i(DtConstant.TAG, ">>>exportIdentityDataToFile>>>identityList size:"+identityList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (Identity d:identityList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(String.valueOf(d.getDeviceid()));
                    beanList.add(d.getMac());
                    beanList.add(d.getTag());
                    if (d.getTime() > 0) {
                        beanList.add(TimeUtils.getTime(d.getTime()));
                    } else {
                        beanList.add("" + d.getTime());
                    }
                    beanList.add(d.getValue());
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_IDENTITY, context);
                Log.i(DtConstant.TAG, ">>>exportIdentityDataToFile>>>write over");
            }else{
                Log.i(DtConstant.TAG, ">>>exportIdentityDataToFile>>>identityList size:0");
            }
        }else{
            Log.i(DtConstant.TAG, ">>>exportIdentityDataToFile>>>identityList:null");
        }

    }

    public void exportWhiteBlackDataToFile(List<SpecialDevice> deviceList) {
        Log.i(DtConstant.TAG, ">>>exportWhiteBlackDataToFile>>>start");
        String[] title = new String[]{"id", "ssid", "mac", "password", "role",
                "special_flag", "encryption", "description",
                "time_inserted", "venderen", "vendercn"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_white_black_list);
        ExcelUtils.initExcel(file.toString() + FILE_WHITE_BLACK, workbookName, title);

        if (deviceList != null) {
            if (deviceList.size() > 0) {
                Log.i(DtConstant.TAG, ">>>exportWhiteBlackDataToFile>>>deviceList size:" + deviceList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (SpecialDevice d : deviceList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(d.getSsid());
                    beanList.add(d.getMac());
                    beanList.add(d.getPassword());
                    beanList.add(d.getRole());
                    beanList.add(String.valueOf(d.getSpecialFlag()));
                    beanList.add(d.getEncryption());
                    beanList.add(d.getDescription());
                    if (d.getTimeInserted() > 0) {
                        beanList.add(TimeUtils.getTime(d.getTimeInserted()));
                    } else {
                        beanList.add("" + d.getTimeInserted());
                    }
                    beanList.add(d.getVenderen());
                    beanList.add(d.getVendercn());
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_WHITE_BLACK, context);
                Log.i(DtConstant.TAG, ">>>exportWhiteBlackDataToFile>>>write over");
            } else {
                Log.i(DtConstant.TAG, ">>>exportWhiteBlackDataToFile>>>deviceList size:0");
            }
        } else {
            Log.i(DtConstant.TAG, ">>>exportWhiteBlackDataToFile>>>deviceList:null");
        }

    }


    public void exportSSIDDataToFile(List<SSID> ssidList) {
        Log.i(DtConstant.TAG, ">>>exportSSIDDataToFile>>>start");
        String[] title = new String[]{"id", "ssid", "password", "type", "encryption", "mac", "time"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_white_ssid_list);
        ExcelUtils.initExcel(file.toString() + FILE_SSID, workbookName, title);

        if (ssidList != null) {
            if (ssidList.size() > 0) {
                Log.i(DtConstant.TAG, ">>>exportSSIDDataToFile>>>ssidList size:" + ssidList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (SSID d : ssidList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(d.getSsid());
                    beanList.add(d.getPassword());
                    beanList.add(d.getType());
                    beanList.add(d.getEncryption());
                    beanList.add(d.getMac());
                    if (d.getTime() > 0) {
                        beanList.add(TimeUtils.getTime(d.getTime()));
                    } else {
                        beanList.add("" + d.getTime());
                    }
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_SSID, context);
                Log.i(DtConstant.TAG, ">>>exportSSIDDataToFile>>>write over");
            } else {
                Log.i(DtConstant.TAG, ">>>exportSSIDDataToFile>>>ssidList size:0");
            }
        } else {
            Log.i(DtConstant.TAG, ">>>exportSSIDDataToFile>>>ssidList:null");
        }

    }

    public void exportTaskDataToFile(List<Task> taskList) {
        Log.i(DtConstant.TAG, ">>>exportTaskDataToFile>>>start");
        String[] title = new String[]{"id", "latitude", "longitude", "start_time", "stop_time", "description", "address"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_white_task_list);
        ExcelUtils.initExcel(file.toString() + FILE_TASK, workbookName, title);

        if (taskList != null) {
            if (taskList.size() > 0) {
                Log.i(DtConstant.TAG, ">>>exportTaskDataToFile>>>urlList size:" + taskList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (Task d : taskList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(String.valueOf(d.getLatitude()));
                    beanList.add(String.valueOf(d.getLongitude()));
                    if (d.getStartTime() > 0) {
                        beanList.add(TimeUtils.getTime(d.getStartTime()));
                    } else {
                        beanList.add("" + d.getStartTime());
                    }

                    if (d.getStopTime() > 0) {
                        beanList.add(TimeUtils.getTime(d.getStopTime()));
                    } else {
                        beanList.add("" + d.getStopTime());
                    }
                    beanList.add(String.valueOf(d.getDescription()));
                    beanList.add(d.getAddress());
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_TASK, context);
                Log.i(DtConstant.TAG, ">>>exportTaskDataToFile>>>write over");
            } else {
                Log.i(DtConstant.TAG, ">>>exportTaskDataToFile>>>taskList size:0");
            }
        } else {
            Log.i(DtConstant.TAG, ">>>exportTaskDataToFile>>>taskList:null");
        }
    }


    public void exportUrlDataToFile(List<URL> urlList) {
        Log.i(DtConstant.TAG, ">>>exportUrlDataToFile>>>start");
        String[] title = new String[]{"id", "deviceid", "mac", "tag", "time", "url"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = context.getResources().getString(R.string.workbook_name_white_url_list);
        ExcelUtils.initExcel(file.toString() + FILE_URL, workbookName, title);

        if (urlList != null) {
            if (urlList.size() > 0) {
                Log.i(DtConstant.TAG, ">>>exportUrlDataToFile>>>urlList size:" + urlList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (URL d : urlList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(d.getId()));
                    beanList.add(String.valueOf(d.getDeviceid()));
                    beanList.add(d.getMac());
                    beanList.add(d.getTag());
                    if (d.getTime() > 0) {
                        beanList.add(TimeUtils.getTime(d.getTime()));
                    } else {
                        beanList.add("" + d.getTime());
                    }
                    beanList.add(d.getUrl());
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_URL, context);
                Log.i(DtConstant.TAG, ">>>exportUrlDataToFile>>>write over");
            } else {
                Log.i(DtConstant.TAG, ">>>exportUrlDataToFile>>>urlList size:0");
            }
        } else {
            Log.i(DtConstant.TAG, ">>>exportUrlDataToFile>>>urlList:null");
        }

    }


    public void exportTopApDataToFile(List<TopAP> topAPList) {
        Log.i(DtConstant.TAG, ">>>exportTopApToFile>>>start");
        String[] title = new String[]{"id", "ssid", "password", "type", "encryption", "mac", "description", "areadescription", "time"};

        File file = new File(FileUtils.getSDPath() + FILE_EXPORT_DIR);
        FileUtils.makeDir(file);
        String workbookName = "";//context.getResources().getString(R.string.workbook_name_white_topaplist_list);
        ExcelUtils.initExcel(file.toString() + FILE_TOPAP, workbookName, title);

        if (topAPList != null) {
            if (topAPList.size() > 0) {
                Log.i(DtConstant.TAG, ">>>exportTopApToFile>>>topAPList size:" + topAPList.size());
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for (TopAP topAP : topAPList) {
                    ArrayList<String> beanList = new ArrayList<String>();
                    beanList.add(String.valueOf(topAP.getId()));
                    beanList.add(topAP.getSsid());
                    beanList.add(topAP.getPassword());
                    beanList.add(topAP.getType());
                    beanList.add(topAP.getEncryption());
                    beanList.add(topAP.getMac());
                    beanList.add(topAP.getDescription());
                    beanList.add(topAP.getAreadescription());
                    if (topAP.getTime() > 0) {
                        beanList.add(TimeUtils.getTime(topAP.getTime()));
                    } else {
                        beanList.add("" + topAP.getTime());
                    }
                    content.add(beanList);
                }
                ExcelUtils.writeObjListToExcel(content, FileUtils.getSDPath() + FILE_EXPORT_DIR + FILE_TOPAP, context);
                Log.i(DtConstant.TAG, ">>>exportTopApToFile>>>write over");
            } else {
                Log.i(DtConstant.TAG, ">>>exportTopApToFile>>>topAPList size:0");
            }
        } else {
            Log.i(DtConstant.TAG, ">>>exportTopApToFile>>>topAPList:null");
        }

    }

/////////////////////////导出数据库到文件 end////////////////////////////


////////////////////dnd mac list db operation start/////////////

    public List<DndMac> getDndMacListFromDb()
    {
        final DndMacDao dndMacDao = new DndMacDao(context);
        List<DndMac> dndMacList = null;
        try{
            dndMacList = dndMacDao.getDao().queryForAll();
            String temp = "";
            if(dndMacList!=null)
            {
                for(DndMac d:dndMacList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getDndMacListFromDb>>>db:white name devices:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getDndMacListFromDb>>>exception:"+e.toString());
        }
        return dndMacList;
    }


    public void addDndMacListToDb(final List<DndMac> dndMacList)
    {
        if(dndMacList!=null)
        {
            for(DndMac d:dndMacList)
            {
                addDndMacToDb(d);
            }
        }
    }

    public void addDndMacToDb(final DndMac dndMac)
    {
        if(dndMac==null)
            return;
        DndMacDao dao = new DndMacDao(context);
        dndMac.setMac(dndMac.getMac().toLowerCase());
        dao.add(dndMac);
    }



    public void removeDndMacFromDb(String mac)
    {
        Log.i(DtConstant.TAG, "removeDndMacFromDb>>");
        if(mac==null)
            return;
        mac = mac.toLowerCase();
        DndMacDao dao = new DndMacDao(context);
        DeleteBuilder<DndMac, Integer> deleteBuilder = dao.getDao().deleteBuilder();
        try {
            deleteBuilder.where().eq("mac", mac);
            deleteBuilder.delete();
            Log.i(DtConstant.TAG, "removeDndMacFromDb>>"+mac+" success");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "removeDndMacFromDb>>"+mac+" fail"+e.toString());
        }
    }


    public List<DndMac> queryMacInDndMaclist(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        final DndMacDao dao = new DndMacDao(context);
        List<DndMac> dndMacs = null;
        try{
            dndMacs = dao.getDao().queryBuilder().where().like("mac", "%"+mac+"%").query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryMacInDndMaclist>>>exception:"+e.toString());
        }
        return dndMacs;
    }


////////////////////dnd mac list db operation end/////////////

//////////////////// mac vendor list db operation start/////////////

    public List<MacVendor> getMacVendorListFromDb()
    {
        final MacVendorDao macVendorDao = new MacVendorDao(context);
        List<MacVendor> macVendorList = null;
        try{
            macVendorList = macVendorDao.getDao().queryForAll();
            String temp = "";
            if(macVendorList!=null)
            {
                for(MacVendor d:macVendorList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getMacVendorListFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getMacVendorListFromDb>>>exception:"+e.toString());
        }
        return macVendorList;
    }


    public void addMacVendorListToDb(final List<MacVendor> macVendorList)
    {
        if(macVendorList!=null)
        {
            for(MacVendor d:macVendorList)
            {
                addMacVendorToDb(d);
            }
        }
    }

    public void addMacVendorToDb(final MacVendor macVendor)
    {
        if(macVendor!=null)
        {
            Log.i(DtConstant.TAG, "addMacVendorToDb>>"+macVendor.getMac());
            MacVendorDao dao = new MacVendorDao(context);
            dao.add(macVendor);
        }else{
            Log.i(DtConstant.TAG, "addMacVendorToDb>>null");
        }

    }



    public void removeMacVendorFromDb(String mac)
    {
        Log.i(DtConstant.TAG, "removeMacVendorFromDb>>");
        if(mac==null)
            return;
        MacVendorDao dao = new MacVendorDao(context);
        DeleteBuilder<MacVendor, Integer> deleteBuilder = dao.getDao().deleteBuilder();
        try {
            deleteBuilder.where().eq("mac", mac);
            deleteBuilder.delete();
            Log.i(DtConstant.TAG, "removeMacVendorFromDb>>"+mac+" success");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "removeMacVendorFromDb>>"+mac+" fail"+e.toString());
        }
    }

//返回值 String[0] :厂商中文简称， String[1] :厂商英文简称
    public String[] queryVendor(String mac)
    {
        if(mac==null)
            return null;
        String macHead = mac.substring(0, 6);
        Log.i(DtConstant.DT_TAG ,">>>queryVendorCn>>>macHead:"+macHead);
        macHead += ":";
        String macHeadNew = macHead.toUpperCase();
        final MacVendorDao dao = new MacVendorDao(context);
        List<MacVendor> macVendorList = null;
        try{
            macVendorList = dao.getDao().queryBuilder().where().eq("mac", macHeadNew).query();
        }catch (SQLException e){
            Log.i(DtConstant.DT_TAG, "queryVendorCn>>>exception:"+e.toString());
        }
        if(macVendorList!=null && macVendorList.size()>=1)
        {
            Log.i(DtConstant.DT_TAG ,">>>queryVendorCn>>>vendor:"+macVendorList.get(0).getVendercn());
            String [] res = new String[2];
            res[0] = macVendorList.get(0).getVendercn();
            res[1] = macVendorList.get(0).getVenderen();
            return res;
        }else{
            Log.i(DtConstant.DT_TAG ,">>>queryVendorCn>>>macVendorList:"+macVendorList);
            if(macVendorList!=null)
            {
                Log.i(DtConstant.DT_TAG ,">>>queryVendorCn>>>macVendorList size:"+macVendorList.size());
            }
        }
        return null;
    }


//////////////////// mac vendor list db operation end/////////////

//////////////////// identity list db operation start/////////////


    public List<String> getIdentityMacListFromDb()
    {
        List<String> macList = new ArrayList<String>();
        String sqlQueryMac = "SELECT DISTINCT mac FROM t_identity";
        Log.i("identity", "getIdentityMacListFromDb>>>sqlQueryMac:"+sqlQueryMac);
        Cursor cursor = rawQuery(sqlQueryMac);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String mac = cursor.getString(cursor.getColumnIndex("mac"));
                Log.i("identity", "getIdentityMacListFromDb mac:>>>" + mac);
                if (mac != null) {
                    macList.add(mac);
                }
            }
        }
        return macList;
    }

    public List<Identity> getIdentityListFromDb()
    {
        final IdentityDao dao = new IdentityDao(context);
        List<Identity> identityList = null;
        try{
            identityList = dao.getDao().queryForAll();
            String temp = "";
            if(identityList!=null)
            {
                for(Identity d:identityList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getIdentityListFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getIdentityListFromDb>>>exception:"+e.toString());
        }
        return identityList;
    }


    public void addIdentityListToDb(final List<Identity> identityList)
    {
        if(identityList!=null)
        {
            for(Identity d:identityList)
            {
                addIdentityToDb(d);
            }
        }
    }

    public void addIdentityToDb(final Identity identity)
    {
        if(identity!=null)
        {
            identity.setMac(identity.getMac().toLowerCase());
            Log.i(DtConstant.TAG, "addIdentityToDb>>"+identity.getMac());
            IdentityDao dao = new IdentityDao(context);
            List<Identity> identityList = null;
            try{
                identityList = dao.getDao().queryBuilder().where().eq("mac", identity.getMac())
                        .and().eq("tag", identity.getTag())
                        .and().eq("value", identity.getValue()).query();
            }catch (SQLException e){
                Log.i(DtConstant.TAG, "queryIdentity>>>exception:"+e.toString());
            }
            if(identityList!=null)
            {
                if(identityList.size()==0)
                {
                    dao.add(identity);
                }
            }else{
                dao.add(identity);
            }

        }else{
            Log.i(DtConstant.TAG, "addIdentityToDb>>null");
        }

    }

    public void removeIdentityFromDb(String mac)
    {
        Log.i(DtConstant.TAG, "removeIdentityFromDb>>");
        if(mac==null)
            return;
        mac = mac.toLowerCase();
        IdentityDao dao = new IdentityDao(context);
        DeleteBuilder<Identity, Integer> deleteBuilder = dao.getDao().deleteBuilder();
        try {
            deleteBuilder.where().eq("mac", mac);
            deleteBuilder.delete();
            Log.i(DtConstant.TAG, "removeIdentityFromDb>>"+mac+" success");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "removeIdentityFromDb>>"+mac+" fail"+e.toString());
        }
    }


    public List<Identity> queryIdentity(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        Log.i(DtConstant.TAG ,">>>queryIdentity>>>mac:"+mac);
        final IdentityDao dao = new IdentityDao(context);
        List<Identity> identityList = null;
        try{
            identityList = dao.getDao().queryBuilder().where().eq("mac", mac).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryIdentity>>>exception:"+e.toString());
        }

        return identityList;
    }

//////////////////// identity list db operation end/////////////


//////////////////// url list db operation start/////////////

    public List<String> getUrlMacListFromDb()
    {
        List<String> macList = new ArrayList<String>();
        String sqlQueryMac = "SELECT DISTINCT mac FROM t_url";
        Log.i("identity", "getUrlMacListFromDb>>>sqlQueryMac:"+sqlQueryMac);
        Cursor cursor = rawQuery(sqlQueryMac);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String mac = cursor.getString(cursor.getColumnIndex("mac"));
                Log.i("identity", "getUrlMacListFromDb mac:>>>" + mac);
                if (mac != null) {
                    macList.add(mac);
                }
            }
        }
        return macList;
    }


    public List<URL> getUrlListFromDb()
    {
        final UrlDao dao = new UrlDao(context);
        List<URL> urlList = null;
        try{
            urlList = dao.getDao().queryForAll();
            String temp = "";
            if(urlList!=null)
            {
                for(URL d:urlList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getUrlListFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getUrlListFromDb>>>exception:"+e.toString());
        }
        return urlList;
    }


    public void addUrlListToDb(final List<URL> urlList)
    {
        if(urlList!=null)
        {
            for(URL d:urlList)
            {
                addUrlToDb(d);
            }
        }
    }

    public void addUrlToDb(final URL url)
    {
        if(url!=null)
        {
            Log.i(DtConstant.TAG, "addUrlToDb>>"+url.getMac());
            UrlDao dao = new UrlDao(context);
            List<URL> urlList = null;
            try{
                urlList = dao.getDao().queryBuilder().where().eq("mac", url.getMac())
                        .and().eq("url", url.getUrl()).query();
            }catch (SQLException e){
                Log.i(DtConstant.TAG, "queryIdentity>>>exception:"+e.toString());
            }
            if(urlList!=null)
            {
                if(urlList.size()==0)
                {
                    url.setMac(url.getMac().toLowerCase());
                    dao.add(url);
                }
            }else{
                dao.add(url);
            }
        }else{
            Log.i(DtConstant.TAG, "addUrlToDb>>null");
        }

    }

    public void removeUrlFromDb(String mac)
    {
        Log.i(DtConstant.TAG, "removeUrlFromDb>>");
        if(mac==null)
            return;
        mac = mac.toLowerCase();
        UrlDao dao = new UrlDao(context);
        DeleteBuilder<URL, Integer> deleteBuilder = dao.getDao().deleteBuilder();
        try {
            deleteBuilder.where().eq("mac", mac);
            deleteBuilder.delete();
            Log.i(DtConstant.TAG, "removeUrlFromDb>>"+mac+" success");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG, "removeUrlFromDb>>"+mac+" fail"+e.toString());
        }
    }

    public List<URL> queryUrl(String mac)
    {
        if(mac==null)
            return null;
        mac = mac.toLowerCase();
        Log.i(DtConstant.TAG ,">>>queryUrl>>>mac:"+mac);
        final UrlDao dao = new UrlDao(context);
        List<URL> urlList = null;
        try{
            urlList = dao.getDao().queryBuilder().orderBy("time", true).where().eq("mac", mac).query();
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryUrl>>>exception:"+e.toString());
        }
        if(DtConstant.DEBUG_MODE)
        {
            if(urlList==null)
                urlList = new ArrayList<URL>() ;
        }
        if(DtConstant.DEBUG_MODE && urlList.size()==0)
        {
            urlList = new ArrayList<URL>() ;
            URL id1 = new URL();
            id1.setMac(mac);
            id1.setUrl("www.baidu.com");
            id1.setTag("url");
            id1.setTime(1502504598);
            urlList.add(id1);

            URL id2 = new URL();
            id2.setMac(mac);
            id2.setUrl("www.163.com");
            id2.setTag("url");
            id2.setTime(1502850145);
            urlList.add(id2);

            URL id3 = new URL();
            id3.setMac(mac);
            id3.setUrl("www.qq.com");
            id3.setTag("url");
            id3.setTime(1502850198);
            urlList.add(id3);

            URL id4 = new URL();
            id4.setMac(mac);
            id4.setUrl("www.sohu.com");
            id4.setTag("url");
            id4.setTime(1502936500);
            urlList.add(id4);

            URL id5 = new URL();
            id5.setMac(mac);
            id5.setUrl("www.jd.com");
            id5.setTag("url");
            id5.setTime(1502936598);
            urlList.add(id5);

            URL id6 = new URL();
            id6.setMac(mac);
            id6.setUrl("www.taobao.com");
            id6.setTag("url");
            id6.setTime(1503022949);
            urlList.add(id6);

            URL id7 = new URL();
            id7.setMac(mac);
            id7.setUrl("www.163.com");
            id7.setTag("url");
            id7.setTime(1503022998);
            urlList.add(id7);
            addUrlListToDb(urlList);
        }
        return urlList;
    }

//////////////////// url list db operation end/////////////

//////////////////// task list db operation start/////////////

    public List<Task> getTaskListFromDb()
    {
        final TaskDao taskDao = new TaskDao(context);
        List<Task> taskList = null;
        try{
            taskList = taskDao.getDao().queryForAll();
            String temp = "";
            if(taskList!=null)
            {
                for(Task d:taskList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getTaskListFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getTaskListFromDb>>>exception:"+e.toString());
        }
        return taskList;
    }


    public void addTaskListToDb(final List<Task> taskList)
    {
        if(taskList!=null)
        {
            for(Task d:taskList)
            {
                addTaskToDb(d);
            }
        }
    }

    public void addTaskToDb(final Task task)
    {
        if(task!=null)
        {
            Log.i(DtConstant.TAG, "addTaskToDb>>");
            TaskDao dao = new TaskDao(context);
            dao.add(task);
        }else{
            Log.i(DtConstant.TAG, "addTaskToDb>>null");
        }
    }


    public void updateTaskToDb(final Task task)
    {
        if(task!=null)
        {
            Log.i(DtConstant.TAG, "addTaskToDb>>");
            TaskDao dao = new TaskDao(context);
            dao.updata(task);
        }else{
            Log.i(DtConstant.TAG, "addTaskToDb>>null");
        }
    }


    public void removeTaskFromDb(Task task)
    {
        Log.i(DtConstant.TAG, "removeTaskFromDb>>");
        if(task==null)
            return;
        TaskDao dao = new TaskDao(context);
        dao.delete((int)task.getId());
    }


    public void removeTaskListFromDb(List<Task> taskList)
    {
        if(taskList==null)
        {
            return;
        }
        TaskDao dao = new TaskDao(context);

        for(Task t:taskList)
        {
            if(t!=null)
                dao.delete((int)t.getId());
        }
    }

//////////////////// task list db operation end/////////////

//////////////////// analysis db operation start/////////////

    public List<StrikeAnalysis> getAllStrikeAnalysisFromDb() {
            final StrikeAnalysisDao dao = new StrikeAnalysisDao(context);
            List<StrikeAnalysis> strikeAnalysises = null;
            try{
                strikeAnalysises = dao.getDao().queryForAll();
                String temp = "";
                if(strikeAnalysises!=null)
                {
                    for(StrikeAnalysis d:strikeAnalysises)
                    {
                        temp+=d.toString()+"\n";
                    }
                }
                Log.i(DtConstant.TAG, "getAllStrikeAnalysisFromDb>>>db:"+temp);
            }catch (SQLException e){
                Log.i(DtConstant.TAG, "getAllStrikeAnalysisFromDb>>>exception:"+e.toString());
            }
            return strikeAnalysises;
    }

    public int getStrikeAnalysisCountByName(String name) {
        if(name==null)
            return -1;
        final StrikeAnalysisDao dao = new StrikeAnalysisDao(context);
        List<StrikeAnalysis> strikeAnalysises = null;
        try{
            strikeAnalysises = dao.getDao().queryBuilder().where().eq("name", name).query();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getStrikeAnalysisCountByName>>>exception:"+e.toString());
        }
        int count = 0;
        if(strikeAnalysises!=null)
        {
            count = strikeAnalysises.size();
        }
        Log.i(DtConstant.TAG, "getStrikeAnalysisCountByName>>>count:"+count);
        return count;
    }


    public void addStrikeAnalysisToDb(StrikeAnalysis strikeAnalysis) {
        if(strikeAnalysis!=null)
        {
            final StrikeAnalysisDao dao = new StrikeAnalysisDao(context);
            dao.add(strikeAnalysis);
        }
    }

    public void updateStrikeAnalysisToDb(StrikeAnalysis strikeAnalysis) {
        if(strikeAnalysis!=null)
        {
            StrikeAnalysisDao dao = new StrikeAnalysisDao(context);
            dao.update(strikeAnalysis);
        }
    }

    public void deleteStrikeAnalysisFromDb(StrikeAnalysis strikeAnalysis) {
        if(strikeAnalysis!=null)
        {
            StrikeAnalysisDao dao = new StrikeAnalysisDao(context);
            dao.delete(strikeAnalysis);
        }
    }


    public List<AnalysisTask> getAnalysisTaskListFromDb(long analysisId) {
        final AnalysisTaskDao dao = new AnalysisTaskDao(context);
        List<AnalysisTask> analysisTasks = null;
        try{
            analysisTasks = dao.getDao().queryBuilder().where().eq("analysis_id",analysisId).query();
            String temp = "";
            if(analysisTasks!=null)
            {
                for(AnalysisTask d:analysisTasks)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getAllStrikeAnalysisFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllStrikeAnalysisFromDb>>>exception:"+e.toString());
        }
        return analysisTasks;
    }

    public List<AnalysisMac> getAllAnalysisMacFromDb() {
        final AnalysisMacDao dao = new AnalysisMacDao(context);
        List<AnalysisMac> analysisMacs = null;
        try{
            analysisMacs = dao.getDao().queryForAll();
            String temp = "";
            if(analysisMacs!=null)
            {
                for(AnalysisMac d:analysisMacs)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getAllAnalysisMacFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllAnalysisMacFromDb>>>exception:"+e.toString());
        }
        return analysisMacs;
    }

    public List<AnalysisTask> getAllAnalysisTaskFromDb() {
        final AnalysisTaskDao dao = new AnalysisTaskDao(context);
        List<AnalysisTask> analysisTasks = null;
        try{
            analysisTasks = dao.getDao().queryForAll();
            String temp = "";
            if(analysisTasks!=null)
            {
                for(AnalysisTask d:analysisTasks)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getAllAnalysisTaskFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllAnalysisTaskFromDb>>>exception:"+e.toString());
        }
        return analysisTasks;
    }


    public void addAnalysisTaskToDb(AnalysisTask analysisTask) {
        if(analysisTask!=null)
        {
            AnalysisTaskDao dao = new AnalysisTaskDao(context);
            dao.add(analysisTask);
        }
    }

    public void deleteAnalysisTaskFromDb(AnalysisTask analysisTask) {
        if(analysisTask!=null)
        {
            AnalysisTaskDao dao = new AnalysisTaskDao(context);
            dao.delete(analysisTask);
        }
    }

    public void deleteAnalysisTaskByAnalysisId(long analysisId) {

        AnalysisTaskDao dao = new AnalysisTaskDao(context);
        List<AnalysisTask> analysisTaskList = null;
        try {
            analysisTaskList = dao.getDao().queryBuilder().where().eq("analysis_id", analysisId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(analysisTaskList!=null){
            for(AnalysisTask at:analysisTaskList){
                dao.delete(at);
            }
        }
    }

    public void updateAnalysisTaskToDb(AnalysisTask analysisTask) {
        if(analysisTask!=null)
        {
            AnalysisTaskDao dao = new AnalysisTaskDao(context);
            dao.updata(analysisTask);
        }
    }

    public List<AnalysisMac> getAnalysisMacListFromDb(long analysisId) {
        final AnalysisMacDao dao = new AnalysisMacDao(context);
        List<AnalysisMac> analysisMacs = null;
        try{
            analysisMacs = dao.getDao().queryBuilder().where().eq("analysis_id",analysisId).query();
            String temp = "";
            if(analysisMacs!=null)
            {
                for(AnalysisMac d:analysisMacs)
                {
                    try {
                        d.setTaskIdList(new ArrayList<Integer>());
                        List<Integer> taskIds = d.getTaskIdList();
                        String strTaskIds = d.getTaskIds();
                        String [] arrayTaskIds = strTaskIds.split(",");
                        for(int i=0;i<arrayTaskIds.length;i++)
                        {
                            taskIds.add(Integer.parseInt(arrayTaskIds[i]));
                        }

                    }catch (Exception e){
                        Log.i(DtConstant.TAG, "getAnalysisMacListFromDb>>>parse task ids exception:"+e.toString());
                    }
                    temp+=d.toString()+"\n";
                }
            }
            Log.i(DtConstant.TAG, "getAnalysisMacListFromDb>>>db:"+temp);
        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAnalysisMacListFromDb>>>exception:"+e.toString());
        }
        return analysisMacs;
    }

    public void addAnalysisMacToDb(AnalysisMac analysisMac) {
        if(analysisMac!=null)
        {
            AnalysisMacDao dao = new AnalysisMacDao(context);
            dao.add(analysisMac);
        }
    }

    public void deleteAnalysisMacFromDb(AnalysisMac analysisMac) {
        if(analysisMac!=null)
        {
            AnalysisMacDao dao = new AnalysisMacDao(context);
            dao.delete(analysisMac);
        }
    }

    public void deleteAnalysisMacByAnalysisId(long analysisId) {

        AnalysisMacDao dao = new AnalysisMacDao(context);
        List<AnalysisMac> analysisMacList = null;
        try {
            analysisMacList = dao.getDao().queryBuilder().where().eq("analysis_id", analysisId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(analysisMacList!=null){
            for(AnalysisMac am:analysisMacList){
                dao.delete(am);
            }
        }
    }

    public List<Task> queryTaskInTime(long startTime, long endTime) {

        List<Task> taskList = null;
        TaskDao dao = new TaskDao(context);
        try {
            taskList = dao.getDao().queryBuilder().where().ge("start_time", startTime)
                    .and().le("start_time",endTime).query();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(DtConstant.TAG,"queryTaskInTime>>>exception:"+e.toString());
        }
        Log.i(DtConstant.TAG,"queryTaskInTime>>>taskList:"+taskList.size());
        return taskList;
    }


    public List<AnalysisMac> startStrikeAnalyse(StrikeAnalysis strikeAnalysis, List<Task> tasks, Handler handler) {

        if(tasks==null)
            return null;
        List<AnalysisTask> analysisTasks = new ArrayList<AnalysisTask>();
        for(Task task :tasks)
        {
            AnalysisTask at = new AnalysisTask();
            at.setAnalysisId(strikeAnalysis.getId());
            at.setTaskId(task.getId());
            at.setTaskName(task.getAddress());
            at.setTaskStartTime(task.getStartTime());
            at.setTaskStopTime(task.getStopTime());
            analysisTasks.add(at);
        }
        return startStrikeAnalyseExt(strikeAnalysis, analysisTasks, handler);
    }


    public List<AnalysisMac> startStrikeAnalyseExt(StrikeAnalysis strikeAnalysis, List<AnalysisTask> analysisTasks, Handler handler)
    {
        if(strikeAnalysis==null)
        {
            Log.i(DtConstant.TAG,"startStrikeAnalyse strikeAnalysis:null");
            return null;
        }
        if(analysisTasks==null)
        {
            Log.i(DtConstant.TAG,"startStrikeAnalyse tasks:null");
            return null;
        }
        if(analysisTasks.size()==0)
        {
            Log.i(DtConstant.TAG,"startStrikeAnalyse analysisTasks size:0");
            return null;
        }
        Log.i(DtConstant.TAG,"startStrikeAnalyse analysisTasks size:"+analysisTasks.size());
        addStrikeAnalysisToDb(strikeAnalysis);
        Log.i(DtConstant.TAG,"startStrikeAnalyse>>>addStrikeAnalysisToDb id:"+strikeAnalysis.getId());

        List<AnalysisMac> analysisMacs = new ArrayList<AnalysisMac>();

        for(AnalysisTask at :analysisTasks)
        {
            if(at!=null){
                at.setAnalysisId(strikeAnalysis.getId());
                addAnalysisTaskToDb(at);
            }
        }
        String sqlQueryMac = "SELECT DISTINCT mac FROM t_device WHERE ";
        for(int i=0;i<analysisTasks.size();i++)
        {
            sqlQueryMac+= "task_id=\"";
            sqlQueryMac+= analysisTasks.get(i).getTaskId()+"\"";
            if(i!=analysisTasks.size()-1)
            {
                sqlQueryMac+=" OR ";
            }
        }
        Log.i(DtConstant.TAG, "startStrikeAnalyse>>>sqlQueryMac:"+sqlQueryMac);
        Cursor cursor = rawQuery(sqlQueryMac);

        if(cursor!=null){

             DeviceDao deviceDao = new DeviceDao(context);
             List<Device> deviceList = null;
             while (cursor.moveToNext()) {
                 String mac = cursor.getString(cursor.getColumnIndex("mac"));
                 Log.i(DtConstant.TAG,"startStrikeAnalyse mac:>>>" + mac);
                 if(mac!=null)
                 {
                     AnalysisMac analysisMac = new AnalysisMac();
                     analysisMac.setAnalysisId(strikeAnalysis.getId());
                     analysisMac.setMac(mac);
                     analysisMac.setTimes(0);
                     analysisMac.setTaskIds("");
                     analysisMacs.add(analysisMac);
                 }
            }

            for(AnalysisTask task :analysisTasks)
            {
                for(AnalysisMac analysisMac: analysisMacs)
                {
                    String mac = analysisMac.getMac();
                    long taskId = task.getTaskId();
                    try {
                        deviceList = deviceDao.getDao().queryBuilder().where().eq("task_id", taskId).and().eq("mac",mac).query();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.i(DtConstant.TAG, "startStrikeAnalyse>>>exception:"+e.toString());
                    }
                    int count =0;
                    if(deviceList!=null)
                    {
                        Log.i(DtConstant.TAG, "startStrikeAnalyse>>>deviceList not null");
                        count = deviceList.size();
                    }
                    Log.i(DtConstant.TAG, "startStrikeAnalyse>>>"+analysisMac.getMac()+" count:"+count);
                    if(count>0)
                    {
                        analysisMac.setTimes(analysisMac.getTimes()+1);
                        Log.i(DtConstant.TAG, "startStrikeAnalyse>>>"+analysisMac.getMac()+" times:"+analysisMac.getTimes());
                        String ids = analysisMac.getTaskIds();
                        ids = ids + taskId +",";
                        analysisMac.setTaskIds(ids);
                    }
                }
                task.setStatus(MSG_TASK_ANALYSE_OK);
                updateAnalysisTaskToDb(task);
                if(handler!=null)
                {
                    Message message = new Message();
                    message.what = MSG_TASK_ANALYSE_OK;
                    message.arg1 = (int)task.getTaskId();
                    Log.i(DtConstant.TAG, "startStrikeAnalyse>>>sendMessage what:"+message.what+" arg1:"+message.arg1);
                    handler.sendMessage(message);
                }

            }

            for (Iterator it = analysisMacs.iterator(); it.hasNext(); ) {
                AnalysisMac analysisMac = (AnalysisMac) it.next();
                if (analysisMac.getTimes() > 1) {
                    addAnalysisMacToDb(analysisMac);
                    Log.i(DtConstant.TAG, "startStrikeAnalyse>>>" + analysisMac.getMac() + " times:" + analysisMac.getTimes());
                }else{
                    it.remove();
                }
            }

        }else{
            Log.i(DtConstant.TAG, "startStrikeAnalyse>>>query sqlQueryMac null");
        }
        Log.i(DtConstant.TAG, "startStrikeAnalyse>>>return analysisMacs size:"+analysisMacs.size());
        if(handler!=null)
        {
            Message message = new Message();
            message.what = MSG_STRIKE_ANALYSE_OVER;
            Log.i(DtConstant.TAG, "startStrikeAnalyse>>>sendMessage what:"+message.what);
            handler.sendMessage(message);
        }
        return analysisMacs;
    }


//////////////////// analysis db operation end/////////////

////////////////////follow analysis start//////////////////

    public List<MacData> startFollowAnalyse(long startTime, long endTime, String mac, Handler handler)
    {
        if(mac==null)
            return null;
        String destMac = mac.toLowerCase();
        List<Task> taskList = getFollowAnalyseTask(startTime, endTime, destMac);
        if(taskList!=null)
            Log.i("follow", "startFollowAnalyse>>>tasklist size:"+taskList.size());
        else
            Log.i("follow", "startFollowAnalyse>>>tasklist null");

        FollowAnalysis followAnalysis = new FollowAnalysis();
        followAnalysis.setMac(destMac);
        followAnalysis.setStartTime(startTime);
        followAnalysis.setStopTime(endTime);
        String taskIds = "";
        if(taskList!=null){
            for(Task task:taskList){
                taskIds += (""+task.getId()+",");
            }
        }

        List<String> macList = getFollowAnalyseMacs(taskList);
        if(macList!=null)
            Log.i("follow", "startFollowAnalyse>>>macList size:"+macList.size());
        else
            Log.i("follow", "startFollowAnalyse>>>macList null");
        List<MacData> macDataList = getFollowAnalyseData(mac, taskList, macList, handler);
        if(macDataList!=null)
            Log.i("follow", "startFollowAnalyse>>>macDataList size:"+macDataList.size());
        else
            Log.i("follow", "startFollowAnalyse>>>macDataList null");
        if(macDataList!=null)
        {
            if(macDataList.size()>0){
                addFollowAnalysisToDb(followAnalysis);
                for(MacData m:macDataList)
                {
                    Log.i("follow", "startFollowAnalyse>>>mac:"+m.mac+" total:"+m.totalTasks.size()
                            +" appear:"+m.appearTasks.size());
                    if(m!=null){
                        FollowAnalysisMac fmac = new FollowAnalysisMac();
                        fmac.setMac(m.mac);
                        fmac.setAnalysisId(followAnalysis.getId());
                        String strTaskIds = "";
                        for(Task task:m.totalTasks){
                            strTaskIds += (task.getId()+",");
                        }
                        fmac.setTaskIds(strTaskIds);
                        String strAppearTaskIds = "";
                        for(Task task:m.appearTasks){
                            strAppearTaskIds += (task.getId()+",");
                        }
                        fmac.setAppearTaskIds(strAppearTaskIds);
                        fmac.setAppearTimes(m.appearTasks.size());
                        fmac.setTotalTimes(m.totalTasks.size());
                        addFollowAnalysisMacToDb(fmac);
                    }
                }
                sendMessage(handler, MSG_FOLLOW_ANALYSE_SUCCESSS, (int)followAnalysis.getId());
            }
        }

        return macDataList;
    }


    public List<Task> getFollowAnalyseTask(long startTime, long endTime, String mac)
    {
        List<Task> taskList = null;
        TaskDao dao = new TaskDao(context);
        try{
            taskList = dao.getDao().queryBuilder().where().ge("start_time", startTime)
                    .and().le("start_time",endTime).query();
            String temp = "";
            if(taskList!=null)
            {
                for(Task d:taskList)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i("follow", "getFollowAnalyseTask>>>task:"+temp);
        }catch (SQLException e){
            Log.i("follow", "getFollowAnalyseTask>>>exception:"+e.toString());
        }

        if(taskList!=null)
        {
            List<Task> tasks = new ArrayList<Task>();
            DeviceDao deviceDao = new DeviceDao(context);
            for(Task t:taskList)
            {
                List<Device> deviceList = null;
                try {
                    deviceList = deviceDao.getDao().queryBuilder().where().eq("task_id", t.getId())
                            .and().eq("mac",mac).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("follow", "getFollowAnalyseTask>>> 2 exception:"+e.toString());
                }
                if(deviceList!=null){
                    if(deviceList.size()>0){
                        tasks.add(t);
                    }
                }
            }
            String temp = "";
            if(tasks!=null)
            {
                for(Task d:tasks)
                {
                    temp+=d.toString()+"\n";
                }
            }
            Log.i("follow", "getFollowAnalyseTask>>>real task:"+temp);
            return tasks;
        }
        return null;
    }


    public List<String> getFollowAnalyseMacs(List<Task> taskList) {

        if(taskList==null)
            return null;
        if(taskList.size()==0)
            return null;
        List<String> macList = new ArrayList<String>();
        String sqlQueryMac = "SELECT DISTINCT mac FROM t_device WHERE ";
        for(int i=0;i<taskList.size();i++)
        {
            sqlQueryMac+= "task_id=\"";
            sqlQueryMac+= taskList.get(i).getId()+"\"";
            if(i!=taskList.size()-1)
            {
                sqlQueryMac+=" OR ";
            }
        }
        Log.i("follow", "getFollowAnalyseMacs>>>sqlQueryMac:"+sqlQueryMac);
        Cursor cursor = rawQuery(sqlQueryMac);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String mac = cursor.getString(cursor.getColumnIndex("mac"));
                Log.i("follow", "getFollowAnalyseMacs mac:>>>" + mac);
                if (mac != null) {
                    macList.add(mac);
                }
            }
        }
        return macList;
    }

    public List<MacData> getFollowAnalyseData(String destMac, List<Task> taskList, List<String> macList,
                                              Handler handler) {

        if(macList==null) {
            sendMessage(handler, MSG_FOLLOW_ANALYSE_NO_MAC , -1);
            return null;
        }

        if(taskList==null) {
            sendMessage(handler, MSG_FOLLOW_ANALYSE_NO_TASK, -1);
            return null;
        }
        if(macList.size()==0) {
            sendMessage(handler, MSG_FOLLOW_ANALYSE_NO_MAC, -1);
            return null;
        }

        if(taskList.size()==0){
            sendMessage(handler, MSG_FOLLOW_ANALYSE_NO_TASK, -1);
            return null;
        }

        DeviceDao deviceDao = new DeviceDao(context);
        List<Device> deviceList = null;
        List<MacData> macDataList = new ArrayList<MacData>();
        for(String mac:macList){
            MacData m = new MacData();
            m.mac = mac;
            m.appearTasks = new ArrayList<Task>();
            m.totalTasks = taskList;
            if(!m.mac.equalsIgnoreCase(destMac))
                macDataList.add(m);
        }
        float total = macDataList.size();
        float count = 0;
        int sendPercent = 0;
        for(MacData macData:macDataList){
            for(Task t:taskList){
                try {
                    deviceList = deviceDao.getDao().queryBuilder().where().eq("task_id", t.getId())
                            .and().eq("mac",macData.mac).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("follow", "getFollowAnalyseData>>> exception:"+e.toString());
                }
                if(deviceList!=null){
                    if(deviceList.size()>0)
                        macData.appearTasks.add(t);
                }
            }
            count++;
            int percent = (int)(100*(count/total));
            if(percent!=sendPercent){
                if(handler!=null){
                    Message message = new Message();
                    message.what = MSG_FOLLOW_ANALYSE_PERCENT;
                    message.arg1 = percent;
                    sendPercent = percent;
                    Log.i("follow", "getFollowAnalyseData>>>sendMessage what:"+message.what+" arg1:"+message.arg1);
                    handler.sendMessage(message);
                }
            }
        }

        return macDataList;
    }

    void sendMessage(Handler handler, int what, int arg){
        if(handler!=null){
            Message message = new Message();
            message.what = what;
            message.arg1 = arg;
            Log.i("follow", "sendMessage>>> what:"+message.what);
            handler.sendMessage(message);
        }
    }

    public class MacData{
        public String mac;
        public List<Task> totalTasks;
        public List<Task> appearTasks;
    }

//follow analysis db operation

    public void addFollowAnalysisToDb(FollowAnalysis followAnalysis) {
        if(followAnalysis!=null)
        {
            FollowAnalysisDao dao = new FollowAnalysisDao(context);
            dao.add(followAnalysis);
        }
    }

    public void deleteFollowAnalysisFromDb(FollowAnalysis followAnalysis) {
        if(followAnalysis!=null)
        {
            FollowAnalysisDao dao = new FollowAnalysisDao(context);
            dao.delete(followAnalysis);
        }
    }


    public List<FollowAnalysis> getAllFollowAnalysisFromDb() {

        FollowAnalysisDao dao = new FollowAnalysisDao(context);
        List<FollowAnalysis> followAnalysises = null;
        try{
            followAnalysises = dao.getDao().queryForAll();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllFollowAnalysisFromDb>>>exception:"+e.toString());
        }
        return followAnalysises;
    }


    public List<FollowAnalysis> queryFollowAnalysis(String mac,
                     long startTime, long stopTime) {

        FollowAnalysisDao dao = new FollowAnalysisDao(context);
        List<FollowAnalysis> followAnalysises = null;
        try{
            followAnalysises = dao.getDao().queryBuilder().where().eq("mac", mac)
                    .and().eq("start_time", startTime).and().eq("stop_time", stopTime).query();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "queryFollowAnalysis>>>exception:"+e.toString());
        }
        return followAnalysises;
    }



    public void updateFollowAnalysisToDb(FollowAnalysis followAnalysis) {
        if(followAnalysis!=null)
        {
            FollowAnalysisDao dao = new FollowAnalysisDao(context);
            dao.update(followAnalysis);
        }
    }

////

    public void addFollowAnalysisMacToDb(FollowAnalysisMac followAnalysisMac) {
        if(followAnalysisMac!=null)
        {
            FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
            dao.add(followAnalysisMac);
        }
    }

    public void deleteFollowAnalysisMacFromDb(FollowAnalysisMac followAnalysisMac) {
        if(followAnalysisMac!=null)
        {
            FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
            dao.delete(followAnalysisMac);
        }
    }

    public void deleteFollowAnalysisMacFromDb(long id) {

        FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
        dao.delete(id);
    }


    public List<FollowAnalysisMac> getAllFollowAnalysisMacFromDb() {

        FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
        List<FollowAnalysisMac> followAnalysisMacs = null;
        try{
            followAnalysisMacs = dao.getDao().queryForAll();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllFollowAnalysisMacFromDb>>>exception:"+e.toString());
        }
        return followAnalysisMacs;
    }

    public void updateFollowAnalysisMacToDb(FollowAnalysisMac followAnalysisMac) {
        if(followAnalysisMac!=null)
        {
            FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
            dao.update(followAnalysisMac);
        }
    }

    public List<FollowAnalysisMac> getFollowAnalysisMacById(long analysisId) {

        FollowAnalysisMacDao dao = new FollowAnalysisMacDao(context);
        List<FollowAnalysisMac> followAnalysisMacs = null;
        try{
            followAnalysisMacs = dao.getDao().queryBuilder().where().eq("analysis_id", analysisId).query();

        }catch (SQLException e){
            Log.i(DtConstant.TAG, "getAllFollowAnalysisMacFromDb>>>exception:"+e.toString());
        }
        return followAnalysisMacs;
    }



////////////////////follow analysis end//////////////////

    public Cursor rawQuery(String sql)
    {
        return DtDatabaseHelper.getInstance(context).rawQuery(sql);
    }

}
