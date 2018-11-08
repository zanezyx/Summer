package com.deter.TaxManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;

import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.utils.DownloadUtil;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.Log;

import java.io.File;
import java.util.List;

import static com.deter.TaxManager.PackageUpdateActivity.MESSAGE_UPDATE_PKG_DOWNLOAD_COMPLETE;
import static com.deter.TaxManager.PackageUpdateActivity.MESSAGE_UPDATE_PROGRESS;
import static com.deter.TaxManager.PackageUpdateActivity.archiveFilePath;

public class DownloadService extends Service {

    public static final int MESSAGE_UPDATE_NOTIFICATION_INFO = 3;

    private static final String TAG = "DownloadService";

    private final int DOWNLOAD_STATE_NOT_DOWNLOAD = 0;

    private final int DOWNLOAD_STATE_DOWNLOADING = 1;

    private final int DOWNLOAD_STATE_DOWNLOAD_SUCCESS = 2;

    private final int DOWNLOAD_STATE_FAIL = 3;

    private final int NotificationID = 0x10000;

    private int downloadState = DOWNLOAD_STATE_NOT_DOWNLOAD;

    private int mProgress;

    private Context context;

    private Handler handler;

    private BuzokuFuction buzokuFuction;

    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder builder;

    private DownloadServiceBinder downloadServiceBinder = new DownloadServiceBinder();

    private Handler notificationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_UPDATE_NOTIFICATION_INFO) {
                updateNotificationInfo();
            }
        }
    };


    private DownloadUtil.OnDownloadListener onDownloadListener = new DownloadUtil
            .OnDownloadListener() {
        @Override
        public void onDownloadSuccess() {
            downloadState = DOWNLOAD_STATE_DOWNLOAD_SUCCESS;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,
                    0, getInstallIntent(), 0);
            builder.setContentText(getText(R.string.settings_online_upgrade_download_complete));
            builder.setProgress(100, mProgress, false);
            builder.setContentInfo(mProgress + "%");
            builder.setContentIntent(mPendingIntent);
            mNotificationManager.notify(NotificationID, builder.build());
            if (handler != null) {
                handler.sendEmptyMessage(MESSAGE_UPDATE_PKG_DOWNLOAD_COMPLETE);
            } else {
                Log.d(TAG, "activity is destoryed so stop self");
                stopSelf();
            }
        }

        @Override
        public void onDownloading(int progress) {
            mProgress = progress;
            if (handler == null) {
                return;
            }
            Message message = handler.obtainMessage(MESSAGE_UPDATE_PROGRESS);
            message.arg1 = progress;
            handler.sendMessage(message);
        }

        @Override
        public void onDownloadFailed() {
            downloadState = DOWNLOAD_STATE_FAIL;
        }
    };

    private Intent getInstallIntent() {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        List<String> apklist = FileUtils.getFileListWithExtention(archiveFilePath, "apk");
        if (apklist == null || apklist.size() == 0) {
            throw new IllegalArgumentException("the pkg is not exist !");
        }
        String apkFilePath = archiveFilePath + "/" + apklist.get(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + "" +
                    ".fileProvider", new File(apkFilePath));
            installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installIntent.setDataAndType(Uri.fromFile(new File(apkFilePath)), "application/vnd" +
                    ".android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return installIntent;
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setTicker(getText(R.string.settings_online_upgrade_download_pkg_ticker));
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getText(R.string.settings_online_upgrade_download_pkg_title));
        builder.setNumber(0);
        builder.setAutoCancel(true);
        mNotificationManager.notify(NotificationID, builder.build());
        notificationHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_NOTIFICATION_INFO, 150);
    }

    private void updateNotificationInfo() {
        if (downloadState != DOWNLOAD_STATE_DOWNLOADING) return;
        builder.setProgress(100, mProgress, false);
        builder.setContentInfo(mProgress + "%");
        mNotificationManager.notify(NotificationID, builder.build());
        if (mProgress < 100) {
            notificationHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_NOTIFICATION_INFO, 150);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        buzokuFuction = BuzokuFuction.getInstance(this);
    }

    public void checkNewVersion() {
        buzokuFuction.queryCheckAppUpdate(new MyFunctionListener(handler));
    }

    public void starDownloadPkg(VersionInfo newVersionInfo) {
        if (downloadState != DOWNLOAD_STATE_DOWNLOADING) {
            List<String> apklist = FileUtils.getFileListWithExtention(archiveFilePath, "apk");
            if (apklist != null && apklist.size() > 0) {
                FileUtils.deleteFile(archiveFilePath + "/" + apklist.get(0));
            }
            buzokuFuction.startDownloadFile(newVersionInfo.getVersionUrl(),
                    onDownloadListener);
            downloadState = DOWNLOAD_STATE_DOWNLOADING;
            initNotification();
        }

    }

    public void installNewPkg() {
        startActivity(getInstallIntent());
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NotificationID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return downloadServiceBinder;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public static class VersionInfo {

        private String versionName;

        private int versionCode;

        private String versionUrl;

        private String fileMD5;

        private String versionNote;

        public VersionInfo(String versionName, int versionCode, String versionUrl, String
                fileMD5, String versionNote) {
            this.versionName = versionName;
            this.versionCode = versionCode;
            this.versionUrl = versionUrl;
            this.fileMD5 = fileMD5;
            this.versionNote = versionNote;
        }

        public String getVersionName() {
            return versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public String getFileMD5() {
            return fileMD5;
        }

        public String getVersionNote() {
            return versionNote;
        }

        @Override
        public String toString() {
            return "VersionInfo{" +
                    "versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", versionUrl='" + versionUrl + '\'' +
                    ", fileMD5='" + fileMD5 + '\'' +
                    ", versionNote='" + versionNote + '\'' +
                    '}';
        }
    }

    /**
     * binder.
     */
    public class DownloadServiceBinder extends Binder {

        DownloadService getService() {
            return DownloadService.this;
        }
    }

}
