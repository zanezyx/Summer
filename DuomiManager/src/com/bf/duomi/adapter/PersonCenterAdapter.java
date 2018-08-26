package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.holder.PersonCenterHolder;
import com.bf.duomimanager.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 个人中心自定义适配器
 * @author lenovo
 *
 */
public class PersonCenterAdapter extends BaseAdapter{
	 
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
     
    public PersonCenterAdapter(Context context, List<Map<String, Object>> mData){
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
         
        PersonCenterHolder holder = null;
        if (convertView == null) {
            holder=new PersonCenterHolder();  
            
            convertView = mInflater.inflate(R.layout.personcenter_list_item_view, null);
            holder.remark = (ImageView)convertView.findViewById(R.id.remark);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }
        else {
            holder = (PersonCenterHolder)convertView.getTag();
        }
        holder.remark.setImageResource((Integer)mData.get(position).get("remark"));
        holder.title.setText((String)mData.get(position).get("title"));
        return convertView;
    }
     
}
