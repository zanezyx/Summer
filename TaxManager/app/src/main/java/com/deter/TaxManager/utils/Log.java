package com.deter.TaxManager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.deter.TaxManager.network.DtConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuxinz on 2017/8/7.
 */

public class Log{

    public static String TAG = "Log";
    static ExecutorService fixedThreadPool = null;
    static final String logDirName = Environment.getExternalStorageDirectory()+"/deter/";
    static final String logFileName = Environment.getExternalStorageDirectory()+"/deter/dtlog.txt";
    public static long MAX_LOG_SIZE = 100*1024*1024;

    public static void init(Activity activity)
    {
        //新建一个线程数为3的定长线程池
        if(DtConstant.ENABLE_DEBUG_LOG_FILE)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //当前系统大于等于6.0
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    android.util.Log.i(TAG, "version>6.0 has permission");
                    if(FileUtils.isFileExist(logFileName))
                    {
                        if(FileUtils.getFileSize(logFileName)>MAX_LOG_SIZE)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    FileUtils.deleteFile(logFileName);
                                }
                            }).start();
                        }
                    }
                } else {
                    android.util.Log.i( TAG, "version>6.0 do not have permission");
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 1);
                }
            } else {
                android.util.Log.i( TAG, "version<6.0 ");
                if(FileUtils.isFileExist(logFileName))
                {
                    if(FileUtils.getFileSize(logFileName)>MAX_LOG_SIZE){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtils.deleteFile(logFileName);
                            }
                        }).start();
                    }
                }
            }
            fixedThreadPool = Executors.newFixedThreadPool(3);
        }
    }



    public static void i(final String tag, final String msg) {

        android.util.Log.i(tag,msg);
        if(fixedThreadPool==null)
        {
            return;
        }
        //if(!tag.equals(DtConstant.DT_TAG2))
        //    return;
        //执行
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //xxx

                if(DtConstant.ENABLE_DEBUG_LOG_FILE
                        && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    android.util.Log.i( TAG, ">>>i>>>write log");
                    writelog(tag, msg);

                }
            }
        });

    }


    public static void writelog(final String tag, final String msg)
    {
        FileUtils.makeDirs(logDirName);
        String temp = ">>>"+TimeUtils.getCurrentTimeInString();
        temp+=" ";
        temp+=tag;
        temp+=" ";
        temp+=msg;
        temp+="\n";
        android.util.Log.i(TAG,"Log "+ Environment.getExternalStorageDirectory());
        try {
            FileUtils.writeFile(logFileName, temp,true);
        }catch (Exception e){
            android.util.Log.i(TAG,"Log "+ Environment.getExternalStorageDirectory()
                    +"exception:"+e.toString());
        }
    }

    public static void d(String tag, String msg) {

        i(tag,msg);
    }

    public static void v(String tag, String msg) {

        i(tag,msg);
    }
}
