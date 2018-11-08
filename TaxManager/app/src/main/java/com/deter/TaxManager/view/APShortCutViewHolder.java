package com.deter.TaxManager.view;

import android.view.View;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-9.
 */

public class APShortCutViewHolder extends BaseViewHolder {

    public static final String TAG = "APShortCutViewHolder";

    public View apInfo;

    public TextView apName;

    public TextView apMac;

    public View deauthInfo;

    public View stationNumberInfo;

    public TextView stationNumber;

    public APShortCutViewHolder(View itemView) {

        super(itemView);

        apInfo = itemView.findViewById(R.id.ap_info);
        apName = (TextView) itemView.findViewById(R.id.ap_name);
        apMac = (TextView) itemView.findViewById(R.id.ap_mac);

        deauthInfo = itemView.findViewById(R.id.deauch_info);

        stationNumberInfo = itemView.findViewById(R.id.station_number_info);
        stationNumber = (TextView) itemView.findViewById(R.id.station_number);
    }
}
