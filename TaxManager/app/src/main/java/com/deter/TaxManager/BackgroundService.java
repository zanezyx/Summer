package com.deter.TaxManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.network.TcpMsgService;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";

    private static final Object object = new Object();

    public static boolean isServiceRunning = false;

    public static boolean isEnterQueryLoopHandler = false;

    private static List<Object> devices;

    private static List<Device> deauthList = new ArrayList<>();

    private static List<Device> mitmList = new ArrayList<>();

    private static List<SpecialDevice> lastOnlineBlackList = new ArrayList<>();
    private static List<SpecialDevice> lastOnlineWhiteList = new ArrayList<>();

    private static Device detailDevice;

    private static boolean isInOperation = false;

    private static Context applicationContext;

    private final IBinder mBinder = new LocalBinder();

    private static ArrayList<WeakReference<DeviceDataChangeListener>> deviceDataChangeListeners;

    private static Runnable queryCurrentDevicesRunnable = new Runnable() {
        @Override
        public void run() {
            queryCurrentDevice();
        }
    };

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
//                case MyInfoFragment.MESSAGE_DATA_UPDATE:
//                    devices = (List<Object>) msg.obj;
//                    if (deviceDataChangeListeners != null &&
//                            deviceDataChangeListeners.size() > 0) {
//                        Log.d(TAG, "deviceDataChangeListeners.size() =" + deviceDataChangeListeners
//                                .size());
//                        playAlarmSound(devices);
//                        for (WeakReference<DeviceDataChangeListener> deviceDataChangeListener :
//                                deviceDataChangeListeners) {
//                            Log.d(TAG, "queryCurrentDevice ------onResult---------------- " +
//                                    "resultList =" + devices.size());
//                            if (deviceDataChangeListener.get() != null) {
//                                deviceDataChangeListener.get().onDeviceDataChanged(devices);
//
//                            }
//                        }
//                    }
//                    postDelayed(queryCurrentDevicesRunnable, 8000);
//                    break;
            }

        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TcpMsgService.TCP_MSG_ACTION.equalsIgnoreCase(intent.getAction())) {
                Log.d(TAG, "BackgroundService --- onReceive----BackgroundService");
                Bundle bundle = intent.getExtras();
                String[] macs = bundle.getStringArray("mac");
                int state = bundle.getInt("state");
                switch (bundle.getInt("msg_id")) {
                    case TcpMsgService.MSG_DEAUTH_SUCCEED:
                        boolean isMacInDeauthList = isMacInDeauthList(macs[0]);
                        if (isMacInDeauthList) {
                            updateDeviceDeauthState(macs[0], state);
                            Log.d(TAG, "broadcastReceiver =====broadcastReceiver " +
                                    "MSG_DEAUTH_SUCCEED " + "macs[0] =" + macs[0] + ", state =" +
                                    state);
                        }
                        sendDeviceBroadCast(context, bundle);
                        break;
                    case TcpMsgService.MSG_MITM_SUCCEED:
                        String ip = bundle.getString("ip");
                        String ssid = bundle.getString("ssid");
                        Log.d(TAG, "broadcastReceiver =====broadcastReceiver MSG_MITM_SUCCEED " +
                                "macs[0] =" + macs[0]
                                + ", state =" + state + ", ip =" + ip + ",ssid =" + ssid);
                        updateDeviceMitmState(macs[0], state, ip, ssid);
                        sendDeviceBroadCast(context, bundle);
                        break;
                }
            }
        }
    };

    private void sendDeviceBroadCast(Context context, Bundle bundle) {
        Intent intent = new Intent("com.deter.wifimonitor.device.state.changed");
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    public static void updateDeviceState(Bundle bundle, List<Device> deviceList) {
        String[] macs = bundle.getStringArray("mac");
        int state = bundle.getInt("state");
        switch (bundle.getInt("msg_id")) {
            case TcpMsgService.MSG_DEAUTH_SUCCEED:
                boolean isMacInDeauthList = isMacInDeauthList(macs[0]);
                if (isMacInDeauthList) {
                    for (Device device : deviceList) {
                        if (device.getMac().equals(macs[0])) {
                            device.setDeauthState(state);
                            break;
                        }
                    }
                }
                break;
            case TcpMsgService.MSG_MITM_SUCCEED:
                String ip = bundle.getString("ip");
                String ssid = bundle.getString("ssid");
                for (Device device : deviceList) {
                    if (device.getMac().equalsIgnoreCase(macs[0])) {
                        device.setMitmState(state);
                        device.setIpaddr(ip);
                        device.setIpInfoName(ssid);
                        break;
                    }
                }
                break;
        }
    }

    private void updateDeviceMitmState(String mac, int state, String ip, String ssid) {
        isInOperation = true;
        for (Device device : mitmList) {
            if (device.getMac().equalsIgnoreCase(mac)) {
                device.setMitmState(state);
                device.setIpInfoName(ssid);
                device.setIpaddr(ip);
                updateDeviceStateifNeed(device);
                break;
            }
        }
        isInOperation = false;
    }


    private void updateDeviceDeauthState(String mac, int state) {
        isInOperation = true;
        for (Device device : deauthList) {
            if (device.getMac().equalsIgnoreCase(mac)) {
                device.setDeauthState(state);
                updateDeviceStateifNeed(device);
                break;
            }
        }
        isInOperation = false;
    }


    public BackgroundService() {

    }

    public static void startEnterQueryLoopHandler() {
        Log.d(TAG, "startEnterQueryLoopHandler ---- startEnterQueryLoopHandler");
        handler.removeCallbacksAndMessages(null);
        isEnterQueryLoopHandler = true;
        handler.postDelayed(queryCurrentDevicesRunnable, 8000);
    }


    public static void exitQueryLoopHandler() {
        Log.d(TAG, "exitQueryLoopHandler ---- exitQueryLoopHandler");
        handler.removeCallbacks(queryCurrentDevicesRunnable);
        handler.removeCallbacksAndMessages(null);
        isEnterQueryLoopHandler = false;
    }

    public static void updateDeviceStateifNeed(Device device) {
        isInOperation = true;
        if (devices.contains(device)) {
            return;
        }
        if (true) {
            for (Object device1 : devices) {
                Device device2 = (Device) device1;
                if (device2.getMac().equalsIgnoreCase(device.getMac())) {
                    device2.setDeauthState(device.getDeauthState());
                    device2.setMitmState(device.getMitmState());
                    device2.setIpaddr(device.getIpaddr());
                    device2.setIpInfoName(device.getIpInfoName());
                    device2.setInblacklist(device.isInblacklist());
                    device2.setInwhitelist(device.isInwhitelist());
                }
            }
        }
        isInOperation = false;
    }

    public static void updateDeauthDeviceState(Device device) {
        isInOperation = true;
        if (deauthList.contains(device)) {
            return;
        }
        for (Device device1 : deauthList) {
            if (device1.getMac().equalsIgnoreCase(device.getMac())) {
                device1.setDeauthState(device.getDeauthState());
            }
        }
        isInOperation = false;
    }


    public static void updateDeviceState(Handler handler) {
        if (devices == null) {
            return;
        }
        updateDeviceState(devices, handler);
    }

    public static void updateDeviceState(final List<Object> list, final Handler handler) {
        final DataManager dataManager = DataManager.getInstance(applicationContext);
        synchronized (object) {

            List<Device> scanDevices = new ArrayList<>();
            for (Object object : list) {
                if (object instanceof Device) {
                    Device device = (Device) object;
                    scanDevices.add(device);
                }
            }

            List<SpecialDevice> localWhiteList = DataManager.getInstance(applicationContext).getWhiteListFromDb();
            List<SpecialDevice> lastWhiteDevices = BackgroundService.getLastOnlineWhiteList();
            if (null != localWhiteList && !localWhiteList.isEmpty()) {
                List<SpecialDevice> onlineWhiteDevices = APPUtils.findOnlineDevice(scanDevices, localWhiteList);
                if (!onlineWhiteDevices.isEmpty()) {
                    lastWhiteDevices.clear();
                    lastWhiteDevices.addAll(onlineWhiteDevices);
                }
            } else {
                if (!lastWhiteDevices.isEmpty()) {
                    lastWhiteDevices.clear();
                }
            }

            Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                Device device = (Device) iterator.next();
                device.setDeauthState(getDeviceDeauthState(device));
                device.setMitmState(getDeviceMitmState(device));
                if (isMacInMitmList(device.getMac())) {
                    device.setIpaddr(mitmList.get(0).getIpaddr());
                    device.setIpInfoName(mitmList.get(0).getIpInfoName());
                }
                device.setInblacklist(dataManager.queryMacIsInBlacklist(device.getMac()));
                if (dataManager.queryMacIsInWhitelist(device.getMac())) {
                    device.setInwhitelist(dataManager.queryMacIsInWhitelist(device.getMac()));
                    iterator.remove();
                }
            }
//            Message message = handler.obtainMessage();
//            message.what = MyInfoFragment.MESSAGE_DATA_UPDATE;
//            message.obj = list;
//            handler.sendMessage(message);
        }
    }


    public static void updateMitmDeviceState(Device device) {
        if (mitmList.contains(device)) {
            return;
        }
        for (Device device1 : mitmList) {
            if (device1.getMac().equalsIgnoreCase(device.getMac())) {
                device1.setMitmState(device.getMitmState());
                device1.setSsid(device.getSsid());
                device1.setIpaddr(device.getIpaddr());
            }
        }
    }

    public static void resetAllData() {
        if (devices != null) {
            devices.clear();
        }
        if(deviceDataChangeListeners != null){
            deviceDataChangeListeners.clear();
            deviceDataChangeListeners = null;
        }
        mitmList.clear();
        deauthList.clear();
    }

    public static void setDetailDevice(Device device) {
        detailDevice = device;
    }

    public static Device getDevice(String deiceMac) {
        if (devices != null) {
            for (Object object : devices) {
                Device device = (Device) object;
                if (device.getMac().equals(deiceMac)) {
                    return device;
                }
            }
        }
        return detailDevice;
    }

    public static void setFirstDeviceData(final List<Object> objectList, final DataManager
            dataManager, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (new Object()) {
                    Iterator<Object> iterator = objectList.iterator();
                    while (iterator.hasNext()) {
                        Device device = (Device) iterator.next();
                        device.setDeauthState(getDeviceDeauthState(device));
                        device.setMitmState(getDeviceMitmState(device));
                        if (dataManager.queryMacIsInWhitelist(device.getMac())) {
                            device.setInwhitelist(dataManager.queryMacIsInWhitelist(device.getMac
                                    ()));
                            iterator.remove();
                        }
                    }

                }
                devices = objectList;
                playAlarmSound(devices);
                if (handler != null) {
//                    Message message = handler.obtainMessage();
//                    message.what = MyInfoFragment.MESSAGE_DATA_UPDATE;
//                    message.obj = devices;
//                    handler.sendMessage(message);
                }

            }
        }).start();

    }

    public static void clearDeviceList() {
        devices.clear();
    }


    public static void getDeviceList(Handler handler) {
        updateDeviceState(handler);
    }

    public static List<Device> addDeviceToDeauthList(Device device) {
        isInOperation = true;
        if (devices.contains(device)) {
            deauthList.add(device);
        } else {
            for (Object object : devices) {
                Device device1 = (Device) object;
                if (device1.getMac().equalsIgnoreCase(device.getMac())) {
                    device1.setInwhitelist(device.isInwhitelist());
                    device1.setInblacklist(device.isInblacklist());
                    device1.setMitmState(device.getMitmState());
                    device1.setDeauthState(device.getDeauthState());
                    deauthList.add(device1);
                    break;
                }
            }
        }
        Log.d(TAG, "addDeauthList device mac=" + device.getMac() + ", deauth size =" + deauthList
                .size());
        isInOperation = false;
        return deauthList;
    }

    public static List<Device> removeDeviceFromDeauthList(Device device) {
        isInOperation = true;
        if (deauthList.contains(device)) {
            deauthList.remove(device);
        } else {
            for (Device device1 : deauthList) {
                if (device1.getMac().equalsIgnoreCase(device.getMac())) {
                    deauthList.remove(device1);
                    break;
                }
            }
        }
        Log.d(TAG, "removeDeviceFromDeauthList device mac =" + device.getMac());
        isInOperation = false;
        return deauthList;
    }

    public static List<Device> getDeauthList() {
        return deauthList;
    }

    public static List<Device> addDeviceToMitmList(Device device) {
        isInOperation = true;
        device.setIpaddr("");
        device.setIpInfoName("");
        if (mitmList.size() > 0) {
            Device oldDevice = mitmList.get(0);
            oldDevice.setMitmState(0);
        }
        mitmList.clear();
        if (devices.contains(device)) {
            mitmList.add(device);
        } else {
            for (Object object : devices) {
                Device device1 = (Device) object;
                if (device1.getMac().equals(device.getMac())) {
                    device1.setInblacklist(device.isInblacklist());
                    device1.setInwhitelist(device.isInwhitelist());
                    device1.setDeauthState(device.getDeauthState());
                    device1.setMitmState(device.getMitmState());
                    mitmList.add(device1);
                    break;
                }
            }
        }
        Log.d(TAG, "addDeviceToMitmList device mac=" + device.getMac() + ", mitm size =" + mitmList
                .size());
        isInOperation = false;
        return mitmList;
    }

    public static void clearDeauthList() {
        for (Device device : deauthList) {
            device.setDeauthState(0);
        }
        deauthList.clear();
    }

    public static void setDeauthListStateToStart() {
        isInOperation = true;
        for (Device device : deauthList) {
            device.setDeauthState(1);
        }
        isInOperation = false;
    }


    public static List<Device> removeDeviceFromMitmList(Device device) {
        Log.d(TAG, "removeDeviceFromMitmList device mac =" + device.getMac());
        isInOperation = true;
        if (mitmList.contains(device)) {
            device.setIpInfoName("");
            device.setIpaddr("");
            mitmList.remove(device);
            return mitmList;
        }
        for (Device device1 : mitmList) {
            if (device1.getMac().equalsIgnoreCase(device.getMac())) {
                device1.setIpInfoName("");
                device1.setIpaddr("");
                mitmList.remove(device1);
                break;
            }
        }
        isInOperation = false;
        return mitmList;
    }


    public static boolean isMacInDeauthList(String mac) {
        if (TextUtils.isEmpty(mac)) {
            return false;
        }
        for (Device device : deauthList) {
            if (device.getMac().equalsIgnoreCase(mac)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isMacInMitmList(String mac) {
        if (TextUtils.isEmpty(mac)) {
            return false;
        }
        for (Device device : mitmList) {
            if (device.getMac().equalsIgnoreCase(mac)) {
                return true;
            }
        }
        return false;
    }


    public static List<Device> getMitmList() {
        return mitmList;
    }

    public static List<SpecialDevice> getLastOnlineBlackList() {
        return lastOnlineBlackList;
    }

    public static List<SpecialDevice> getLastOnlineWhiteList() {
        return lastOnlineWhiteList;
    }

    public static void removeOnlineBlackList(SpecialDevice oprDevice) {
        Iterator<SpecialDevice> lastDeviceIterator = lastOnlineBlackList.iterator();
        while (lastDeviceIterator.hasNext()) {
            SpecialDevice lastDevice = lastDeviceIterator.next();
            if (oprDevice.getMac().equals(lastDevice.getMac())) {
                lastDeviceIterator.remove();
                break;
            }
        }
    }


    public static int getDeviceDeauthState(Device device) {
        for (int i = 0; i < deauthList.size(); i++) {
            if (deauthList.get(i).getMac().equalsIgnoreCase(device.getMac())) {
                return deauthList.get(i).getDeauthState();
            }
        }
        return 0;
    }

    public static int getDeviceMitmState(Device device) {
        for (int i = 0; i < mitmList.size(); i++) {
            if (mitmList.get(i).getMac().equalsIgnoreCase(device.getMac())) {
                return mitmList.get(i).getMitmState();
            }
        }
        return 0;
    }

    public static void excludeWhitleList(final List<Device> stations, final List<Device> result,
                                         final Handler handler) {
        final DataManager dataManager = DataManager.getInstance(applicationContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Device station : stations) {
                    if (dataManager.queryMacIsInWhitelist(station.getMac())) {
                        continue;
                    }
                    result.add(station);
                }
                Message message = handler.obtainMessage();
                message.arg1 = result.size();
                handler.sendMessage(message);
            }
        }).start();
    }

    public static void updateStationNumberView(Device device, Handler handler) {
        if (device.getStations() != null) {
            excludeWhitleList(device.getStations(), new ArrayList<Device>(), handler);
        } else {
            Message message = handler.obtainMessage();
            message.arg1 = 0;
            handler.sendMessage(message);
        }

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        applicationContext = getApplicationContext();
        startEnterQueryLoopHandler();
        isServiceRunning = true;
        IntentFilter intentFilter = new IntentFilter(TcpMsgService.TCP_MSG_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory level =" + level);
        super.onTrimMemory(level);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        isServiceRunning = false;
        unregisterReceiver(broadcastReceiver);
        exitQueryLoopHandler();
        resetAllData();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public void addDeviceDataChangeListener(DeviceDataChangeListener deviceDataChangeListener) {
        if (deviceDataChangeListeners == null) {
            deviceDataChangeListeners = new ArrayList<>();
        }
        for (Iterator<WeakReference<DeviceDataChangeListener>> iterator =
             deviceDataChangeListeners.iterator(); iterator.hasNext(); ) {
            WeakReference<DeviceDataChangeListener> weakRef = iterator.next();
            if (weakRef.get() == deviceDataChangeListener) {
                return;
            }
        }
        deviceDataChangeListeners.add(new WeakReference<>(deviceDataChangeListener));

    }

    public void removeDeviceDataChangeListener(DeviceDataChangeListener deviceDataChangeListener) {
        if (deviceDataChangeListeners == null) {
            return;
        }
        for (Iterator<WeakReference<DeviceDataChangeListener>> iterator =
             deviceDataChangeListeners.iterator();
             iterator.hasNext(); ) {
            WeakReference<DeviceDataChangeListener> weakRef = iterator.next();
            if (weakRef.get() == deviceDataChangeListener) {
                iterator.remove();
            }
        }
        if (deviceDataChangeListeners.size() == 0) {
            deviceDataChangeListeners = null;
        }
    }

    class LocalBinder extends Binder {

        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    private static void queryCurrentDevice() {
        final long startQueryTime = SystemClock.elapsedRealtime();
        Log.d(TAG,"queryCurrentDevice start time ="+startQueryTime);
        if(APPUtils.isUsbTethered(applicationContext)){
            BuzokuFuction buzokuFuction = BuzokuFuction.getInstance(applicationContext);
            buzokuFuction.queryCurrDevice(new FuctionListener() {
                @Override
                public void onPreExecute(int fuctionId) {

                }

                @Override
                public void onPostExecute(int fuctionId, int result) {

                }

                @Override
                public void onResult(int fuctionId, final List<Object> resultList) {
                    if(!isEnterQueryLoopHandler) return;
                    long resultTime = SystemClock.elapsedRealtime();
                    Log.d(TAG,"onResult resultTime ="+resultTime+", spend time ="+(resultTime-startQueryTime)+"ms");
                    if (resultList != null && deviceDataChangeListeners != null &&
                            deviceDataChangeListeners.size() > 0) {
                        Log.d(TAG, "updateDeviceState-------------------------resultList size ="+resultList.size());
                        if (!isInOperation) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateDeviceState(resultList, handler);
                                }
                            }, 500);

                        } else {
                            updateDeviceState(resultList, handler);
                        }
                    } else {
                        Log.d(TAG, "updateDeviceState-------------------------resultList or deviceDataChangeListeners = null");
                        handler.postDelayed(queryCurrentDevicesRunnable, 8000);
                    }
                }
            });
        }

    }

    private static void playAlarmSound(List<Object> deviceList) {
        if (null == applicationContext || deviceList.isEmpty()) {
            return;
        }
        boolean isHasBlack = APPUtils.isOnLineInBlackList(applicationContext, deviceList);
        boolean isPlayEnable = APPUtils.isBlackWarningToneEnable(applicationContext);
        if (isHasBlack && isPlayEnable) {
            APPUtils.play(applicationContext, APPConstans.WHITE_BLACK_PROMPT_TONE_TYPE);
        }
    }

}
