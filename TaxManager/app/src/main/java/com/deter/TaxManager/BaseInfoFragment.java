package com.deter.TaxManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by yuxinz on 2018/10/26.
 */

public class BaseInfoFragment extends Fragment {

    private TextView tv;

    public static BaseInfoFragment newInstance() {

        BaseInfoFragment fragment = new BaseInfoFragment();
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
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String name = bundle.get("name").toString();
//            tv.setText(name);
//        }

    }

}
