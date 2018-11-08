package com.deter.TaxManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.view.DeviceMacRecyclerViewAdapter;
import com.deter.TaxManager.view.DeviceRecyclerAdapter;
import com.deter.TaxManager.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";

    private DeviceRecyclerAdapter deviceRecyclerAdapter;

    private DeviceMacRecyclerViewAdapter deviceMacRecyclerViewAdapter;

    private List<Device> mDevices = new ArrayList<>();

    private List<String> mMaclist = new ArrayList<>();

    private Context context;

    private String filter = "";

    private int type;

    private boolean firstLauch = true;

    private boolean isDeviceItem = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Object> resultlist = (List<Object>) msg.obj;
            getDeviceForType(type, resultlist);
            sortDeviceList(mDevices);
            filter(filter, isDeviceItem);
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged");
            filter = s.toString();
            filter(filter, isDeviceItem);
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView
            .OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            return true;
        }
    };

    private boolean stringIsMac(String val) {
        String trueMacAddressWithSeparator = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
        String trueMacAddress = "[A-Fa-f0-9]{12}";
        if (val.matches(trueMacAddressWithSeparator) || val.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.back);
        TextView textView = (TextView) toolbar.findViewById(R.id.cancle);
        EditText search = (EditText) findViewById(R.id.search);

        type = getIntent().getIntExtra(APPConstans.KEY_DETAIL_TYPE, 0);
        setSearchHint(type,search);

        imageView.setOnClickListener(this);
        textView.setOnClickListener(this);
        search.setOnEditorActionListener(onEditorActionListener);

        if (type == APPConstans.DETAIL_VIRTUAL_ID || type == APPConstans.DETAIL_WEBPAGES_SEARCH) {
            isDeviceItem = false;
            search.addTextChangedListener(textWatcher);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            deviceMacRecyclerViewAdapter = new DeviceMacRecyclerViewAdapter(this, mMaclist, type);
            recyclerView.setAdapter(deviceMacRecyclerViewAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new SpaceItemDecoration(28));
            recyclerView.requestFocus();
            updateMacList();

        } else {
            isDeviceItem = true;
            search.addTextChangedListener(textWatcher);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            deviceRecyclerAdapter = new DeviceRecyclerAdapter(this, mDevices, type);
            deviceRecyclerAdapter.setInSearchMode();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(deviceRecyclerAdapter);
            recyclerView.addItemDecoration(new SpaceItemDecoration(28));

            updateDevieList();
        }

    }

    private void setSearchHint(int type, EditText search) {
        switch (type) {
            case APPConstans.DETAIL_AROUND_AP:
                search.setHint(R.string.devices_search_ap_hint);
                break;
            case APPConstans.DETAIL_AROUND_STATION:
                search.setHint(R.string.devices_search_station_hint);
                break;
            case APPConstans.DETAIL_FORCE_CONNECTED:
                search.setHint(R.string.devices_search_station_hint);
                break;
            case APPConstans.DETAIL_FORCE_DISCONNECTED:
                search.setHint(R.string.devices_search_force_disconnect_hint);
                break;
            case APPConstans.DETAIL_WEBPAGES_SEARCH:
                search.setHint(R.string.devices_search_weburl_hint);
                break;
            case APPConstans.DETAIL_VIRTUAL_ID:
                search.setHint(R.string.devices_search_virtual_id_hint);
                break;
            default:
                search.setHint(R.string.devices_search_weburl_hint);
        }
    }


    private void filter(String text, boolean isDevice) {
        if (isDevice) {
            List<Device> temp = new ArrayList();
            for (Device d : mDevices) {
                if (d.getMac().toLowerCase().contains(text.toLowerCase()) || d.getMac()
                        .toLowerCase().contains(APPUtils.formateMacAddressToText(text)) || d
                        .getRole().equalsIgnoreCase("ap") && d.getSsid().toLowerCase()
                        .contains(text.toLowerCase()) || d.getRole().equalsIgnoreCase
                        ("station") && d.getVender().toLowerCase().contains(text
                        .toLowerCase())) {
                    temp.add(d);
                }
            }
            sortDeviceList(temp);
            deviceRecyclerAdapter.setDeviceData(temp);
        } else {
            List<String> temp = new ArrayList<>();
            for (String mac : mMaclist) {
                if (mac.toLowerCase().contains(text.toLowerCase()) || mac.toLowerCase()
                        .toLowerCase()
                        .contains(APPUtils.formateMacAddressToText(text))) {
                    temp.add(mac);
                }
            }
            sortMacList(temp);
            deviceMacRecyclerViewAdapter.setMacInfo(temp);
        }
    }

    private void updateDevieList() {
        if (type == APPConstans.DETAIL_FORCE_CONNECTED || type == APPConstans
                .DETAIL_FORCE_DISCONNECTED) {
            mDevices.clear();
            switch (type) {
                case APPConstans.DETAIL_FORCE_CONNECTED:
                    mDevices.addAll(BackgroundService.getMitmList());
                    break;
                case APPConstans.DETAIL_FORCE_DISCONNECTED:
                    mDevices.addAll(BackgroundService.getDeauthList());
                    break;
            }
            sortDeviceList(mDevices);
            filter(filter, true);
        } else {
            BackgroundService.getDeviceList(handler);
        }
    }

    private void updateMacList() {
        if (type == APPConstans.DETAIL_VIRTUAL_ID) {
            mMaclist = DataManager.getInstance(this).getIdentityMacListFromDb();

        } else {
            mMaclist = DataManager.getInstance(this).getUrlMacListFromDb();

        }
        sortMacList(mMaclist);
        filter(filter, false);
    }


    @Override
    protected void onResume() {
        if (!firstLauch) {
            if (isDeviceItem) {
                updateDevieList();
            } else {
                updateMacList();
            }
        } else {
            firstLauch = false;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
            case R.id.cancle:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void getDeviceForType(int type, List<Object> deviceList) {
        Log.d(TAG, "deviceList =" + deviceList.size() + ", type =" + type);
        mDevices.removeAll(mDevices);
        for (Object object : deviceList) {
            Device device = (Device) object;
            if (device.getRole().equalsIgnoreCase("ap")) {
                switch (type) {
                    case APPConstans.DETAIL_AROUND_AP:
                    case APPConstans.DETAIL_SPECIAL_SEARCH:
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

    private void sortDeviceList(List<Device> mDevices) {
        Collections.sort(mDevices, new Comparator<Device>() {
            @Override
            public int compare(Device device1, Device device2) {
                return Float.compare(device1.getDistance(), device2.getDistance());

            }
        });
    }

    private void sortMacList(List<String> mMaclist) {
        Collections.sort(mMaclist, new Comparator<String>() {
            @Override
            public int compare(String mac1, String mac2) {
                return mac1.compareTo(mac2);
            }
        });
    }

}
