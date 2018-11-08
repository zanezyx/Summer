package com.deter.TaxManager.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.R;
import com.deter.TaxManager.VirtualIdActivity;
import com.deter.TaxManager.WebQueryActivity;
import com.deter.TaxManager.utils.APPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-10-30.
 */

public class DeviceMacRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private static final String TAG = "DeviceMacAdapter";

    private Context context;

    private List<String> macList = new ArrayList<>();

    private LayoutInflater layoutInflater;

    private int type;

    public DeviceMacRecyclerViewAdapter(Context context, List<String> macList, int type) {
        this.context = context;
        this.macList = macList;
        this.type = type;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMacInfo(List<String> macInfo) {
        macList.clear();
        macList.addAll(macInfo);
        android.util.Log.d(TAG, "macList size =" + macList.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MacInfoViewHolder(layoutInflater.inflate(R.layout.layout_search_mac_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String mac = macList.get(position);
        MacInfoViewHolder macInfoViewHolder = (MacInfoViewHolder) holder;
        macInfoViewHolder.macInfo.setText(APPUtils.formateTextToMacAddress(mac));

        macInfoViewHolder.itemView.setTag(mac);
        macInfoViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return macList.size();
    }

    @Override
    public void onClick(View v) {
        String mac = (String) v.getTag();
        if (!TextUtils.isEmpty(mac)) {
            goToActivityByType(mac);
        }
    }

    private void goToActivityByType(String mac) {
        Intent intent = new Intent();
        if (type == APPConstans.DETAIL_VIRTUAL_ID) {
            intent.setClass(context, VirtualIdActivity.class);
        } else if (type == APPConstans.DETAIL_WEBPAGES_SEARCH) {
            intent.setClass(context, WebQueryActivity.class);
        }
        intent.putExtra("deviceMac", mac);
        context.startActivity(intent);
    }


    private class MacInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView macInfo;

        public MacInfoViewHolder(View itemView) {
            super(itemView);
            macInfo = (TextView) itemView.findViewById(R.id.mac);
        }
    }

}
