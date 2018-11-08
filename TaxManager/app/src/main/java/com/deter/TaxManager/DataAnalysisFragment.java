package com.deter.TaxManager;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deter.TaxManager.view.ViewPagerTabs;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataAnalysisFragment extends Fragment implements MainActivity.OnkeyBackDownListener {

    private final static int TAB_COLLISION_POSITION = 0;
    private final static int TAB_ANALYS_POSITION = 1;

    private View contentView;

    private DataAnalysisPagerAdapter dataAnalysisPagerAdapter;

    private ViewPagerTabs listsPagerHeader;
    private ViewPager analysPager;

    public DataAnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.instance.setOnkeyBackDownListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_data_analysis, container, false);
        initView();
        return contentView;
    }


    private void initView() {
        dataAnalysisPagerAdapter = new DataAnalysisPagerAdapter(getFragmentManager());
        analysPager = (ViewPager) contentView.findViewById(R.id.analys_pager);
        analysPager.setOffscreenPageLimit(2);
        analysPager.setAdapter(dataAnalysisPagerAdapter);
        listsPagerHeader = (ViewPagerTabs) contentView.findViewById(R.id.lists_pager_header);
        listsPagerHeader.setViewPager(analysPager);
        analysPager.addOnPageChangeListener(listsPagerHeader);
    }


    class DataAnalysisPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        private final int[] TAB_TITLE = {R.string.collision_analys_title, R.string.accompany_analys_title};

        public DataAnalysisPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new CollisionFragment());
            fragments.add(new AccompanyFragment());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case TAB_COLLISION_POSITION:
                    fragment = fragments.get(TAB_COLLISION_POSITION);
                    break;

                case TAB_ANALYS_POSITION:
                    fragment = fragments.get(TAB_ANALYS_POSITION);
                    break;

                default:
                    fragment = fragments.get(TAB_COLLISION_POSITION);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_TITLE.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(TAB_TITLE[position]);
        }
    }


    @Override
    public boolean onBackClick() {
        int position = analysPager.getCurrentItem();
        MainActivity.OnkeyBackDownListener onkeyBackDownListener = (MainActivity.OnkeyBackDownListener) dataAnalysisPagerAdapter.getItem(position);
        if (null != onkeyBackDownListener) {
            return onkeyBackDownListener.onBackClick();
        }
        return false;
    }
}
