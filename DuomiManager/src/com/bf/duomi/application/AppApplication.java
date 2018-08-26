package com.bf.duomi.application;

import com.bf.duomi.util.PropertiesUtils;
import com.bf.duomi.util.SharePreferenceManager;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class AppApplication extends Application {
	private ConnectivityManager mConnManager;
	private static AppApplication mApplication;
	
	private boolean wifi;// 是否是wifi状态
	private boolean connected;// 是否联网
	private boolean cmwap;// cmwap网络
	private boolean fast;// 快速状态

	@Override
	public void onCreate() {
		super.onCreate();
		SharePreferenceManager.init(getApplicationContext());// 初始化SharePreferences
		mApplication = this;
		PropertiesUtils.init(getApplicationContext());

	}

	public void initScreenManager() {
		ScreenManager sm = ScreenManager.getInstance(getApplicationContext());
		sm.init();
	}

	/**
	 * 初始化ImageLoader
	 */
	public void initImageLoader() {
	}

	public synchronized static AppApplication getInstance() {
		return mApplication;
	}

	/** 首先要通过{@link #isConnected()}来判断是否有网络连接，然后调用此方法判断当前连接是否wifi */
	public boolean isWifi() {
		return wifi;
	}

	public boolean isConnected() {
		return connected;
	}

	/** 首先要通过{@link #isConnected()}来判断是否有网络连接，然后调用此方法判断当前连接是否cmwap */
	public boolean isCmwap() {
		return cmwap;
	}

	/** 网速快不快呀？ */
	public boolean isFast() {
		return fast;
	}

	void checkNetworkState() {

		if (mConnManager == null) {
			mConnManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		}

		connected = wifi = cmwap = fast = false;

		NetworkInfo info = mConnManager.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {

			connected = true;
			int type = info.getType();

			if (type == ConnectivityManager.TYPE_WIFI) {
				wifi = true;
				fast = true;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {

				String extra = info.getExtraInfo();
				cmwap = !TextUtils.isEmpty(extra)
						&& "cmwap".equals(info.getExtraInfo().toLowerCase());
				// 电信2G是 NETWORK_TYPE_CDMA

				// 移动2G卡 2 NETWORK_TYPE_EDGE

				// 联通的2G 1 NETWORK_TYPE_GPRS
				int subtype = info.getSubtype();
				switch (subtype) {
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_GPRS:
					Log.w("APPApplication", "网络变慢了。");
					fast = false;
					break;
				default:
					fast = true;
					break;
				}
			}
		}

	}

}
