package com.deter.TaxManager.network;

import android.os.AsyncTask;
import com.deter.TaxManager.utils.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tyasui on 4/26/17.
 */

public class OkHttpGetHandler extends AsyncTask<String, Void, String> {

    RestApiListener listener;
    OkHttpClient client = new OkHttpClient();

    public OkHttpGetHandler(){};

    public OkHttpGetHandler(RestApiListener listener){
        this.listener = listener;
    }


    @Override
    protected String doInBackground(String... params) {

        Log.i(DtConstant.TAG, "OkHttpGetHandler>>>doInBackground>>>start");
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);

        Request request = builder.build();

        if(DtConstant.DEBUG_MODE)
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {

                Response response = client.newCall(request).execute();
                Log.i(DtConstant.TAG, "OkHttpGetHandler>>>doInBackground>>>end");
                return response.body().string();

            } catch (Exception e) {
                Log.i(DtConstant.TAG, "OkHttpGetHandler>>>doInBackground>>>e:"+e.toString());
            }
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(listener!=null){
            listener.onPreExecute();
        }


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(listener!=null){
            listener.onPostExecute(s);
        }


    }

}

