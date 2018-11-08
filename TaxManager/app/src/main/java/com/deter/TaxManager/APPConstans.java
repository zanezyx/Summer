package com.deter.TaxManager;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-8-7.
 */

public class APPConstans {

    public final static String KEY_DETAIL_TYPE = "key_detail_type";

    public final static String KEY_DETAIL_TITLE = "key_detail_title";

    public final static String EXPORT_FILENAME = "ssid_library.txt";

    public final static String WIFI_CONFIG_FILE_NAME = "wpa_supplicant.conf";

    public final static int DETAIL_AROUND_AP = 1;

    public final static int DETAIL_AROUND_STATION = 2;

    public final static int DETAIL_SPECIAL_SEARCH = 3;

    public final static int DETAIL_FORCE_CONNECTED = 4;

    public final static int DETAIL_FORCE_DISCONNECTED = 5;

    public final static int DETAIL_BLACKLIST = 6;

    public final static int DETAIL_WHITELIST = 7;

    public final static int DETAIL_SSID = 8;

    public final static int DETAIL_WEBPAGES_SEARCH = 9;

    public final static int DETAIL_VIRTUAL_ID = 10;

    public final static int DETAIL_FILTER = 11;

    public final static int DETAIL_START_MAP_TASK_LIST = 12;

    public final static int DETAIL_TOP_AP_LIST = 13;

    public final static int DETAIL_START_MAP_TASK_LIST_BY_TIME = 15;

    public final static int DETAIL_ANALYSIS_TASK_LIST = 16;

    public static final int RECYCLE_TYPE_DETAIL_AP = 1;

    public static final int RECYCLE_TYPE_DETAIL_STATION = 2;

    public static final int RECYCLE_TYPE_DETAIL_STATION_MITM = 3;

    public final static String CONSTANT_NETWORK_OUTAGE_DISTANCE = "10";

    public final static int WHITE_BLACK_PROMPT_TONE_TYPE = 22;
    public final static int ACCOMPANT_TONE_TYPE = 23;
    public final static int COLLISION_TONE_TYPE = 24;

    private final static String PARENT_DIR_NAME = "deter";

    public final static String CONFIG_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + PARENT_DIR_NAME;


    /* virtual id tag */

    public static final String TAG = "VirtualIdActivity";

    public static final String TAG_PHONE = "phone";

    public static final String TAG_IMEI = "imei";

    public static final String TAG_IMSI = "imsi";

    public static final String TAG_QQ = "qq";

    public static final String TAG_WEIXIN = "weixinid";

    public static final String TAG_SINAWEIBO = "sinaweibo";

    public static final String TAG_TAOBAO = "taobao";

    public static final String TAG_BAIDU = "baiduid";

    public static final String TAG_DOUBAN = "douban";

    public static final String TAG_LUMI = "lumi";

    public static final String TAG_MOMO = "momoid";

    public static final String TAG_CTRIP = "ctrip";

    public static final String TAG_DIDI = "didi";

    public static final String TAG_DIANPING = "dianping";

    public static final String TAG_JD = "jdid";

    public static final String TAG_SUNING = "suning";

    public static final String TAG_QQMAIL = "qqmail";


    public static final List<String> VIRTUAL_ID_TAGS = new ArrayList<String>() {
        {
            add(TAG_PHONE);
            add(TAG_IMEI);
            add(TAG_IMSI);
            add(TAG_QQ);
            add(TAG_WEIXIN);
            add(TAG_SINAWEIBO);
            add(TAG_TAOBAO);
            add(TAG_BAIDU);
            add(TAG_DOUBAN);
            add(TAG_LUMI);
            add(TAG_MOMO);
            add(TAG_CTRIP);
            add(TAG_DIDI);
            add(TAG_DIANPING);
            add(TAG_JD);
            add(TAG_SUNING);
            add(TAG_QQMAIL);
        }
    };

    public final static int TYPE_SELECT_START_TIME = 0x0011;
    public final static int TYPE_SELECT_END_TIME = 0x0096;

}
