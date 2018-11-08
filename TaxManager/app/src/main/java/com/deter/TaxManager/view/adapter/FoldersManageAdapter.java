package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.view.View;

import com.deter.TaxManager.FoldersManageActivity;
import com.deter.TaxManager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public class FoldersManageAdapter extends CommonAdapter {

    private ArrayList<HashMap<String, Object>> selectListItem = new ArrayList<HashMap<String,
            Object>>();

    private int oprType;

    private RadioCallBack radioCallBack;

    public FoldersManageAdapter(int mItemLayoutId, Context mContext, List mDatas, int oprType) {
        super(mItemLayoutId, mContext, mDatas);
        this.oprType = oprType;
    }

    public ArrayList<HashMap<String, Object>> getSelectListItem() {
        return selectListItem;
    }

    public void setRadioCallBack(RadioCallBack radioCallBack) {
        this.radioCallBack = radioCallBack;
    }

    @Override
    public void convert(ViewHolder viewHolder, Object item) {
        if (item instanceof HashMap) {
            HashMap<String, Object> curItem = (HashMap<String, Object>) item;
            String fileName = curItem.get("fileName").toString();
            viewHolder.setText(R.id.fileName, fileName);
            String lastModify = curItem.get("lastModify").toString();
            viewHolder.setText(R.id.fileLastModify, lastModify);
            int resid = (int) curItem.get("fileTypeImg");
            viewHolder.setImageResource(R.id.fileTypeImage, resid);

            if (oprType == FoldersManageActivity.TYPE_FILE) {
                boolean isFolder = (boolean) curItem.get("isFolder");
                if (isFolder) {
                    viewHolder.getView(R.id.folder_choice_layout).setVisibility(View.GONE);
                } else {
                    viewHolder.getView(R.id.folder_choice_layout).setVisibility(View.VISIBLE);
                }
            }

            if (View.VISIBLE == viewHolder.getView(R.id.folder_choice_layout).getVisibility()) {
                viewHolder.getView(R.id.folder_choice_layout).setOnClickListener(new
                        RadioClickListener(curItem));
                if (selectListItem.contains(curItem)) {
                    viewHolder.setImageResource(R.id.folder_choice_iv, R.drawable
                            .bottom_checkbox_select_icon);
                } else {
                    viewHolder.setImageResource(R.id.folder_choice_iv, R.drawable
                            .bottom_checkbox_unselect_icon);
                }
            }

        }
    }

    //onclick opr
    public void onItemClick(HashMap<String, Object> selectItem) {
        if (selectListItem.contains(selectItem)) {
            selectListItem.remove(selectItem);
            radioCallBack.onRadioClick(null);
        } else {
            selectListItem.clear();
            selectListItem.add(selectItem);
            radioCallBack.onRadioClick(selectItem);
        }
        notifyDataSetChanged();
    }

    public interface RadioCallBack {

        void onRadioClick(HashMap<String, Object> item);
    }

    class RadioClickListener implements View.OnClickListener {

        HashMap<String, Object> item;

        public RadioClickListener(HashMap<String, Object> item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            onItemClick(item);
        }
    }

}
