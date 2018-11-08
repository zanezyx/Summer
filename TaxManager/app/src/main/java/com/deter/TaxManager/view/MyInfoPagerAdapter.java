package com.deter.TaxManager.view;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-26.
 */

public class MyInfoPagerAdapter extends PagerAdapter {

    private static final String TAG = "MyInfoPagerAdapter";

    private final int[] TAB_TITLE = {R.string.tab_my_info,  R.string.tab_parent_info,R.string.tab_children_info};

    private LayoutInflater layoutInflater;

    private Activity context;

    public MyInfoPagerAdapter(Activity context) {
        this.context = context;
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
        View view = null;
        switch(position){
            case 0:
                view = layoutInflater.inflate(R.layout.fragment_base_info, container, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.fragment_parent_info, container, false);
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.fragment_children_info, container, false);
                break;
        }
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


}

