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

import com.deter.TaxManager.BackgroundService;
import com.deter.TaxManager.DeviceDetailActivity;
import com.deter.TaxManager.R;
import com.deter.TaxManager.WifiMonitorOpt1;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.utils.APPUtils;

import java.util.List;

/**
 * Created by xiaolu on 17-7-21.
 */

public class DeviceShortCutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int TYPE_AP = 0;

    public final static int TYPE_STATION = 1;

    public final static int TYPE_STATION_FORCE_CONNECTED = 2;

    private final static String TAG = "DeviceShortCutAdapter";

    private Activity context;

    private LayoutInflater layoutInflater;

    private List<Device> data;

    private StarView starView;

    public DeviceShortCutRecyclerAdapter(Activity context, List<Device> data, StarView starView) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.starView = starView;
    }

    public int getDevicePostion(Device device) {
        int position = 0;
        if(data.contains(device)){
            position =data.indexOf(device);
        } else {
            for(Device device1:data){
                if(device1.getMac().equalsIgnoreCase(device.getMac())){
                    position = data.indexOf(device1);
                }
            }
        }
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        Device device = data.get(position);
        String role = device.getRole();

        if (role.equalsIgnoreCase("ap")) {
            Log.d(TAG, "role =" + role);
            return TYPE_AP;
        } else {
            if (device.getMitmState() != 0) {
                return TYPE_STATION_FORCE_CONNECTED;
            } else {
                return TYPE_STATION;
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_AP:
                return new APShortCutViewHolder(layoutInflater.inflate(R.layout
                        .layout_devices_ap_item, parent, false));
            case TYPE_STATION_FORCE_CONNECTED:
                return new StationShortCutViewHolder(layoutInflater.inflate(R.layout
                        .layout_devices_mitm_item, parent, false));
            case TYPE_STATION:
                return new StationShortCutViewHolder(layoutInflater.inflate(R.layout
                        .layout_devices_item, parent, false));

        }
        return new StationShortCutViewHolder(layoutInflater.inflate(R.layout.layout_devices_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Device device = data.get(position);
        switch (getItemViewType(position)) {
            case TYPE_AP:
                bindAPView((APShortCutViewHolder) holder, device);
                break;
            case TYPE_STATION:
                bindStationView((StationShortCutViewHolder) holder, device);
                break;
            case TYPE_STATION_FORCE_CONNECTED:
                bindStationWithMITMView((StationShortCutViewHolder) holder, device);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void bindAPView(final APShortCutViewHolder apShortCutViewHolder, Device device) {
        apShortCutViewHolder.apName.setText(TextUtils.isEmpty(device.getSsid()) ? "" : device
                .getSsid());
        apShortCutViewHolder.apMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        BackgroundService.updateStationNumberView(device, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                apShortCutViewHolder.stationNumber.setText(String.valueOf(msg.arg1));
                apShortCutViewHolder.stationNumber.setTag(msg.arg1);
            }
        });
        ViewOnClickListener viewOnClickListener = new ViewOnClickListener(apShortCutViewHolder,
                device);
        apShortCutViewHolder.apInfo.setOnClickListener(viewOnClickListener);
        apShortCutViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        apShortCutViewHolder.stationNumberInfo.setOnClickListener(viewOnClickListener);

        new WifiMonitorOpt1(context, apShortCutViewHolder, device, false).queryOrDeauthDevice();
    }

    private void bindStationView(StationShortCutViewHolder stationShortCutViewHolder, Device
            device) {
        boolean isSelfMac = APPUtils.isLocalMacAddress(context,device.getMac());
        String stationName = context.getString(R.string.around_station_name, device.getVender(),
                isSelfMac ? context.getString(R.string.around_station_local) : "");
        stationShortCutViewHolder.stationName.setText(Html.fromHtml(stationName));
        stationShortCutViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device
                .getMac()));

        ViewOnClickListener viewOnClickListener = new ViewOnClickListener
                (stationShortCutViewHolder, device);
        stationShortCutViewHolder.stationInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.mitmInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.blacklistInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.whitelistInfo.setOnClickListener(viewOnClickListener);

        new WifiMonitorOpt1(context, stationShortCutViewHolder, device, false).queryOrDeauthDevice
                ().queryOrMitmDevice().queryOrSetBlacklistState().queryOrSetWhitelistState();
    }

    private void bindStationWithMITMView(StationShortCutViewHolder stationShortCutViewHolder,
                                         Device device) {
        stationShortCutViewHolder.stationName.setText(device.getVender());
        stationShortCutViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device
                .getMac()));
        stationShortCutViewHolder.mitmApName.setText(TextUtils.isEmpty(device.getIpInfoName()) ?
                "" : device.getIpInfoName());
        stationShortCutViewHolder.ip.setText(TextUtils.isEmpty(device.getIpaddr()) ? "" : device
                .getIpaddr());

        ViewOnClickListener viewOnClickListener = new ViewOnClickListener
                (stationShortCutViewHolder, device);
        stationShortCutViewHolder.stationInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.deauthInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.mitmInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.blacklistInfo.setOnClickListener(viewOnClickListener);
        stationShortCutViewHolder.whitelistInfo.setOnClickListener(viewOnClickListener);

        new WifiMonitorOpt1(context, stationShortCutViewHolder, device, false).queryOrDeauthDevice
                ().queryOrMitmDevice().queryOrSetBlacklistState().queryOrSetWhitelistState();
    }

    private class ViewOnClickListener implements View.OnClickListener {

        private RecyclerView.ViewHolder viewHolder;

        private Device device;

        private WifiMonitorOpt1 wifiMonitorOpt;

        private ViewOnClickListener(RecyclerView.ViewHolder viewHolder, Device device) {
            this.viewHolder = viewHolder;
            this.device = device;
            wifiMonitorOpt = new WifiMonitorOpt1(context, viewHolder, device, true);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ap_info:
                case R.id.station_info:
                    if (device != null) {
                        Intent intent = new Intent(context, DeviceDetailActivity.class);
                        intent.putExtra("deviceMac", device.getMac());
                        context.startActivity(intent);
                        starView.dissmissPopupWindow();
                    }
                    break;
                case R.id.station_number_info:
                    if (device != null && device.getStations() != null && viewHolder instanceof
                            APShortCutViewHolder) {
                        int size = (int) ((APShortCutViewHolder) viewHolder).stationNumber.getTag();
                        if (size > 0) {
                            Intent intent = new Intent(context, DeviceDetailActivity.class);
                            intent.putExtra("deviceMac", device.getMac());
                            intent.putExtra("ap_station_detail", 1);
                            context.startActivity(intent);
                            starView.dissmissPopupWindow();
                        }
                    }
                    break;
                case R.id.deauch_info:
                    Log.d(TAG, "deauch click");
                    viewHolder.itemView.setTag(R.id.tag_view, starView);
                    wifiMonitorOpt.queryOrDeauthDevice();
                    break;
                case R.id.mitm_info:
                    viewHolder.itemView.setTag(R.id.tag_view, starView);
                    starView.dissmissPopupWindow();
                    wifiMonitorOpt.queryOrMitmDevice();
                    Log.d(TAG, "mitm click");
                    break;
                case R.id.whitelist_info:
                    viewHolder.itemView.setTag(R.id.tag_view, starView);
                    Log.d(TAG, "whitelist_info click");
                    wifiMonitorOpt.queryOrSetWhitelistState();
                    break;
                case R.id.blacklist_info:
                    viewHolder.itemView.setTag(R.id.tag_view, starView);
                    Log.d(TAG, "blacklist_info click");
                    wifiMonitorOpt.queryOrSetBlacklistState();
                    break;
            }

        }
    }


}




