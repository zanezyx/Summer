package com.deter.TaxManager.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-21.
 */

public class WifiMonitorPreference extends Preference {

    private TextView moreInfoTextView;

    private CharSequence moreInfo;

    public WifiMonitorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.layout_preference);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        moreInfoTextView = (TextView) view.findViewById(R.id.more_info);
        moreInfoTextView.setText(moreInfo);
    }

    public CharSequence getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(CharSequence charSequence) {
        moreInfo = charSequence;
        notifyChanged();
    }
}
