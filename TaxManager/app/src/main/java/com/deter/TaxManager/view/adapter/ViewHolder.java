package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhongqiusheng on 2017/8/2 0002.
 */
public class ViewHolder {

    private final SparseArray<View> mViews;

    private View mConvertView;

    private int position;

    public int getPosition() {
        return position;
    }

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        this.position = position;
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.position = position;
        return viewHolder;
    }


    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            if (null!=view){
                mViews.put(viewId, view);
            }
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }


    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }


}
