package com.deter.TaxManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.view.TaskDetailPagerAdapter;
import com.deter.TaxManager.view.ViewPagerTabs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxinz on 2017/11/1.
 */

public class TaskDetailActivity extends Activity {

    private static final String TAG = "zyxddd";
    private static final int MSG_START_LOAD_DATA = 1;
    private static final int MSG_DATA_LOADED = 2;
    private List<Device> deviceList;
    private ViewPager mViewPager;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DATA_LOADED:
                    Log.d(TAG, "handleMessage>>>MSG_DATA_LOADED");
                    TaskDetailPagerAdapter viewPagerAdapter = new TaskDetailPagerAdapter(TaskDetailActivity.this, deviceList);
                    mViewPager.setAdapter(viewPagerAdapter);
                    ViewPagerTabs mViewPagerTabs = (ViewPagerTabs) findViewById(R.id.lists_pager_header);
                    mViewPagerTabs.setViewPager(mViewPager);
                    mViewPager.setOffscreenPageLimit(2);
                    mViewPager.addOnPageChangeListener(mViewPagerTabs);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_layout);
        Intent intent = getIntent();
        long taskId = intent.getLongExtra("task_id", 1);
        Log.i("zyxtest", "TaskDetailActivity>>>onCreate>>>taskId:"+taskId);
        deviceList = new ArrayList<Device>();
        mViewPager = (ViewPager) findViewById(R.id.task_detail_pager);
        View back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startLoadData(taskId);
    }

    void startLoadData(final long taskId)
    {
        Log.d(TAG, ">>>startLoadData");
        if(deviceList.size()>0){
            Message msg = new Message();
            msg.what = MSG_DATA_LOADED;
            mHandler.sendMessage(msg);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                deviceList = DataManager.getInstance(TaskDetailActivity.this).getDeviceListByTaskId(taskId);
                Message msg = new Message();
                msg.what = MSG_DATA_LOADED;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

}
