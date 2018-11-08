package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.DndMac;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DateUtil;

import java.util.List;

/**
 * Created by zhongqiusheng on 2017/8/8 0008.
 */
public class BlackWhiteListAdapter extends CommonAdapter {

    public BlackWhiteListAdapter(int mItemLayoutId, Context mContext, List mDatas) {
        super(mItemLayoutId, mContext, mDatas);
    }


    @Override
    public void convert(ViewHolder viewHolder, Object item) {
        String nowVender = "";
        String nowMac = "";
        Object selectObject = null;
        boolean isOnLine=false;

        //black white data
        if (item instanceof SpecialDevice) {
            SpecialDevice itm = (SpecialDevice) item;
            if (!TextUtils.isEmpty(itm.getVendercn())){
                nowVender = itm.getVendercn();
            }

            if (itm.isOnLine() /*&& itm.getSpecialFlag() == 1*/) {
                isOnLine = true; // black white item online
            }

            nowMac = APPUtils.formateTextToMacAddress(itm.getMac());
            selectObject = selectListItems.get(itm.getMac());
        } else if(item instanceof DndMac) {
            //filter data
            DndMac itm = (DndMac) item;
            if (!TextUtils.isEmpty(itm.getVender())) {
                nowVender = itm.getVender();
            }
            nowMac = APPUtils.formateTextToMacAddress(itm.getMac());
            selectObject = selectListItems.get(itm.getMac());
        } else if (item instanceof Task) {
            //Star map task list data
            Task itm = (Task) item;
            if (!TextUtils.isEmpty(itm.getAddress())) {
                nowVender = itm.getAddress();
            } else {
                nowVender = mContext.getString(R.string.longitude_latitude, String.valueOf(itm.getLongitude()), String.valueOf(itm.getLatitude()));
            }
            String startTime = Long.toString(itm.getStartTime());
            selectObject = selectListItems.get(startTime);
            nowMac = DateUtil.formatDate(itm.getStartTime());
            long stopTime = itm.getStopTime();

            View lineView = viewHolder.getView(R.id.double_line);
            TextView stopTv = viewHolder.getView(R.id.star_map_list_end_time);

            if (stopTime > 0) {
                lineView.setVisibility(View.VISIBLE);
                stopTv.setVisibility(View.VISIBLE);
                stopTv.setText(DateUtil.formatDate(stopTime));
            } else {
                lineView.setVisibility(View.GONE);
                stopTv.setVisibility(View.GONE);
            }
        }else if(item instanceof SSID){
            //SSID Password Library Manager
            SSID ssid = (SSID) item;
            String key = TextUtils.isEmpty(ssid.getPassword()) ? ssid.getSsid() : ssid.getSsid() + "" + ssid.getPassword();
            selectObject = selectListItems.get(key);
            nowVender = ssid.getSsid();
            if (!TextUtils.isEmpty(ssid.getPassword())) {
                nowMac = ssid.getPassword();
            }
        } else if (item instanceof TopAP) {
            //TOP LIST  Library Manager
            TopAP topAP = (TopAP) item;
            String key = TextUtils.isEmpty(topAP.getPassword()) ? topAP.getSsid() : topAP.getSsid() + "" + topAP.getPassword();
            selectObject = selectListItems.get(key);
            nowVender = topAP.getSsid();
            if (!TextUtils.isEmpty(topAP.getPassword())) {
                nowMac = topAP.getPassword();
            }
        }

        //viewHolder.getView(R.id.black_white_ssid_item_name).setSelected(true);
        //viewHolder.getView(R.id.black_white_ssid_mac_address).setSelected(true);
        viewHolder.setText(R.id.black_white_ssid_item_name, nowVender);
        viewHolder.setText(R.id.black_white_ssid_mac_address, nowMac);
        ImageView onLineView = viewHolder.getView(R.id.black_online_iv);
        if (null != onLineView) {
            if (isOnLine) {
                onLineView.setVisibility(View.VISIBLE);
            } else {
                onLineView.setVisibility(View.GONE);
            }
        }

        ImageView choiceIv = viewHolder.getView(R.id.black_white_choice_iv);

        if (oprType == DELETE_MODE) {
            if (null != selectObject && selectObject == item) {
                choiceIv.setImageResource(R.drawable.bottom_checkbox_select_icon);
            } else {
                choiceIv.setImageResource(R.drawable.bottom_checkbox_unselect_icon);
            }
            choiceIv.setVisibility(View.VISIBLE);
        } else {
            choiceIv.setVisibility(View.GONE);
        }

    }



    @Override
    public void radioChoice(Object selectItemObject) {
        String mac = null;
        if (selectItemObject instanceof SpecialDevice) {
            mac = ((SpecialDevice) selectItemObject).getMac();
        } else if (selectItemObject instanceof DndMac) {
            mac = ((DndMac) selectItemObject).getMac();
        } else if (selectItemObject instanceof Task) {
            Task itm = (Task) selectItemObject;
            mac = Long.toString(itm.getStartTime());
        } else if (selectItemObject instanceof SSID) {
            SSID ssid = (SSID) selectItemObject;
            mac = TextUtils.isEmpty(ssid.getPassword()) ? ssid.getSsid() : ssid.getSsid() + "" + ssid.getPassword();
        } else if (selectItemObject instanceof TopAP) {
            TopAP topAP = (TopAP) selectItemObject;
            mac = TextUtils.isEmpty(topAP.getPassword()) ? topAP.getSsid() : topAP.getSsid() + "" + topAP.getPassword();
        }

        Object device = selectListItems.get(mac);

        if (null != device) {
            selectListItems.remove(mac);
        } else {
            selectListItems.put(mac, selectItemObject);
        }
        onRadioClick();
    }



    @Override
    public void checkAllChoice() {
        List<Object> list = mDatas;
        if (mDatas.size() != selectListItems.size()) {
            selectListItems.clear();
            for (Object device : list) {
                String mac = null;
                if (device instanceof SpecialDevice) {
                    mac = ((SpecialDevice) device).getMac();
                } else if (device instanceof DndMac) {
                    mac = ((DndMac) device).getMac();
                } else if (device instanceof Task) {
                    Task itm = (Task) device;
                    mac = Long.toString(itm.getStartTime());
                } else if (device instanceof SSID) {
                    SSID ssid = (SSID) device;
                    mac = TextUtils.isEmpty(ssid.getPassword()) ? ssid.getSsid() : ssid.getSsid() + "" + ssid.getPassword();
                } else if (device instanceof TopAP) {
                    TopAP topAP = (TopAP) device;
                    mac = TextUtils.isEmpty(topAP.getPassword()) ? topAP.getSsid() : topAP.getSsid() + "" + topAP.getPassword();
                }
                selectListItems.put(mac, device);
            }
        } else {
            selectListItems.clear();
        }

        onRadioClick();
    }



}
