package com.deter.TaxManager;

import android.app.Application;

import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.Log;

/**
 * Created by xiaolu on 17-8-1.
 */

public class WifimonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UncaughtHandler catchExcep = new UncaughtHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        APPUtils.initDbFile(this);
        DtConstant.DEBUG_MODE = APPUtils.getValueFromSharePreference(this, "debug_mode", false);

    }

    public class UncaughtHandler implements Thread.UncaughtExceptionHandler {

        public static final String TAG = "CatchExcep";

        WifimonitorApplication application;

        private Thread.UncaughtExceptionHandler mDefaultHandler;

        public UncaughtHandler(WifimonitorApplication application) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            this.application = application;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if (!handleException(ex) && mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                //uncaughtException here
            }
        }

        private boolean handleException(Throwable ex) {
            if (ex == null) {
                return false;
            }
            Log.d(TAG,"handleException-------------handleException");
            //BackgroundService.exitThread();
            //TcpMsgService.exitThread();
            return false;
        }

    }
}
