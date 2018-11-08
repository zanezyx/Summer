package com.deter.TaxManager;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.DndMac;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.model.URL;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.BasePopupWindow;
import com.deter.TaxManager.view.PickerView;
import com.deter.TaxManager.view.WifiMonitorPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.deter.TaxManager.network.BuzokuFuction.DETER_DIR_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaxDeductionFragment extends PreferenceFragment implements Preference
        .OnPreferenceChangeListener {

    DataManager dataManager;


    public TaxDeductionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_tax_deduction);
        init();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((TextView) v.findViewById(R.id.title)).setText(R.string.tax_deduction);
        return v;
    }


    private void init() {

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Boolean isOn = (Boolean) newValue;
        String msg;
        switch (preference.getKey()) {
        }

        return false;
    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (null == preference.getKey()) {
            return false;
        }

        switch (preference.getKey()) {

            case "support_parents":
                Intent intent = new Intent(getActivity(), SupportParentsActivity.class);
                //intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_START_MAP_TASK_LIST);
                startActivity(intent);
                break;

            case "raise_children":
//                Intent starMapIntent1 = new Intent(getActivity(), StarMapTaskListActivity.class);
//                starMapIntent1.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_START_MAP_TASK_LIST);
//                startActivity(starMapIntent1);
                break;

            case "interest_of_loans":

                break;

            case "treatment_of_diseases":

                break;
            case "xljy":

                break;
            case "zyzgjy":

                break;
            case "fzba":

                break;


        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


    @Override
    public void onResume() {
        super.onResume();
        ListView listView = ((ListView) getView().findViewById(android.R.id.list));
        listView.setDivider(getResources().getDrawable(R.color.fragment_around_background));
        listView.setDividerHeight(20);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
