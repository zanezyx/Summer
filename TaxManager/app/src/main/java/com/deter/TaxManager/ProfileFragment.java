package com.deter.TaxManager;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.DndMac;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.model.URL;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.BasePopupWindow;
import com.deter.TaxManager.view.PickerView;
import com.deter.TaxManager.view.WifiMonitorPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.deter.TaxManager.network.BuzokuFuction.DETER_DIR_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends PreferenceFragment implements Preference
        .OnPreferenceChangeListener, USBChangeListener {

    private final String KEY_SOFTWARE_VERION = "software_version";

    private int opr_type_reboot = 0x0029;  //当前操作为 设备重启
    private int opr_type_orienteer_scan = 0x0075;  //当前操作为  定向扫描
    private int opr_type_sd_expand = 0x0030;

    private boolean isSDExpand = false;
    private final int sdexpand_command_code = 0x0010;
    private final int sdexpand_result_code = 0x0011;

    private boolean isRebooting = false;
    private final int reboot_command_code = 0x0027;//重启命令，响应结果码
    private final int reboot_result_code = 0x0092;//重启命令之后，根据usb连接状态，响应结果码

    private final int export_succeed_code = 0x0057;

    private final int export_fail_code = 0x0051;

    private final int orienteer_scan_switch_result_code = 0x0035;

    private final int usb_not_connected_code = 0x0017;

    private final int usb_shared_network_not_open_code = 0x0063;

    private final int mitm_or_listening = 0x0073;//是否正在强连或者正在监听扫描中

    private Dialog confirmCancelDialog;
    ProgressDialog progressDialog;

    ProgressDialogRunnable showProgressDialogRunnable;

    BasePopupWindow oprResultPopupWindow;

    DataManager dataManager;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what != reboot_command_code && msg.what != sdexpand_command_code) {
                dismissProgressDialog();
            }

            switch (msg.what) {

                case 0:
                    mHandler.removeCallbacks(showProgressDialogRunnable);
//                    String str = ProfileFragment.this.getActivity().getResources().getString(R.string.synchronize_data_success);
//                    ToastUtils.showShort(ProfileFragment.this.getActivity(), str);
                    //showOprResultPopupWindow(R.string.synchronize_data_success, R.drawable.ssid_opr_succeed);

                    showExportSynchronizeDataDetail(0);
                    break;

                case sdexpand_command_code:
                    int resultCode = (int) msg.obj;
                    if (resultCode == 0) {
                        if (!isSDExpand) {
                            isSDExpand = true;
                        }

                        if (DtConstant.DEBUG_MODE) {
                            Message message = mHandler.obtainMessage();
                            message.what = sdexpand_result_code;
                            message.obj = 1;
                            mHandler.postDelayed(timeOutSDExpandRunnable, 8000);
                        }
                    } else {
                        mHandler.removeCallbacks(showProgressDialogRunnable);
                        dismissProgressDialog();
                        isSDExpand = false;
                        showOprResultPopupWindow(R.string.settings_sd_expand_fail, R.drawable.ssid_opr_fail);
                    }
                    break;
                case sdexpand_result_code:
                    mHandler.removeCallbacks(showProgressDialogRunnable);
                    mHandler.removeCallbacks(timeOutSDExpandRunnable);
                    if (isSDExpand) {
                        isSDExpand = false;
                    }
                    int commandCode = (int) msg.obj;
                    if (commandCode == 0) {
                        showOprResultPopupWindow(R.string.settings_sd_expand_succeed, R.drawable.ssid_opr_succeed);
                    } else {
                        showOprResultPopupWindow(R.string.settings_sd_expand_fail, R.drawable.ssid_opr_fail);
                    }
                    break;
                case reboot_command_code:
                    int result = (int) msg.obj;
                    if (result == 0) {
                        if (!isRebooting) {
                            isRebooting = true;
                        }

                        if (DtConstant.DEBUG_MODE) {
                            Message message = mHandler.obtainMessage();
                            message.what = reboot_result_code;
                            message.obj = 1;
                            mHandler.postDelayed(timeOutRebootRunnable, 8000);
                        }
                    } else {
                        mHandler.removeCallbacks(showProgressDialogRunnable);
                        dismissProgressDialog();
                        isRebooting = false;
                        showOprResultPopupWindow(R.string.settings_device_reboot_fail, R.drawable.ssid_opr_fail);
                    }
                    break;

                case reboot_result_code:
                    mHandler.removeCallbacks(showProgressDialogRunnable);
                    mHandler.removeCallbacks(timeOutRebootRunnable);
                    if (isRebooting) {
                        isRebooting = false;
                    }
                    int rebootCode = (int) msg.obj;
                    if (rebootCode == 0) {
                        showOprResultPopupWindow(R.string.settings_device_reboot_succeed, R.drawable.ssid_opr_succeed);
                    } else {
                        showOprResultPopupWindow(R.string.settings_device_reboot_fail, R.drawable.ssid_opr_fail);
                    }
                    break;

                case orienteer_scan_switch_result_code:
                    String prompt = (String) msg.obj;
                    ToastUtils.showShort(ProfileFragment.this.getActivity(), prompt);
                    break;

                case export_succeed_code:
                    mHandler.removeCallbacks(showProgressDialogRunnable);
                    //showOprResultPopupWindow(R.string.ssid_opr_status_export_succeed, R.drawable.ssid_opr_succeed);
                    showExportSynchronizeDataDetail(export_succeed_code);
                    break;

                case export_fail_code:
                    mHandler.removeCallbacks(showProgressDialogRunnable);
                    showOprResultPopupWindow(R.string.ssid_opr_status_export_fail, R.drawable.ssid_opr_fail);
                    break;

                case usb_not_connected_code:
                    if (null != getActivity()) {
                        String usbUnconnetedPrompt = getActivity().getResources().getString(R.string.wifi_map_pi_noresponding_hint);
                        showConfirmDialog(usbUnconnetedPrompt);
                    }

                    break;

                case usb_shared_network_not_open_code:
                    if (null != getActivity()) {
                        String usbSharedPrompt = getActivity().getString(R.string.app_usbtether_off_title);
                        confirmCancelDialog = DialogUtils.showConfirmAndCancelDialog(getActivity(), usbSharedPrompt, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmCancelDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmCancelDialog.dismiss();
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$TetherSettingsActivity"));
                                startActivity(intent);
                            }
                        });
                    }

                    break;

                case mitm_or_listening:
                    String promptStr = (String) msg.obj;
                    showConfirmDialog(promptStr);
                    break;
            }
        }
    };

    private void showConfirmDialog(String prompt) {
        dismissConfirmDialog();
        confirmCancelDialog = DialogUtils.showConfirmDialog(getActivity(), prompt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCancelDialog.dismiss();
            }
        });
    }


    private void dismissConfirmDialog() {
        if (null != confirmCancelDialog && confirmCancelDialog.isShowing()) {
            confirmCancelDialog.dismiss();
            confirmCancelDialog = null;
        }
    }

    private BuzokuFuction buzokuFuction;

    private MyFuctionListener myFuctionListener;

    private List<String> distanceSelectDatas;

    private int selectPosition = -1;

    private String units;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_profile);
        init();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((TextView) v.findViewById(R.id.title)).setText(R.string.my_profile);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            USBChangeBroadcastReceiver.addUSBChangeListener(this);
        } else {
            USBChangeBroadcastReceiver.removeUSBChangeListener(this);
        }
    }

    private void init() {
        units = getResources().getString(R.string.network_outage_distance_meter);
        distanceSelectDatas = new ArrayList<>();
        distanceSelectDatas.add("10" + units);
        distanceSelectDatas.add("20" + units);
        distanceSelectDatas.add("30" + units);
        distanceSelectDatas.add("40" + units);
        distanceSelectDatas.add("50" + units);

        SwitchPreference debugPreference = (SwitchPreference) getPreferenceManager().findPreference("debug_mode");
        //debugPreference.setChecked(DtConstant.DEBUG_MODE);
        //DtConstant.DEBUG_MODE = debugPreference.isChecked();
        SwitchPreference scanPreference = (SwitchPreference) getPreferenceManager().findPreference("orienteer_scan");
        boolean isCheck = scanPreference.isChecked();
        switchOpration(scanPreference, isCheck, null);

        scanPreference.setOnPreferenceChangeListener(this);
        debugPreference.setOnPreferenceChangeListener(this);

        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            android.util.Log.d("xiaolu", "packageInfo name =" + packageInfo.versionName);
            ((WifiMonitorPreference) findPreference(KEY_SOFTWARE_VERION)).setMoreInfo(packageInfo.versionName);

            String defaultStr = APPUtils.getStringValueFromSharePreference(ProfileFragment.this.getActivity(), "network_outage_distance");
            if (TextUtils.isEmpty(defaultStr)) {
                defaultStr = APPConstans.CONSTANT_NETWORK_OUTAGE_DISTANCE;
                selectPosition = 0;
                APPUtils.saveValueToSharePreference(ProfileFragment.this.getActivity(), "network_outage_distance", defaultStr);
            } else {
                for (int i = 0; i < distanceSelectDatas.size(); i++) {
                    String subStr = distanceSelectDatas.get(i).substring(0, 2);
                    if (subStr.equals(defaultStr)) {
                        selectPosition = i;
                        break;
                    }
                }
            }
            ((WifiMonitorPreference) findPreference("network_outage_distance")).setMoreInfo(defaultStr + units);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        buzokuFuction = BuzokuFuction.getInstance(getActivity().getApplicationContext());
        myFuctionListener = new MyFuctionListener();

    }


    @Override
    public void onUSBConnectChanged(boolean isConnected) {
        if (isRebooting) {
            Message message = mHandler.obtainMessage();
            message.what = reboot_result_code;
            if (isConnected) {
                message.obj = 0;
                mHandler.sendMessageDelayed(message, 0);
            } else {
                mHandler.postDelayed(timeOutRebootRunnable, 100000);
            }
        } else if(isSDExpand){
            Message message = mHandler.obtainMessage();
            message.what = sdexpand_result_code;
            if (isConnected) {
                message.obj = 0;
                mHandler.sendMessageDelayed(message, 0);
            } else {
                mHandler.postDelayed(timeOutSDExpandRunnable, 100000);
            }
        }
    }

    Runnable timeOutSDExpandRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = mHandler.obtainMessage();
            message.what = sdexpand_result_code;
            message.obj = 1;
            mHandler.sendMessageDelayed(message, 0);
        }
    };

    Runnable timeOutRebootRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = mHandler.obtainMessage();
            message.what = reboot_result_code;
            message.obj = 1;
            mHandler.sendMessageDelayed(message, 0);
        }
    };


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Boolean isOn = (Boolean) newValue;
        String msg;
        switch (preference.getKey()) {

            case "orienteer_scan":
                if(!isUsbTether()) return false;
                rebootAndScanOpration(preference, isOn,opr_type_orienteer_scan);
                break;

            case "debug_mode":
                if (isOn) {
                    DtConstant.DEBUG_MODE = true;
                    msg = getActivity().getResources().getString(R.string.debug_mode_enable);
                } else {
                    DtConstant.DEBUG_MODE = false;
                    msg = getActivity().getResources().getString(R.string.debug_mode_disable);
                }
                switchOpration(preference, isOn, msg);
                Context context = getActivity();
                Intent mStartActivity = new Intent(context, MainActivity.class);
                int mPendingIntentId = 123456;
                final PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300, mPendingIntent);
                        System.exit(0);
                    }
                },200);
                break;
        }

        return false;
    }


    //设备扫描，以及设备重启之前的操作
    private void rebootAndScanOpration(Preference preference, Boolean isOn, int type) {
        if (type == opr_type_orienteer_scan) {
            myFuctionListener.setPreference(preference);
            myFuctionListener.setOn(isOn);
            showProgressDialog(0, R.string.is_response);
        } else if (type == opr_type_reboot) {
            showProgressDialog(0, R.string.settings_device_reboot_prompt);
        } else if(type == opr_type_sd_expand){
            showProgressDialog(0, R.string.settings_sd_expand_prompt);
        }

        myFuctionListener.setType(type);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (opr_type_orienteer_scan == myFuctionListener.getType()) {
                        Thread.sleep(800);
                    }
                    buzokuFuction.queryListenState(myFuctionListener);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void switchOpration(Preference preference, boolean check, String msg) {
        SwitchPreference sp = (SwitchPreference) preference;
        sp.setChecked(check);
        if (null != msg) {
            ToastUtils.showShort(ProfileFragment.this.getActivity(), msg);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (null == preference.getKey()) {
            return false;
        }

        switch (preference.getKey()) {

            case "star_map_list":
                Intent starMapIntent = new Intent(getActivity(), StarMapTaskListActivity.class);
                starMapIntent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_START_MAP_TASK_LIST);
                startActivity(starMapIntent);
                break;

            case "export_all_data":
                exportAllData();
                break;

            case "warning_tone":
                Intent intent = new Intent(getActivity(), WarningToneActivity.class);
                startActivity(intent);
                break;

            case "network_outage_distance":
                showNetWorkOutageDialog();
                break;

            case "device_reboot":
                if(!isUsbTether()) return false;
                rebootAndScanOpration(null,null,opr_type_reboot);
                break;

            case "synchronize_data":
                if(!isUsbTether()) return false;
                synchronizeDataOpration();
                break;

            case "sd_expand":
                if(!isUsbTether()) return false;
                rebootAndScanOpration(null,null,opr_type_sd_expand);
                break;

            case "firmware_update":
                if(!isUsbTether()) return false;
                Intent intent1 = new Intent(getActivity(),FirmwareUpdateActivity.class);
                startActivity(intent1);
                break;

        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void sdExpandOperation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //showProgressDialog(0, R.string.settings_device_reboot_prompt);
                    Thread.sleep(1000);
                    if(!isUsbTether()) return;
                    buzokuFuction.startExtendFileSystem(myFuctionListener);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Message message = mHandler.obtainMessage();
                    message.what = sdexpand_command_code;
                    message.obj = 1;
                    mHandler.sendMessageDelayed(message, 0);
                }
            }
        }).start();
    }

    private boolean isUsbTether(){
        if (!DtConstant.DEBUG_MODE){
            //Determine whether USB has been connected to the pad
            Intent usbConnect = getActivity().getApplication().registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
            boolean isUSBConnected = usbConnect.getExtras().getBoolean("connected");
            if (!isUSBConnected) {
                mHandler.sendEmptyMessage(usb_not_connected_code);
                return false;
            }

            //Determine whether USB is open for sharing
            if (null != getActivity()) {
                boolean isTethered = APPUtils.isUsbTethered(getActivity().getApplicationContext());
                if (!isTethered) {
                    mHandler.sendEmptyMessage(usb_shared_network_not_open_code);
                    return false;
                }
            }
        }
        return true;
    }



    private void rebootOpration() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //showProgressDialog(0, R.string.settings_device_reboot_prompt);
                    Thread.sleep(1000);
                    if (!DtConstant.DEBUG_MODE){
                        //Determine whether USB has been connected to the pad
                        Intent usbConnect = getActivity().getApplication().registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
                        boolean isUSBConnected = usbConnect.getExtras().getBoolean("connected");
                        if (!isUSBConnected) {
                            mHandler.sendEmptyMessage(usb_not_connected_code);
                            return;
                        }

                        //Determine whether USB is open for sharing
                        if (null != getActivity()) {
                            boolean isTethered = APPUtils.isUsbTethered(getActivity().getApplicationContext());
                            if (!isTethered) {
                                mHandler.sendEmptyMessage(usb_shared_network_not_open_code);
                                return;
                            }
                        }
                    }

                    buzokuFuction.reboot(myFuctionListener);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Message message = mHandler.obtainMessage();
                    message.what = reboot_command_code;
                    message.obj = 1;
                    mHandler.sendMessageDelayed(message, 0);
                }
            }
        }).start();

    }




    private void synchronizeDataOpration() {
        showProgressDialog(0, R.string.start_synchronize_data);
        //String msg = getActivity().getResources().getString(R.string.start_synchronize_data);
        //ToastUtils.showShort(ProfileFragment.this.getActivity(), msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                buzokuFuction.startSynPhoneDataToPi();
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }


    protected void showNetWorkOutageDialog() {
        View edit_avatar_option_view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_network_outage_distance, null);
        TextView cancelBtn = (TextView) edit_avatar_option_view.findViewById(R.id.cancel_btn);
        TextView confrimBtn = (TextView) edit_avatar_option_view.findViewById(R.id.confrim_btn);
        final PickerView pv = (PickerView) edit_avatar_option_view.findViewById(R.id.pv);
        pv.setData(distanceSelectDatas);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        pv.setPosition(selectPosition);

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(edit_avatar_option_view);
        //WindowManager windowManager = getActivity().getWindowManager();
        //Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        //Point outSize = new Point();
        //display.getSize(outSize);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT; // 设置宽度
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        confrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = pv.getPosition();
                String selectStr = distanceSelectDatas.get(selectPosition);
                ((WifiMonitorPreference) findPreference("network_outage_distance")).setMoreInfo(selectStr);
                String subStr = selectStr.substring(0, 2);
                APPUtils.saveValueToSharePreference(ProfileFragment.this.getActivity(), "network_outage_distance", subStr);
                alertDialog.dismiss();
            }
        });
    }

    class MyFuctionListener implements FuctionListener {

        boolean isOn;

        Preference preference;

        int type;//定向扫描，设备重启

        public void setOn(boolean on) {
            isOn = on;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setPreference(Preference preference) {
            this.preference = preference;
        }

        @Override
        public void onPreExecute(int fuctionId) {

        }

        @Override
        public void onPostExecute(int fuctionId, int result) {

            Message message = mHandler.obtainMessage();

            if (BuzokuFuction.FUCTION_SYSTEM_REBOOT == fuctionId) {
                message.what = reboot_command_code;
                message.obj = result;
                mHandler.sendMessageDelayed(message, 0);
            }

            if(BuzokuFuction.FUCTION_EXTEND_FILE_SYS == fuctionId){
                message.what = sdexpand_command_code;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            if (BuzokuFuction.FUCTION_LISTENER_STATE == fuctionId) {
                if (result == 0) {//正在监听扫描中
                    String msg = getActivity().getResources().getString(R.string.settings_orienteer_can_islistener_prompt);
                    message.what = mitm_or_listening;
                    message.obj = msg;
                    mHandler.sendMessageDelayed(message, 0);
                } else {
                    buzokuFuction.queryMitmState(myFuctionListener); //判断是否正在强连
                }
            }

            if (BuzokuFuction.FUCTION_MTIM_STATE == fuctionId) {
                if (result == 0) {
                    String msg = getActivity().getResources().getString(R.string.mitm_isrunning);
                    message.what = mitm_or_listening;
                    message.obj = msg;
                    mHandler.sendMessageDelayed(message, 0);
                } else {
                    if (type == opr_type_orienteer_scan) {
                        String msg;
                        if (isOn) {
                            msg = getActivity().getResources().getString(R.string.settings_orienteer_can_result_open_prompt);
                        } else {
                            msg = getActivity().getResources().getString(R.string.settings_orienteer_can_result_closed_prompt);
                        }
                        switchOpration(preference, isOn, null);
                        message.what = orienteer_scan_switch_result_code;
                        message.obj = msg;
                        mHandler.sendMessageDelayed(message, 0);
                    } else if (type == opr_type_reboot) {
                        rebootOpration();
                    } else if (type == opr_type_sd_expand){
                        sdExpandOperation();
                    }
                }
            }

        }

        @Override
        public void onResult(int fuctionId, List<Object> resultList) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView listView = ((ListView) getView().findViewById(android.R.id.list));
        listView.setDivider(getResources().getDrawable(R.color.fragment_around_background));
        listView.setDividerHeight(20);
    }

    private void exportAllData() {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(getActivity());
        }

        showProgressDialog(0, R.string.export_all_data_prompt);

        //mHandler.removeCallbacks(exportRunnable);

        new Thread(exportRunnable).start();
        //mHandler.postDelayed(exportRunnable,3000);

    }

    Runnable exportRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                //export device list
                Thread.sleep(1000);
                List<Device> deviceList = dataManager.getAllDeviceFromDb();
                dataManager.exportMacDataToFile(deviceList);

                //export dnd list
                List<DndMac> dndMacList = dataManager.getDndMacListFromDb();
                dataManager.exportDndDataToFile(dndMacList);

                //export identity list
                List<Identity> identityList = dataManager.getIdentityListFromDb();
                dataManager.exportIdentityDataToFile(identityList);

                //export white black list
                List<SpecialDevice> specialDeviceList = dataManager.getAllSpecialDeviceListFromDb();
                dataManager.exportWhiteBlackDataToFile(specialDeviceList);

                //export ssid list
                List<SSID> ssidList = dataManager.getAllSsidFromDb();
                dataManager.exportSSIDDataToFile(ssidList);

                //export task list
                List<Task> taskList = dataManager.getTaskListFromDb();
                dataManager.exportTaskDataToFile(taskList);

                //export url list
                List<URL> urlList = dataManager.getUrlListFromDb();
                dataManager.exportUrlDataToFile(urlList);

                //export TopAp list
                List<TopAP> topAPList=dataManager.getAllTopApFromDb();
                dataManager.exportTopApDataToFile(topAPList);

                //export database file
                File src = getActivity().getDatabasePath("db_dtwireless");
                FileUtils.copyFile(src.getAbsolutePath(), Environment.getExternalStorageDirectory
                        () + "/" + DETER_DIR_NAME+"/db_dtwireless");

                mHandler.sendEmptyMessage(export_succeed_code);

            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(export_fail_code);
            }

        }
    };


    private void showProgressDialog(long delayMillis, int resid) {
        mHandler.removeCallbacks(showProgressDialogRunnable);
        if (null == showProgressDialogRunnable) {
            showProgressDialogRunnable = new ProgressDialogRunnable();
        }
        showProgressDialogRunnable.setResId(resid);
        mHandler.postDelayed(showProgressDialogRunnable, delayMillis);
    }


    class ProgressDialogRunnable implements Runnable {
        int resId;

        public void setResId(int resId) {
            this.resId = resId;
        }

        @Override
        public void run() {
            showProgressDialog(resId);
        }
    }


    private void showProgressDialog(int resId) {
        if (null==getActivity()){
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    private void dismissProgressDialog() {
        mHandler.removeCallbacks(showProgressDialogRunnable);
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private void showOprResultPopupWindow(int textResid, int imgResid) {
        mHandler.removeCallbacks(showProgressDialogRunnable);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (null != oprResultPopupWindow) {
            if (oprResultPopupWindow.isShowing()) {
                oprResultPopupWindow.dismiss();
            }
            TextView promptTv = oprResultPopupWindow.getViewById(R.id.ssid_opr_status_tv);
            promptTv.setText(textResid);
            ImageView imageView = oprResultPopupWindow.getViewById(R.id.ssid_opr_status_iv);
            imageView.setImageResource(imgResid);
            oprResultPopupWindow.showAtLocation(appCompatActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            oprResultPopupWindow = DialogUtils.showOprResultPopupWindow(appCompatActivity, textResid, imgResid);
        }
    }


    private void showExportSynchronizeDataDetail(int type) {
        dismissConfirmDialog();
        confirmCancelDialog = DialogUtils.showConfirmDialog(getActivity(), type, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCancelDialog.dismiss();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

}
