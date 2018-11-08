package com.deter.TaxManager;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.Log;

import java.io.File;
import java.util.List;

import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_ONRESULT;
import static com.deter.TaxManager.network.BuzokuFuction.DETER_DIR_NAME;

public class PackageUpdateActivity extends AppCompatActivity {

    public static final int MESSAGE_UPDATE_PROGRESS = 0;

    public static final int MESSAGE_UPDATE_NEWVERSION_INFO = 1;

    public static final int MESSAGE_UPDATE_CURRENTVERSION_INFO = 2;

    public static final int MESSAGE_UPDATE_PKG_DOWNLOAD_COMPLETE = 3;

    public static final String archiveFilePath = Environment.getExternalStorageDirectory() + "/"
            + DETER_DIR_NAME;

    private static String TAG = "PackageUpdateActivity";

    private int currentVersionCode = -1;

    private Dialog dialog;

    private View currentOpt;

    private TextView currentVersionTv;

    private ProgressBar progressBar;

    private TextView checkVersionHint;

    private View updateOpt;

    private TextView textViewVersionInfo;

    private TextView buttonUpgrade;

    private TextView buttonCancle;

    private TextView buttonConfirm;

    private TextView dialogVersionInfo;

    private DownloadService.VersionInfo newVersionInfo;

    private DownloadService downloadService;

    private boolean downloadComplete = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LISTENER_ONRESULT:
                    onResult(msg.arg1, (List<Object>) msg.obj);
                    break;
                case MESSAGE_UPDATE_PROGRESS:
                    updateProgress(msg.arg1);
                    break;
                case MESSAGE_UPDATE_NEWVERSION_INFO:
                    currentOpt.setVisibility(View.GONE);
                    DownloadService.VersionInfo versionInfo = (DownloadService.VersionInfo) msg.obj;
                    Log.d(TAG, "new version info =" + versionInfo.toString());
                    updateNewVersionInfo(versionInfo.getVersionName(), versionInfo.getVersionNote
                            ());
                    break;
                case MESSAGE_UPDATE_CURRENTVERSION_INFO:
                    updateCurrentVersionInfo();
                    break;
                case MESSAGE_UPDATE_PKG_DOWNLOAD_COMPLETE:
                    downloadComplete = true;
                    if (downloadService != null) {
                        downloadService.installNewPkg();
                        dissmissDialog();
                    }
                    break;
                default:
                    Log.d(TAG, "shouldn't go to here! msg id" + msg.what);
            }

        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadServiceBinder serviceBinder = (DownloadService
                    .DownloadServiceBinder) service;
            downloadService = serviceBinder.getService();
            downloadService.setHandler(handler);
            downloadService.checkNewVersion();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            downloadService = null;
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update:
                    showConfirmDialog();
                    break;
                case R.id.btn_update_cancle:
                    dissmissDialog();
                    break;
                case R.id.btn_update_confirm:
                    if (!checkNewPkgIsExist()) {
                        if (downloadService != null) {
                            downloadService.starDownloadPkg(newVersionInfo);
                        }
                    } else {
                        if (downloadService != null) {
                            downloadService.installNewPkg();
                            dissmissDialog();
                        }
                    }
                    break;
                case R.id.back:
                    finish();
                    break;
                default:
                    Log.d(TAG, "shouldn't go here !");
            }
        }
    };


    private boolean checkNewPkgIsExist() {
        if (!FileUtils.isFolderExist(archiveFilePath)) {
            return false;
        }
        List<String> apklist = FileUtils.getFileListWithExtention(archiveFilePath, "apk");
        if (apklist == null || apklist.size() == 0) {
            return false;
        }
        String apkFilePath = archiveFilePath + "/" + apklist.get(0);
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            String filemd5 = FileUtils.getMD5(new File(apkFilePath));
            Log.d(TAG, "versionName =" + versionName + ", new versionname =" + newVersionInfo
                    .getVersionName());
            return packageName.equals(getPackageName()) && versionName.equals(newVersionInfo
                    .getVersionName()) && versionCode == newVersionInfo.getVersionCode() &&
                    filemd5.equals(newVersionInfo.getFileMD5());
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_update);
        initView();
        initServie();
    }

    private void initView() {
        TextView textView = (TextView) findViewById(R.id.title);
        View back = findViewById(R.id.back);
        textView.setText(R.string.settings_online_upgrade);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(onClickListener);

        currentOpt = findViewById(R.id.current_opt);
        currentVersionTv = (TextView) findViewById(R.id.current_version_info);
        progressBar = (ProgressBar) findViewById(R.id.check_version_progress);
        progressBar.setIndeterminate(true);
        checkVersionHint = (TextView) findViewById(R.id.check_version_hint);
        updateOpt = findViewById(R.id.update_opt);
        textViewVersionInfo = (TextView) findViewById(R.id.newversion_info);
        buttonUpgrade = (TextView) findViewById(R.id.btn_update);

        buttonUpgrade.setOnClickListener(onClickListener);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionTv.setText(getString(R.string
                    .settings_online_upgrade_current_version_hint, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initServie() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadService != null) downloadService.setHandler(null);
        handler.removeCallbacksAndMessages(null);
        unbindService(serviceConnection);
        if (downloadComplete) {
            stopService(new Intent(this, DownloadService.class));
        }
    }

    private void showConfirmDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(this, R.style.Theme_dialog_update_pkg);
        dialog.setContentView(R.layout.dialog_pkg_update_reminder);

        Window window = dialog.getWindow();
        buttonCancle = (TextView) window.findViewById(R.id.btn_update_cancle);
        buttonConfirm = (TextView) window.findViewById(R.id.btn_update_confirm);
        dialogVersionInfo = (TextView) window.findViewById(R.id.version_releasenote);
        dialogVersionInfo.setText(newVersionInfo.getVersionNote());

        buttonCancle.setOnClickListener(onClickListener);
        buttonConfirm.setOnClickListener(onClickListener);

        android.view.WindowManager.LayoutParams p = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        p.width = (int) (displayWidth * 0.80);
        p.height = (int) (displayHeight * 0.60);
        dialog.getWindow().setAttributes(p);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog = null;
                buttonCancle = null;
                buttonConfirm = null;
            }
        });
        dialog.show();
    }

    private void dissmissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void updateProgress(int progress) {
        if (dialog != null && dialog.isShowing()) {
            buttonConfirm.setText(getString(R.string
                    .settings_online_upgrade_dialog_btn_download_progress, progress));
        }
    }

    private void updateCurrentVersionInfo() {
        progressBar.setVisibility(View.GONE);
        checkVersionHint.setText(R.string.settings_online_upgrade_version_is_updated);

    }

    private void updateNewVersionInfo(String versionName, String versionNote) {
        updateOpt.setVisibility(View.VISIBLE);
        textViewVersionInfo.setText(getString(R.string.settings_online_upgrade_new_version_msg,
                versionName));
    }


    private void onResult(int functionid, List<Object> resultList) {
        if (functionid != BuzokuFuction.FUCTION_CHECK_APP_UPDATE) {
            return;
        }
        if (resultList == null || resultList.size() != 5) {
            handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_CURRENTVERSION_INFO, 0);
            //Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            return;
        }
        String versionName = (String) resultList.get(0);
        int versionCode = Integer.valueOf((String) resultList.get(1));
        String downloadUrl = (String) resultList.get(2);
        String fileMD5 = (String) resultList.get(3);
        String versionNote = (String) resultList.get(4);

        if (currentVersionCode < versionCode) {
            newVersionInfo = new DownloadService.VersionInfo(versionName, versionCode,
                    downloadUrl, fileMD5, versionNote);
            Message message = handler.obtainMessage(MESSAGE_UPDATE_NEWVERSION_INFO);
            message.obj = newVersionInfo;
            handler.sendMessage(message);
        } else {
            handler.sendEmptyMessage(MESSAGE_UPDATE_CURRENTVERSION_INFO);
        }

    }

}
