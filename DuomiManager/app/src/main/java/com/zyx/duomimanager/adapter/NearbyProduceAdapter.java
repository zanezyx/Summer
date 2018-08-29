package com.zyx.duomimanager.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zyx.duomimanager.R;
import com.zyx.duomimanager.holder.ProduceHolder;

/**
 * 自定义产品适配器
 * @author bhl
 *
 */
public class NearbyProduceAdapter extends BaseAdapter{
	 
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
     
    public NearbyProduceAdapter(Context context, List<Map<String, Object>> mData){
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }
    //获得记录数
    @Override
    public int getCount() {
    	if(mData != null){
			return mData.size();
		}
		return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * 重写记录
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
        ProduceHolder holder = null;
        if (convertView == null) {
            holder=new ProduceHolder();  
            convertView = mInflater.inflate(R.layout.product_list_item_view, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            //holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.info = (TextView)convertView.findViewById(R.id.info);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        }
        else {
            holder = (ProduceHolder)convertView.getTag();
        }
         
        holder.name.setText((String)mData.get(position).get("name"));
        //holder.title.setText((String)mData.get(position).get("title"));
        holder.info.setText(mData.get(position).get("info").toString());
        holder.price.setText(mData.get(position).get("price").toString());
        return convertView;
    }
     
}
