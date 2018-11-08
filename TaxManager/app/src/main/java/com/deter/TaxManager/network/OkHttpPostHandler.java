package com.deter.TaxManager.network;

import android.os.AsyncTask;
import com.deter.TaxManager.utils.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tyasui on 4/26/17.
 */

public class OkHttpPostHandler extends AsyncTask<String, Void, String> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RestApiListener listener;
    OkHttpClient client = new OkHttpClient();

    public OkHttpPostHandler(){};

    public OkHttpPostHandler(RestApiListener listener){
        this.listener = listener;
    }


    @Override
    protected String doInBackground(String... params) {

//        Request.Builder builder = new Request.Builder();
//        builder.url(params[0]);
//
//        Request request = builder.build();
//
//        try {
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//
//        } catch (Exception e) {
//        }
        String result = okHttpPost(params[0], params[1], params[2]);
        return result;
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

    private String okHttpPost (String apihost, String apiurl, String postBody) {
        OkHttpClient client = new OkHttpClient();
        String postUrl = apihost + apiurl;
        Log.d("TAG", "POST to " + postUrl);
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

