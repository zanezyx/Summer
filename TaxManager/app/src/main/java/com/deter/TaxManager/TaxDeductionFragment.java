package com.deter.TaxManager;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;


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
                Intent intent1 = new Intent(getActivity(), RaiseChildrenActivity.class);
                startActivity(intent1);
                break;

            case "interest_of_loans":
                Intent intent2 = new Intent(getActivity(), RoanInterestActivity.class);
                startActivity(intent2);
                break;

            case "treatment_of_diseases":
                Intent intent3 = new Intent(getActivity(), TreatmentActivity.class);
                startActivity(intent3);
                break;
            case "xljy":
                Intent intent4 = new Intent(getActivity(), EducationActivity.class);
                startActivity(intent4);
                break;
            case "zyzgjy":
                Intent intent5 = new Intent(getActivity(), VocationActivity.class);
                startActivity(intent5);
                break;
            case "fzba":
                Intent intent6 = new Intent(getActivity(), RentActivity.class);
                startActivity(intent6);
                break;
            case "bsp":
                Intent intent7 = new Intent(getActivity(), BspActivity.class);
                startActivity(intent7);
                break;

            case "insurance":
                Intent intent8 = new Intent(getActivity(), InsuranceActivity.class);
                startActivity(intent8);
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
