package com.deter.TaxManager.model;

import android.content.Context;

import com.deter.TaxManager.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CacheManager {

    /**
     * 保存对象
     *
     * @param ser 要保存的序列化对象
     * @param file 保存在本地的文件名
     * @throws IOException
     */
    public static boolean saveObject(Context context, Serializable ser,
                                     String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            Log.i("tax", "CacheManager>>>saveObject>>>OK");
            return true;
        } catch ( Exception e ) {
            Log.i("tax", "CacheManager>>>saveObject>>>1 e:"+e.toString());
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取对象
     *
     * @param file 保存在本地的文件名
     * @return
     * @throws IOException
     */
    public static Serializable readObject(Context context, String file) {
//        if ( !isExistDataCache(context, file) )
//            return null;
        if(file==null)
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            Serializable serializable = (Serializable) ois.readObject();
            Log.i("tax", "CacheManager>>>readObject>>>ok");
            return serializable;
        } catch ( FileNotFoundException e ) {
            Log.i("tax", "CacheManager>>>readObject>>>1 e:"+e.toString());
        } catch ( Exception e ) {
            Log.i("tax", "CacheManager>>>readObject>>>2 e:"+e.toString());
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if ( e instanceof InvalidClassException) {
                Log.i("tax", "CacheManager>>>readObject>>>delete cache file");
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch ( Exception e ) {
            }
            try {
                fis.close();
            } catch ( Exception e ) {
            }
        }
        return null;
    }


    //MyInfosBean bean = (MyInfosBean) CacheManager.readObject(context, getCacheName());
    //public class Bean implements Serializable{}
}
