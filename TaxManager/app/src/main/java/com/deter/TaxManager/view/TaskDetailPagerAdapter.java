package com.deter.TaxManager.view;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiaolu on 17-8-26.
 */

public class TaskDetailPagerAdapter extends PagerAdapter {

    private static final String TAG = "zyxddd";


    private final int[] TAB_TITLE = {R.string.around_ap, R.string.around_station};

    private LayoutInflater layoutInflater;

    private Activity context;
    private ListView mListView;
    private SimpleAdapter simpleAdapter;
    List<Device> deviceList = new ArrayList<Device>();



    public TaskDetailPagerAdapter(Activity context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        View view = layoutInflater.inflate(R.layout.layout_task_detail_pager, container, false);
        mListView = (ListView)view.findViewById(R.id.listview);
        //创建简单适配器SimpleAdapter
        simpleAdapter = new SimpleAdapter(context, this.getItemList(position), R.layout.layout_listview_item,
        new String[] {"text1","text2", "text3", "text4", "timeText"},
        new int[] {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tvTime});
        //加载SimpleAdapter到ListView中
        mListView.setAdapter(simpleAdapter);
        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return TAB_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TAB_TITLE[position]);
    }


    public ArrayList<HashMap<String, Object>> getItemList(int type) {
        ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
        Log.d(TAG, "getItemList>>>type:"+type);
        if(type==0)
        {
            for (Device device:deviceList) {
                if(device.getRole().equalsIgnoreCase("ap")){

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("text1", device.getSsid());
                    if(device.getMac()!=null){
                        String strMac = APPUtils.formateTextToMacAddress(device.getMac());
                        map.put("text4", strMac);
                    }
                    String strDistant = context.getResources().getString(R.string.distance);
                    String strRealDistant = String.format(strDistant,""+device.getDistance());
                    map.put("text2", strRealDistant);
                    String strChannel = context.getResources().getString(R.string.detail_info_channel);
                    map.put("text3", device.getChannel()+strChannel);
                    map.put("timeText", TimeUtils.getTime(device.getTime()));
                    itemList.add(map);
                    Log.d(TAG, "getItemList>>>add ap:"+device.toString());
                }
            }
        }else if(type==1){
            for (Device device:deviceList) {
                if(device.getRole().equalsIgnoreCase("station")) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("text1", device.getVendorcn());
                    if(device.getMac()!=null){
                        String strMac = APPUtils.formateTextToMacAddress(device.getMac());
                        map.put("text4", strMac);
                    }
                    String strDistant = context.getResources().getString(R.string.distance);
                    String strRealDistant = String.format(strDistant,""+device.getDistance());
                    map.put("text2", strRealDistant);
                    map.put("text3", device.getSsid());
                    map.put("timeText", TimeUtils.getTime(device.getTime()));
                    itemList.add(map);
                    Log.d(TAG, "getItemList>>>add station:"+device.toString());
                }
            }
        }

        return itemList;
    }

    public ListView getmListView() {
        return mListView;
    }

    public void setmListView(ListView mListView) {
        this.mListView = mListView;
    }


}

