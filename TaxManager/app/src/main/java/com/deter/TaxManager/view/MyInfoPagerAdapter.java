package com.deter.TaxManager.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.deter.TaxManager.DatePickerUtil;
import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-26.
 */

public class MyInfoPagerAdapter extends PagerAdapter {

    private static final String TAG = "MyInfoPagerAdapter";

    private final int[] TAB_TITLE = {R.string.tab_my_info,  R.string.tab_parent_info,R.string.tab_children_info};

    private LayoutInflater layoutInflater;

    private Activity context;
    private DatePickerUtil datePickerUtil;
    private Button tvChooseBornDate;
    private Button btFatherBornDate;
    private Button btMotherBornDate;
    private Button btChildren1BornDate;
    private Button btChildren2BornDate;

    public MyInfoPagerAdapter(Activity context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        datePickerUtil = new DatePickerUtil(context);
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
                initbaseInfoView(context, view);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.fragment_parent_info, container, false);
                initParentsInfoView(context, view);
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.fragment_children_info, container, false);
                initChildrenInfoView(context, view);
                break;
        }
        container.addView(view);
        return view;
    }

    void initbaseInfoView(Context context, View view){

        tvChooseBornDate = (Button) view.findViewById(R.id.tvChooseBornDate);
        tvChooseBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tax", "MyInfoFragment>>>onViewCreated>>>onClick 1");
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        Log.i("tax", "MyInfoFragment>>>onViewCreated>>>onClick 2");
                        tvChooseBornDate.setText(year+"."+monthOfYear+"."+dayOfMonth);
                    }
                });
            }
        });
    }

    void initParentsInfoView(Context context, View view){

        btFatherBornDate = (Button) view.findViewById(R.id.btFatherBornDate);
        btFatherBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        btFatherBornDate.setText(year+"."+monthOfYear+"."+dayOfMonth);
                    }
                });
            }
        });

        btMotherBornDate = (Button) view.findViewById(R.id.btMotherBornDate);
        btMotherBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        btMotherBornDate.setText(year+"."+monthOfYear+"."+dayOfMonth);
                    }
                });
            }
        });
    }

    void initChildrenInfoView(Context context, View view){

        btChildren1BornDate = (Button) view.findViewById(R.id.btChildren1BornDate);
        btChildren1BornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        btChildren1BornDate.setText(year+"."+monthOfYear+"."+dayOfMonth);
                    }
                });
            }
        });

        btChildren2BornDate = (Button) view.findViewById(R.id.btChildren2BornDate);
        btChildren2BornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        btChildren2BornDate.setText(year+"."+monthOfYear+"."+dayOfMonth);
                    }
                });
            }
        });
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

