package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.holder.ProduceTypeHolder;
import com.bf.duomi.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义产品类型适配器
 * @author bhl
 *
 */
public class ProduceTypeAdapter extends BaseAdapter{
	
	public int selectIndex;
	private boolean isMainMenu;
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
    private Context mContext;
    
    public ProduceTypeAdapter(Context context, List<Map<String, Object>> mData, boolean isMainMenu){
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.isMainMenu = isMainMenu;
        this.mContext = context;
        selectIndex=-1;
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
         
        ProduceTypeHolder holder = null;
        if (convertView == null) {
            holder=new ProduceTypeHolder();  
            convertView = mInflater.inflate(R.layout.producetype_list_item_view, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.number = (ImageView)convertView.findViewById(R.id.ivRightArrow);
            //holder.info = (TextView)convertView.findViewById(R.id.info);
            convertView.setTag(holder);
        }
        else {
            holder = (ProduceTypeHolder)convertView.getTag();
        }
        if(selectIndex==position && selectIndex!=-1 && isMainMenu)
        {
//			rl.setBackgroundColor(getResources().getColor(R.color.greencolor));
        	convertView.setBackgroundColor(((Activity)mContext).getResources().getColor(R.color.greencolor));
        }else{
        	convertView.setBackgroundColor(Color.WHITE);
        }
        String strName = (String) mData.get(position).get("name");
        String text = "";
        if(isMainMenu)
        {
        	text = ""+(1+position)+"."+strName;
        }else{
        	text = strName;
        	holder.number.setVisibility(View.INVISIBLE);
        }
        
        if(strName!=null)
        {
        	holder.name.setText(String.valueOf(text));
        }
        //holder.number.setImageResource(Integer.valueOf(mData.get(position).get("number").toString()));
//        String info = (String) mData.get(position).get("info");
//        if(info!=null)
//        {
//        	holder.info.setText(info);
//        }
        return convertView;
    }
     
}
