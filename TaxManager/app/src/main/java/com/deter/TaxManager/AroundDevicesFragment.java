package com.deter.TaxManager;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.view.DeviceRecyclerAdapter;
import com.deter.TaxManager.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AroundDevicesFragment extends Fragment implements DeviceDataChangeListener,
        USBChangeListener, View.OnClickListener {

    private final static String TAG = "AroundDevicesFragment";

    private int type;

    private TextView loadingTextView;

    private RecyclerView recyclerView;

    private DeviceRecyclerAdapter deviceRecyclerAdapter;

    private List<Device> mDevices = new ArrayList<>();

    private List<Device> checkedDevices = new ArrayList<>();

    private ArrayList<Device> tmp = new ArrayList<>();

    private boolean mBound;

    private boolean isFirstLauch = true;

    private boolean isRunningBackService;

    private BackgroundService mService;

    private View deauthSelectContainer;

    private ProgressDialog dialog;

    private CheckBox selectAll;

    private Button ok;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Object> resultlist = (List<Object>) msg.obj;
            getDeviceForType(type, resultlist);
            loadingTextView.setVisibility(View.GONE);

        }
    };

    private DeviceRecyclerAdapter.CheckboxCheckedCallback checkboxCheckedCallback = new
            DeviceRecyclerAdapter.CheckboxCheckedCallback() {
                @Override
                public void onChecked(boolean checked, Device device) {
                    if (checked) {
                        checkedDevices.add(device);
                    } else {
                        checkedDevices.remove(device);
                    }
                    if (checkedDevices.size() > 0 && mDevices.size() > checkedDevices
                            .size()) {
                        tmp.clear();
                        for (Device device1 : mDevices) {
                            if (checkedDevices.contains(device1)) {
                                continue;
                            }
                            tmp.add(device1);
                        }

                    }
                    Log.d(TAG, "checkedDeauthDevices size = " + checkedDevices.size() + ", " +
                            "tmp size" +
                            " =" + tmp.size() + ", mdevices size =" + mDevices.size());
                    updateSelectContainerState();
                }

                @Override
                public void onCheckedAll(boolean isAll) {
                    if (isAll) {
                        checkedDevices.clear();
                        checkedDevices.addAll(mDevices);
                        tmp.clear();
                    } else {
                        checkedDevices.clear();
                    }
                    Log.d(TAG, "checkedDeauthDevices size = " + checkedDevices.size());
                    updateSelectContainerState();
                }
            };

    private FuctionListener devicesFuctionListener = new FuctionListener() {
        @Override
        public void onPreExecute(int fuctionId) {

        }

        @Override
        public void onPostExecute(int fuctionId, int result) {
            switch (fuctionId) {
                case BuzokuFuction.FUCTION_DEAUTH_STOP:
                    if (result == 0) {
                        BackgroundService.getDeauthList().clear();
                        checkedDevices.clear();
                    } else {
                        Toast.makeText(getActivity(), R.string.deauth_cancel_fail, Toast
                                .LENGTH_SHORT).show();
                    }
                    dissmissProgressDialog();
                    initData(type, isRunningBackService);
                    break;
                case BuzokuFuction.FUCTION_DEAUTH_RESTART:
                    if (result == 0) {
                        for (Device device1 : checkedDevices) {
                            BackgroundService.removeDeviceFromDeauthList(device1);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.deauth_cancel_fail, Toast
                                .LENGTH_SHORT).show();

                    }
                    dissmissProgressDialog();
                    initData(type, isRunningBackService);
                    break;
                case BuzokuFuction.FUCTION_MITM_STOP:
                    if (result == 0) {
                        for (Device device1 : checkedDevices) {
                            BackgroundService.removeDeviceFromMitmList(device1);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.mitm_cancel_fail, Toast
                                .LENGTH_SHORT).show();
                    }
                    BuzokuFuction.getInstance(getActivity()).startListen(null, -1, -1, !APPUtils
                            .isSpecialDirectionScanEnable(getActivity()), devicesFuctionListener);
                    dissmissProgressDialog();
                    initData(type, isRunningBackService);
                    break;
            }
        }

        @Override
        public void onResult(int fuctionId, List<Object> resultList) {

        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            mService = binder.getService();
            mService.addDeviceDataChangeListener(AroundDevicesFragment.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService.removeDeviceDataChangeListener(AroundDevicesFragment.this);
            mService = null;
        }
    };

    public AroundDevicesFragment() {

    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt(APPConstans.KEY_DETAIL_TYPE);
        isRunningBackService = BackgroundService.isServiceRunning;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (BackgroundService.isServiceRunning && type != APPConstans.DETAIL_FORCE_DISCONNECTED
                && type != APPConstans.DETAIL_FORCE_CONNECTED) {
            Intent intent = new Intent(getActivity(), BackgroundService.class);
            getActivity().bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void initData(int Type, boolean isRunningBackService) {
        final int type = Type;
        if (type == APPConstans.DETAIL_FORCE_DISCONNECTED || type == APPConstans
                .DETAIL_FORCE_CONNECTED) {
            checkedDevices.clear();
            getDeviceForType(type, new ArrayList<>());
            updateSelectContainerState();
            if (type == APPConstans.DETAIL_FORCE_CONNECTED) {
                ok.setText(R.string.bottom_mitm_confirm);
            } else {
                ok.setText(R.string.bottom_deauth_confirm);
            }
            loadingTextView.setVisibility(View.GONE);
        } else {
            if (isRunningBackService) {
                BackgroundService.getDeviceList(handler);
            } else {
                loadingTextView.setVisibility(View.GONE);
            }
        }
    }

    private void getDeviceForType(int type, List<Object> deviceList) {
        Log.d(TAG, "deviceList =" + deviceList.size() + ", type =" + type);
        mDevices.clear();
        if (type == APPConstans.DETAIL_FORCE_CONNECTED || type == APPConstans
                .DETAIL_FORCE_DISCONNECTED) {
            switch (type) {
                case APPConstans.DETAIL_FORCE_CONNECTED:
                    mDevices.addAll(BackgroundService.getMitmList());
                    deviceRecyclerAdapter.setCheckedCallback(checkboxCheckedCallback);
                    deviceRecyclerAdapter.setCheckboxAll(false);
                    break;
                case APPConstans.DETAIL_FORCE_DISCONNECTED:
                    mDevices.addAll(BackgroundService.getDeauthList());
                    deviceRecyclerAdapter.setCheckedCallback(checkboxCheckedCallback);
                    deviceRecyclerAdapter.setCheckboxAll(false);
                    break;
            }
        } else {
            for (Object object : deviceList) {
                Device device = (Device) object;
                if (device.getRole().equalsIgnoreCase("ap")) {
                    switch (type) {
                        case APPConstans.DETAIL_AROUND_AP:
                            mDevices.add(device);
                            break;
                    }
                } else {
                    switch (type) {
                        case APPConstans.DETAIL_AROUND_STATION:
                        case APPConstans.DETAIL_SPECIAL_SEARCH:
                            mDevices.add(device);
                            break;
                    }
                }


            }
        }

        if (deviceRecyclerAdapter != null) {
            Log.d(TAG, "mDevices =" + mDevices.size());
            Collections.sort(mDevices, new Comparator<Device>() {
                @Override
                public int compare(Device device1, Device device2) {
                    return Float.compare(device1.getDistance(), device2.getDistance());
                }
            });
            deviceRecyclerAdapter.setDeviceData(mDevices);
        }
    }

    private void updateSelectContainerState() {
        if (checkedDevices.size() > 0) {
            deauthSelectContainer.setVisibility(View.VISIBLE);
            if (checkedDevices.size() == mDevices.size()) {
                selectAll.setChecked(true);
            } else {
                selectAll.setChecked(false);
            }
        } else {
            deauthSelectContainer.setVisibility(View.GONE);
        }

    }

    private void deauthDevices() {
        if (tmp.size() == 0) {
            showProgressDialog(R.string.deauth_cancel_progress_hint, getContext());
            BuzokuFuction.getInstance(getActivity()).stopDeauth(devicesFuctionListener);
        } else {
            showProgressDialog(R.string.deauth_cancel_progress_hint, getContext());
            BuzokuFuction.getInstance(getActivity()).restartDeauth(devicesFuctionListener,
                    tmp, APPUtils.getRssiForCurrentDistance(getActivity()), false);
        }
    }

    private void mitmDevices() {
        if (tmp.size() == 0) {
            BuzokuFuction.getInstance(getActivity()).stopMitm(devicesFuctionListener);
            showProgressDialog(R.string.mitm_cancel_progress_hint, getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_around_devices, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        loadingTextView = (TextView) view.findViewById(R.id.around_devices_loading);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        deauthSelectContainer = view.findViewById(R.id.select_container);
        selectAll = (CheckBox) view.findViewById(R.id.select_all);
        ok = (Button) view.findViewById(R.id.ok);

        selectAll.setOnClickListener(this);
        ok.setOnClickListener(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        deviceRecyclerAdapter = new DeviceRecyclerAdapter(getActivity(), mDevices, type);
        recyclerView.setAdapter(deviceRecyclerAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(28));
        initData(type, isRunningBackService);
        return view;
    }

    @Override
    public void onDeviceDataChanged(List<Object> devices) {
        getDeviceForType(type, devices);

    }

    @Override
    public void onUSBConnectChanged(boolean isConnected) {
        if (!isConnected) {
//            if (mBound) {
//                getActivity().unbindService(mConnection);
//                mService.removeDeviceDataChangeListener(this);
//                mBound = false;
//            }
            onDeviceDataChanged(new ArrayList<>());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "type =" + type + ", isRunningBackService =" + isRunningBackService);
        if (!isFirstLauch) {
            initData(type, isRunningBackService);
        } else {
            isFirstLauch = false;
        }
        if (mService != null && mBound) {
            mService.addDeviceDataChangeListener(this);
        }
        USBChangeBroadcastReceiver.addUSBChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mService != null && mBound) {
            mService.removeDeviceDataChangeListener(this);
        }
        USBChangeBroadcastReceiver.removeUSBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_all:
                if (selectAll.isChecked()) {
                    deviceRecyclerAdapter.setCheckboxAll(true);
                } else {
                    deviceRecyclerAdapter.setCheckboxAll(false);
                }
                break;
            case R.id.ok:
                if (type == APPConstans.DETAIL_FORCE_DISCONNECTED) {
                    deauthDevices();
                } else if (type == APPConstans.DETAIL_FORCE_CONNECTED) {
                    mitmDevices();
                }
                break;
        }
    }


    private void showProgressDialog(int resId, Context context) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(resId));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void dissmissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
