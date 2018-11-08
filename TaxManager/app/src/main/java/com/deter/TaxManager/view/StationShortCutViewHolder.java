package com.deter.TaxManager.view;

import android.view.View;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-7-21.
 */

public class StationShortCutViewHolder extends BaseViewHolder {

    View stationInfo;

    TextView stationName;

    TextView stationMac;

    View doubleLine;

    View mitmApInfo;

    TextView mitmApName;

    TextView ip;

    View deauthInfo;

    View mitmInfo;

    View blacklistInfo;

    View whitelistInfo;


    public StationShortCutViewHolder(View itemView) {
        super(itemView);

        stationInfo = itemView.findViewById(R.id.station_info);
        stationMac = (TextView) itemView.findViewById(R.id.station_mac);
        stationName = (TextView) itemView.findViewById(R.id.station_name);
        doubleLine = itemView.findViewById(R.id.double_line);

        mitmApInfo = itemView.findViewById(R.id.mitm_ap_info);
        mitmApName = (TextView) itemView.findViewById(R.id.mitm_ap_name);
        ip = (TextView) itemView.findViewById(R.id.ip);

        deauthInfo = itemView.findViewById(R.id.deauch_info);

        mitmInfo = itemView.findViewById(R.id.mitm_info);

        blacklistInfo = itemView.findViewById(R.id.blacklist_info);

        whitelistInfo = itemView.findViewById(R.id.whitelist_info);


    }
}