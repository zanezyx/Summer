package com.deter.TaxManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.view.LocationListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-8-4.
 */

public class LocationHelper {

    private static final String TAG = "LocationHelper";

    private LocationClient client = null;

    private LocationClientOption mOption, DIYoption;

    private Object objLock = new Object();

    /***
     *
     * @param locationContext
     */
    public LocationHelper(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     *
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);
            //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
            // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation
            // .getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption;
    }

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                Log.d("xiaolu", "location start");
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    public boolean requestHotSpotState() {

        return client.requestHotSpotState();

    }

    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public static class MyBDLocationListener implements BDLocationListener {

        private boolean isLocationChoiceEnable = false;

        private Context context;

        private Handler handler;

        private LocationHelper locationHelper;

        private List<PoiInfo> poiInfoList = new ArrayList<>();

        private MyOnGetGeoCoderResultListener myOnGetGeoCoderResultListener;

        private View view;

        public MyBDLocationListener(View rootView, Context context, LocationHelper
                locationHelper, Handler handler) {
            this.view = rootView;
            this.context = context;
            this.locationHelper = locationHelper;
            this.handler = handler;
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            locationHelper.stop();
            locationHelper.unregisterListener(this);
            PoiInfo poiInfo = new PoiInfo();
            double mLatitude = bdLocation.getLatitude();
            double mLongitude = bdLocation.getLongitude();
            String address = bdLocation.getAddrStr();
            poiInfo.address = TextUtils.isEmpty(bdLocation.getAddrStr()) ? context.getString(R
                    .string
                    .longitude_latitude, String.valueOf(bdLocation.getLongitude()), String
                    .valueOf(bdLocation.getLatitude())) : bdLocation.getAddrStr();
            poiInfo.name = bdLocation.getStreet();
            poiInfoList.clear();
            poiInfoList.add(0, poiInfo);
            if (isLocationChoiceEnable && APPUtils.isNetworkConnected(context)) {
                GeoCoder geoCoder = GeoCoder.newInstance();
                myOnGetGeoCoderResultListener = new MyOnGetGeoCoderResultListener(view,
                        context, mLatitude, mLongitude, address, poiInfoList, handler);
                geoCoder.setOnGetGeoCodeResultListener(myOnGetGeoCoderResultListener);
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng
                        (mLatitude, mLongitude)));

            } else {
                //handler.sendEmptyMessage(MyInfoFragment.MESSAGE_DISSMISS_PROGRESS_DIALOG);
//                Message message = handler.obtainMessage();
//                message.what = MyInfoFragment.MESSAGE_SET_LOCATION;
//                message.obj = new LoactionInfo(mLatitude, mLongitude, address == null ? "" :
//                        address);
//                handler.sendMessageDelayed(message,10000);
            }
            handler = null;
            view = null;
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.d(TAG, "onConnectHotSpotMessage --- s =" + s + ", i =" + i);
        }

        void setUSBConnected(boolean isUSBConnected) {
            if (myOnGetGeoCoderResultListener != null) {
                myOnGetGeoCoderResultListener.setUSBConnected(isUSBConnected);
            }
        }
    }

    public static class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        private View view;

        private Context context;

        private Handler handler;

        private PopupWindow popupWindow;

        private List<PoiInfo> poiInfoList = new ArrayList<>();

        private double baseLatitude;

        private double baseLongitude;

        private String baseAddress;

        private boolean isUSBConnected = true;

        public MyOnGetGeoCoderResultListener(View view, Context context, double baseLatitude,
                                             double baseLongitude, String baseAddress,
                                             List<PoiInfo> poiInfoList,
                                             Handler handler) {
            this.view = view;
            this.context = context;
            this.baseLatitude = baseLatitude;
            this.baseLongitude = baseLongitude;
            this.baseAddress = baseAddress;
            this.poiInfoList = poiInfoList;
            this.handler = handler;
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//            Message message = handler.obtainMessage();
//            message.what = MyInfoFragment.MESSAGE_SET_LOCATION;
//            if (geoCodeResult != null && geoCodeResult.getLocation() != null) {
//                Log.d(TAG, "goeCodeResult =" + geoCodeResult.getLocation().toString());
//                double mLongitude = geoCodeResult.getLocation().longitude;
//                double mLatitude = geoCodeResult.getLocation().latitude;
//                String address = geoCodeResult.getAddress();
//                message.obj = new LoactionInfo(mLatitude, mLongitude, address);
//                Log.d(TAG, "address =" + address + ", mLatitude =" + mLatitude + ", " +
//                        "mLongitude =" + mLongitude);
//            } else {
//                message.obj = new LoactionInfo(baseLatitude, baseLongitude, baseAddress);
//            }
//            handler.sendMessage(message);

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//            handler.sendEmptyMessage(MyInfoFragment.MESSAGE_DISSMISS_PROGRESS_DIALOG);
//            if (reverseGeoCodeResult == null || reverseGeoCodeResult.getPoiList()
//                    == null) {
//                Log.d(TAG, "no network please check");
//            } else {
//                Log.d(TAG, "reverseGeoCodeResult.getPoiList() =" +
//                        reverseGeoCodeResult
//                                .getPoiList().size());
//                poiInfoList.addAll(1, reverseGeoCodeResult.getPoiList());
//
//            }
//            showPopWindow(context, view, handler, this);
        }

        private void showPopWindow(Context context, View view, final Handler handler, final
        MyOnGetGeoCoderResultListener myOnGetGeoCoderResultListener) {

            if (!isUSBConnected) {
                return;
            }

            View v = LayoutInflater.from(context).inflate(R.layout
                    .layout_location_popwindow, null, false);

            popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.setBackgroundDrawable(null);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(false);
            popupWindow.setAnimationStyle(R.style.popWindowAnimation);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            LocationListAdapter locationListAdapter = new LocationListAdapter(context, poiInfoList);
            ListView listView = (ListView) v.findViewById(R.id.location);
            listView.setAdapter(locationListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Message message = handler.obtainMessage();
//                    message.what = MyInfoFragment.MESSAGE_SET_LOCATION;
                    if (position == 0) {
                        Log.d(TAG, "address =" + baseAddress + ", mLatitude =" + baseLatitude +
                                ", mLongitude =" + baseLongitude);
                        message.obj = new LoactionInfo(baseLatitude, baseLongitude, baseAddress);
                        handler.sendMessage(message);
                    } else {
                        if (poiInfoList.get(position).city != null && poiInfoList.get
                                (position).address != null) {
                            GeoCoder geoCoder = GeoCoder.newInstance();
                            geoCoder.setOnGetGeoCodeResultListener
                                    (myOnGetGeoCoderResultListener);
                            geoCoder.geocode((new GeoCodeOption().city(
                                    poiInfoList.get(position).city).address(
                                    poiInfoList.get(position).address)));
                            baseAddress = poiInfoList.get(position).address;
                        } else {
                            Log.d(TAG, "address =" + baseAddress + ", mLatitude =" + baseLatitude +
                                    ", mLongitude =" + baseLongitude);
                            message.obj = new LoactionInfo(baseLatitude, baseLongitude,
                                    baseAddress);
                            handler.sendMessage(message);
                        }
                    }
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
            if (!isUSBConnected) {
                dissmissPopupWindow();
            }
            Log.d(TAG, "poiInfoList size =" + poiInfoList.size());
        }

        void dissmissPopupWindow() {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }

        private void setUSBConnected(boolean isUSBConnected) {
            this.isUSBConnected = isUSBConnected;
            if (!isUSBConnected) {
                dissmissPopupWindow();
            }
        }

    }

    public static class LoactionInfo {

        private double mLatitude;

        private double mLongitude;

        private String mAddress;

        public LoactionInfo(double latitude, double longitude, String address) {
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            this.mAddress = address;
        }

        public double getmLatitude() {
            return mLatitude;
        }

        public void setmLatitude(double mLatitude) {
            this.mLatitude = mLatitude;
        }

        public double getmLongitude() {
            return mLongitude;
        }

        public void setmLongitude(double mLongitude) {
            this.mLongitude = mLongitude;
        }

        public String getmAddress() {
            return mAddress;
        }

        public void setmAddress(String mAddress) {
            this.mAddress = mAddress;
        }

        @Override
        public String toString() {
            return "LocationInfo is mLatitude = " + mLatitude + ", mLongitude =" + mLongitude +
                    ", mAddress =" + mAddress;
        }
    }


}
