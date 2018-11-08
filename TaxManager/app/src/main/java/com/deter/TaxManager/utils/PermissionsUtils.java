package com.deter.TaxManager.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class PermissionsUtils {

    public final static int PERMISSION_REQUESTCODE = 0x0024;
    private PermissionsListener permissionsListener;
    private Activity activity;

    public PermissionsUtils(Activity requestActivity,PermissionsListener permissionsListener) {
        this.permissionsListener = permissionsListener;
        this.activity = requestActivity;
    }


    public void requestRunPermisssion(String[] permissions) {
        List<String> permissionLists = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLists.add(permission);
            }
        }

        if (!permissionLists.isEmpty()) {
//            for (String permissionList : permissionLists) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
//                    //Toast.makeText(MainActivity.this, "您之前拒绝了该权限,请重新设置", Toast.LENGTH_SHORT).show();
//                }
//            }
            ActivityCompat.requestPermissions(activity, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        } else {
            permissionsListener.onGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionsUtils.PERMISSION_REQUESTCODE) {

            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                String permission = permissions[i];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }

            if (deniedPermissions.isEmpty()){
                permissionsListener.onGranted();
            }else{
                permissionsListener.onDenied(deniedPermissions);
            }

        }
    }

    public interface PermissionsListener {
        void onGranted();
        void onDenied(List<String> deniedPermission);
    }

}
