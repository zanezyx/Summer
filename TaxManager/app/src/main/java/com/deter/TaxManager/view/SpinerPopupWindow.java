package com.deter.TaxManager.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.view.adapter.BaseSpinerAdapter;
import com.deter.TaxManager.view.adapter.BaseSpinerAdapter.IOnItemSelectListener;

import java.util.List;

/**
 * Created by zhongqiusheng on 2017/7/27 0027.
 */
public class SpinerPopupWindow<T> extends BasePopupWindow implements AdapterView
        .OnItemClickListener {

    private ListView mListView;

    private BaseSpinerAdapter<T> mAdapter;

    private IOnItemSelectListener mItemSelectListener;

    public SpinerPopupWindow(Context context) {
        super(context, R.layout.spiner_window_layout, true);
    }

    @Override
    public void convert(View contentView) {
        setOutsideTouchable(false);
        mListView = getViewById(R.id.listview);
        mAdapter = new BaseSpinerAdapter<>(R.layout.spiner_item_layout, mContext, null, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void refreshData(List<T> list, int selIndex) {
        if (list != null && selIndex != -1) {
            mAdapter.refreshData(list, selIndex);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(pos);
        }
    }

    public void setItemListener(IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }

}