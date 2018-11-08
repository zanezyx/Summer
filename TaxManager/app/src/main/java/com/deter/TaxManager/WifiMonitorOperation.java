package com.deter.TaxManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.StreamUtils;
import com.deter.TaxManager.view.APShortCutViewHolder;
import com.deter.TaxManager.view.AddSSIDAccountPopupWindow;
import com.deter.TaxManager.view.BaseViewHolder;
import com.deter.TaxManager.view.DevicesMoreDetailRecyclerViewAdapter;
import com.deter.TaxManager.view.StarView;
import com.deter.TaxManager.view.StationDetailViewHolder;
import com.deter.TaxManager.view.StationShortCutViewHolder;

import java.util.List;

/**
 * Created by xiaolu on 17-8-8.
 */

public class WifiMonitorOperation {

    private static final String TAG = "WifiMonitorOperation";

    private static WifiMonitorOperation instance;

    private BuzokuFuction buzokuFuction;

    private DataManager dataManager;

    private Context context;

    private boolean setWhitelist = false;

    private ProgressDialog dialog;

    private WifiMonitorOperation(Context context) {
        this.context = context;
        buzokuFuction = BuzokuFuction.getInstance(context);
        dataManager = DataManager.getInstance(context);
    }

    public static WifiMonitorOperation getInstance(Context context) {
        if (instance == null) {
            synchronized (WifiMonitorOperation.class) {
                if (instance == null) {
                    instance = new WifiMonitorOperation(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void queryOrDeauthDevice(Device device, RecyclerView.ViewHolder viewHolder, boolean
            isNotJustQuery) {
        buzokuFuction.queryDeauthState(new OperationFuctionListener(viewHolder, device,
                isNotJustQuery));
    }


    public void queryOrMitmDevice(Device device, RecyclerView.ViewHolder viewHolder, boolean
            isNotJustQuery) {
        buzokuFuction.queryMitmState(new OperationFuctionListener(viewHolder, device,
                isNotJustQuery));
    }


    public void queryOrSetBlacklistState(Device device, RecyclerView.ViewHolder viewHolder,
                                         boolean isNotJustQuery) {
        boolean isInblacklist = dataManager.queryMacIsInBlacklist(device.getMac());
        if (isNotJustQuery) {
            if (isInblacklist) {
                dataManager.removeBlackListFromDb(device.getMac());
            } else {
                dataManager.addBlackListToDb(device);
                dataManager.removeWhiteListFromDb(device.getMac());
            }
            isInblacklist = dataManager.queryMacIsInBlacklist(device.getMac());
        }
        updateViewHolderBlacklistState(viewHolder, isInblacklist, isNotJustQuery);
        updateStarView(viewHolder, isNotJustQuery);
        if (isInblacklist && isNotJustQuery) {
            updateViewHolderWhitelistState(viewHolder, !isInblacklist, isNotJustQuery);
        }
    }


    public void queryOrSetWhitelistState(Device device, RecyclerView.ViewHolder viewHolder,
                                         boolean isNotJustQuery) {
        boolean isInWhitelist = dataManager.queryMacIsInWhitelist(device.getMac());
        if (isNotJustQuery) {
            if (isInWhitelist) {
                dataManager.removeWhiteListFromDb(device.getMac());
            } else {
                dataManager.addWhiteListToDb(device);
                dataManager.removeBlackListFromDb(device.getMac());
                setWhitelist = true;// flag for set whitelist then check the deauth or mitm state
                queryOrDeauthDevice(device, viewHolder, isNotJustQuery);//check the device
                // wheather in deauth or mitm state if in,set to out

            }
            isInWhitelist = dataManager.queryMacIsInWhitelist(device.getMac());
        }
        updateViewHolderWhitelistState(viewHolder, isInWhitelist, isNotJustQuery);
        if (isInWhitelist && isNotJustQuery) {
            updateViewHolderBlacklistState(viewHolder, !isInWhitelist, isNotJustQuery);
        }
    }

    private void updateRecyclerViewForMitm(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof StationDetailViewHolder) {
            DevicesMoreDetailRecyclerViewAdapter devicesMoreDetailRecyclerViewAdapter =
                    (DevicesMoreDetailRecyclerViewAdapter) viewHolder.itemView.getTag(R.id
                            .tag_objected);
            //int position = (int) viewHolder.itemView.getTag(R.id.tag_position);
            if (devicesMoreDetailRecyclerViewAdapter != null) {
                //BackgroundService.updateMitmDeviceState(devicesMoreDetailRecyclerViewAdapter
                //        .getDevices());
                //devicesMoreDetailRecyclerViewAdapter.notifyItemChanged(position,position);
                devicesMoreDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateViewHolderDeauthState(RecyclerView.ViewHolder viewHolder, int result,
                                             boolean isNotJustQuery) {
        if (viewHolder instanceof BaseViewHolder) {
            if (result == 0) {
                ((BaseViewHolder) viewHolder).deauthIndicator.setImageResource(R.drawable
                        .action_force_disconnected);
                ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                        .getColor(R.color.tabs_selected));
            } else {
                ((BaseViewHolder) viewHolder).deauthIndicator.setImageResource(R.drawable
                        .action_wifi_connected);
                ((BaseViewHolder) viewHolder).deauthTitle.setTextColor(context.getResources()
                        .getColor(android.R.color.tertiary_text_dark));
            }
            //updateStarView(viewHolder, isNotJustQuery);

        }

    }

    private void updateStarView(RecyclerView.ViewHolder viewHolder, boolean isNotJustQuery) {
        if (isNotJustQuery && (viewHolder instanceof APShortCutViewHolder || viewHolder
                instanceof StationShortCutViewHolder)) {
            StarView starView = (StarView) viewHolder.itemView.getTag(R.id.tag_view);
            if (starView != null) {
                //Log.d(TAG, Log.getStackTraceString(new Throwable()));
                starView.updatDeviceState();
            }
        }
    }

    private void updateViewHolderMitmState(RecyclerView.ViewHolder viewHolder, int result,
                                           boolean isNotJustQuery) {
        if (viewHolder instanceof BaseViewHolder) {
            if (result == 0) {
                ((BaseViewHolder) viewHolder).mitmIndicator.setImageResource(R.drawable
                        .action_force_connected);
                ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                        .getColor(R.color.tabs_selected));
            } else {
                ((BaseViewHolder) viewHolder).mitmIndicator.setImageResource(R.drawable
                        .action_wifi_connected);
                ((BaseViewHolder) viewHolder).mitmTitle.setTextColor(context.getResources()
                        .getColor(android.R.color.tertiary_text_dark));
            }
            //updateStarView(viewHolder, isNotJustQuery);
        }


    }

    public void updateViewHolderBlacklistState(RecyclerView.ViewHolder viewHolder, boolean
            isInblacklist, boolean isNotJustQuery) {
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
            //updateStarView(viewHolder, isNotJustQuery);
        }

    }

    public void updateViewHolderWhitelistState(RecyclerView.ViewHolder viewHolder, boolean
            isInWhitelist, boolean isNotJustQuery) {
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
            //updateStarView(viewHolder, isNotJustQuery);
        }

    }

    private void showPassWordEditorDialog(final RecyclerView.ViewHolder viewHolder, final
    List<Device> deviceList, final FuctionListener fuctionListener, final boolean isNotJustQuery) {
        AddSSIDAccountPopupWindow addSSIDAccountPopupWindow = new AddSSIDAccountPopupWindow
                (context, new AddSSIDAccountPopupWindow.OnConfirmCallback() {
                    @Override
                    public void onConfirm(String ssid, String password) {

                        String encryption = TextUtils.isEmpty(password) ? "OPEN" : "WPA2";
                        buzokuFuction.startMitm(deviceList, ssid, password, encryption,
                                fuctionListener);
                        if (isNotJustQuery && !setWhitelist) {
                            showProgressDialog(R.string.mitm_progress_hint,
                                    ((View) viewHolder.itemView.getTag(R.id
                                            .tag_view)).getContext());
                        }
                    }
                });
        addSSIDAccountPopupWindow.setSSID(deviceList.get(0).getSsid());
        addSSIDAccountPopupWindow.showAtLocation((View) (((View) viewHolder.itemView.getTag(R.id
                .tag_view))).getParent(), Gravity.CENTER, 0, 0);


    }

    public boolean findDeviceWithPassword(Device device) {
        boolean isFind;
        if (TextUtils.isEmpty(device.getSsid())) {
            return false;
        }

        if (device.getEncryption().equalsIgnoreCase("open")) {
            device.setEncryption("OPEN");//encryption
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
            isFind = findDeviceFromList(device, configSsids);
            if (isFind) {
                return isFind;
            }
        }

        List<SSID> localSsidList = dataManager.getSsidFromDbBySsid(device.getSsid());
        isFind = findDeviceFromList(device, localSsidList);

        return isFind;
    }

    private boolean findDeviceFromList(Device device, List<SSID> ssids) {
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

    private class OperationFuctionListener implements FuctionListener {

        private RecyclerView.ViewHolder viewHolder;

        private Device device;

        private boolean isNotJustQuery;

        public OperationFuctionListener(RecyclerView.ViewHolder viewHolder, Device device,
                                        boolean isNotJustQuery) {
            this.viewHolder = viewHolder;
            this.device = device;
            this.isNotJustQuery = isNotJustQuery;
        }

        @Override
        public void onPreExecute(int fuctionId) {
            switch (fuctionId) {
                case BuzokuFuction.FUCTION_SYSTEM_STATE:
                    break;
                case BuzokuFuction.FUCTION_LISTENER_STATE:
                    break;
                case BuzokuFuction.FUCTION_LISTENER_START:
                    break;
                case BuzokuFuction.FUCTION_DEAUTH_STOP:
                    break;
            }
        }

        @Override
        public void onPostExecute(int fuctionId, int result) {
            switch (fuctionId) {
                case BuzokuFuction.FUCTION_LISTENER_STATE:
                    break;
                case BuzokuFuction.FUCTION_LISTENER_START:
                    break;
//                case BuzokuFuction.FUCTION_DEAUTH_STATE:
//                    List<Device> deauthlist = BackgroundService.getDeauthList();
//                    int isInDeauthList = BackgroundService.isInDeatuthList(device);// 0 inlist ;
//                    // 1 notinlist
//                    Log.d(TAG, "FUCTION_DEAUTH_STATE result" + result + ", setWhitelist =" +
//                            setWhitelist + ", isInDeauthList =" + isInDeauthList + ", " +
//                            "isNotJustQuery =" + isNotJustQuery);
//                    updateViewHolderDeauthState(viewHolder, isInDeauthList, isNotJustQuery);
//                    if (isNotJustQuery) {
//                        if (result == 0) {//some devices running deauth
//                            if (isInDeauthList == 0) {
//                                if (!setWhitelist) {
//                                    showProgressDialog(R.string
//                                            .deauth_cancel_progress_hint, viewHolder.itemView
//                                            .getContext());
//                                }
//                                if (deauthlist.size() == 1) {
//                                    Log.d(TAG, "FUCTION_DEAUTH_STATE .... STOPDEAUTH");
//                                    buzokuFuction.stopDeauth(this);
//                                } else {
//                                    Log.d(TAG, "FUCTION_DEAUTH_STATE .... RESTART 111");
//                                    buzokuFuction.restartDeauth(this, BackgroundService
//                                            .getTmpDeauthList(device, false), APPUtils
//                                            .getRssiForCurrentDistance(context), false);
//                                }
//
//                            } else if (!setWhitelist) {
//                                Log.d(TAG, "FUCTION_DEAUTH_STATE .... RESTART 222");
//                                showProgressDialog(R.string.deauth_progress_hint,
//                                        viewHolder.itemView
//                                                .getContext());
//                                buzokuFuction.restartDeauth(this, BackgroundService
//                                        .getTmpDeauthList(device, true), APPUtils
//                                        .getRssiForCurrentDistance(context), false);
//
//                            } else {
//                                queryOrMitmDevice(device, viewHolder, isNotJustQuery);
//                            }
//                        } else if (!setWhitelist) {
//                            showProgressDialog(R.string.deauth_progress_hint, viewHolder
//                                    .itemView
//                                    .getContext());
//                            List<Device> deviceList = new ArrayList<>();
//                            deviceList.add(device);
//                            Log.d(TAG, "FUCTION_DEAUTH_STATE .... STARTDEAUTH" + deviceList.size());
//                            buzokuFuction.startDeauth(this, deviceList, APPUtils
//                                    .getRssiForCurrentDistance(context), false);
//                        } else {
//                            Log.d(TAG, " FUCTION_DEAUTH_STATE ----queryOrMitmDevice");
//                            queryOrMitmDevice(device, viewHolder, isNotJustQuery);
//                        }
//                    }
//                    break;
//                case BuzokuFuction.FUCTION_DEAUTH_START:
//                    Log.d(TAG, "FUCTION_DEAUTH_START result" + result + ", device =" + device
//                            .getMac());
//                    if (result == 0) {
//                        BackgroundService.addDeauthList(device);
//                        BackgroundService.removeDeviceFromMitmList(device);
//                    }
//                    updateViewHolderDeauthState(viewHolder, result, isNotJustQuery);
//                    if (!setWhitelist) {
//                        updateStarView(viewHolder, isNotJustQuery);
//                        dissmissProgressDialog();
//                        if (result != 0) {
//                            Toast.makeText(context, R.string.deauth_fail, Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }
//                    updateRecyclerViewForMitm(viewHolder);
//                    break;
//                case BuzokuFuction.FUCTION_DEAUTH_RESTART:
//                    Log.d(TAG, "FUCTION_DEAUTH_RESTART .... result =" + result);
//                    if (result == 0) {
//                        if (BackgroundService.isInDeatuthList(device) == 0) {
//                            BackgroundService.removeDeviceFromDeauthList(device);
//                        } else {
//                            BackgroundService.addDeauthList(device);
//                            BackgroundService.removeDeviceFromMitmList(device);
//                        }
//                    }
//                    updateViewHolderDeauthState(viewHolder, BackgroundService.isInDeatuthList
//                            (device), isNotJustQuery);
//                    if (!setWhitelist) {
//                        updateStarView(viewHolder, isNotJustQuery);
//                        dissmissProgressDialog();
//                        if (result != 0) {
//                            if (BackgroundService.isInDeatuthList(device) == 0) {
//                                Toast.makeText(context, R.string.deauth_cancel_fail, Toast
//                                        .LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, R.string.deauth_fail, Toast.LENGTH_SHORT)
//                                        .show();
//                            }
//
//                        }
//                    } else {
//                        queryOrMitmDevice(device, viewHolder, isNotJustQuery);
//                    }
//                    updateRecyclerViewForMitm(viewHolder);
//                    break;
//                case BuzokuFuction.FUCTION_DEAUTH_STOP:
//                    Log.d(TAG, "FUCTION_DEAUTH_STOP result" + result + ", device =" + device
//                            .getMac());
//                    if (result == 0) {
//                        BackgroundService.removeDeviceFromDeauthList(device);
//                    }
//                    updateViewHolderDeauthState(viewHolder, result == 1 ? 0 : 1, isNotJustQuery);
//                    if (!setWhitelist) {
//                        updateStarView(viewHolder, isNotJustQuery);
//                        dissmissProgressDialog();
//                        if (result != 0) {
//                            Toast.makeText(context, R.string.deauth_cancel_fail, Toast
//                                    .LENGTH_SHORT).show();
//                        }
//                    } else {
//                        queryOrMitmDevice(device, viewHolder, isNotJustQuery);
//                    }
//                    break;
//
//                case BuzokuFuction.FUCTION_MTIM_STATE:
//                    Log.d(TAG, "FUCTION_MTIM_STATE result" + result + ", device =" + device
//                            .getMac());
//                    int inMitmlist = BackgroundService.isInMitmList(device);
//                    updateViewHolderMitmState(viewHolder, inMitmlist, isNotJustQuery);
//                    if (isNotJustQuery) {
//
//                        if (inMitmlist == 0) {
//                            buzokuFuction.stopMitm(this);
//                            if (isNotJustQuery && !setWhitelist) {
//                                showProgressDialog(R.string.mitm_cancel_progress_hint,
//                                        viewHolder.itemView.getContext());
//                            }
//                        } else if (!setWhitelist) {
//                            List<Device> deviceList = new ArrayList<>();
//                            deviceList.add(device);
//                            boolean hasPassword = findDeviceWithPassword(device);
//                            if (hasPassword) {
//                                buzokuFuction.startMitm(deviceList, device.getSsid(), device
//                                        .getPassword(), device.getEncryption(), this);
//                                if (isNotJustQuery && !setWhitelist) {
//                                    showProgressDialog(R.string.mitm_progress_hint,
//                                            viewHolder.itemView.getContext());
//                                }
//                            } else {
//                                showPassWordEditorDialog(viewHolder, deviceList, this,
//                                        isNotJustQuery);
//                            }
//
//                        } else {
//                            updateStarView(viewHolder, isNotJustQuery);
//                            setWhitelist = false;
//                        }
//                    }
//                    break;
                case BuzokuFuction.FUCTION_MITM_START:
                    Log.d(TAG, "FUCTION_MITM_START result" + result + ", device =" + device
                            .getMac());
                    if (result == 0) {
                        //BackgroundService.addMitmList(device);
                        BackgroundService.removeDeviceFromDeauthList(device);
                    }
                    updateStarView(viewHolder, isNotJustQuery);
                    dissmissProgressDialog();
                    if (result != 0) {
                        Toast.makeText(context, R.string.mitm_fail, Toast.LENGTH_SHORT).show();
                    }
                    updateRecyclerViewForMitm(viewHolder);
                    break;
                case BuzokuFuction.FUCTION_MITM_STOP:
                    Log.d(TAG, "FUCTION_MITM_STOP result" + result + ", device =" + device.getMac
                            ());
                    if (result == 0) {
                        BackgroundService.removeDeviceFromMitmList(device);
                    }
                    updateStarView(viewHolder, isNotJustQuery);
                    dissmissProgressDialog();
                    if (result != 0) {
                        Toast.makeText(context, R.string.mitm_cancel_fail, Toast.LENGTH_SHORT)
                                .show();
                    }
                    updateRecyclerViewForMitm(viewHolder);
                    setWhitelist = false;
                    break;
            }
        }

        @Override
        public void onResult(int fuctionId, List<Object> resultList) {
            switch (fuctionId) {
                case BuzokuFuction.FUCTION_LISTENER_STATE:
                    break;
                case BuzokuFuction.FUCTION_LISTENER_START:
                    break;
                case BuzokuFuction.FUCTION_DEAUTH_STOP:
                    break;
            }

        }
    }

}