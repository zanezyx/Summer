package com.deter.TaxManager.view;

import android.view.View;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-9.
 */

public class APDetailViewHolder extends BaseViewHolder {

    public TextView apName;

    public TextView apMac;

    public TextView date;

    public TextView distance;

    public TextView channel;

    public View deauthInfo;

    public View stationInfo;

    public TextView stationNumber;


    public APDetailViewHolder(View itemView) {
        super(itemView);

        apName = (TextView) itemView.findViewById(R.id.ap_name);
        apMac = (TextView) itemView.findViewById(R.id.ap_mac);

        date = (TextView) itemView.findViewById(R.id.date);
        distance = (TextView) itemView.findViewById(R.id.distance);
        channel = (TextView) itemView.findViewById(R.id.channel);

        deauthInfo = itemView.findViewById(R.id.deauch_info);

        stationNumber = (TextView) itemView.findViewById(R.id.station_number);
        stationInfo = itemView.findViewById(R.id.station_info);

    }
}
