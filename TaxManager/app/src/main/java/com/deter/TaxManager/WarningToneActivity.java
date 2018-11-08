package com.deter.TaxManager;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.utils.ToastUtils;

/**
 * Created by zhongqiusheng on 2017/8/24 0024.
 */
public class WarningToneActivity extends PreferenceActivity implements Preference
        .OnPreferenceChangeListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = LayoutInflater.from(this).inflate(R.layout.fragment_profile, null);
        ((TextView) v.findViewById(R.id.title)).setText(R.string.settings_alarm);
        ((ListView) v.findViewById(android.R.id.list)).setDividerHeight(40);
        View backView = v.findViewById(R.id.back);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setContentView(v);

        addPreferencesFromResource(R.xml.warning_tone);

        init();
    }


    private void init() {
        SwitchPreference blackWhitePreference = (SwitchPreference) getPreferenceManager().findPreference("warning_tone_black_white");
        SwitchPreference collisionPreference = (SwitchPreference) getPreferenceManager().findPreference("warning_tone_collision");
        SwitchPreference accompanyPreference = (SwitchPreference) getPreferenceManager().findPreference("warning_tone_accompany");

        blackWhitePreference.setOnPreferenceChangeListener(this);
        collisionPreference.setOnPreferenceChangeListener(this);
        accompanyPreference.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Boolean isOn = (Boolean) newValue;
        String msg;
        switch (preference.getKey()) {

            case "warning_tone_black_white":
                if (isOn) {
                    msg = getResources().getString(R.string.warning_tone_black_white_open_prompt);
                } else {
                    msg = getResources().getString(R.string.warning_tone_black_white_closed_prompt);
                }
                switchOpration(preference, isOn, msg);

                break;

            case "warning_tone_collision":
                if (isOn) {
                    msg = getResources().getString(R.string.warning_tone_collision_open_prompt);
                } else {
                    msg = getResources().getString(R.string.warning_tone_collision_closed_prompt);
                }
                switchOpration(preference, isOn, msg);

                break;

            case "warning_tone_accompany":
                if (isOn) {
                    msg = getResources().getString(R.string.warning_tone_accompany_open_prompt);
                } else {
                    msg = getResources().getString(R.string.warning_tone_accompany_closed_prompt);
                }
                switchOpration(preference, isOn, msg);
                break;
        }
        return false;
    }


    private void switchOpration(Preference preference, boolean check, String msg) {
        SwitchPreference sp = (SwitchPreference) preference;
        sp.setChecked(check);
        ToastUtils.showShort(this, msg);
        //APPUtils.saveValueToSharePreference(this, oprKey, check);
    }

}
