package com.deter.TaxManager.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.ToastUtils;

/**
 * Created by zhongqiusheng on 2017/8/11 0011.
 */
public class AddSSIDAccountPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout parentView;

    private EditText addSsidAccountEt;

    private EditText addSsidPwdEt;

    private Button btnOprCancel;

    private Button btnOprConfirm;

    private OnConfirmCallback onConfirmCallback;

    public AddSSIDAccountPopupWindow(Context context, OnConfirmCallback myConfirmCallback) {
        super(context, R.layout.layout_add_ssid_account_popwindow);
        this.onConfirmCallback = myConfirmCallback;
    }

    public void setSSID(String ssid) {
        if (!TextUtils.isEmpty(ssid)) {
            addSsidAccountEt.setText(ssid);
            addSsidAccountEt.clearFocus();
            addSsidPwdEt.requestFocus();
        }
    }

    @Override
    public void convert(View contentView) {
        parentView = getViewById(R.id.add_ssid_account_parent_view);
        addSsidAccountEt = getViewById(R.id.add_ssid_account_et);
        addSsidPwdEt = getViewById(R.id.add_ssid_pwd_et);
        btnOprCancel = getViewById(R.id.btn_opr_cancel);
        btnOprConfirm = getViewById(R.id.btn_opr_confirm);
        btnOprCancel.setOnClickListener(this);
        btnOprConfirm.setOnClickListener(this);
        parentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_opr_cancel:
                dismiss();
                break;
            case R.id.btn_opr_confirm:
                onConfrimClick();
                break;
        }
    }

    DataManager  dataManager;

    private void onConfrimClick() {

        String ssid = addSsidAccountEt.getText().toString();
        String pwd = addSsidPwdEt.getText().toString();
        if (!TextUtils.isEmpty(pwd)){
            if (TextUtils.isEmpty(ssid)) {
                ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.ssid_account_input_prompt));
                return;
            }
        }

/*        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string
            .ssid_pwd_input_prompt));
            return;
        }*/


        //If there is a password ,So save to the database
        if (addSsidPwdEt.getText().toString().trim().length() > 0) {

            final TopAP topAP = new TopAP();
            topAP.setSsid(ssid);
            if (TextUtils.isEmpty(pwd)) {
                topAP.setPassword(null);
            } else {
                topAP.setPassword(pwd);
            }

            final SSID ssid1 = new SSID();
            ssid1.setSsid(ssid);
            if (TextUtils.isEmpty(pwd)) {
                ssid1.setPassword(null);
            } else {
                ssid1.setPassword(pwd);
            }

            if (TextUtils.isEmpty(pwd)) {
                ssid1.setEncryption("OPEN");
                topAP.setEncryption("OPEN");
            } else {
                ssid1.setEncryption("WPA2");
                topAP.setEncryption("WPA2");
            }

            //save ssid account and password to password library
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (null == dataManager) {
                        dataManager = DataManager.getInstance(mContext);
                    }

                    boolean isExist = APPUtils.ssidIsExistInSSIDLibrary(ssid1, dataManager);

                    if (!isExist) {
                        DataManager.getInstance(mContext).addSsidToDb(ssid1);
                    }

                    boolean isTopApExist = APPUtils.topApIsExistInTopListLibrary(topAP, dataManager);
                    if (!isTopApExist) {
                        DataManager.getInstance(mContext).addTopApToDb(topAP);
                    }

                }
            }).start();
        }

        onConfirmCallback.onConfirm(ssid, pwd);
        dismiss();
    }


    public interface OnConfirmCallback {

        void onConfirm(String ssid, String password);
    }

}
