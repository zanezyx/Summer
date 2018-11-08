package com.deter.TaxManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.utils.ToastUtils;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int TAB_INIT_SELECTED = 0;

    private static final int[] TAB_SELECTED_ICONS = new int[]{R.drawable.star_map_selected, R
            .drawable.around_selected, R.drawable.analysis_selected, R.drawable.profile_selected};

    private static final int[] TAB_UNSELECTED_ICONS = new int[]{R.drawable.star_map_unselected, R
            .drawable.around_unselected, R.drawable.analysis_unselected, R.drawable
            .profile_unselected};

    private static final int TAB_WIFI_STAR_MAP = 0;

    private static final int TAB_AROUND = 1;

    private static final int TAB_ANALYSIS = 2;

    private static final int TAB_PROFILE = 3;

    private boolean isBackPressed = false;

    private ViewPager mViewPager;

    private WifiMonitorViewPagerAdapter wifiMonitorViewPagerAdapter;

    private TabLayout mTabLayout;

    public static MainActivity instance;
    public OnkeyBackDownListener onkeyBackDownListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.init(this);
        instance=this;
        DataManager.getInstance(this).init();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        wifiMonitorViewPagerAdapter = new WifiMonitorViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(wifiMonitorViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (i == TAB_INIT_SELECTED) {
                mTabLayout.getTabAt(i).setIcon(TAB_SELECTED_ICONS[i]);
                mViewPager.setCurrentItem(TAB_INIT_SELECTED, false);
            } else {
                mTabLayout.getTabAt(i).setIcon(TAB_UNSELECTED_ICONS[i]);
            }
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private int unSelectedTab = -1;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(TAB_SELECTED_ICONS[tab.getPosition()]);
                mTabLayout.getTabAt(unSelectedTab).setIcon(TAB_UNSELECTED_ICONS[unSelectedTab]);
                mViewPager.setCurrentItem(tab.getPosition(), false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unSelectedTab = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

//        if (!TcpMsgService.isServiceRunning) {
//            Intent intent = new Intent(this, TcpMsgService.class);
//            startService(intent);
//        }else{
//            Log.i("TcpMsgService", "TcpMsgService isServiceRunning true");
//        }
    }

    public MyInfoFragment getWifiMapFragment() {
//        if (wifiMonitorViewPagerAdapter == null) {
//            return null;
//        }
//        Fragment fragment = wifiMonitorViewPagerAdapter.getItem(0);
//        if (fragment != null && fragment instanceof MyInfoFragment) {
//            return (MyInfoFragment) fragment;
//        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBackPressed) {
            mViewPager.setCurrentItem(0, false);
            isBackPressed = false;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        isBackPressed = true;
        //super.onBackPressed();
    }

    public class WifiMonitorViewPagerAdapter extends FragmentStatePagerAdapter {

        private int[] TitleResID = new int[]{R.string.my_info, R.string.tax_deduction, R.string
                .tax_manager, R.string.my_profile};

        public WifiMonitorViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case TAB_WIFI_STAR_MAP:
                    fragment = new InfoFragment();
                    break;
                case TAB_ANALYSIS:
                    fragment = new TaxDeductionFragment();
                    break;
                case TAB_AROUND:
                    fragment = new TaxManagerFragment();
                    break;
                case TAB_PROFILE:
                    fragment = new ProfileFragment();
                    break;
                default:
                    fragment = new InfoFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TitleResID.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(TitleResID[position]);
        }


    }


    private long firstTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TAB_ANALYSIS == mViewPager.getCurrentItem() && null != onkeyBackDownListener) {
                boolean isDeleteMode = onkeyBackDownListener.onBackClick();
                if (isDeleteMode) {
                    return false;
                }
            }

            if (System.currentTimeMillis() - firstTime < 3000) {
                //System.exit(0);
                //finish();
                moveTaskToBack(true);
                return super.onKeyDown(keyCode, event);
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.press_again_backrun));
                firstTime = System.currentTimeMillis();
            }
        }
        return false;
    }

    public void setOnkeyBackDownListener(OnkeyBackDownListener onkeyBackDownListener) {
        this.onkeyBackDownListener = onkeyBackDownListener;
    }

    public interface OnkeyBackDownListener {
        boolean onBackClick();
    }

}
