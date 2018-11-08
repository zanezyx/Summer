package com.deter.TaxManager.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-9.
 */

public class StationDetailViewHolder extends BaseViewHolder {

    TextView stationName;

    TextView stationMac;

    TextView date;

    TextView mitm_name;

    TextView station_ip;

    View apInfo;

    TextView networkType;

    TextView ssidName;

    TextView encryptionType;

    TextView distance;

    View mitmInfo;

    View blacklistInfo;

    View whitelistInfo;

    View deauthInfo;

    View alermInfo;

    ImageView alermIndicator;

    TextView alermTitle;


    View virtualIdInfo;

    ImageView virtualIdIndicator;

    TextView virtaulIdTitle;

    public StationDetailViewHolder(View itemView) {
        super(itemView);
        stationName = (TextView) itemView.findViewById(R.id.station_name);
        stationMac = (TextView) itemView.findViewById(R.id.station_mac);
        date = (TextView) itemView.findViewById(R.id.date);

        mitm_name = (TextView) itemView.findViewById(R.id.ap_name);
        station_ip = (TextView) itemView.findViewById(R.id.ap_ip);
        apInfo = itemView.findViewById(R.id.ap_info);

        networkType = (TextView) itemView.findViewById(R.id.network_type);
        ssidName = (TextView) itemView.findViewById(R.id.ssid_name);
        encryptionType = (TextView) itemView.findViewById(R.id.encryption_type);
        distance = (TextView) itemView.findViewById(R.id.distance);

        mitmInfo = itemView.findViewById(R.id.mitm_info);
        blacklistInfo = itemView.findViewById(R.id.blacklist_info);
        whitelistInfo = itemView.findViewById(R.id.whitelist_info);
        deauthInfo = itemView.findViewById(R.id.deauch_info);


        alermInfo = itemView.findViewById(R.id.alerm_info);
        alermIndicator = (ImageView) itemView.findViewById(R.id.alerm_indicator);
        alermTitle = (TextView) itemView.findViewById(R.id.alerm_title);

        virtualIdInfo = itemView.findViewById(R.id.virtual_id_info);
        virtualIdIndicator = (ImageView) itemView.findViewById(R.id.virtual_id_indicator);
        virtaulIdTitle = (TextView) itemView.findViewById(R.id.virtual_id_title);


    }
}
