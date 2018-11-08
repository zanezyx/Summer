package com.deter.TaxManager.view;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.deter.TaxManager.CollisionAnalysisResultActivity;

/**
 * Created by zhongqiusheng on 2017/10/17 0017.
 */
public class CHScrollView extends HorizontalScrollView {

    CollisionAnalysisResultActivity activity;

    public CHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        activity = (CollisionAnalysisResultActivity) context;
    }


    public CHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (CollisionAnalysisResultActivity) context;
    }

    public CHScrollView(Context context) {
        super(context);
        activity = (CollisionAnalysisResultActivity) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        activity.mTouchView = this;
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (activity.mTouchView == this) {
            activity.onScrollChanged(l, t, oldl, oldt);
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
