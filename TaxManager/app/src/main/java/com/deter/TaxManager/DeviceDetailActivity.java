package com.deter.TaxManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.view.DevicesMoreDetailRecyclerViewAdapter;
import com.deter.TaxManager.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DeviceDetailActivity extends AppCompatActivity implements USBChangeListener{

    private static final String TAG = "DeviceDetailActivity";

    private List<Device> devices = new ArrayList<>();

    private DevicesMoreDetailRecyclerViewAdapter devicesMoreDetailRecyclerViewAdapter;

    private int deviceType;

    private Device device;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            devicesMoreDetailRecyclerViewAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.deter.wifimonitor.device.state.changed".equalsIgnoreCase(intent.getAction())) {
                Log.d(TAG, "DeviceDetailActivity --- onReceive----DeviceDetailActivity");
                Bundle bundle = intent.getExtras();
                BackgroundService.updateDeviceState(bundle, devices);
                if (devicesMoreDetailRecyclerViewAdapter != null) {
                    devicesMoreDetailRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String deviceMac = intent.getStringExtra("deviceMac");
        deviceType = intent.getIntExtra("ap_station_detail", -1);
        device = BackgroundService.getDevice(deviceMac);
        Log.d(TAG, "deviceType =" + deviceType);
        if (deviceType == 1) {
            //devices.addAll(device.getStations());
            BackgroundService.excludeWhitleList(device.getStations(), devices, handler);
        } else {
            devices.add(device);
        }
        if (device == null) {
            finish();
        } else {
            Log.d(TAG, "device =" + device.toString());

        }
        setContentView(R.layout.activity_device_detail);

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(R.string.device_detail);

        View back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        devicesMoreDetailRecyclerViewAdapter = new DevicesMoreDetailRecyclerViewAdapter(this,
                devices);
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                LocalBroadcastManager.getInstance(DeviceDetailActivity.this).registerReceiver
                        (broadcastReceiver, new IntentFilter("com.deter.wifimonitor" +
                        ".device.state.changed"));

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                LocalBroadcastManager.getInstance(DeviceDetailActivity.this).unregisterReceiver
                        (broadcastReceiver);
            }
        });
        recyclerView.setAdapter(devicesMoreDetailRecyclerViewAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        USBChangeBroadcastReceiver.addUSBChangeListener(this);

    }

    @Override
    protected void onResume() {
        devicesMoreDetailRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        USBChangeBroadcastReceiver.removeUSBChangeListener(this);
    }

    @Override
    public void onUSBConnectChanged(boolean isConnected) {
        if(isConnected){

        }else {
            finish();
        }
    }
}
