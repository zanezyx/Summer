package com.bf.duomi.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;


/**
 * ImageView extended class allowing easy downloading
 * of remote images
 */
public class RemoteImageView extends ImageView{

    public static HashMap<String,Bitmap> imageCache = new HashMap<String, Bitmap>();
    
    private static final int MAX_FAIL_TIME = 5;
    private int mFails = 0;
    
    private String mUrl;
    
    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setDefaultImage(int resId){
        this.setImageResource(resId);
    }
    
    public void setImageUrl(String url){
        
        if(mUrl != null && mUrl.equals(url)){
            mFails++;
        }else{
            mFails = 0;
            mUrl = url;
        }
        
        if(mFails >= MAX_FAIL_TIME)
            return;
        
        mUrl = url;
        
        if(isCached(url))
            return;
        
        startDownload(url);
    }
    
    public boolean isCached(String url){
        if(imageCache.containsKey(url)){
            this.setImageBitmap(imageCache.get(url));
            return true;
        }
        
        return false;
    }
    
    private void startDownload(String url){
        try{
            new DownloadTask().execute(url);
        }catch (RejectedExecutionException e) {
            //捕获RejectedExecutionException同时加载的图片过多而导致程序崩溃
        }
    }
    
    private void reDownload(String url){
        setImageUrl(url);
    }
    
    class DownloadTask extends AsyncTask<String, Void, String>{

        private String imageUrl;
        
        @Override
        protected String doInBackground(String... params) {
            imageUrl = params[0];
            InputStream is = null;
            Bitmap bmp = null;
            
            try {
                URL url = new URL(imageUrl);
                is = url.openStream();
                bmp = BitmapFactory.decodeStream(is);
                if(bmp != null){
                    imageCache.put(imageUrl, bmp);
                }else{
                    reDownload(imageUrl);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
            return imageUrl;
        }

        @Override
        protected void onPostExecute(String result) {
            Bitmap bmp = null;
            if(imageCache.containsKey(result)){
                bmp = imageCache.get(result);
                RemoteImageView.this.setImageBitmap(bmp);
            }else{
                reDownload(imageUrl);
            }
            
            super.onPostExecute(result);
        }
        
    }

}
