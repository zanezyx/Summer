package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.activity.MainActivity;
import com.bf.duomi.activity.ProductDetailActivity;
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
public class ProductAdapter extends BaseAdapter{
	 
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
    Context context;
	public LoadImageUtil loadImageUtil;
    
    
    public ProductAdapter(Context context, List<Map<String, Object>> mData){
    	this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        loadImageUtil = new LoadImageUtil();
        loadImageUtil.mInSampleSize = 6;
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
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.image = (ImageView)convertView.findViewById(R.id.ivIcon);
            holder.productId = (TextView)convertView.findViewById(R.id.tvProductId);
            convertView.setTag(holder);
            //mData.get(position).put("imageView", holder.image);
        }
        else {
            holder = (ProduceHolder)convertView.getTag();
        }
        
        holder.name.setText((String)mData.get(position).get("name"));
        if(mData.get(position).get("price")!=null)
        {
        	holder.price.setText("￥" + mData.get(position).get("price").toString());
        }
        if(mData.get(position).get("id")!=null)
        {
        	holder.productId.setText("商品编号:" + mData.get(position).get("id"));
        }
        //ImageView iv = (ImageView) mData.get(position).get("imageView");
        if(holder.image==null)
        {
        	holder.image = (ImageView)convertView.findViewById(R.id.ivIcon);
        	//mData.get(position).put("imageView", holder.image);
        }
        showImage(position, holder.image);
        final int index = position;
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putInt("id", (Integer)mData.get(index).get("id"));
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(context, ProductDetailActivity.class);
				context.startActivity(intent);
			}
		});
        
        return convertView;
    }
    
    
	void showImage(int position, ImageView iv) {

		String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
		String url = URL + mData.get(position).get("imgURl") + (1) + ".jpg";
		//ImageView iv = (ImageView) mData.get(position).get("imageView");
		if (url != null && iv != null) {
			loadImageUtil.showImageAsyn(iv, url, 0);
		}

	}
     
}
