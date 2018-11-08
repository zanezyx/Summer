package com.deter.TaxManager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xiaolu on 17-9-29.
 */

public class ScrollControllableViewPager extends ViewPager {

    private boolean isScrollable = false;

    public ScrollControllableViewPager(Context context) {
        super(context);
    }

    public ScrollControllableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }
}
