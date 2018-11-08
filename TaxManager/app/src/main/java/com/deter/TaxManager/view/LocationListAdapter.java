package com.deter.TaxManager.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.deter.TaxManager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-8-4.
 */

public class LocationListAdapter extends BaseAdapter {

    private List<PoiInfo> list = new ArrayList<>();

    private Context context;

    public LocationListAdapter(Context context, List<PoiInfo> list) {

        this.list.addAll(list);
        this.context = context;
    }

    public void setList(List<PoiInfo> list1) {
        list.removeAll(list);
        list.addAll(list1);
        Log.d("xioalu", "list1 size =" + list1.size() + ", list size =" + list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_location_item,
                    parent, false);
        }
        TextView buildingName = (TextView) convertView.findViewById(R.id.buildingName);
        TextView buildingAddress = (TextView) convertView.findViewById(R.id.buildAddress);
        buildingName.setText(list.get(position).name);
        buildingAddress.setText(list.get(position).address);
        return convertView;
    }
}
