package com.deter.TaxManager;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.StreamUtils;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/08/04
 */
public class SSIDHistoryActivity extends BaseActivity {

    private final int ref_code = 0x0025;

    private TextView titleView;

    Dialog reminderDialog;

    private ListView listView;

    private StreamUtils<SSID> streamUtils;

    private List<SSID> ssidList;

    private CommonAdapter<SSID> commonAdapter;

    private DataManager dataManager;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ref_code:
                    commonAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    Runnable readRunnable = new Runnable() {

        @Override
        public void run() {
            List<SSID> ssids = streamUtils.readWifiConfigFromSdCard(APPConstans.CONFIG_PATH, APPConstans.WIFI_CONFIG_FILE_NAME);
            if (ssids.isEmpty()) {
                List<SSID> findList = dataManager.getAllSsidFromDb();
                ssidList.addAll(findList);
            } else {
                ssidList.addAll(ssids);
            }

            Collections.reverse(ssidList);
            Message msg = mhandler.obtainMessage();
            msg.what = ref_code;
            mhandler.sendMessageDelayed(msg, 10);

            if (!ssids.isEmpty()) {
                saveLocalConfigToDB(ssids);
            }
        }

    };

    Runnable writeRunnable = new Runnable() {

        @Override
        public void run() {
            streamUtils = new StreamUtils<>();
            boolean isCopySuccess = streamUtils.copyWifiConfigFromSdCard(APPConstans.CONFIG_PATH);
            if (!isCopySuccess) {
                reminderDialog = DialogUtils.showConfirmDialog(SSIDHistoryActivity.this, getResources().getString(R.string.ssid_history_no_root_permission), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reminderDialog.dismiss();
                        finish();
                    }
                });
                return;
            }
            mhandler.postDelayed(readRunnable, 200);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_ssidhistory);
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(R.string.ssid_history_title_str);
        listView = (ListView) findViewById(R.id.ssid_listview);
    }

    @Override
    public void initData() {

        if (null == dataManager) {
            dataManager = DataManager.getInstance(getApplicationContext());
        }

        ssidList = new ArrayList<>();
        commonAdapter = new CommonAdapter<SSID>(R.layout.ssid_history_item, SSIDHistoryActivity.this, ssidList) {
            @Override
            public void convert(ViewHolder viewHolder, SSID item) {
                viewHolder.setText(R.id.ssid_history_item_name, item.getSsid());
                ImageView lockIv = viewHolder.getView(R.id.has_pwd_lock_iv);
                if (!item.getEncryption().equals("OPEN")) {
                    lockIv.setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.ssid_history_item_password,item.getPassword());
                } else {
                    viewHolder.setText(R.id.ssid_history_item_password,"");
                    lockIv.setVisibility(View.GONE);
                }
            }
        };

        listView.setAdapter(commonAdapter);

        mhandler.postDelayed(writeRunnable, 200);
    }

    private void saveLocalConfigToDB(List<SSID> ssids) {

        List<SSID> findList = dataManager.getAllSsidFromDb();//get old data save to TopAp List

        for (SSID ssid : findList) {
            if (null != ssid) {
                TopAP topAP = new TopAP();
                topAP.setEncryption(ssid.getEncryption());
                topAP.setSsid(ssid.getSsid());
                topAP.setPassword(ssid.getPassword());
                boolean isExistTopAp = APPUtils. topApIsExistInTopListLibrary(topAP, dataManager);
                if (!isExistTopAp) {
                    dataManager.addTopApToDb(topAP);
                }
            }
        }


        if (!findList.isEmpty()) {
            dataManager.removeSsidListFromDb(findList);
        }

        dataManager.addSsidListToDb(ssids);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != reminderDialog && reminderDialog.isShowing()) {
            reminderDialog.dismiss();
            reminderDialog = null;
        }
        mhandler.removeCallbacks(readRunnable);
        mhandler.removeCallbacks(writeRunnable);
    }

}
