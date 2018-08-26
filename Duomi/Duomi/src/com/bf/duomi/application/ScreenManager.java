
package com.bf.duomi.application;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕管理器
 * @author galis
 * @date: 2014-2-24-下午4:22:22
 */
public class ScreenManager {

    public static ScreenManager screenManager;
    private Context context;
    
    private static int screenWidth;
    private static int screenHeight;
    private static float density;
    
    public static ScreenManager getInstance(Context context)
    {
        if(screenManager==null) {
            screenManager = new ScreenManager();
            screenManager.context = context;
            return screenManager;
        }
        return screenManager;
    }
    
    public static int getScreenWidth()
    {
        return screenWidth;
    }
    public static int getScreenHeight()
    {
        return screenHeight;
    }
    public static float getScreenDensity()
    {
        return density;
    }
    
    public void init()
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        density = dm.density;
        
    }
    
    
}
