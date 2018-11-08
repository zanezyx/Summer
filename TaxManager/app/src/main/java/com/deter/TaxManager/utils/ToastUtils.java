package com.deter.TaxManager.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by zhongqiusheng on 2017/7/28 0028.
 */
public class ToastUtils {

    private static Toast mToast;

    private static Handler mHandler = new Handler();

    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    private ToastUtils() {
    }

    public static void show(final Context context, final CharSequence content, final int iLastMillis) {
        Toast.makeText(context.getApplicationContext(), content, iLastMillis).show();
    }

    public static void showShort(final Context context, final CharSequence content) {

        mHandler.removeCallbacks(r);
        if (mToast != null) {
            mToast.setText(content);
        } else {
            mToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
        }

        mHandler.postDelayed(r, 1000);
        mToast.show();

    }

    public static void showLong(final Context context, final CharSequence content) {
        ToastUtils.show(context.getApplicationContext(), content, Toast.LENGTH_LONG);
    }

    private static void showOnUiThread(final Activity activity, final CharSequence content, final int iLastMillis) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), content, iLastMillis).show();
            }
        });
    }

    public static void showShortOnUiThread(final Activity activity, final CharSequence content) {
        ToastUtils.showOnUiThread(activity, content, Toast.LENGTH_SHORT);
    }

    public static void showLongOnUiThread(final Activity activity, final CharSequence content) {
        ToastUtils.showOnUiThread(activity, content, Toast.LENGTH_LONG);
    }
}
