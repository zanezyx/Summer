package com.deter.TaxManager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by yuxinz on 2018/10/26.
 */

public class MyInfoFragment extends Fragment {

    private DatePickerUtil datePickerUtil;
    private Button tvChooseBornDate;

    public static MyInfoFragment newInstance() {

        MyInfoFragment fragment = new MyInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        Log.i("tax", "MyInfoFragment>>>onCreateView>>>onClick 1");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("tax", "MyInfoFragment>>>onViewCreated>>>onClick 1");
        datePickerUtil = new DatePickerUtil(getActivity());
        tvChooseBornDate = (Button) view.findViewById(R.id.tvChooseBornDate);
        tvChooseBornDate.setClickable(true);
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

}
