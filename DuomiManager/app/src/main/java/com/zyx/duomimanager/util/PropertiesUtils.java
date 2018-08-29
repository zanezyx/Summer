package com.zyx.duomimanager.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 文件工具操作类
 * @author Sunny
 * @date 2014.10.21
 *
 */
public class PropertiesUtils {
	
	 public static Properties urlProps = null;

	    /**
	     * 在AppMainActivity中初始化
	     * @param c
	     * @return
	     */
	    public static Properties init(Context c) {

	        if (urlProps == null) {
	            urlProps = new Properties();
	            try {
	                // 方法一：通过activity中的context攻取setting.properties的FileInputStream
	                InputStream in = c.getAssets().open("appConfig.properties");
	                // 方法二：通过class获取setting.properties的FileInputStream
	                // InputStream in =
	                // PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
	                urlProps.load(in);
	                in.close();
	            } catch (Exception e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
	        }
	        return urlProps;
	    }
	    
	    public static Properties getProperties()
	    {
	        return urlProps;
	    }

}
