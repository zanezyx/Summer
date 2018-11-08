package com.deter.TaxManager.view.dateview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.R;
import com.deter.TaxManager.utils.*;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.view.BasePopupWindow;

/**
 * Created by zhongqiusheng on 2017/9/29 0029.
 */
public class CalendarPopupWindow extends BasePopupWindow implements CalendarCard.OnCellClickListener, View.OnClickListener {

    boolean isEn;
    private ImageView btnPreMonth;
    private ImageView btnNextMonth;
    private TextView tvCurrentMonth;
    private ViewPager mViewPager;

    private Button cancelSelectTimeBtn;
    private Button confirmSelectTimeBtn;

    private int mCurrentIndex = 498;
    private CalendarCard[] mShowViews;
    private CalendarViewAdapter<CalendarCard> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;
    private String yearStr;
    private String monthStr;
    private String[] monthArray;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    private CustomDate selectDate;
    private int select_time_type;
    private ClickDateListener clickDateListener;
    private String selectDateStr;
    private long lastStartTime;
    private long lastEndTime;
    private String startTimePrompt;
    private String endTimePrompt;

    public CalendarPopupWindow(Context context) {
        super(context, R.layout.dialog_calends_layout, true);
    }

    public void setSelect_time_type(int select_time_type) {
        this.select_time_type = select_time_type;
    }

    public void setClickDateListener(ClickDateListener clickDateListener) {
        this.clickDateListener = clickDateListener;
    }

    @Override
    public void convert(View contentView) {
        btnPreMonth = getViewById(R.id.btnPreMonth);
        btnNextMonth = getViewById(R.id.btnNextMonth);
        tvCurrentMonth = getViewById(R.id.tvCurrentMonth);
        mViewPager = getViewById(R.id.vp_calendar);
        cancelSelectTimeBtn = getViewById(R.id.cancel_select_time_btn);
        confirmSelectTimeBtn = getViewById(R.id.confirm_select_time_btn);
        btnPreMonth.setOnClickListener(this);
        btnNextMonth.setOnClickListener(this);
        cancelSelectTimeBtn.setOnClickListener(this);
        confirmSelectTimeBtn.setOnClickListener(this);
        initDate();
    }


    private void initDate() {
        isEn = APPUtils.isEn(mContext);
        if (isEn) {
            if (null == monthArray) {
                monthArray = new String[12];
            }
            monthArray[0] = getResourceString(R.string.january_str);
            monthArray[1] = getResourceString(R.string.february_str);
            monthArray[2] = getResourceString(R.string.march_str);
            monthArray[3] = getResourceString(R.string.april_str);
            monthArray[4] = getResourceString(R.string.may_str);
            monthArray[5] = getResourceString(R.string.june_str);
            monthArray[6] = getResourceString(R.string.july_str);
            monthArray[7] = getResourceString(R.string.august_str);
            monthArray[8] = getResourceString(R.string.september_str);
            monthArray[9] = getResourceString(R.string.october_str);
            monthArray[10] = getResourceString(R.string.november_str);
            monthArray[11] = getResourceString(R.string.december_str);
        }

        startTimePrompt = getResourceString(R.string.select_start_time_prompt);
        endTimePrompt = getResourceString(R.string.select_end_time_prompt);

        CalendarCard[] views = new CalendarCard[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new CalendarCard(mContext, this);
        }

        adapter = new CalendarViewAdapter<>(views);
        setViewPager();
    }


    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPreMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;

            case R.id.btnNextMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;

            case R.id.confirm_select_time_btn:
                if (isUnReasonable()) {
                    return;
                }
                clickDateListener.clickDate(selectDateStr, lastStartTime, lastEndTime, select_time_type);
            case R.id.cancel_select_time_btn:
                dismiss();
                break;
        }
    }

    @Override
    public void exceeds(String nowDateStr) {
        ToastUtils.showShort(mContext,getResourceString(R.string.exceed_time_prompt));
    }


    @Override
    public void clickDate(CustomDate date) {
        //isUnReasonable(date);
        selectDate = date;
    }

    private boolean isUnReasonable() {
        String timeStr = "";
        boolean unreasonable = false;

        if (null==selectDate){
            return false;
        }

        if (select_time_type == APPConstans.TYPE_SELECT_START_TIME) {
            timeStr = selectDate.toString() + " 00:00:00";
            long timeStamp = Long.valueOf(DateUtil.getTimestamp(timeStr, DateUtil.FORMAT_YMDHMS));
            if (lastEndTime > 0 && lastEndTime < timeStamp) {
                timeStr = "";
                if (null != startTimePrompt) {
                    ToastUtils.showShort(mContext, startTimePrompt);
                }
                unreasonable = true;
            } else {
                lastStartTime = timeStamp;
            }
        } else if (select_time_type == APPConstans.TYPE_SELECT_END_TIME) {
            timeStr = selectDate.toString() + " 23:59:59";
            long timeStamp = Long.valueOf(DateUtil.getTimestamp(timeStr, DateUtil.FORMAT_YMDHMS));
            if (lastStartTime > 0 && lastStartTime > timeStamp) {
                timeStr = "";
                if (null != endTimePrompt) {
                    ToastUtils.showShort(mContext, endTimePrompt);
                }
                unreasonable = true;
            } else {
                lastEndTime = timeStamp;
            }
        }

        selectDateStr = timeStr;
        return unreasonable;
    }

    @Override
    public void changeDate(CustomDate date) {
        int month = date.month;

        StringBuilder stringBuilder = new StringBuilder();
        if (!isEn) {
            if (null == yearStr) {
                yearStr = getResourceString(R.string.year_str);
            }
            if (null == monthStr) {
                monthStr = getResourceString(R.string.month_str);
            }
            stringBuilder.append(date.getYear());
            stringBuilder.append(yearStr);
            stringBuilder.append(month);
            stringBuilder.append(monthStr);
            tvCurrentMonth.setText(stringBuilder.toString());
        } else {
            switch (month) {
                case 1:
                    stringBuilder.append(monthArray[0]);
                    break;

                case 2:
                    stringBuilder.append(monthArray[1]);
                    break;

                case 3:
                    stringBuilder.append(monthArray[2]);
                    break;

                case 4:
                    stringBuilder.append(monthArray[3]);
                    break;

                case 5:
                    stringBuilder.append(monthArray[4]);
                    break;

                case 6:
                    stringBuilder.append(monthArray[5]);
                    break;

                case 7:
                    stringBuilder.append(monthArray[6]);
                    break;

                case 8:
                    stringBuilder.append(monthArray[7]);
                    break;

                case 9:
                    stringBuilder.append(monthArray[8]);
                    break;

                case 10:
                    stringBuilder.append(monthArray[9]);
                    break;

                case 11:
                    stringBuilder.append(monthArray[10]);
                    break;

                case 12:
                    stringBuilder.append(monthArray[11]);
                    break;
            }
            stringBuilder.append(" " + date.getYear());
            tvCurrentMonth.setText(stringBuilder.toString());
        }
    }


    /**
     * 计算方向
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }


    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }


    private String getResourceString(int resID) {
        if (null != mContext) {
            return mContext.getApplicationContext().getResources().getString(resID);
        }

        return "";
    }

    public interface ClickDateListener {
        void clickDate(String timeStr, long startTimeStamp,long endTimeStamp, int selectType); // 回调点击的日期
    }

}
