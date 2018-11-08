package com.deter.TaxManager;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AroundTabFragment extends Fragment implements View.OnClickListener {

    public AroundTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_around_tab, container, false);

        ((TextView) v.findViewById(R.id.title)).setText(R.string.around);

        View aroundAP = v.findViewById(R.id.around_ap);
        View aroundStation = v.findViewById(R.id.around_station);
        View specialScan = v.findViewById(R.id.around_special_search);
        View forceConnected = v.findViewById(R.id.around_force_connected);
        View forceDisconnected = v.findViewById(R.id.around_force_disconnected);
        View blacklist = v.findViewById(R.id.around_blacklist);
        View whitelist = v.findViewById(R.id.around_whitelist);
        View ssidKeyStore = v.findViewById(R.id.around_ssid);
        View webpages = v.findViewById(R.id.webpages_search);
        View virtualID = v.findViewById(R.id.around_virtual_id);
        View filterlist = v.findViewById(R.id.filterlist);

        aroundAP.setOnClickListener(this);
        aroundStation.setOnClickListener(this);
        specialScan.setOnClickListener(this);
        forceDisconnected.setOnClickListener(this);
        forceConnected.setOnClickListener(this);
        blacklist.setOnClickListener(this);
        whitelist.setOnClickListener(this);
        ssidKeyStore.setOnClickListener(this);
        webpages.setOnClickListener(this);
        virtualID.setOnClickListener(this);
        filterlist.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AroundActivity.class);
        switch (v.getId()) {
            case R.id.around_ap:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_AROUND_AP);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string.around_ap));
                break;
            case R.id.around_station:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_AROUND_STATION);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string.around_station));
                break;
            case R.id.around_special_search:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_SPECIAL_SEARCH);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string
                        .around_specail_search));
                break;
            case R.id.around_force_connected:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_FORCE_CONNECTED);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string
                        .around_force_connected));
                break;
            case R.id.around_force_disconnected:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_FORCE_DISCONNECTED);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string
                        .around_force_disconnected));
                break;
            case R.id.around_blacklist:
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string.around_blacklist));
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_BLACKLIST);
                intent.setClass(getActivity(), BlackWhiteListActivity.class);
                break;
            case R.id.around_whitelist:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_WHITELIST);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string.around_whitelist));
                intent.setClass(getActivity(), BlackWhiteListActivity.class);
                break;
            case R.id.around_ssid:
                intent.setClass(getActivity(), SsidPwdLibraryActivity.class);
                break;
            case R.id.webpages_search:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_WEBPAGES_SEARCH);
                intent.setClass(getActivity(), SearchActivity.class);
                break;
            case R.id.around_virtual_id:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_VIRTUAL_ID);
                intent.setClass(getActivity(), SearchActivity.class);
                break;
            case R.id.filterlist:
                intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_FILTER);
                intent.putExtra(APPConstans.KEY_DETAIL_TITLE, getString(R.string
                        .around_filterlist));
                intent.setClass(getActivity(), BlackWhiteListActivity.class);
                break;
        }

        startActivity(intent);

    }
}
