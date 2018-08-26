package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.activity.SelectAddressActivity;
import com.bf.duomi.entity.ReceiveAddressMgr;
import com.bf.duomi.holder.SelectAddressHolder;
import com.bf.duomi.util.SharePreferenceManager;
import com.bf.duomimanager.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义地址适配器
 * @author zyx
 *
 */
public class SelectAddressAdapter extends BaseAdapter{
	 
	private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
     
    public SelectAddressAdapter(Context context, List<Map<String, Object>> mData){
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        mContext = context;
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
         
        SelectAddressHolder holder = null;
        if (convertView == null) {
            holder=new SelectAddressHolder();  
            convertView = mInflater.inflate(R.layout.address_list_item_view, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.addresss = (TextView)convertView.findViewById(R.id.address1);
            holder.addresssDetail = (TextView)convertView.findViewById(R.id.address2);
            holder.phone = (TextView)convertView.findViewById(R.id.phone);
            holder.remove = (View)convertView.findViewById(R.id.remove);
            holder.isDefault = (TextView)convertView.findViewById(R.id.isDefault);
            holder.isDefault.setVisibility(View.INVISIBLE);
            holder.editAddress = (ImageView)convertView.findViewById(R.id.iv1);
            convertView.setTag(holder);
        }
        else {
            holder = (SelectAddressHolder)convertView.getTag();
        }
        holder.name.setText((String)mData.get(position).get("name"));
        holder.addresss.setText((String)mData.get(position).get("area"));
        holder.addresssDetail.setText((String)mData.get(position).get("address"));
        holder.phone.setText((String)mData.get(position).get("phone"));
        holder.remove.setTag((Integer)mData.get(position).get("id"));
        holder.editAddress.setTag(position);
        holder.remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(mContext,(Integer)arg0.getTag());

			}
		});
        
        holder.editAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int index = (Integer)arg0.getTag();
				((SelectAddressActivity)mContext).editAddress(index);
			}
		});
        
        
        ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
        rmgr.currDefault = SharePreferenceManager.getUserDefaultAddressId();
        int id = (Integer)mData.get(position).get("id");
        if(rmgr.currDefault == id)
        {
        	holder.isDefault.setVisibility(View.VISIBLE);
        }else{
        	holder.isDefault.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
     
    
	

    //显示基本的AlertDialog  
    private void showDialog(final Context context, final int addressId) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        //builder.setIcon(R.drawable.icon);  
        builder.setTitle("温馨提示");  
        builder.setMessage("是否确定删除地址？");  
        builder.setPositiveButton("确定",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
        				try {
        					((SelectAddressActivity)context).deleteAddress(addressId);
        				} catch (Exception e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
                    }  
                });  
        builder.setNegativeButton("取消",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        
                    }  
                });  
        builder.show();  
    }  
    
}


