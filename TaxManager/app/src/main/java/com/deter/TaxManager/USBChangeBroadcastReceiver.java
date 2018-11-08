package com.deter.TaxManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.deter.TaxManager.utils.Log;

import java.util.ArrayList;

/**
 * Created by xiaolu on 17-8-28.
 */

public class USBChangeBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "USBBroadcastReceiver";

    private static ArrayList<USBChangeListener> usbChangeListeners;


    public void onReceive(Context context, Intent intent) {
        if ("android.hardware.usb.action.USB_STATE".equalsIgnoreCase(intent.getAction())) {
            if (intent.getExtras().getBoolean("connected")) {
                Log.d(TAG,"USBChangeBroadcastReceiver --- connected");
                if (usbChangeListeners != null && usbChangeListeners.size() > 0) {
                    for (USBChangeListener usbChangeListener : usbChangeListeners) {
                        usbChangeListener.onUSBConnectChanged(true);
                    }
                }
            } else {
                Log.d(TAG,"USBChangeBroadcastReceiver --- disconnected");
                if (BackgroundService.isServiceRunning) {
                    Toast.makeText(context, R.string.wifi_map_pi_noresponding_hint, Toast
                            .LENGTH_SHORT).show();
                    BackgroundService.exitQueryLoopHandler();
                    BackgroundService.clearDeviceList();
                }
                if (usbChangeListeners != null && usbChangeListeners.size() > 0) {
                    for (USBChangeListener usbChangeListener : usbChangeListeners) {
                        usbChangeListener.onUSBConnectChanged(false);
                    }
                }
            }
        } else if ("android.net.conn.TETHER_STATE_CHANGED".equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "tehter state changde");
        }
    }

    public static void addUSBChangeListener(USBChangeListener usbChangeListener) {
        if (usbChangeListeners == null) {
            usbChangeListeners = new ArrayList<>();
        }
        if (!usbChangeListeners.contains(usbChangeListener)) {
            usbChangeListeners.add(usbChangeListener);
        }
    }

    public static void removeUSBChangeListener(USBChangeListener usbChangeListener) {
        if (usbChangeListeners == null) {
            return;
        }
        usbChangeListeners.remove(usbChangeListener);
        if (usbChangeListeners.size() == 0) {
            usbChangeListeners = null;
        }
    }
}
