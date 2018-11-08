package com.deter.TaxManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.utils.APPUtils;

import java.util.List;

import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_ONRESULT;
import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_POSTEXECUTE;

public class FirmwareUpdateActivity extends AppCompatActivity implements USBChangeListener {

    private static final String TAG = "FirmwareUpdateActivity";

    private static final int MESSAGE_NETWORK_ERROR = 1;

    private static final int MESSAGE_USBTETHER_NOT_ENABLE = 2;

    private static final int MESSAGE_UPDATE_VERSION_INFO = 3;

    private static final int MESSAGE_SHOW_PROGRESS_DIALOG = 4;

    private static final int MESSAGE_DISSMISS_PROGRESS_DIALOG = 5;

    private final int UPDATE_TYPE_LAST_VERSION = 1000;

    private final int UPDATE_TYPE_SPECIAL_VERSION = 1001;

    private final int UPDATE_TIMEOUT_TIMES = 3*60000;

    private String currentVersionName;

    private String currentBuildID = "-1";

    private String newVersionName;

    private String newBuildID = "-1";

    private boolean isCheckedCurrentVersion = false;

    private boolean isCheckedLastVersion = false;

    private ProgressBar CheckVersionProgressBar;

    private TextView checkVersionHint;

    private View checkInfoView;

    private View updateOptLast;

    private View updateOptSpecial;

    private TextView checkedVirsionInfoView;

    private TextView updateLastButton;

    private TextView updateSpecialButton;

    private EditText versionNumberEditTv;

    private AlertDialog usbTetherDialog;

    private ProgressDialog dialog;

    private BuzokuFuction buzokuFuction;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LISTENER_POSTEXECUTE:
                    onPostExecute(msg.arg1, msg.arg2);
                    break;
                case MESSAGE_LISTENER_ONRESULT:
                    onResult(msg.arg1, (List<Object>) msg.obj);
                    break;
                case MESSAGE_NETWORK_ERROR:
                    Toast.makeText(FirmwareUpdateActivity.this, R.string
                            .settings_firmware_upgrade_network_error, Toast.LENGTH_SHORT).show();
                    showNoVersionInfo();
                    break;
                case MESSAGE_USBTETHER_NOT_ENABLE:
                    Toast.makeText(FirmwareUpdateActivity.this, R.string
                            .settings_firmware_upgrade_usbtether_error, Toast.LENGTH_SHORT).show();
                    showNoVersionInfo();
                    break;
                case MESSAGE_UPDATE_VERSION_INFO:
                    if (!currentBuildID.equals("-1")  && !newBuildID.equals("-1")) {
                        showVersionInfo();
                    } else {
                        showNoVersionInfo();
                    }
                    break;
                case MESSAGE_SHOW_PROGRESS_DIALOG:
                    break;
                case MESSAGE_DISSMISS_PROGRESS_DIALOG:
                    dissmissProgressDialog();
                    break;
                default:

            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.btn_update_last:
                    if (currentBuildID.compareToIgnoreCase(newBuildID) >= 0) {
                        Toast.makeText(FirmwareUpdateActivity.this, R.string
                                .settings_online_upgrade_version_is_updated, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        updateFirmware(UPDATE_TYPE_LAST_VERSION);
                    }
                    break;
                case R.id.btn_update_special:
                    updateFirmware(UPDATE_TYPE_SPECIAL_VERSION);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_update);
        USBChangeBroadcastReceiver.addUSBChangeListener(this);
        initView();
        buzokuFuction = BuzokuFuction.getInstance(this);
        checkVersionInfo();
    }

    private void initView() {
        TextView textView = (TextView) findViewById(R.id.title);
        View back = findViewById(R.id.back);
        textView.setText(R.string.settings_firmware_upgrade_title);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(onClickListener);

        checkInfoView = findViewById(R.id.check_info);
        CheckVersionProgressBar = (ProgressBar) findViewById(R.id.check_version_progress);
        checkVersionHint = (TextView) findViewById(R.id.check_version_hint);

        updateOptLast = findViewById(R.id.update_opt1);
        updateOptSpecial = findViewById(R.id.update_opt2);
        checkedVirsionInfoView = (TextView) findViewById(R.id.checked_version_info);
        updateLastButton = (TextView) findViewById(R.id.btn_update_last);
        updateSpecialButton = (TextView) findViewById(R.id.btn_update_special);
        versionNumberEditTv = (EditText) findViewById(R.id.version_number);

        updateLastButton.setOnClickListener(onClickListener);
        updateSpecialButton.setOnClickListener(onClickListener);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        USBChangeBroadcastReceiver.removeUSBChangeListener(this);
        handler.removeCallbacksAndMessages(null);
    }

    private void showNoVersionInfo() {
        CheckVersionProgressBar.setVisibility(View.GONE);
        checkVersionHint.setText(R.string.settings_firmware_upgrade_no_versoin_info);
    }

    private void showVersionInfo() {
        checkInfoView.setVisibility(View.GONE);
        updateOptLast.setVisibility(View.VISIBLE);
        updateOptSpecial.setVisibility(View.VISIBLE);
        checkedVirsionInfoView.setText(getString(R.string.settings_firmware_upgrade_version_info,
                currentBuildID, newBuildID));
        if (currentBuildID.compareToIgnoreCase(newBuildID) >= 0) {
            updateLastButton.setText(R.string.settings_firmware_upgrade_version_is_updated);
        }
    }

    private boolean checkNetworkAndUsbTetherState() {
        if (!APPUtils.isNetworkConnected(this)) {
            handler.sendEmptyMessage(MESSAGE_NETWORK_ERROR);
            return false;
        }
        if (!APPUtils.isUsbTethered(this)) {
            handler.sendEmptyMessage(MESSAGE_USBTETHER_NOT_ENABLE);
            ShowUsbTetherDialog(this);
            return false;
        }
        return true;
    }

    private void checkVersionInfo() {
        if (checkNetworkAndUsbTetherState()) {
            checkcurrentVersionInfo();
            checkLastVersionInfo();
        }
    }

    private void checkcurrentVersionInfo() {
        buzokuFuction.queryPiSystemVersion(new MyFunctionListener(handler));

    }

    private void checkLastVersionInfo() {
        buzokuFuction.queryCheckPiUpdate(new MyFunctionListener(handler));
    }

    private void updateFirmware(int updateVersionType) {
        if (checkNetworkAndUsbTetherState()) {
            if (updateVersionType == UPDATE_TYPE_LAST_VERSION) {
                showProgressDialog(R.string.settings_firmware_upgrade_version_in_progress);
                updateLastVersion();
            } else if (updateVersionType == UPDATE_TYPE_SPECIAL_VERSION) {
                showProgressDialog(R.string.settings_firmware_upgrade_version_in_progress);
                updateSpecialVersion();
            }
        }
    }

    private void updateLastVersion() {
        buzokuFuction.startPiSystemUpdate(null, new MyFunctionListener(handler));
    }

    private void updateSpecialVersion() {
        String versionNumber = versionNumberEditTv.getText().toString();
        if (TextUtils.isEmpty(versionNumber)) {
            Toast.makeText(this, R.string.settings_firmware_upgrade_input_hint, Toast
                    .LENGTH_SHORT).show();
        } else {
            buzokuFuction.startPiSystemUpdate(versionNumber, new
                    MyFunctionListener(handler));
        }
    }

    private void onPostExecute(int functionid, int result) {
        switch (functionid) {
            case BuzokuFuction.FUCTION_PI_SW_UPDATE:
                if (result == 0) {
                    Log.d(TAG, "the pi update command is success done");
                    handler.sendEmptyMessageDelayed(MESSAGE_DISSMISS_PROGRESS_DIALOG, UPDATE_TIMEOUT_TIMES);
                } else {
                    Log.d(TAG, "the pi update command is fail!");
                    Toast.makeText(this, R.string.settings_firmware_upgrade_error, Toast
                            .LENGTH_SHORT).show();
                    handler.sendEmptyMessage(MESSAGE_DISSMISS_PROGRESS_DIALOG);
                }
                break;
        }
    }

    private void onResult(int functionid, List<Object> result) {
        switch (functionid) {
            case BuzokuFuction.FUCTION_PI_SW_VERSION:
                Log.d(TAG, "FUCTION_PI_SW_VERSION");
                if (result == null) {
                    Log.d(TAG, "FUCTION_PI_SW_VERSION,result null");
                    Toast.makeText(this, R.string.settings_firmware_upgrade_no_versoin_info, Toast
                            .LENGTH_SHORT).show();
                } else if (result.size() == 2) {
                    currentVersionName = (String) result.get(0);
                    currentBuildID = (String) result.get(1);
                    Log.d(TAG, "currentVersionName =" + currentVersionName + ",currentBuildID ="
                            + currentBuildID);
                } else {
                    Log.d(TAG, "FUCTION_PI_SW_VERSION,result size not 2");
                }
                isCheckedCurrentVersion = true;
                if (isCheckedLastVersion && isCheckedCurrentVersion) {
                    handler.sendEmptyMessage(MESSAGE_UPDATE_VERSION_INFO);
                }
                break;
            case BuzokuFuction.FUCTION_CHECK_PI_UPDATE:
                Log.d(TAG, "FUCTION_CHECK_PI_UPDATE");
                if (result == null) {
                    Log.d(TAG, "FUCTION_CHECK_PI_UPDATE,result null");
                    Toast.makeText(this, R.string.settings_firmware_upgrade_no_versoin_info, Toast
                            .LENGTH_SHORT).show();
                } else if (result.size() == 2) {
                    newVersionName = (String) result.get(0);
                    newBuildID = (String) result.get(1);
                    Log.d(TAG, "newVersionName =" + newVersionName + ",newBuildID =" + newBuildID);
                } else {
                    Log.d(TAG, "FUCTION_CHECK_PI_UPDATE,result size not 2");
                }
                isCheckedLastVersion = true;
                if (isCheckedLastVersion && isCheckedCurrentVersion) {
                    handler.sendEmptyMessage(MESSAGE_UPDATE_VERSION_INFO);
                }
                break;
        }

    }


    private void ShowUsbTetherDialog(final Context context) {
        if (usbTetherDialog != null && usbTetherDialog.isShowing()) return;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.app_usbtether_off_title);
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.android.settings", "com.android" +
                            ".settings.Settings$TetherSettingsActivity"));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    com.deter.TaxManager.utils.Log.d(TAG, "can't find the " +
                            "activity---Settings$TetherSettingsActivity");
                }
            }
        });
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        usbTetherDialog = alertDialog.create();
        usbTetherDialog.show();
    }


    private void dissUsbTetherDialog() {
        if (usbTetherDialog != null && usbTetherDialog.isShowing()) {
            usbTetherDialog.dismiss();
        }
    }

    private void showProgressDialog(int resId) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(getString(resId));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void dissmissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onUSBConnectChanged(boolean isConnected) {
        if (isConnected) {
            dissUsbTetherDialog();
            dissmissProgressDialog();
            if (!APPUtils.isUsbTethered(this)) {
                ShowUsbTetherDialog(this);
            }
        } else {

        }
    }


}
