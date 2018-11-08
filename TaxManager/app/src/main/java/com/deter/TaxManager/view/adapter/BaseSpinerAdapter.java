package com.deter.TaxManager.view.adapter;

import android.content.Context;

import com.deter.TaxManager.R;

import java.util.List;

/**
 * Created by zhongqiusheng on 2017/7/27 0027.
 */
public class BaseSpinerAdapter<T> extends CommonAdapter {

    private int mSelectItem = 0;

    public BaseSpinerAdapter(int mItemLayoutId, Context mContext, List mDatas, int mSelectItem) {
        super(mItemLayoutId, mContext, mDatas);
        this.mSelectItem = mSelectItem;
    }

    @Override
    public void convert(ViewHolder viewHolder, Object item) {
        viewHolder.setText(R.id.spiner_item_tv, item.toString());
    }

    public void refreshData(List<T> objects, int selIndex) {
        mDatas = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mDatas.size()) {
            selIndex = mDatas.size() - 1;
        }

        mSelectItem = selIndex;
    }


    public interface IOnItemSelectListener {

        void onItemClick(int pos);
    }

}
