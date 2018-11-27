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
public class ProfileFragment extends PreferenceFragment{

    private final String KEY_SOFTWARE_VERION = "software_version";
    private Dialog confirmCancelDialog;
    ProgressDialog progressDialog;


    private void showConfirmDialog(String prompt) {
        dismissConfirmDialog();
        confirmCancelDialog = DialogUtils.showConfirmDialog(getActivity(), prompt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCancelDialog.dismiss();
            }
        });
    }

    private void dismissConfirmDialog() {
        if (null != confirmCancelDialog && confirmCancelDialog.isShowing()) {
            confirmCancelDialog.dismiss();
            confirmCancelDialog = null;
        }
    }


    private List<String> distanceSelectDatas;

    private int selectPosition = -1;

    private String units;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_profile);
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            android.util.Log.d("zyx", "packageInfo name =" + packageInfo.versionName);
            ((WifiMonitorPreference) findPreference(KEY_SOFTWARE_VERION)).setMoreInfo(packageInfo
                    .versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((TextView) v.findViewById(R.id.title)).setText(R.string.my_profile);
        return v;
    }



//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//        Boolean isOn = (Boolean) newValue;
//        String msg;
//        switch (preference.getKey()) {
//
//            case "orienteer_scan":
//
//                break;
//
//            case "debug_mode":
//
//                break;
//        }
//
//        return false;
//    }



    private void switchOpration(Preference preference, boolean check, String msg) {
        SwitchPreference sp = (SwitchPreference) preference;
        sp.setChecked(check);
        if (null != msg) {
            ToastUtils.showShort(ProfileFragment.this.getActivity(), msg);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (null == preference.getKey()) {
            return false;
        }

        switch (preference.getKey()) {

            case "software_upgrade":
                String msg = getActivity().getResources().getString(R.string.settings_online_upgrade_version_is_updated);
                ToastUtils.showShort(ProfileFragment.this.getActivity(), msg);
                break;

            case "export_all_data":

                break;

            case "warning_tone":

                break;

            case "network_outage_distance":

                break;

            case "device_reboot":

                break;

            case "synchronize_data":

                break;

            case "sd_expand":

                break;

            case "firmware_update":

                break;

        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ListView listView = ((ListView) getView().findViewById(android.R.id.list));
//        listView.setDivider(getResources().getDrawable(R.color.fragment_around_background));
//        listView.setDividerHeight(20);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
