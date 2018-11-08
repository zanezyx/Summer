package com.deter.TaxManager.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.BackgroundService;
import com.deter.TaxManager.DeviceDetailActivity;
import com.deter.TaxManager.R;
import com.deter.TaxManager.VirtualIdActivity;
import com.deter.TaxManager.WifiMonitorOpt1;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.TimeUtils;

import java.util.List;

import static com.deter.TaxManager.utils.APPUtils.hasVirtualId;

/**
 * Created by xiaolu on 17-8-9.
 */

public class DevicesMoreDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView
        .ViewHolder> {

    private static final String TAG = "DevicesMoreDetail";

    private List<Device> devices;

    private LayoutInflater layoutInflater;

    private Activity context;


    public DevicesMoreDetailRecyclerViewAdapter(Activity context, List<Device> devices) {
        this.devices = devices;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "devices =" + devices.size());
    }

    public List<Device> getDevices() {
        return devices;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (APPConstans.RECYCLE_TYPE_DETAIL_AP == viewType) {
            view = layoutInflater.inflate(R.layout.layout_devices_ap_more_details, parent, false);
            return new APDetailViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.layout_devices_more_details, parent, false);
            return new StationDetailViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case APPConstans.RECYCLE_TYPE_DETAIL_AP:
                bindAPView((APDetailViewHolder) holder, devices.get(position), position);
                break;
            case APPConstans.RECYCLE_TYPE_DETAIL_STATION:
                bindStationView((StationDetailViewHolder) holder, devices.get(position), position);
                break;
            case APPConstans.RECYCLE_TYPE_DETAIL_STATION_MITM:
                bindStationViewWithMITM((StationDetailViewHolder) holder, devices.get(position),
                        position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Device device = devices.get(position);
        if (device.getRole().equalsIgnoreCase("ap")) {
            return APPConstans.RECYCLE_TYPE_DETAIL_AP;
        } else {
            if (device.getMitmState() != 0) {
                return APPConstans.RECYCLE_TYPE_DETAIL_STATION_MITM;
            } else {
                return APPConstans.RECYCLE_TYPE_DETAIL_STATION;
            }
        }
    }

    @Override
    public int getItemCount() {
        return devices != null ? devices.size() : 0;
    }

    private void bindAPView(final APDetailViewHolder apMoreDetailViewHolder, Device device, int
            position) {

        apMoreDetailViewHolder.apName.setText(TextUtils.isEmpty(device.getSsid()) ? "" :
                device.getSsid());
        apMoreDetailViewHolder.apMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        apMoreDetailViewHolder.date.setText(TimeUtils.getTime(device.getTime()));
        apMoreDetailViewHolder.distance.setText(String.format("%.2f", device.getDistance()));
        apMoreDetailViewHolder.channel.setText(String.valueOf(device.getChannel()));
        BackgroundService.updateStationNumberView(device, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                apMoreDetailViewHolder.stationNumber.setText(String.valueOf(msg.arg1));
                apMoreDetailViewHolder.stationNumber.setTag(msg.arg1);
            }
        });

        ViewOnClickListener viewOnClickListener = new ViewOnClickListener(apMoreDetailViewHolder,
                device, position);
        apMoreDetailViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        apMoreDetailViewHolder.stationInfo.setOnClickListener(viewOnClickListener);

        WifiMonitorOpt1 wifiMonitorOpt = new WifiMonitorOpt1(context, apMoreDetailViewHolder,
                device, false);
        wifiMonitorOpt.queryOrDeauthDevice();
    }

    private void bindStationView(StationDetailViewHolder stationDetailViewHolder, Device device,
                                 int position) {

        stationDetailViewHolder.apInfo.setVisibility(View.GONE);
        boolean isSelfMac = APPUtils.isLocalMacAddress(context,device.getMac());
        String stationName = context.getString(R.string.around_station_name, device.getVender(),
                isSelfMac ? context.getString(R.string.around_station_local) : "");
        stationDetailViewHolder.stationName.setText(Html.fromHtml(stationName));
        stationDetailViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device.getMac
                ()));
        stationDetailViewHolder.date.setText(TimeUtils.getTime(device.getTime()));
        stationDetailViewHolder.ssidName.setText(TextUtils.isEmpty(device.getSsid()) ? "" :
                device.getSsid());
        stationDetailViewHolder.encryptionType.setText(device.getEncryption());
        stationDetailViewHolder.distance.setText(String.format("%.2f", device.getDistance()));

        ViewOnClickListener viewOnClickListener = new ViewOnClickListener
                (stationDetailViewHolder, device, position);
        stationDetailViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.mitmInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.blacklistInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.whitelistInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.virtualIdInfo.setOnClickListener(viewOnClickListener);
        updateVirtaulIdState(stationDetailViewHolder, device);

        WifiMonitorOpt1 wifiMonitorOpt = new WifiMonitorOpt1(context, stationDetailViewHolder,
                device, false);
        wifiMonitorOpt.queryOrSetBlacklistState().queryOrSetWhitelistState().queryOrDeauthDevice
                ().queryOrMitmDevice();
    }

    private void bindStationViewWithMITM(StationDetailViewHolder stationDetailViewHolder, Device
            device, int position) {

        stationDetailViewHolder.stationName.setText(device.getVender());
        stationDetailViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device.getMac
                ()));
        stationDetailViewHolder.date.setText(TimeUtils.getTime(device.getTime()));
        stationDetailViewHolder.ssidName.setText(TextUtils.isEmpty(device.getSsid()) ? "" :
                device.getSsid());
        stationDetailViewHolder.encryptionType.setText(device.getEncryption());
        stationDetailViewHolder.distance.setText(String.format("%.2f", device.getDistance()));
        stationDetailViewHolder.mitm_name.setText(TextUtils.isEmpty(device.getIpInfoName()) ?
                "" : device.getIpInfoName());
        stationDetailViewHolder.station_ip.setText(TextUtils.isEmpty(device.getIpaddr()) ?
                "" : device.getIpaddr());

        ViewOnClickListener viewOnClickListener = new ViewOnClickListener
                (stationDetailViewHolder, device, position);
        stationDetailViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.mitmInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.blacklistInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.whitelistInfo.setOnClickListener(viewOnClickListener);
        stationDetailViewHolder.virtualIdInfo.setOnClickListener(viewOnClickListener);
        updateVirtaulIdState(stationDetailViewHolder, device);

        WifiMonitorOpt1 wifiMonitorOpt = new WifiMonitorOpt1(context, stationDetailViewHolder,
                device, false);
        wifiMonitorOpt.queryOrSetBlacklistState().queryOrSetWhitelistState().queryOrDeauthDevice
                ().queryOrMitmDevice();
    }

    private void updateVirtaulIdState(StationDetailViewHolder viewHolder, Device device) {
        List<Identity> identityList = DataManager.getInstance(context).queryIdentity(device
                .getMac());
        if (identityList != null && hasVirtualId(identityList)) {
            viewHolder.virtualIdIndicator.setImageResource(R.drawable.action_virtual_id);
            viewHolder.virtaulIdTitle.setTextColor(context.getResources().getColor(R.color
                    .tabs_selected));
        } else {
            viewHolder.virtualIdIndicator.setImageResource(R.drawable.action_virtual_id_none);
            viewHolder.virtaulIdTitle.setTextColor(context.getResources().getColor(android.R
                    .color.tertiary_text_dark));
        }
    }


    class ViewOnClickListener implements View.OnClickListener {

        private RecyclerView.ViewHolder viewHolder;

        private Device device;

        private int position;

        private WifiMonitorOpt1 wifiMonitorOpt;

        public ViewOnClickListener(RecyclerView.ViewHolder viewHolder, Device device, int
                position) {
            this.viewHolder = viewHolder;
            this.device = device;
            this.position = position;
            wifiMonitorOpt = new WifiMonitorOpt1(context, viewHolder, device, true);
        }

        @Override
        public void onClick(View v) {
            if (viewHolder instanceof APDetailViewHolder) {
                switch (v.getId()) {
                    case R.id.deauch_info:
                        Log.d(TAG, "ap devices =" + devices.toString());
                        wifiMonitorOpt.queryOrDeauthDevice();
                        break;
                    case R.id.station_info:
                        Log.d(TAG, "station_info click");
                        if (device.getStations() == null || (int) (((APDetailViewHolder)
                                viewHolder).stationNumber.getTag()) == 0)
                            return;
                        Intent intent = new Intent(context, DeviceDetailActivity.class);
                        intent.putExtra("ap_station_detail", 1);
                        intent.putExtra("deviceMac", device.getMac());
                        context.startActivity(intent);
                        break;
                }

            } else if (viewHolder instanceof StationDetailViewHolder) {
                switch (v.getId()) {
                    case R.id.deauch_info:
                        viewHolder.itemView.setTag(R.id.tag_objected,
                                DevicesMoreDetailRecyclerViewAdapter.this);
                        Log.d(TAG, "click deauch_info station devices =" + devices.toString());
                        wifiMonitorOpt.queryOrDeauthDevice();
                        break;
                    case R.id.mitm_info:
                        viewHolder.itemView.setTag(R.id.tag_objected,
                                DevicesMoreDetailRecyclerViewAdapter.this);
                        viewHolder.itemView.setTag(R.id.tag_view, viewHolder.itemView);
                        wifiMonitorOpt.queryOrMitmDevice();
                        Log.d(TAG, "click mitm_info station devices =" + devices.toString());
                        break;
                    case R.id.whitelist_info:
                        wifiMonitorOpt.queryOrSetWhitelistState();
                        Log.d(TAG, "click whitelist_info station devices =" + devices.toString());
                        break;
                    case R.id.blacklist_info:
                        wifiMonitorOpt.queryOrSetBlacklistState();
                        Log.d(TAG, "click blacklist_info station devices =" + devices.toString());
                        break;
                    case R.id.virtual_id_info:
                        List<Identity> identityList = DataManager.getInstance(context)
                                .queryIdentity(device.getMac());
                        if (identityList != null && hasVirtualId(identityList)) {
                            Intent intent = new Intent(context, VirtualIdActivity.class);
                            intent.putExtra("deviceMac", device.getMac());
                            context.startActivity(intent);
                        }
                        break;
                }
            }
        }
    }

}

