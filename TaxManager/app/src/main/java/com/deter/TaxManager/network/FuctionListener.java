package com.deter.TaxManager.network;

import java.util.List;
import java.lang.Object;

/**
 * Created by yuxinz on 2017/7/20.
 */

public interface FuctionListener{

    void onPreExecute(int fuctionId);
    void onPostExecute(int fuctionId, int result);//result 0:成功 1：失败
    void onResult(int fuctionId , List<Object> resultList);
}
