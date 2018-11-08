package com.deter.TaxManager.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-9.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public ProgressImgView deauthIndicator;

    public TextView deauthTitle;


    public ProgressImgView mitmIndicator;

    public TextView mitmTitle;


    public ImageView whitelistIndicator;

    public TextView whitelistTitle;

    public ImageView blacklistIndicator;

    public TextView blacklistTitle;


    public BaseViewHolder(View itemView) {
        super(itemView);

        deauthIndicator = (ProgressImgView) itemView.findViewById(R.id.deauch_indicator);
        deauthTitle = (TextView) itemView.findViewById(R.id.deauch_title);

        mitmIndicator = (ProgressImgView) itemView.findViewById(R.id.mitm_indicator);
        mitmTitle = (TextView) itemView.findViewById(R.id.mitm_title);

        whitelistIndicator = (ImageView) itemView.findViewById(R.id.whitelist_indicator);
        whitelistTitle = (TextView) itemView.findViewById(R.id.whitelist_title);

        blacklistIndicator = (ImageView) itemView.findViewById(R.id.blacklist_indicator);
        blacklistTitle = (TextView) itemView.findViewById(R.id.blacklist_title);
    }
}
