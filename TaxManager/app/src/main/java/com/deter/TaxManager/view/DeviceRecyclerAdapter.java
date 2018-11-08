package com.deter.TaxManager.view;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.BackgroundService;
import com.deter.TaxManager.DeviceDetailActivity;
import com.deter.TaxManager.R;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.utils.APPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-8-1.
 */

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private static final String TAG = "DeviceRecyclerAdapter";

    private int type;

    private boolean selectAll = false;

    private boolean inSearchMode = false;

    private Context context;

    private LayoutInflater layoutInflater;

    private CheckboxCheckedCallback checkboxCheckedCallback;

    private List<Device> devices = new ArrayList<>();

    public DeviceRecyclerAdapter(Context context, List<Device> deviceData, int type) {
        this.context = context;
        devices.addAll(deviceData);
        this.type = type;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setInSearchMode() {
        inSearchMode = true;
    }

    public void setDeviceData(List<Device> deviceData) {
        devices.removeAll(devices);
        devices.addAll(deviceData);
        Log.d(TAG, " devices =" + devices.size() + ", deviceData =" + deviceData.size());
        notifyDataSetChanged();
    }

    public void setCheckedCallback(CheckboxCheckedCallback checkedCallback) {
        checkboxCheckedCallback = checkedCallback;
    }

    public void setCheckboxAll(boolean isAll) {
        selectAll = isAll;
        notifyDataSetChanged();
        if (checkboxCheckedCallback != null) checkboxCheckedCallback.onCheckedAll(selectAll);
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case APPConstans.DETAIL_AROUND_AP:
                return new APViewHolder(LayoutInflater.from(context).inflate(R.layout
                        .layout_around_ap_item, parent, false));
            case APPConstans.DETAIL_AROUND_STATION:
                return new StationViewHolder(layoutInflater.inflate(R.layout
                        .layout_around_station_item, parent, false));
            case APPConstans.DETAIL_FORCE_DISCONNECTED:
                return new DeauthViewHolder(layoutInflater.inflate(R.layout
                        .layout_around_deauth_item, parent, false));
            case APPConstans.DETAIL_FORCE_CONNECTED:
                return new MitmViewHolder(layoutInflater.inflate(R.layout
                        .layout_around_mitm_item, parent, false));
            case APPConstans.DETAIL_SPECIAL_SEARCH:
                return new StationViewHolder(layoutInflater.inflate(R.layout
                        .layout_around_station_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case APPConstans.DETAIL_AROUND_AP:
                bindAPView((APViewHolder) holder, devices.get(position));
                break;
            case APPConstans.DETAIL_AROUND_STATION:
                bindStationView((StationViewHolder) holder, devices.get(position));
                break;
            case APPConstans.DETAIL_FORCE_DISCONNECTED:
                bindDeauthView((DeauthViewHolder) holder, devices.get(position));
                break;
            case APPConstans.DETAIL_FORCE_CONNECTED:
                bindMitmView((MitmViewHolder) holder, devices.get(position));
                break;
            case APPConstans.DETAIL_SPECIAL_SEARCH:
                bindSpecialDirectionView((StationViewHolder) holder, devices.get(position),
                        position);
                break;

        }

    }

    @Override
    public int getItemCount() {
        if (type == APPConstans.DETAIL_SPECIAL_SEARCH) {
            return Math.min(devices.size(), 5);
        }
        return devices.size();
    }

    private void bindAPView(final APViewHolder apViewHolder, Device device) {
        apViewHolder.apName.setText(device.getSsid());
        apViewHolder.apMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        if (device.getDeauthState() != 0) {
            apViewHolder.stateIndicator.setVisibility(View.VISIBLE);
            apViewHolder.stateIndicator.setImageResource(R.drawable.ap_force_disconnected);
        } else {
            apViewHolder.stateIndicator.setVisibility(View.GONE);
        }
        apViewHolder.distance.setText(context.getString(R.string.distance, String.format("%.2f",
                device.getDistance())));

        BackgroundService.updateStationNumberView(device, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                apViewHolder.number.setText(context.getString(R.string.station_number, msg.arg1));
            }
        });
        apViewHolder.itemView.setTag(device);
        apViewHolder.itemView.setOnClickListener(this);
    }

    private void bindSpecialDirectionView(StationViewHolder stationViewHolder, Device device, int
            position) {
        stationViewHolder.stationName.setText(device.getVender());
        stationViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        stationViewHolder.distance.setText(context.getString(R.string.distance, String.format
                ("%.2f", device.getDistance())));
        stationViewHolder.arrow.setVisibility(View.GONE);
        if (position == 0) {
            stationViewHolder.stationName.setTextColor(context.getResources().getColor(R.color
                    .tabs_selected));
            stationViewHolder.stationMac.setTextColor(context.getResources().getColor(R.color
                    .tabs_selected));
            stationViewHolder.distance.setTextColor(context.getResources().getColor(R.color
                    .tabs_selected));
        }
    }


    @SuppressLint("DefaultLocale")
    private void bindStationView(StationViewHolder stationViewHolder, Device device) {
        boolean isSelfMac = APPUtils.isLocalMacAddress(context,device.getMac());
        String stationName = context.getString(R.string.around_station_name, device.getVender(),
                isSelfMac ? context.getString(R.string.around_station_local) : "");
        stationViewHolder.stationName.setText(Html.fromHtml(stationName));
        stationViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        if(device.isInblacklist() || device.getDeauthState() != 0 || device.getMitmState() != 0){
            stationViewHolder.stateIndicator.setVisibility(View.VISIBLE);
            updateStationStateIndicator(device,stationViewHolder.stateIndicator);
        } else {
            stationViewHolder.stateIndicator.setVisibility(View.GONE);
        }
        stationViewHolder.distance.setText(context.getString(R.string.distance, String.format
                ("%.2f", device.getDistance())));
        stationViewHolder.itemView.setTag(device);
        stationViewHolder.itemView.setOnClickListener(this);
    }

    private void updateStationStateIndicator(Device device, ImageView stateIndicator) {
        if (device.getDeauthState() != 0) {
            stateIndicator.setImageResource(device.isInblacklist() ? R.drawable
                    .station_black_and_force_disconnected : R.drawable.station_force_disconnected);
        } else if (device.getMitmState() != 0) {
            stateIndicator.setImageResource(device.isInblacklist() ? R.drawable
                    .station_black_and_force_connected : R.drawable.station_force_connected);
        } else if(device.isInblacklist()) {
            stateIndicator.setImageResource(R.drawable.station_blacklist);
        }
    }

    private void bindDeauthView(final DeauthViewHolder deauthViewHolder, final Device device) {
        if (device.getRole().equalsIgnoreCase("ap")) {
            deauthViewHolder.deviceName.setText(device.getSsid());
        } else {
            deauthViewHolder.deviceName.setText(device.getVender());
        }

        if (selectAll) {
            deauthViewHolder.checkBox.setVisibility(View.VISIBLE);
            deauthViewHolder.checkBox.setChecked(true);
        } else {
            deauthViewHolder.checkBox.setChecked(false);
            deauthViewHolder.checkBox.setVisibility(View.GONE);
        }

        if (!inSearchMode) {
            deauthViewHolder.itemView.setOnClickListener(this);
            deauthViewHolder.itemView.setTag(R.id.tag_objected, device);
            deauthViewHolder.itemView.setTag(R.id.tag_view, deauthViewHolder.checkBox);
        }

        deauthViewHolder.deviceMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));


    }

    private void bindMitmView(MitmViewHolder mitmViewHolder, Device device) {
        mitmViewHolder.stationName.setText(device.getVender());
        mitmViewHolder.stationMac.setText(APPUtils.formateTextToMacAddress(device.getMac()));
        mitmViewHolder.apName.setText(TextUtils.isEmpty(device.getIpInfoName()) ?
                "" : device.getIpInfoName());
        mitmViewHolder.ip.setText(TextUtils.isEmpty(device.getIpaddr()) ? "" : device
                .getIpaddr());

        if (selectAll) {
            mitmViewHolder.checkBox.setVisibility(View.VISIBLE);
            mitmViewHolder.checkBox.setChecked(true);
        } else {
            mitmViewHolder.checkBox.setChecked(false);
            mitmViewHolder.checkBox.setVisibility(View.GONE);
        }

        if (!inSearchMode) {
            mitmViewHolder.itemView.setOnClickListener(this);
            mitmViewHolder.itemView.setTag(R.id.tag_objected, device);
            mitmViewHolder.itemView.setTag(R.id.tag_view, mitmViewHolder.checkBox);
        }
    }

    @Override
    public void onClick(View v) {
        switch (type) {
            case APPConstans.DETAIL_AROUND_AP:
                Device device = (Device) v.getTag();
                if (device != null) {
                    Intent intent = new Intent(context, DeviceDetailActivity.class);
                    intent.putExtra("deviceMac", device.getMac());
                    BackgroundService.setDetailDevice(device);
                    context.startActivity(intent);
                }
                break;
            case APPConstans.DETAIL_AROUND_STATION:
                Device station = (Device) v.getTag();
                if (station != null) {
                    Intent intent = new Intent(context, DeviceDetailActivity.class);
                    intent.putExtra("deviceMac", station.getMac());
                    BackgroundService.setDetailDevice(station);
                    context.startActivity(intent);
                }
                break;
            case APPConstans.DETAIL_SPECIAL_SEARCH:
                break;
            case APPConstans.DETAIL_FORCE_DISCONNECTED:
            case APPConstans.DETAIL_FORCE_CONNECTED:
                CheckBox checkBox = (CheckBox) v.getTag(R.id.tag_view);
                checkBox.setVisibility(checkBox.getVisibility() == View.VISIBLE ? View.GONE :
                        View.VISIBLE);
                checkBox.setChecked(!checkBox.isChecked());
                Device device1 = (Device) v.getTag(R.id.tag_objected);
                checkboxCheckedCallback.onChecked(checkBox.isChecked(), device1);
                break;
        }
    }


    public interface CheckboxCheckedCallback {

        void onChecked(boolean checked, Device device);

        void onCheckedAll(boolean isAll);
    }

    private class APViewHolder extends RecyclerView.ViewHolder {

        private TextView apName;

        private TextView apMac;

        private ImageView stateIndicator;

        private TextView distance;

        private TextView number;

        public APViewHolder(View itemView) {
            super(itemView);
            apName = (TextView) itemView.findViewById(R.id.ap_name);
            apMac = (TextView) itemView.findViewById(R.id.ap_mac);
            stateIndicator = (ImageView) itemView.findViewById(R.id.state_indicator);
            distance = (TextView) itemView.findViewById(R.id.distance);
            number = (TextView) itemView.findViewById(R.id.number);
        }
    }

    private class StationViewHolder extends RecyclerView.ViewHolder {

        private TextView stationName;

        private TextView stationMac;

        private TextView distance;

        private ImageView stateIndicator;

        private ImageView arrow;

        public StationViewHolder(View itemView) {
            super(itemView);
            stationName = (TextView) itemView.findViewById(R.id.station_name);
            stationMac = (TextView) itemView.findViewById(R.id.station_mac);
            distance = (TextView) itemView.findViewById(R.id.distance);
            stateIndicator = (ImageView) itemView.findViewById(R.id.state_indicator);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
        }
    }

    private class DeauthViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;

        private TextView deviceMac;

        private CheckBox checkBox;

        public DeauthViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.device_name);
            deviceMac = (TextView) itemView.findViewById(R.id.device_mac);
            checkBox = (CheckBox) itemView.findViewById(R.id.ischecked);
        }
    }

    private class MitmViewHolder extends RecyclerView.ViewHolder {

        private TextView stationName;

        private TextView stationMac;

        private TextView apName;

        private TextView ip;

        private CheckBox checkBox;

        public MitmViewHolder(View itemView) {
            super(itemView);
            stationName = (TextView) itemView.findViewById(R.id.station_name);
            stationMac = (TextView) itemView.findViewById(R.id.station_mac);
            apName = (TextView) itemView.findViewById(R.id.ap_name);
            ip = (TextView) itemView.findViewById(R.id.ip);
            checkBox = (CheckBox) itemView.findViewById(R.id.ischecked);
        }
    }


}
