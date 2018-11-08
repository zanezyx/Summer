package com.deter.TaxManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.utils.StreamUtils;
import com.deter.TaxManager.view.APShortCutViewHolder;
import com.deter.TaxManager.view.AddSSIDAccountPopupWindow;
import com.deter.TaxManager.view.BaseViewHolder;
import com.deter.TaxManager.view.DevicesMoreDetailRecyclerViewAdapter;
import com.deter.TaxManager.view.StarView;
import com.deter.TaxManager.view.StationDetailViewHolder;
import com.deter.TaxManager.view.StationShortCutViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-9-5.
 */

public class WifiMonitorOpt1 {

    static final int MESSAGE_LISTENER_PREEXECUTE = 1000;

    static final int MESSAGE_LISTENER_POSTEXECUTE = 1001;

    static final int MESSAGE_LISTENER_ONRESULT = 1002;

    static final int MESSAGE_START_MITM = 1003;

    static final int ACTION_TYPE_ADD_WHITELIST = 1;

    static final int ACTION_TYPE_MITM = 2;

    static final int ACTION_TYPE_DEAUTH = 3;

    private static final String TAG = "WifiMonitorOpt";

    private int actionType = -1;

    private Activity context;

    private Device device;

    private RecyclerView.ViewHolder viewHolder;

    private BuzokuFuction buzokuFuction;

    private DataManager dataManager;

    private Dialog dialog;

    private ProgressDialog progressDialog;

    private MyFunctionListener myFunctionListener;

    private boolean isNotJustQuery = false;

    private boolean isRestartToAddDeauth = false;

    private boolean isLocalDevice = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LISTENER_PREEXECUTE:
                    break;
                case MESSAGE_LISTENER_POSTEXECUTE:
                    onPostExecute(msg.arg1, msg.arg2);
                    break;
                case MESSAGE_LISTENER_ONRESULT:
                    break;
                case MESSAGE_START_MITM:
                    startMitmDevice((Boolean) msg.obj);
                    break;
            }
        }
    };

    public WifiMonitorOpt1(Activity context, RecyclerView.ViewHolder viewHolder, Device device,
                           boolean isNotJustQuery) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.device = device;
        this.isNotJustQuery = isNotJustQuery;
        this.buzokuFuction = BuzokuFuction.getInstance(this.context);
        this.dataManager = DataManager.getInstance(this.context);
        this.myFunctionListener = new MyFunctionListener(handler);
        this.isLocalDevice = APPUtils.isLocalMacAddress(context,device.getMac());

    }

    public void onPostExecute(int fuctionId, int result) {
        switch (fuctionId) {
            case BuzokuFuction.FUCTION_DEAUTH_START:
                Log.d(TAG, "FUCTION_DEAUTH_START result" + result + ", device =" + device
                        .getMac());
                if (result != 0) {
                    Toast.makeText(context, R.string.toast_message_remind_start_deauth_fail,
                            Toast.LENGTH_SHORT).show();
                    device.setDeauthState(0);
                    BackgroundService.removeDeviceFromDeauthList(device);
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateViewHolderDeauthState(viewHolder, device.getDeauthState());
                    updateStarView(viewHolder, isNotJustQuery);
                } else {
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateStarView(viewHolder, isNotJustQuery);
                    updateViewHolderDeauthState(viewHolder, 1);
                }
                dissmissProgressDialog();
                break;
            case BuzokuFuction.FUCTION_DEAUTH_RESTART:
                Log.d(TAG, "FUCTION_DEAUTH_RESTART result =" + result);
                if (result != 0) {
                    Toast.makeText(context, R.string.toast_message_remind_restart_deauth_fail,
                            Toast.LENGTH_SHORT).show();
                    BackgroundService.clearDeauthList();
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateRecyclerViewForMitm(viewHolder);
                    updateStarView(viewHolder, isNotJustQuery);
                } else {
                    BackgroundService.updateDeviceStateifNeed(device);
                    BackgroundService.setDeauthListStateToStart();
                    updateStarView(viewHolder, isNotJustQuery);
                    if (isRestartToAddDeauth) {
                        updateViewHolderDeauthState(viewHolder, 1);
                    } else {
                        updateViewHolderDeauthState(viewHolder, 0);
                    }
                    isRestartToAddDeauth = false;
                }
                dissmissProgressDialog();
                break;
            case BuzokuFuction.FUCTION_DEAUTH_STOP:
                Log.d(TAG, "FUCTION_DEAUTH_STOP result" + result + ", device =" + device
                        .getMac());
                if (result != 0) {
                    Toast.makeText(context, R.string.toast_message_remind_sop_deauth_fail,
                            Toast.LENGTH_SHORT).show();
                    device.setDeauthState(1);
                    BackgroundService.addDeviceToDeauthList(device);
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateStarView(viewHolder, isNotJustQuery);
                    updateViewHolderDeauthState(viewHolder, 1);
                } else {
                    device.setDeauthState(0);
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateStarView(viewHolder, isNotJustQuery);
                    updateViewHolderDeauthState(viewHolder, 0);
                }
                dissmissProgressDialog();
                break;
            case BuzokuFuction.FUCTION_MITM_START:
                Log.d(TAG, "FUCTION_MITM_START result =" + result + ", device =" + device.getMac());
                if (result != 0) {
                    Toast.makeText(context, R.string.toast_message_remind_start_mitm_fail,
                            Toast.LENGTH_SHORT).show();
                    device.setMitmState(0);
                    BackgroundService.removeDeviceFromMitmList(device);
                    updateRecyclerViewForMitm(viewHolder);
                    updateStarView(viewHolder, isNotJustQuery);
                } else {
                    updateViewHolderMitmState(viewHolder, 1);
                    BackgroundService.updateDeviceStateifNeed(device);
                    updateStarView(viewHolder, isNotJustQuery);
                }
                dissmissProgressDialog();
                break;
            case BuzokuFuction.FUCTION_MITM_STOP:
                dissmissProgressDialog();
                break;
        }
    }


    public WifiMonitorOpt1 queryOrDeauthDevice() {
        if (isNotJustQuery) {
            actionType = ACTION_TYPE_DEAUTH;
        }
        queryDeauthState();
        return this;
    }

    public WifiMonitorOpt1 queryOrMitmDevice() {
        if (isNotJustQuery) {
            actionType = ACTION_TYPE_MITM;
        }
        queryMitmState();
        return this;
    }

    private void queryDeauthState() {
        updateViewHolderDeauthState(viewHolder, device.getDeauthState());
        if (actionType == ACTION_TYPE_DEAUTH) {
            if (!isLocalDevice) {
                setDeauthDeviceState(device.getDeauthState());
            } else {
                showRemindMessageDialog(R.string.action_not_allow_on_local_device_hint);
            }
        }
    }

    private void setDeauthDeviceState(int currentState) {
        switch (currentState) {
            case 0:
                if (BackgroundService.getMitmList().size() != 0) {
                    showRemindMessageDialog(R.string.dialog_message_remind_stop_mitm);
                    return;
                }
                if (TextUtils.isEmpty(device.getSsid())) {
                    showRemindMessageDialog(R.string.dialog_message_remind_need_ssid);
                    return;
                }
                startDeauthDevice(BackgroundService.getDeauthList().size() > 0);
                break;
            case 1:
                stopDeauthDevice(BackgroundService.getDeauthList().size() > 1);
                break;
            case 2:
                stopDeauthDevice(BackgroundService.getDeauthList().size() > 1);
                break;
        }
    }

    private void startDeauthDevice(boolean isRestart) {
        showProgressDialog(R.string.deauth_progress_hint, context);
        List<Device> deviceList = BackgroundService.addDeviceToDeauthList(device);
        Log.d(TAG, "startDeauthDevice isRestart =" + isRestart + ", deviceList size =" +
                deviceList.size());
        if (isRestart) {
            isRestartToAddDeauth = true;
            buzokuFuction.restartDeauth(myFunctionListener, deviceList,
                    APPUtils.getRssiForCurrentDistance(context), false);
        } else {
            buzokuFuction.startDeauth(myFunctionListener, deviceList, APPUtils
                    .getRssiForCurrentDistance(context), false);
        }
        device.setDeauthState(1);
//        updateStarView(viewHolder, isNotJustQuery);
//        updateViewHolderDeauthState(viewHolder, 1);
    }

    private void stopDeauthDevice(boolean isRestart) {
        showProgressDialog(R.string.deauth_cancel_progress_hint, context);
        device.setDeauthState(0);
        List<Device> deviceList = BackgroundService.removeDeviceFromDeauthList(device);
        if (isRestart) {
            Log.d(TAG, "stopDeauthDevice isRestart =" + isRestart + ",deviceList size =" +
                    deviceList.size());
            isRestartToAddDeauth = false;
            buzokuFuction.restartDeauth(myFunctionListener, deviceList,
                    APPUtils.getRssiForCurrentDistance(context), false);
        } else {
            buzokuFuction.stopDeauth(myFunctionListener);
        }
    }

    private void queryMitmState() {
        updateViewHolderMitmState(viewHolder, device.getMitmState());
        //buzokuFuction.queryMitmState(myFunctionListener);
        if (actionType == ACTION_TYPE_MITM) {
            if (!isLocalDevice) {
                setMitmDeviceState(device.getMitmState());
            } else {
                showRemindMessageDialog(R.string.action_not_allow_on_local_device_hint);
            }
        }
    }

    private void setMitmDeviceState(int currentState) {
        switch (currentState) {
            case 0:
                if (!APPUtils.isNetworkConnected(context)) {
                    showRemindMessageDialog(R.string.action_mitm_no_network_hint);
                    return;
                }
                if (BackgroundService.getDeauthList().size() != 0) {
                    showRemindMessageDialog(R.string.dialog_message_remind_stop_deauth);
                    return;
                } else if (BackgroundService.getMitmList().size() > 0) {
                    showRemindMessageDialog(R.string.dialog_message_remind_stop_mitm_to_mitm);
                    return;
                }
                checkPasswordAndStartMitmDevice();
                break;
            case 1:
                stopMitmDevice();
                break;
            case 2:
                stopMitmDevice();
                break;
        }
    }

    private void checkPasswordAndStartMitmDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasPassword = findDeviceWithPassword(device);
                Message message = handler.obtainMessage(MESSAGE_START_MITM);
                message.obj = hasPassword;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void startMitmDevice(boolean hasPassword) {
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        if (hasPassword) {
            showProgressDialog(R.string.mitm_progress_hint, context);
            buzokuFuction.startMitm(deviceList, device.getSsid(), device.getPassword(), device
                    .getEncryption(), myFunctionListener);
            device.setMitmState(1);
            BackgroundService.addDeviceToMitmList(device);
//            updateViewHolderMitmState(viewHolder, 1);
//            BackgroundService.updateDeviceStateifNeed(device);
//            updateStarView(viewHolder, isNotJustQuery);
        } else {
            showPassWordEditorDialog(viewHolder, deviceList, myFunctionListener, isNotJustQuery);
        }
        //updateRecyclerViewForMitm(viewHolder);
    }

    private void stopMitmDevice() {
        showProgressDialog(R.string.mitm_cancel_progress_hint, context);
        buzokuFuction.stopMitm(myFunctionListener);
    }


    public WifiMonitorOpt1 queryOrSetBlacklistState() {
        boolean isInblacklist = dataManager.queryMacIsInBlacklist(device.getMac());
        if (isNotJustQuery) {
            if(isLocalDevice) {
                showRemindMessageDialog(R.string.action_not_allow_on_local_device_hint);
                return this;
            };
            SpecialDevice oprBlackDevice = new SpecialDevice();
            oprBlackDevice.setId(device.getId());
            oprBlackDevice.setMac(device.getMac());

            if (isInblacklist) {
                dataManager.removeBlackListFromDb(device.getMac());
                device.setInblacklist(false);
                BackgroundService.removeOnlineBlackList(oprBlackDevice);
            } else {
                dataManager.addBlackListToDb(device);
                device.setInblacklist(true);
                device.setInwhitelist(false);
                dataManager.removeWhiteListFromDb(device.getMac());
                BackgroundService.getLastOnlineBlackList().add(oprBlackDevice);
            }
            isInblacklist = dataManager.queryMacIsInBlacklist(device.getMac());
            BackgroundService.updateDeviceStateifNeed(device);
            updateStarView(viewHolder, isNotJustQuery);
        }
        updateViewHolderBlacklistState(viewHolder, isInblacklist);
        if (isInblacklist && isNotJustQuery) {
            updateViewHolderWhitelistState(viewHolder, false);
        }
        return this;
    }


    public WifiMonitorOpt1 queryOrSetWhitelistState() {
        boolean isInWhitelist = dataManager.queryMacIsInWhitelist(device.getMac());
        if (isNotJustQuery) {
            actionType = ACTION_TYPE_ADD_WHITELIST;
            if (isInWhitelist) {
                dataManager.removeWhiteListFromDb(device.getMac());
                device.setInwhitelist(false);
            } else {
                if (device.getDeauthState() != 0) {
                    showRemindMessageDialog(R.string.dialog_message_remind_add_to_whitelist);
                    return this;
                } else if (device.getMitmState() != 0) {
                    showRemindMessageDialog(R.string.dialog_message_remind_add_to_whitelist);
                    return this;
                }
                dataManager.addWhiteListToDb(device);
                dataManager.removeBlackListFromDb(device.getMac());
                device.setInblacklist(false);
                device.setInwhitelist(true);
            }
            isInWhitelist = dataManager.queryMacIsInWhitelist(device.getMac());
            BackgroundService.updateDeviceStateifNeed(device);
            updateStarView(viewHolder, isNotJustQuery);
        }
        updateViewHolderWhitelistState(viewHolder, isInWhitelist);
        if (isInWhitelist && isNotJustQuery) {
            updateViewHolderBlacklistState(viewHolder, false);
            if (context instanceof DeviceDetailActivity) {
                context.finish();
            }
        }
        return this;
    }


    private void updateRecyclerViewForMitm(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof StationDetailViewHolder) {
            Log.d(TAG,"updateRecyclerViewForMitm");
            DevicesMoreDetailRecyclerViewAdapter devicesMoreDetailRecyclerViewAdapter =
                    (DevicesMoreDetailRecyclerViewAdapter) viewHolder.itemView.getTag(R.id
                            .tag_objected);
            if (devicesMoreDetailRecyclerViewAdapter != null) {
                devicesMoreDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateViewHolderDeauthState(RecyclerView.ViewHolder viewHolder, int state) {
        if (viewHolder instanceof BaseViewHolder) {
            switch (state) {
                case 0:
                    ((BaseViewHolder) viewHolder).deauthIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).deauthIndicator.setImageResource(R.drawable
                            .action_wifi_connected);
                    ((BaseViewHolder) viewHolder).deauthTitle.setText(R.string
                            .detail_action_force_disconnected);
                    ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;
                case 1:
                    ((BaseViewHolder) viewHolder).deauthIndicator.setState(1);
                    ((BaseViewHolder) viewHolder).deauthTitle.setText(R.string
                            .deauth_progress_hint);
                    ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;
                case 2:
                    ((BaseViewHolder) viewHolder).deauthIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).deauthIndicator.setImageResource(R.drawable
                            .action_force_disconnected);
                    ((BaseViewHolder) viewHolder).deauthTitle.setText(R.string
                            .deauth_progress_success);
                    ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                            .getColor(R.color.tabs_selected));
                    break;
                case 3:
                    ((BaseViewHolder) viewHolder).deauthIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).deauthIndicator.setImageResource(R.drawable
                            .action_wifi_connected);
                    ((BaseViewHolder) viewHolder).deauthTitle.setText(R.string
                            .detail_action_force_disconnected);
                    ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;
            }
        }

    }

    private void updateStarView(RecyclerView.ViewHolder viewHolder, boolean isNotJustQuery) {
        if (isNotJustQuery && (viewHolder instanceof APShortCutViewHolder || viewHolder
                instanceof StationShortCutViewHolder)) {
            StarView starView = (StarView) viewHolder.itemView.getTag(R.id.tag_view);
            if (starView != null) {
                //Log.d(TAG, Log.getStackTraceString(new Throwable()));xiaolu123

                Log.d(TAG, "updataStarView deauth size =" + dataManager.getCurrDeauthDevice
                        (device.getTaskId()).size());
                starView.updatDeviceState();
            }
        }
    }

    private void updateViewHolderMitmState(RecyclerView.ViewHolder viewHolder, int state) {
        if (viewHolder instanceof BaseViewHolder) {
            switch (state) {
                case 0:
                    ((BaseViewHolder) viewHolder).mitmIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).mitmIndicator.setImageResource(R.drawable
                            .action_wifi_connected);
                    ((BaseViewHolder) viewHolder).mitmTitle.setText(R.string
                            .detail_action_force_connected);
                    ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;
                case 1:
                    ((BaseViewHolder) viewHolder).mitmIndicator.setState(1);
                    ((BaseViewHolder) viewHolder).mitmTitle.setText(R.string.mitm_progress_hint);
                    ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;
                case 2:
                    ((BaseViewHolder) viewHolder).mitmIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).mitmIndicator.setImageResource(R.drawable
                            .action_force_connected);
                    ((BaseViewHolder) viewHolder).mitmTitle.setText(R.string
                            .mitm_progress_success);
                    ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                            .getColor(R.color.tabs_selected));
                    break;
                case 3:
                    ((BaseViewHolder) viewHolder).mitmIndicator.setState(0);
                    ((BaseViewHolder) viewHolder).mitmIndicator.setImageResource(R.drawable
                            .action_wifi_connected);
                    ((BaseViewHolder) viewHolder).mitmTitle.setText(R.string
                            .detail_action_force_connected);
                    ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                            .getColor(android.R.color.tertiary_text_dark));
                    break;

            }
        }


    }

    private void updateViewHolderBlacklistState(RecyclerView.ViewHolder viewHolder, boolean
            isInblacklist) {
        if (viewHolder instanceof BaseViewHolder) {
            if (isInblacklist) {
                ((BaseViewHolder) viewHolder).blacklistIndicator.setImageResource(R.drawable
                        .action_blacklist_added);
                ((BaseViewHolder) viewHolder).blacklistTitle.setTextColor(context.getResources()
                        .getColor(R.color.tabs_selected));
            } else {
                ((BaseViewHolder) viewHolder).blacklistIndicator.setImageResource(R.drawable
                        .action_blacklist_unadded);
                ((BaseViewHolder) viewHolder).blacklistTitle.setTextColor(context.getResources()
                        .getColor(android.R.color.tertiary_text_dark));
            }
        }

    }

    private void updateViewHolderWhitelistState(RecyclerView.ViewHolder viewHolder, boolean
            isInWhitelist) {
        if (viewHolder instanceof BaseViewHolder) {
            if (isInWhitelist) {
                ((BaseViewHolder) viewHolder).whitelistIndicator.setImageResource(R.drawable
                        .action_whitelist_added);
                ((BaseViewHolder) viewHolder).whitelistTitle.setTextColor(context.getResources()
                        .getColor(R.color.tabs_selected));
            } else {
                ((BaseViewHolder) viewHolder).whitelistIndicator.setImageResource(R.drawable
                        .action_whitelist_unadded);
                ((BaseViewHolder) viewHolder).whitelistTitle.setTextColor(context.getResources()
                        .getColor(android.R.color.tertiary_text_dark));
            }
        }

    }

    private void showPassWordEditorDialog(final RecyclerView.ViewHolder viewHolder, final
    List<Device> deviceList, final FuctionListener fuctionListener, final boolean isNotJustQuery) {
        AddSSIDAccountPopupWindow addSSIDAccountPopupWindow = new AddSSIDAccountPopupWindow
                (context, new AddSSIDAccountPopupWindow.OnConfirmCallback() {
                    @Override
                    public void onConfirm(String ssid, String password) {
                        showProgressDialog(R.string.mitm_progress_hint, context);
                        String encryption = TextUtils.isEmpty(password) ? "OPEN" : "WPA2";
                        buzokuFuction.startMitm(deviceList, ssid, password, encryption, fuctionListener);
                        device.setMitmState(1);
                        BackgroundService.addDeviceToMitmList(device);
//                        BackgroundService.updateDeviceStateifNeed(device);
//                        updateStarView(viewHolder, isNotJustQuery);
//                        updateViewHolderMitmState(viewHolder, 1);
                    }
                });
        addSSIDAccountPopupWindow.setSSID(deviceList.get(0).getSsid());
        addSSIDAccountPopupWindow.showAtLocation((View) (((View) viewHolder.itemView.getTag(R.id.tag_view))).getParent(), Gravity.CENTER, 0, 0);


    }

    private boolean findDeviceWithPassword(Device device) {
        boolean isFind = false;
        if (TextUtils.isEmpty(device.getSsid())) {
            return false;
        }

        if (device.getEncryption().equalsIgnoreCase("open")) {
            return true;
        }

        StreamUtils<SSID> streamUtils = new StreamUtils<>();

        //Look for the password in the configuration file
        boolean isCopySuccess = streamUtils.copyWifiConfigFromSdCard(APPConstans.CONFIG_PATH);
        //Copy the files to the specified directory
        if (isCopySuccess) {
            List<SSID> configSsids = streamUtils.readWifiConfigFromSdCard(APPConstans
                    .CONFIG_PATH, APPConstans.WIFI_CONFIG_FILE_NAME);//Read the data for the
            // configuration file
            isFind = findSSIDDeviceFromList(device, configSsids);
        }

        //Find from the SSID library
        if (!isFind) {
            List<SSID> localSsidList = DataManager.getInstance(context).getSsidFromDbBySsid
                    (device.getSsid());
            isFind = findSSIDDeviceFromList(device, localSsidList);
        }

        //Find from the TopApList library
        if (!isFind) {
            List<TopAP> topAPList = DataManager.getInstance(context).getTopApFromDbBySsid(device
                    .getSsid());
            isFind = findTopApDeviceFromList(device, topAPList);
        }

        return isFind;
    }

    private boolean findSSIDDeviceFromList(Device device, List<SSID> ssids) {
        for (SSID ssid : ssids) {
            if (null != ssid && null != ssid.getSsid()) {
                if (ssid.getSsid().equals(device.getSsid())) {
                    device.setPassword(ssid.getPassword());
                    device.setEncryption(ssid.getEncryption());
                    return true;
                }
            }
        }

        return false;
    }

    private boolean findTopApDeviceFromList(Device device, List<TopAP> topAPList) {
        for (TopAP topAP : topAPList) {
            if (null != topAP && null != topAP.getSsid()) {
                if (topAP.getSsid().equals(device.getSsid())) {
                    device.setPassword(topAP.getPassword());
                    device.setEncryption(topAP.getEncryption());
                    return true;
                }
            }
        }

        return false;
    }

    private void showRemindMessageDialog(int resId) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = DialogUtils.showConfirmDialog(context, context.getString(resId), new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    private void showProgressDialog(int resId, Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && context != null && !context
                .isFinishing() && !context.isDestroyed()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
