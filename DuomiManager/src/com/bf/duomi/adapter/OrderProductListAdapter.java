package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.activity.MainActivity;
import com.bf.duomi.activity.ProductDetailActivity;
import com.bf.duomi.holder.OrderProductHolder;
import com.bf.duomi.holder.ProduceHolder;
import com.bf.duomi.tools.LoadImageUtil;
import com.bf.duomi.util.PropertiesUtils;
import com.bf.duomimanager.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义产品适配器
 * @author zyx
 *
 */
public class OrderProductListAdapter extends BaseAdapter{
	 
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
    Context context;
    LoadImageUtil loadImageUtil;
    
    
    public OrderProductListAdapter(Context context, List<Map<String, Object>> mData){
    	this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        loadImageUtil = new LoadImageUtil();
        loadImageUtil.mInSampleSize = 4;
        loadImageUtil.adapter = this;
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
         
    	OrderProductHolder holder = null;
        if (convertView == null) {
            holder=new OrderProductHolder();  
            convertView = mInflater.inflate(R.layout.order_product_list_item_view, null);
            holder.icon = (ImageView)convertView.findViewById(R.id.ivIcon);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.count = (TextView) convertView.findViewById(R.id.tvProductCount);
            holder.icon = (ImageView) convertView.findViewById(R.id.ivIcon);
            mData.get(position).put("imageView", holder.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (OrderProductHolder)convertView.getTag();
        }
         
        holder.name.setText((String)mData.get(position).get("name"));
        if(mData.get(position).get("price")!=null)
        {
        	holder.price.setText("￥"+ mData.get(position).get("price"));
        }
        holder.count.setText("X"+ mData.get(position).get("count"));
        //holder.name.setText((String)mData.get(position).get("name"));
        showImage(position);
        final int index = position;
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putInt("id", (Integer)mData.get(index).get("id"));
				bundle.putBoolean("enterFromOrder", true);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(context, ProductDetailActivity.class);
				context.startActivity(intent);
			}
		});
        
        return convertView;
    }
    
	void showImage(int position) {

		String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
		String url = URL + mData.get(position).get("imgURl") + (1) + ".jpg";
		ImageView iv = (ImageView) mData.get(position).get("imageView");
		if (url != null && iv != null) {
			loadImageUtil.showImageAsyn(iv, url, 0);
		}

	}
     
}
