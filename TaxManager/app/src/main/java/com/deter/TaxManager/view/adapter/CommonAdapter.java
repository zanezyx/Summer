package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/8/2 0002.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    //以某个参数作为key保存对象
    protected HashMap<String, Object> selectListItems = new HashMap<String, Object>();
    public final static int NORMAL_MODE = 0x0025;
    public final static int DELETE_MODE = 0x0037;
    private CheckBox checkAllBox;
    private Button comfirmBtn;
    protected int oprType = NORMAL_MODE;

    protected Context mContext;

    protected List<T> mDatas;

    protected int mItemLayoutId;


    public CommonAdapter(int mItemLayoutId, Context mContext, List<T> mDatas) {
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (null == mDatas) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
    }

    public HashMap<String, Object> getSelectListItems() {
        return selectListItems;
    }

    public int getOprType() {
        return oprType;
    }

    public void setDeleteModeView(CheckBox checkAllBoxView, Button deleteBtn) {
        this.checkAllBox = checkAllBoxView;
        this.comfirmBtn = deleteBtn;
    }

    public void setOprType(int oprType) {
        this.oprType = oprType;
        notifyDataSetChanged();
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, T item);


    protected void onRadioClick() {
        if (null == checkAllBox || null == comfirmBtn) {
            Log.e("CommonAdapter", "checkAllBox and comfirmBtn is null");
            return;
        }

        notifyDataSetChanged();
        if (selectListItems.size() > 0) {
            if (selectListItems.size() != mDatas.size()) {
                checkAllBox.setChecked(false);
            } else {
                checkAllBox.setChecked(true);
            }
            comfirmBtn.setEnabled(true);
        } else {
            checkAllBox.setChecked(false);
            comfirmBtn.setEnabled(false);
        }
    }

    /**
     * radio click check
     * @param selectItemObject
     */
    public void radioChoice(Object selectItemObject) {

    }

    /**
     * Select all
     */
    public void checkAllChoice() {

    }

}
