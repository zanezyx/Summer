package com.deter.TaxManager.network;

/**
 * Created by yuxinz on 2017/7/18.
 */

public interface RestApiListener {

    void onPreExecute();
    void onPostExecute(String result);
}
