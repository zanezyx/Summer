package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.URL;
import com.deter.TaxManager.utils.DateUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public class WebQueryAdapter extends CommonAdapter<HashMap<String, Object>> {

    public WebQueryAdapter(int mItemLayoutId, Context mContext, List<HashMap<String, Object>> mDatas) {
        super(mItemLayoutId, mContext, mDatas);
    }

    @Override
    public void convert(ViewHolder viewHolder, HashMap<String, Object> item) {
        viewHolder.setText(R.id.access_time_tv, item.get("time").toString());
        List<URL> urls = (List<URL>) item.get("urls");

        ListView accessUrlLv = viewHolder.getView(R.id.access_url_lv);
        CommonAdapter chaildAdpater = null;
        if (null != accessUrlLv.getTag()) {
            chaildAdpater = (CommonAdapter) accessUrlLv.getTag();
            chaildAdpater.setDatas(urls);
        } else {
            chaildAdpater = new CommonAdapter(R.layout.web_query_list_child_item, mContext, urls) {
                @Override
                public void convert(ViewHolder viewHolder, Object item) {
                    URL url = (URL) item;
                    viewHolder.setText(R.id.access_url_tv, url.getUrl());
                    String time = DateUtil.getHourAndMin(url.getTime());
                    viewHolder.setText(R.id.access_hour_tv, time);
                }
            };
        }

        accessUrlLv.setAdapter(chaildAdpater);
        //setListViewHeightBasedOnChildren(accessUrlLv);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
