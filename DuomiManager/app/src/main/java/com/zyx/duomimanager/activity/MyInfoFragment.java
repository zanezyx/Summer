package com.zyx.duomimanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyx.duomimanager.R;

/**
 * Created by yuxinz on 2018/10/26.
 */

public class MyInfoFragment extends Fragment {

    private Context mContext;
    private TextView points, level, name, tvUserId;
    private TextView tvNumber;
    private TextView tvCount;
    private TextView tvYouHuiQuan;
    LinearLayout linearLayout1;
    RelativeLayout relativeLayout2;

    public static MyInfoFragment newInstance() {

        MyInfoFragment fragment = new MyInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        tv = (TextView) view.findViewById(R.id.fragment_test_tv);
        linearLayout1 = (LinearLayout)view.findViewById(R.id.linearLayout1);
        relativeLayout2 = (RelativeLayout)view.findViewById(R.id.relativeLayout2);
        tvUserId = (TextView) view.findViewById(R.id.tvUserId);
        tvNumber = (TextView) view.findViewById(R.id.tvScore);

    }

}
