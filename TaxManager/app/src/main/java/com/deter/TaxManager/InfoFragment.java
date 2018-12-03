package com.deter.TaxManager;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.view.MyInfoPagerAdapter;
import com.deter.TaxManager.view.ViewPagerTabs;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment{

    private final static String TAG = "tax";

    private ViewPager mViewPager;
    private MyInfoPagerAdapter mMyInfoPagerAdapter;
    private Button button;




    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(InfoFragment.this.getActivity()).initAllInfo();
            }
        }).start();
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        ((TextView) view.findViewById(R.id.title)).setText(R.string.my_info);
        mViewPager = (ViewPager) view.findViewById(R.id.map_pager);
        mMyInfoPagerAdapter = new MyInfoPagerAdapter(getActivity());
        mViewPager.setAdapter(mMyInfoPagerAdapter);
        ViewPagerTabs mViewPagerTabs = (ViewPagerTabs) view.findViewById(R.id.lists_pager_header);
        mViewPagerTabs.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(mViewPagerTabs);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        Log.d(TAG, "InfoFragment>>>onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "InfoFragment>>>onPause");
        super.onPause();

    }

    @Override
    public void onStop() {
        Log.d(TAG, "InfoFragment>>>onStop");
        super.onStop();
        if(mMyInfoPagerAdapter!=null){
            mMyInfoPagerAdapter.saveAllInfoToFile();

        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "InfoFragment>>>onDestroyView");
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "InfoFragment>>>onDestroy");
        super.onDestroy();

    }


}
