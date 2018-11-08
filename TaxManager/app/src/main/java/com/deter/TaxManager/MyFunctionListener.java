package com.deter.TaxManager;

import android.os.Handler;
import android.os.Message;

import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.Log;

import java.util.List;

import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_ONRESULT;
import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_POSTEXECUTE;
import static com.deter.TaxManager.WifiMonitorOpt1.MESSAGE_LISTENER_PREEXECUTE;


/**
 * Created by xiaolu on 17-9-5.
 */


public class MyFunctionListener implements FuctionListener {

    private static final String TAG = "MyFunctionListener";

    private Handler handler;

    public MyFunctionListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onPreExecute(int fuctionId) {
        Log.d(TAG,"MyFunctionListener  onPreExecute");
        //Handler handler = wrhandler.get();
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = MESSAGE_LISTENER_PREEXECUTE;
            message.arg1 = fuctionId;
            handler.sendMessage(message);
        } else {
            Log.d(TAG,"MyFunctionListener  handler is null");
        }
    }

    @Override
    public void onPostExecute(int fuctionId, int result) {
        Log.d(TAG,"MyFunctionListener  onPostExecute");
        //Handler handler = wrhandler.get();
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = MESSAGE_LISTENER_POSTEXECUTE;
            message.arg1 = fuctionId;
            message.arg2 = result;
            handler.sendMessage(message);
        } else {
            Log.d(TAG,"MyFunctionListener  handler is null");
        }
    }

    @Override
    public void onResult(int fuctionId, List<Object> resultList) {
        Log.d(TAG,"MyFunctionListener  onResult");
        //Handler handler = wrhandler.get();
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = MESSAGE_LISTENER_ONRESULT;
            message.arg1 = fuctionId;
            message.obj = resultList;
            handler.sendMessage(message);
        } else {
            Log.d(TAG,"MyFunctionListener  handler is null");
        }
    }

}
