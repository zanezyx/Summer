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

import com.deter.TaxManager.view.MyInfoPagerAdapter;
import com.deter.TaxManager.view.ViewPagerTabs;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment{

    private final static String TAG = "InfoFragment";

    private ViewPager mViewPager;

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
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        ((TextView) view.findViewById(R.id.title)).setText(R.string.my_info);
        mViewPager = (ViewPager) view.findViewById(R.id.map_pager);
        MyInfoPagerAdapter mapViewPagerAdapter = new MyInfoPagerAdapter(getActivity());
        mViewPager.setAdapter(mapViewPagerAdapter);
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
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

    }


}
