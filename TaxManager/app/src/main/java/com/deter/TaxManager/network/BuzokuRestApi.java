package com.deter.TaxManager.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.deter.TaxManager.utils.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tyasui on 4/26/17.
 */

public class BuzokuRestApi {

    private static final String TAG = DtConstant.TAG;
    public static final String API_HOST = "http://192.168.42.15:80/api/v1";
    public static final String DETER_API_HOST_TEST = "http://192.168.30.15";
    public static final String DETER_API_HOST = "http://121.201.65.42:8999";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String API_CLIENT_VERSION = "/versions/client";
    public static final String API_TOPAPLIST = "/topaplist";
    public static final String API_TOPAPLISTBYID = "/topaplist/"; // add SSID
    public static final String API_POST_TOPAPLIST = "/topaplist/"; // add SSID

    public static final String API_BLACKLIST = "/blacklist";
    public static final String API_WHITELIST = "/whitelist";
    public static final String API_APBTARGET = "/mac/aptarget";
    public static final String API_APBTARGET_IN_BLACK = "/peers/apbyblacklist";
    public static final String API_APBYRSSI = "/mac/getapbyrssi/"; // need rssi level
    public static final String API_POST_WHITELIST = "/whitelist/";
    public static final String API_POST_BLACKLIST = "/blacklist/";
    public static final String API_DELETE_WHITELIST = "/whitelist/";

    //new api
    //开启及关闭 PI DETER (Reboot/Shutdown PI DETER)
    ///system?operation=[reboot|shutdown]
    public static final String API_SYSTEM_REBOOT = "/system?operation=reboot";
    public static final String API_SYSTEM_SHUTDOWN = "/system?operation=shutdown";

    //Android端发送命令启动和停止PI端监听AP和station
    ///listen?operation=[start|stop|restart|isrunning]&interface=[monitor0|monitor1]&dbmode=[0/1]
    public static final String API_START_LISTEN = "/listener?operation=start&interface=monitor0";
    public static final String API_START_LISTEN_DIRECTIONAL = "/listener?operation=start&interface=monitor3";
    public static final String API_STOP_LISTEN = "/listener?operation=stop";
    public static final String API_RESTART_LISTEN = "/listener?operation=restart";
    public static final String API_LISTEN_STATE = "/listener?operation=isrunning";

    //Android端指定AP或station断网 (DEAUTH),Deter Deauth 压制开启，关闭，重启，及查看状态
    // （Deter Deauth Start/Stop/Restart/Isrunning).
    public static final String API_START_DEAUTH = "/deauth?operation=start";
    public static final String API_STOP_DEAUTH = "/deauth?operation=stop";
    public static final String API_RESTART_DEAUTH = "/deauth?operation=restart";
    public static final String API_DEAUTH_STATE = "/deauth?operation=isrunning";


    //Deter SSID 开启，关闭，重启，及查看状态（Deter SSID hunting Start/Stop/Restart/Isrunning)
    public static final String API_START_SSID = "/ssid?operation=start";
    public static final String API_STOP_SSID = "/ssid?operation=stop";
    public static final String API_RESTART_SSID = "/ssid?operation=restart";
    public static final String API_SSID_STATE = "/ssid?operation=isrunning";


    //Deter SSID 密码库 添加，删除，查询
    public static final String API_SSID_LIB_ADD = "/ssidlib/";
    public static final String API_SSID_LIB_DEL = "/ssidlib/";
    public static final String API_SSID_LIB_QUERY = "/ssidlib";

    //Deter top ap list 添加，删除，查询
    public static final String API_TOP_AP_ADD = "/topaplist/";
    public static final String API_TOP_AP_DEL = "/topaplist/";
    public static final String API_TOP_AP_QUERY = "/topaplist";


    //Deter MITM 强连开启，关闭，重启，及查看状态（Deter MITM Start/Stop/Restart/Isrunning)
    public static final String API_START_MITM = "/mitmt?operation=start";
    public static final String API_STOP_MITM = "/mitmt?operation=stop";
    public static final String API_RESTART_MITM = "/mitmt?operation=restart";
    public static final String API_MITM_STATE = "/mitmt?operation=isrunning";

//    public static final String API_START_MITM = "/mitm?operation=start";
//    public static final String API_STOP_MITM = "/mitm?operation=stop";
//    public static final String API_RESTART_MITM = "/mitm?operation=restart";
//    public static final String API_MITM_STATE = "/mitm?operation=isrunning";



    //Android端查询无线引擎运行状态 ( Check PI DETER Running STATE)
    public static final String API_SYSTEM_STATE = "/system?operation=isrunning";

    //Android端请求当前AP和station（终端）信息列表
    public static final String API_GET_CURR_DEVICE = "/mac/getmac?DTIME=60";

    //Android端请求mac虚拟身份
    public static final String API_GET_IDENTITY = "/mac/getmacdetails/id/";
    //Android端请求mac 访问的url
    public static final String API_GET_URL = "/mac/getmacdetails/url/";

    //Android端请求是否有版本更新访问的url deter服务器
    public static final String API_CHECK_APP_UPDATE = "/deter/api/v1/check_app_update.php";
    //Android端请求获取PI最新版本的url deter服务器
    public static final String API_CHECK_PI_UPDATE = "/deter/api/v1/check_pi_update.php";

    //Android端请求同步时间给PI
    public static final String API_SYN_TIME = "/system/time?time=";
    public static final String API_SYSTEM_UPDATE = "/system/update";
    public static final String API_SYSTEM_VERSION = "/system/version";
    public static final String API_SYSTEM_EXTFSYS = "/system/extfsys";

    public static final int MSG_RESPONSE = 200;
    public final static int CONNECT_TIMEOUT =60;
    public final static int READ_TIMEOUT=80;
    public final static int WRITE_TIMEOUT=60;


    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RESPONSE:
                    Log.i(DtConstant.TAG, "BuzokuRestApi>>>mHandler>>>handleMessage enter");
                    if(msg.obj!=null)
                    {
                        Log.i(DtConstant.TAG, "BuzokuRestApi>>>mHandler>>>handleMessage 1");
                        RestApiListener listener = (RestApiListener)msg.obj;
                        Bundle bundle = msg.getData();
                        String res = bundle.getString("response");
                        Log.i(DtConstant.TAG, "BuzokuRestApi>>>mHandler>>>handleMessage res:"+res);
                        if(listener!=null)
                        {
                            Log.i(DtConstant.TAG, "BuzokuRestApi>>>mHandler>>>handleMessage listener onPostExecute");
                            listener.onPostExecute(res);
                        }
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };



    public static void doApiPostExec (String apiurl, String postBody,
                                      RestApiListener listener) {
        doApiPostExec(API_HOST, apiurl,postBody,listener);
    }

    public static void doApiGetExec (String apiurl, RestApiListener listener) {

        doApiGetExec (API_HOST, apiurl, listener);
    }

    public static void doApiDeleteExec (String apiurl, RestApiListener listener) {

        doApiDeleteExec (API_HOST, apiurl, listener);
    }

    public static void doApiPostExec (String apihost, final String apiurl, String postBody,
                                      final RestApiListener listener) {

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                .build();

        String url = apihost + apiurl;
        Log.i(DtConstant.TAG, ">>>doApiPostExec>>>url:"+url);
        Log.i(DtConstant.TAG, ">>>doApiPostExec>>>postBody:"+postBody);
        if(listener!=null)
        {
            listener.onPreExecute();
        }
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .post(body)
                .build();

        if(DtConstant.DEBUG_MODE)
        {
            Message message = new Message();
            message.what = MSG_RESPONSE;
            Bundle bundle = new Bundle();
            bundle.putString("response", null);
            message.obj = listener;
            message.setData(bundle);
            mHandler.sendMessage(message);
        }else{
            try {

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        if(listener!=null)
                        {
                            Log.i(DtConstant.TAG, "doApiPostExec:"+apiurl+">>>http get fail exception:"+e.toString());
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            bundle.putString("response", null);
                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(listener!=null)
                        {
                            String strRes = response.body().string();
                            Log.i(DtConstant.TAG, "doApiPostExec:"+apiurl+">>>http get response:"+strRes);
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            if(strRes!=null)
                            {
                                bundle.putString("response", strRes);
                            }else{
                                bundle.putString("response", null);
                            }
                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }
                });

            } catch (Exception e) {
                Log.i(DtConstant.TAG, "doApiPostExec:"+apiurl+">>>exception:"+e.toString());
            }
        }
    }


    public static void doApiGetExec (String apihost, final String apiurl, final RestApiListener listener) {

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                .build();

        final String url = apihost + apiurl;
        if(url.contains("getmac"))
        {
            Log.i(DtConstant.DT_TAG, ">>>doApiGetExec>>>url:"+url);
        }else{
            Log.i(DtConstant.TAG, ">>>doApiGetExec>>>url:"+url);
        }

        if(listener!=null)
        {
            listener.onPreExecute();
        }
        Request.Builder builder = new Request.Builder() ;
        builder.url(url);
        builder.addHeader("Connection", "close");

        Request request = builder.build();
        Log.i("header", ">>>header Connection:"+request.header("Connection"));
        if(DtConstant.DEBUG_MODE && !apiurl.equals(BuzokuRestApi.API_CHECK_APP_UPDATE))
        {
            Message message = new Message();
            message.what = MSG_RESPONSE;
            Bundle bundle = new Bundle();
            bundle.putString("response", null);
            message.obj = listener;
            message.setData(bundle);
            mHandler.sendMessage(message);
        }else{
            try {

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        if(listener!=null)
                        {
                            if(!url.contains("getmac"))
                            {
                                Log.i(DtConstant.TAG, "doApiGetExec:"+apiurl+">>>http get fail e:"+e.toString());
                            }else{
                                Log.i(DtConstant.DT_TAG, "doApiGetExec:"+apiurl+">>>http get fail e:"+e.toString());
                            }
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            bundle.putString("response", null);
                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Headers requestHeaders =  response.networkResponse().request().headers();
//                        int requestHeadersLength = requestHeaders.size();
//                        for (int i = 0; i < requestHeadersLength; i++){
//                            String headerName = requestHeaders.name(i);
//                            String headerValue = requestHeaders.get(headerName);
//                            Log.i("header","header----->"+headerName+":"+headerValue);
//                        }
                        if(listener!=null)
                        {
                            String strRes = response.body().string();

                            if(!url.contains("getmac"))
                            {
                                Log.i(DtConstant.TAG, "doApiGetExec:"+apiurl+">>>http get response:"+strRes);
                            }else{
                                Log.i(DtConstant.DT_TAG, "doApiGetExec:"+apiurl+">>>http get response:"+strRes);
                            }
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            if(strRes!=null)
                            {
                                bundle.putString("response", strRes);
                            }else{
                                bundle.putString("response", null);
                            }

                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }
                });

            } catch (Exception e) {
                Log.i(DtConstant.TAG, "doApiGetExec:"+apiurl+">>>exception:"+e.toString());
            }
        }
    }


    public static void doApiDeleteExec (String apihost, final String apiurl,
                                      final RestApiListener listener) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                .build();

        String url = apihost + apiurl;
        Log.i(DtConstant.TAG, ">>>doApiDeleteExec>>>url:"+url);

        if(listener!=null)
        {
            listener.onPreExecute();
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .delete()
                .build();

        if(DtConstant.DEBUG_MODE)
        {
            Message message = new Message();
            message.what = MSG_RESPONSE;
            Bundle bundle = new Bundle();
            bundle.putString("response", null);
            message.obj = listener;
            message.setData(bundle);
            mHandler.sendMessage(message);
        }else{
            try {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        if(listener!=null)
                        {
                            Log.i(DtConstant.TAG, "doApiDeleteExec"+apiurl+">>>http get fail e:"+e.toString());
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            bundle.putString("response", null);
                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(listener!=null)
                        {
                            String strRes = response.body().string();
                            Log.i(DtConstant.TAG, "doApiDeleteExec:"+apiurl+">>>http get response:"+strRes);
                            Message message = new Message();
                            message.what = MSG_RESPONSE;
                            Bundle bundle = new Bundle();
                            if(strRes!=null)
                            {
                                bundle.putString("response", strRes);
                            }else{
                                bundle.putString("response", null);
                            }
                            message.obj = listener;
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    }
                });
                Log.i(DtConstant.TAG, "doApiDeleteExec>>>end");

            } catch (Exception e) {
                Log.i(DtConstant.TAG, "doApiDeleteExec>>>e:"+e.toString());
            }
        }
    }



    public static void doApiPostExec (String apihost, String apiurl, String postBody) {
        OkHttpPostHandler handler = new OkHttpPostHandler();
        String url = apihost + apiurl;
        Log.d(DtConstant.TAG, ">>>doApiPostExec>>>url:" + url);
        String result = null;
        try {
            result = handler.execute(apihost, apiurl, postBody).get();
            Log.i(DtConstant.TAG, ">>>doApiPostExec>>>result :"+result);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    private static void doApiPutExec (String apihost, String apiurl, String putBody) {
        OkHttpClient client = new OkHttpClient();
        String putUrl = apihost + apiurl;
        Log.d("TAG", "PUT to " + putUrl);
        RequestBody body = RequestBody.create(JSON, putBody);
        Request request = new Request.Builder()
                .url(putUrl)
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG",response.body().string());
            }
        });
    }

    public static String doApiGetExec (String apihost, String apiurl) {
        OkHttpGetHandler handler = new OkHttpGetHandler();
        String url = apihost + apiurl;
        Log.d(DtConstant.TAG, "URL:" + url);
        String result = null;
        try {
            result = handler.execute(url).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        return result;
    }

    public static String RestGetClientVersion (String apihost) {
        return doApiGetExec (apihost, API_CLIENT_VERSION);
    }

    public static String RestGetAplistByBlackList (String apihost) {
        return doApiGetExec (apihost, API_APBTARGET_IN_BLACK);
    }
    public static String RestGetApTargetList (String apihost) {
        return doApiGetExec (apihost, API_APBTARGET);
    }

    public static String RestGetTopApList(String apihost) {
        return doApiGetExec (apihost, API_TOPAPLIST);
    }

    public static String RestGetTopApListById(String apihost, String id) {
        String query = null;
        try {
            query = URLEncoder.encode(id, "utf-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}

        String url = API_TOPAPLISTBYID + query;
        return doApiGetExec (apihost, url);
    }
    public static void RestPostTopApList (String apihost, String ssid, String enc, String passwd) {
        String encodedSsid = null;
        try {
            encodedSsid = URLEncoder.encode(ssid, "utf-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}

        String postBody="{\n" +
                "    \"enc\": \"" + enc + "\", \n" +
                "    \"pwd\": \"" + passwd + "\"\n" +
                "}";
        Log.d(TAG, "BODY:" + postBody);
        doApiPostExec (apihost, API_POST_TOPAPLIST + encodedSsid , postBody);
    }

    public static String RestGetBlacklist (String apihost) {
        return doApiGetExec (apihost, API_BLACKLIST);
    }
    public static String RestGetWhitelist (String apihost) {
       //return doApiGetExec (apihost, API_WHITELIST);
        return null;
    }

    public static String RestGetAPByRssi (String apihost, String rssi) {
        String cmd = API_APBYRSSI + rssi;
        Log.d(TAG, "cmd :" + cmd);
        return doApiGetExec (apihost, cmd);
    }
    public static void RestPostRegisterMyMac (String apihost, String myMacAddr) {
        RestPostWhitelist (apihost, myMacAddr);
    }
    public static void RestPostWhitelist (String apihost, String myMacAddr) {
        String postBody="{\n" +
                "    \"mac\": \"XXXXX\",\n" +
                "    \"role\": \"X1\"\n" +
                "}";
        String mac = myMacAddr.replace(":", "").toLowerCase();
        doApiPostExec (apihost, API_POST_WHITELIST + mac , postBody);
    }





}
