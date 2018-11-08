package com.deter.TaxManager.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by zhongqiusheng on 2017/7/27
 */
public abstract class BasePopupWindow extends PopupWindow {

    protected Context mContext;

    protected View mContentView;

    public BasePopupWindow(Context context) {
        super(context);
    }

    public BasePopupWindow(Context context, int layoutID) {
        this(context, layoutID, false);
    }

    public BasePopupWindow(Context context, int layoutID, boolean isTransparent) {
        super(context);
        this.mContext = context;
        init(layoutID, isTransparent);
    }

    private void init(int layoutID, boolean isTransparent) {
        mContentView = LayoutInflater.from(mContext).inflate(layoutID, null);
        setContentView(mContentView);
        setFocusable(true);
        //setAnimationStyle(R.style.AnimBottom);

        ColorDrawable dw;// = new ColorDrawable(0x80000000);  0x00000000
        if (isTransparent) {
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            dw = new ColorDrawable(0x00000000);
        } else {
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            dw = new ColorDrawable(0x80000000);
            setOutsideTouchable(true);
            //setAnimationStyle(R.style.popWindowAnimation);
        }
        setBackgroundDrawable(dw);
        convert(mContentView);
    }

    public abstract void convert(View contentView);

    public <T extends View> T getViewById(int layoutID) {
        return (T) mContentView.findViewById(layoutID);
    }

}
