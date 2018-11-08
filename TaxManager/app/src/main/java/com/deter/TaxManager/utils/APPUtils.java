package com.deter.TaxManager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.BackgroundService;
import com.deter.TaxManager.R;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.network.DtConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static com.deter.TaxManager.APPConstans.VIRTUAL_ID_TAGS;

/**
 * Created by yuxinz on 2017/8/11.
 */

public class APPUtils {

    private static MediaPlayer mMediaPlayer;
    private static String localMac = null;


    public static float rssiToDistance(int rssi, String role) {
        initRssiDistanceMap();
        float distance = 0f;
        int rssiAbs = Math.abs(rssi);
        Log.i(DtConstant.DT_TAG, "rssiToDistance>>>" + rssiAbs);

        if (rssiDistanceMap != null) {
            Float temp = rssiDistanceMap.get(rssiAbs);
            if (temp != null) {
                distance = temp.floatValue();
            } else {
                distance = 1f;
            }
        } else {
            distance = 1f;
        }
        return distance;
    }

    public static int getRssiForCurrentDistance(Context context) {
        String distance = getStringValueFromSharePreference(context, "network_outage_distance");
        switch (Integer.valueOf(distance)) {
            case 10:
                return 76;
            case 20:
                return 83;
            case 30:
                return 88;
            case 40:
                return 91;
            case 50:
                return 100;
            default:
                try {
                    throw new Exception("no support distance");
                } catch (Exception e) {
                    e.printStackTrace();
                    return 100;
                }
        }
    }

    public static String formateTextToMacAddress(String text) {
        if (TextUtils.isEmpty(text) || text.length() != 12) return text;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i = i + 2) {
            stringBuilder.append(text.substring(i, i + 2));
            if (i + 2 < text.length()) stringBuilder.append(":");
        }
        return stringBuilder.toString();
    }

    public static String formateMacAddressToText(String text) {
        if (TextUtils.isEmpty(text) || text.length() != 17) {
            return text.replace(":","");
        }
        String[] a = text.split(":");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            stringBuilder.append(a[i]);
        }
        return stringBuilder.toString().toLowerCase();

    }


    public static List<Object> getUniqList(List<Object> list) {
        Set<Object> newList = new TreeSet<>(new Comparator<Object>() {
            @Override
            public int compare(Object device1, Object device2) {
                return ((Device) device1).getMac().compareToIgnoreCase(((Device) device2).getMac());
            }
        });
        newList.addAll(list);
        List<Object> list1 = new ArrayList<>();
        list1.addAll(newList);
        return list1;
    }


    public static HashMap<Integer, Float> rssiDistanceMap = null;

    private static void initRssiDistanceMap() {
        if (rssiDistanceMap == null) {
            rssiDistanceMap = new HashMap<Integer, Float>();

            rssiDistanceMap.put(0, 0.00f);
            rssiDistanceMap.put(1, 0.01f);
            rssiDistanceMap.put(2, 0.01f);
            rssiDistanceMap.put(3, 0.01f);
            rssiDistanceMap.put(4, 0.01f);
            rssiDistanceMap.put(5, 0.01f);
            rssiDistanceMap.put(6, 0.02f);
            rssiDistanceMap.put(7, 0.03f);
            rssiDistanceMap.put(8, 0.04f);
            rssiDistanceMap.put(9, 0.05f);

            rssiDistanceMap.put(10, 0.06f);
            rssiDistanceMap.put(11, 0.07f);
            rssiDistanceMap.put(12, 0.08f);
            rssiDistanceMap.put(13, 0.09f);
            rssiDistanceMap.put(14, 0.10f);
            rssiDistanceMap.put(15, 0.11f);
            rssiDistanceMap.put(16, 0.12f);
            rssiDistanceMap.put(17, 0.13f);
            rssiDistanceMap.put(18, 0.14f);
            rssiDistanceMap.put(19, 0.15f);

            rssiDistanceMap.put(20, 0.16f);
            rssiDistanceMap.put(21, 0.17f);
            rssiDistanceMap.put(22, 0.18f);
            rssiDistanceMap.put(23, 0.19f);
            rssiDistanceMap.put(24, 0.20f);
            rssiDistanceMap.put(25, 0.21f);
            rssiDistanceMap.put(26, 0.22f);
            rssiDistanceMap.put(27, 0.23f);
            rssiDistanceMap.put(28, 0.24f);
            rssiDistanceMap.put(29, 0.25f);

            rssiDistanceMap.put(30, 0.26f);
            rssiDistanceMap.put(31, 0.27f);
            rssiDistanceMap.put(32, 0.28f);
            rssiDistanceMap.put(33, 0.29f);
            rssiDistanceMap.put(34, 0.30f);
            rssiDistanceMap.put(35, 0.33f);
            rssiDistanceMap.put(36, 0.34f);
            rssiDistanceMap.put(37, 0.35f);
            rssiDistanceMap.put(38, 0.36f);
            rssiDistanceMap.put(39, 0.37f);

            rssiDistanceMap.put(40, 0.38f);
            rssiDistanceMap.put(41, 0.39f);
            rssiDistanceMap.put(42, 0.40f);
            rssiDistanceMap.put(43, 0.45f);
            rssiDistanceMap.put(44, 1.00f);
            rssiDistanceMap.put(45, 1.02f);
            rssiDistanceMap.put(46, 1.04f);
            rssiDistanceMap.put(47, 1.06f);
            rssiDistanceMap.put(48, 1.08f);
            rssiDistanceMap.put(49, 1.10f);

            rssiDistanceMap.put(50, 1.12f);
            rssiDistanceMap.put(51, 1.14f);
            rssiDistanceMap.put(52, 1.16f);
            rssiDistanceMap.put(53, 1.18f);
            rssiDistanceMap.put(54, 1.20f);
            rssiDistanceMap.put(55, 1.23f);

            rssiDistanceMap.put(56, 1.37f);
            rssiDistanceMap.put(57, 1.62f);
            rssiDistanceMap.put(58, 1.78f);
            rssiDistanceMap.put(59, 1.96f);
            rssiDistanceMap.put(60, 2.15f);
            rssiDistanceMap.put(61, 2.37f);
            rssiDistanceMap.put(62, 2.61f);
            rssiDistanceMap.put(63, 2.87f);
            rssiDistanceMap.put(64, 3.16f);
            rssiDistanceMap.put(65, 3.48f);
            rssiDistanceMap.put(66, 3.83f);
            rssiDistanceMap.put(67, 4.22f);
            rssiDistanceMap.put(68, 4.64f);
            rssiDistanceMap.put(69, 5.11f);
            rssiDistanceMap.put(70, 5.62f);
            rssiDistanceMap.put(71, 6.19f);
            rssiDistanceMap.put(72, 6.81f);
            rssiDistanceMap.put(73, 7.50f);
            rssiDistanceMap.put(74, 8.25f);
            rssiDistanceMap.put(75, 9.09f);
            rssiDistanceMap.put(76, 10.00f);
            rssiDistanceMap.put(77, 11.01f);
            rssiDistanceMap.put(78, 12.12f);
            rssiDistanceMap.put(79, 13.34f);
            rssiDistanceMap.put(80, 14.68f);
            rssiDistanceMap.put(81, 16.16f);
            rssiDistanceMap.put(82, 17.78f);
            rssiDistanceMap.put(83, 19.57f);
            rssiDistanceMap.put(84, 21.54f);
            rssiDistanceMap.put(85, 23.71f);
            rssiDistanceMap.put(86, 26.10f);
            rssiDistanceMap.put(87, 28.73f);
            rssiDistanceMap.put(88, 31.62f);
            rssiDistanceMap.put(89, 34.81f);
            rssiDistanceMap.put(90, 38.31f);
            rssiDistanceMap.put(91, 42.17f);
            rssiDistanceMap.put(92, 46.42f);
            rssiDistanceMap.put(93, 51.09f);
            rssiDistanceMap.put(94, 56.23f);
            rssiDistanceMap.put(95, 61.90f);
            rssiDistanceMap.put(96, 68.13f);
            rssiDistanceMap.put(97, 74.99f);
            rssiDistanceMap.put(98, 82.54f);
            rssiDistanceMap.put(99, 90.85f);
        }
    }


    public static int getKeyFromHashMap(HashMap<Integer, Float> map, float value) {
        if (map == null)
            return 60;
        int key = 60;
        for (int getKey : map.keySet()) {
            if (map.get(getKey).floatValue() == value) {
                key = getKey;
            }
        }
        return key;
    }


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                boolean isavailable = mNetworkInfo.isAvailable();
                Log.d("xiaolu", "isNetworkConnected  " + isavailable);
                return isavailable;
            }
        }
        return false;
    }

    public static boolean isLocalMacAddress(Context context, String mac){
        if(localMac == null){
            localMac = getWifiMacAddress(context);
        }
        return localMac.equalsIgnoreCase(formateTextToMacAddress(mac));
    }

    public static String getWifiMacAddress(Context context){
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        String macAddress = wifiInfo == null ? null : wifiInfo.getMacAddress();
        return !TextUtils.isEmpty(macAddress) ? macAddress : "unkown";
    }


    public static boolean isUsbTethered(Context context) {
        if (DtConstant.DEBUG_MODE) {
            return true;
        }
        boolean usbTethered = false;
        String[] mUsbRegexs = new String[]{"rndis\\d"};
        Intent intent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter("android.net.conn.TETHER_STATE_CHANGED"));
        if (intent == null) {
            return false;
        }
        ArrayList<String> active = intent.getStringArrayListExtra("activeArray");
        if (active == null) {
            return false;
        }
        Log.d("xiaolu", "active size =" + active.size());
        for (String s : active) {
            for (String regex : mUsbRegexs) {
                if (s.matches(regex)) usbTethered = true;
            }
        }
        return usbTethered;
    }

    public static boolean hasVirtualId(List<Identity> identityList) {
        if (identityList == null || identityList.size() == 0) {
            return false;
        }
        boolean hasVirtualId = false;
        for (Identity identity : identityList) {
            if (VIRTUAL_ID_TAGS.contains(identity.getTag())) {
                hasVirtualId = true;
                break;
            }
        }
        return hasVirtualId;
    }

    public static boolean isSpecialDirectionScanEnable(Context context) {
        return getValueFromSharePreference(context, "orienteer_scan", true);
    }

    public static boolean isBlackWarningToneEnable(Context context) {
        return getValueFromSharePreference(context, "warning_tone_black_white", true);
    }

    public static boolean isCollisionWarningToneEnable(Context context) {
        return getValueFromSharePreference(context, "warning_tone_collision", false);
    }

    public static boolean isAccompanyWarningToneEnable(Context context) {
        return getValueFromSharePreference(context, "warning_tone_accompany", false);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }

    public static void saveValueToSharePreference(Context context, String key, boolean value) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveValueToSharePreference(Context context, String key, String value) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getValueFromSharePreference(Context context, String key , boolean defaultValue) {
        SharedPreferences sharedPreferences =getSharedPreferences(context);
        boolean value = sharedPreferences.getBoolean(key, defaultValue);
        return value;
    }

    public static String getStringValueFromSharePreference(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String value = sharedPreferences.getString(key, "");
        return value;
    }


    public static void initDbFile(final Context context) {
        Log.i(DtConstant.TAG, ">>>initDbFile>>>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String filePath = context.getCacheDir().getAbsolutePath();
                    Log.i(DtConstant.TAG, ">>>initDbFile>>>filePath:" + filePath);
                    String DATABASE_PATH = filePath.substring(0, filePath.length() - 5)
                            + "databases";
                    String DATABASE_FILENAME = "db_dtwireless";
                    String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
                    Log.i(DtConstant.TAG, ">>>initDbFile>>>databaseFilename:" + databaseFilename);
                    File dir = new File(DATABASE_PATH);

                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    if (!(new File(databaseFilename)).exists()) {
                        InputStream is = context.getResources().openRawResource(R.raw
                                .db_dtwireless);
                        ;
                        FileOutputStream fos = new FileOutputStream(databaseFilename);
                        byte[] buffer = new byte[8192];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i(DtConstant.TAG, ">>>initDbFile>>>FileNotFoundException:" + e.toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i(DtConstant.TAG, ">>>initDbFile>>>IOException:" + e.toString());
                }
            }
        }).start();

    }


    /**
     * Check whether the terminal is on the blacklist
     * @param context
     * @param deviceList
     * @return
     */
    public static boolean isOnLineInBlackList(Context context,List<Object> deviceList) {

        try {
            if (null == deviceList || deviceList.isEmpty()) {
                return false;
            }

            List<Device> scanDevices = new ArrayList<>();
            for (Object object : deviceList) {
                if (object instanceof Device) {
                    Device device = (Device) object;
                    scanDevices.add(device);
                }
            }

            List<SpecialDevice> localBlackList = DataManager.getInstance(context).getBlackListFromDb();
            if (null == localBlackList || localBlackList.isEmpty()) {
                return false;
            }

            List<SpecialDevice> onlineBlackDevices = findOnlineDevice(scanDevices, localBlackList);
            List<SpecialDevice> lastBlackDevices = BackgroundService.getLastOnlineBlackList();

            if (!onlineBlackDevices.isEmpty()) {

                if (lastBlackDevices.isEmpty()) {
                    lastBlackDevices.addAll(onlineBlackDevices);
                    return true;
                }

                Iterator<SpecialDevice> lastDeviceIterator = lastBlackDevices.iterator();

                boolean isSame = true;
                for (SpecialDevice onlineDevice : onlineBlackDevices) {
                    String mac = onlineDevice.getMac();
                    boolean ishaveItem = false;
                    while (lastDeviceIterator.hasNext()) {
                        SpecialDevice lastDevice = lastDeviceIterator.next();
                        if (mac.equals(lastDevice.getMac())) {
                            ishaveItem = true;
                            lastDeviceIterator.remove();
                            break;
                        }
                    }

                    lastDeviceIterator = lastBlackDevices.iterator();
                    if (!ishaveItem) {
                        isSame = false;
                        break;
                    }
                }

                lastBlackDevices.clear();
                lastBlackDevices.addAll(onlineBlackDevices);

                if (!isSame) {
                    return true;
                }
            } else {
                lastBlackDevices.clear();
            }

        } catch (Exception e) {
            com.deter.TaxManager.utils.Log.i("APPUtils", "Exception >>>> current isOnLineInBlackList " + e.toString());
            e.printStackTrace();
            return false;
        }

        return false;
    }


    /**
     * Check out existing online blacklist terminals
     * @param scanDevices
     * @param localBlackList
     * @return
     */
    public static List<SpecialDevice> findOnlineDevice(List<Device> scanDevices, List<SpecialDevice> localBlackList) {
        List<SpecialDevice> onLineDevice = new ArrayList<>();
        Iterator<Device> deviceIterator = scanDevices.iterator();

        for (SpecialDevice localDevice : localBlackList) {
            while (deviceIterator.hasNext()) {
                Device device = deviceIterator.next();
                if (null == localDevice || null == device) {
                    break;
                }

                if (localDevice.getMac().equals(device.getMac())) {
                    onLineDevice.add(localDevice);
                    deviceIterator.remove();
                    break;
                }
            }
            deviceIterator = scanDevices.iterator();
        }

        return onLineDevice;
    }

    public static synchronized MediaPlayer getMediaPlayer() {
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        return mMediaPlayer;
    }


    /**
     * Play alarm
     * @param context
     * @param toneType
     */
    public static synchronized void play(Context context,int toneType) {
        getMediaPlayer();
        if (mMediaPlayer.isPlaying()) {
            return;
        }
        mMediaPlayer.reset();
        AssetFileDescriptor file = null;

        if (toneType == APPConstans.WHITE_BLACK_PROMPT_TONE_TYPE) {
            file = context.getResources().openRawResourceFd(R.raw.veryalarmed);
        } else if (toneType == APPConstans.ACCOMPANT_TONE_TYPE) {
            file = context.getResources().openRawResourceFd(R.raw.office);
        } else if (toneType == APPConstans.COLLISION_TONE_TYPE) {
            file = context.getResources().openRawResourceFd(R.raw.office);
        }

        if (null == file) {
            com.deter.TaxManager.utils.Log.i("APPUtils", "play >>>> current file is null stop play");
            return;
        }

        try {
            mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            com.deter.TaxManager.utils.Log.i("APPUtils", "IOException >>>> current file " + e.toString());
            e.printStackTrace();
        }
    }

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            return true;
        }
        return false;
    }

    public static boolean isEn(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en")) {
            return true;
        }
        return false;
    }


    /**
     * Determine if the SSID exists in a password library
     * @param configSSID
     * @param dataManager
     * @return
     */
    public static boolean ssidIsExistInSSIDLibrary(SSID configSSID, DataManager dataManager) {
        boolean isExist = false;
        List<SSID> findList;
        if (null == configSSID.getPassword()) {
            findList = dataManager.getSsidFromDbBySsid(configSSID.getSsid());
            if (null == findList || findList.isEmpty()) {
                return false;
            }
            for (SSID findSSID : findList) {
                if (null == findSSID) {
                    continue;
                }

                if (null == findSSID.getSsid() || null == configSSID.getSsid()) {
                    break;
                }

                if (findSSID.getSsid().equals(configSSID.getSsid())) {
                    if (null != findSSID.getPassword() && null != configSSID.getPassword()) {
                        if (findSSID.getPassword().equals(configSSID.getPassword())) {
                            isExist = true;
                            break;
                        }
                    } else if (null == findSSID.getPassword() && null == configSSID.getPassword()) {
                        isExist = true;
                        break;
                    }
                }
            }
        } else {
            List<SSID> findSize = dataManager.getSsidFromDbBySsidAndPassword(configSSID.getSsid(), configSSID.getPassword());
            if (null != findSize && findSize.size() > 0) {
                isExist = true;
            }
        }

        return isExist;
    }


    /**
     * Determine whether top exists in the TopApList
     * @param addTopAp
     * @param dataManager
     * @return
     */
    public static boolean topApIsExistInTopListLibrary(TopAP addTopAp,DataManager dataManager) {
        boolean isExist = false;

        List<TopAP> findList;

        if (null == addTopAp.getPassword()) {
            findList = dataManager.getTopApFromDbBySsid(addTopAp.getSsid());//find out if it exists
            if (null == findList || findList.isEmpty()) {
                return false;
            }
            for (TopAP findTopAp : findList) {
                if (null == findTopAp) {
                    continue;
                }

                if (null == findTopAp.getSsid() || null == addTopAp.getSsid()) {
                    break;
                }

                if (findTopAp.getSsid().equals(addTopAp.getSsid())) {
                    if (null != findTopAp.getPassword() && null != addTopAp.getPassword()) {
                        if (findTopAp.getPassword().equals(addTopAp.getPassword())) {
                            isExist = true;
                            break;
                        }
                    } else if (null == findTopAp.getPassword() && null == addTopAp.getPassword()) {
                        isExist = true;
                        break;
                    }
                }
            }
        }else{
            List<TopAP> topAPList = dataManager.getTopApFromDbBySsidAndPassword(addTopAp.getSsid(), addTopAp.getPassword());
            if (null != topAPList && topAPList.size() > 0) {
                return true;
            }
        }


        return isExist;
    }


}



