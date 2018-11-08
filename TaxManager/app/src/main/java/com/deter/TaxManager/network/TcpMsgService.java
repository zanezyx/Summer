package com.deter.TaxManager.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.utils.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpMsgService extends Service implements Runnable{

    public static final String TAG = "TcpMsgService";

    public static final int MSG_SYS_UP = 1;
    public static final int MSG_SYS_DOWN = 2;
    public static final int MSG_SYS_DATA_FULL = 3;

    public static final int MSG_LISTENER_BLACK_LIST_FOUND = 1001;
    public static final int MSG_LISTENER_TARGET_FOUND = 1002;

    public static final int MSG_SSID_START = 2001;
    public static final int MSG_SSID_END = 2009;

    public static final int MSG_DEAUTH_START = 3001;
    public static final int MSG_DEAUTH_END = 3009;
    public static final int MSG_DEAUTH_SUCCEED = 3002;

    public static final int MSG_MITM_START = 4001;
    public static final int MSG_MITM_SUCCEED= 4002;
    public static final int MSG_MITM_END = 4009;
    public static final String TCP_MSG_ACTION = "deter.intent.action.TCP_MSG";

    List<String> currDeauthMacs = null;
    List<String> currMitmMacs = null;
    ServerSocket serverSocket = null;
    private static Thread serverThread = null;
    boolean running = false;
    public static boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        Log.d(TAG, "TcpMsgService>>>onCreate");
        super.onCreate();
        currDeauthMacs = new ArrayList<String>();
        currMitmMacs = new ArrayList<String>();
        isServiceRunning = true;
        startTcpServer();

    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    public static void exitThread() {
        Log.d(TAG, "exitThread");
        if (serverThread != null && !serverThread.isInterrupted()) {
            serverThread.interrupt();
        }
        isServiceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        running = false;
        if(serverSocket!=null)
        {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        exitThread();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void run() {
        try
        {
            running = true;
            Log.i(TAG,"TcpServer>>>run start");
            //创建ServerSocket
            serverSocket = new ServerSocket(9000);
//            serverSocket.setSoTimeout(5000);
            while (running)
            {
                Log.i(TAG,"TcpServer>>>run");
                //接受客户端请求
                Socket client = null;
                try {
                    client = serverSocket.accept();
                    Log.i(TAG,"TcpServer>>>accept");
                }catch (Exception e) {
//                    Log.i(TAG, "TcpServer>>>accept time out e:"+e.toString());
//                    Thread.sleep(500);
//                    continue;
                }
                try
                {
                    //接收客户端消息
                    Log.i(TAG,"TcpServer>>>1");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    Log.i(TAG,"TcpServer>>>2");
                    char[] buf = new char[512];
                    in.read(buf);
                    String str = new String(buf);
                    Log.i(TAG,"TcpServer>>>read:"+str);
                    String res = processClientMsg(str);
                    //服务器发送消息
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(client.getOutputStream())),true);
                    out.println(res);
                    Log.i(TAG,"TcpServer>>>response:"+res);
                    //关闭流
                    out.close();
                    in.close();
                }
                catch (Exception e)
                {
                    Log.i(TAG,"TcpServer>>>132 exception:"+e.toString());
                    e.printStackTrace();
                }
                finally
                {
                    //关闭
                    client.close();
                    Log.i(TAG,"TcpServer>>>close");
                }

                //Thread.sleep(800);
            }
        }
        catch (Exception e)
        {
            Log.i(TAG,"TcpServer>>>452 exception:"+e.toString());
        }
    }


    String processClientMsg(String msg)
    {
        if(msg==null)
            return "error";
        String res = "";
        String msgId = msg.substring(0,4);
        String msgLength = msg.substring(4,8);
        int state = -1;
        Log.i(TAG, "processClientMsg>>>msgId:"+msgId+"  msgLength:"+msgLength);
        int nMsgId = -1;
        int nMsgLenth = 0;
        try {
            nMsgId = Integer.parseInt(msgId);
            nMsgLenth = Integer.parseInt(msgLength);
        }catch (Exception e)
        {
            Log.i(TAG,"parseClientMsg>>> 1 exception:"+e.getMessage());
        }
        switch (nMsgId){
//            case MSG_SYS_UP:
//            case MSG_SYS_DOWN:
//            case MSG_SYS_DATA_FULL:
//            case MSG_LISTENER_BLACK_LIST_FOUND:
//            case MSG_LISTENER_TARGET_FOUND:
//            case MSG_SSID_START:
//            case MSG_SSID_END:
            case MSG_DEAUTH_START:
            case MSG_DEAUTH_END:
                currDeauthMacs.clear();
                sendBroadCast(this, nMsgId, null, 0);
                res = String.format("%04dsuccess",nMsgId);
                break;
            case MSG_MITM_START:
            case MSG_MITM_END:
                currMitmMacs.clear();
                sendBroadCast(this, nMsgId, null, 0);
                res = String.format("%04dsuccess",nMsgId);
                break;
            case MSG_DEAUTH_SUCCEED: {
                Task task = BuzokuFuction.getInstance(this).getCurrTask();
                if(task!=null && nMsgLenth>=13 && msg.length()>=21)
                {
                    String strState = msg.substring(8, 9);
                    try {
                        state = Integer.parseInt(strState);
                        String strMac = msg.substring(9, 21);
                        Log.i(TAG, "processClientMsg>>>strState:"+strState+"  strMac:"+strMac);
                        boolean isFound = false;
                        if(strMac!=null)
                        {
                            for(String mac:currDeauthMacs)
                            {
                                if(mac.equalsIgnoreCase(strMac)){
                                    isFound = true;
                                }
                            }
                        }
                        if(isFound)
                        {
                            Log.i(TAG, "processClientMsg>>>MSG_DEAUTH_SUCCEED message is already sent");
                            res = String.format("%04dsuccess", nMsgId);
                            break;
                        }
                        String [] macs = new String[1];
                        macs[0] = strMac;
                        if(state==1)
                        {
                            DataManager.getInstance(this).updateDeviceDeauthState(task.getId(),
                                    strMac, Device.DEAUTH_STATE_SUCCEEDED);
                            sendBroadCast(this, nMsgId, macs, Device.DEAUTH_STATE_SUCCEEDED);
                            currDeauthMacs.add(strMac);
                        }else{
                            DataManager.getInstance(this).updateDeviceDeauthState(task.getId(),
                                    strMac, Device.DEAUTH_STATE_FAIL);
                            sendBroadCast(this, nMsgId, macs, Device.DEAUTH_STATE_FAIL);
                            currDeauthMacs.add(strMac);
                        }

                    } catch (Exception e) {
                        Log.i(TAG, "processClientMsg>>>1 e:"+e.toString());
                    }

                }
                res = String.format("%04dsuccess", nMsgId);
            }
                break;
            case MSG_MITM_SUCCEED: {
                Task task = BuzokuFuction.getInstance(this).getCurrTask();
                if(task!=null && msg.length()>=36){
                    String strState = msg.substring(8, 9);
                    try {
                        state = Integer.parseInt(strState);
                        String strMac = msg.substring(9, 21);
                        String [] macs = new String[1];
                        macs[0] = strMac;
                        Log.i(TAG, "processClientMsg>>>strState:"+strState+"  strMac:"+strMac);
                        String strIp = msg.substring(21, 36);
                        String strName = msg.substring(36, msg.length()-1);
                        Log.i(TAG, "processClientMsg>>>strIp:"+strIp+"  strName:"+strName);
                        boolean isFound = false;
                        if(strMac!=null)
                        {
                            for(String mac:currMitmMacs)
                            {
                                if(mac.equalsIgnoreCase(strMac)){
                                    isFound = true;
                                }
                            }
                        }
                        if(isFound)
                        {
                            Log.i(TAG, "processClientMsg>>>MSG_MITM_SUCCEED message is already sent");
                            res = String.format("%04dsuccess", nMsgId);
                            break;
                        }
                        if(state==1)
                        {
                            DataManager.getInstance(this).updateDeviceMitmState(task.getId(),
                                    strMac, Device.MITM_STATE_SUCCEEDED);
                            updateDeviceIpInfo(strMac, strIp, strName);
                            sendBroadCast(this, nMsgId, macs, Device.MITM_STATE_SUCCEEDED,
                                    strIp, strName);
                        }else{
                            DataManager.getInstance(this).updateDeviceMitmState(task.getId(),
                                    strMac, Device.MITM_STATE_FAIL);
                            sendBroadCast(this, nMsgId, macs, Device.MITM_STATE_FAIL,
                                    null, null);
                        }

                    } catch (Exception e) {
                        Log.i(TAG, "processClientMsg>>>2 e:"+e.toString());
                    }
                }
                res = String.format("%04dsuccess", nMsgId);
            }
                break;
            default:
                res = String.format("%04dsuccess", nMsgId);
                break;
        }
        return res;
    }


//开启服务器
    public void startTcpServer()
    {
        Log.i(TAG, "startTcpServer>>>enter");
        if (serverThread != null && !serverThread.isInterrupted()) {
            Log.i(TAG, "startTcpServer>>>serverThread != null");
            running = false;
            serverThread.interrupt();
        }
        serverThread = new Thread(this);
        serverThread.start();
    }

    void sendBroadCast(Context context, int msgId, String[] macs, int state)
    {
        Log.i(TAG, "processClientMsg>>>sendBroadCast");
        Intent intent = new Intent();
        intent.setAction(TCP_MSG_ACTION);
        intent.putExtra("msg_id",msgId);
        intent.putExtra("state", state);
        if (macs!=null) {
            intent.putExtra("mac", macs);
        }
        context.sendBroadcast(intent);
    }

    void sendBroadCast(Context context, int msgId, String[] macs, int state, String ip, String ssid)
    {
        Log.i(TAG, "processClientMsg>>>sendBroadCast");
        Intent intent = new Intent();
        intent.setAction(TCP_MSG_ACTION);
        intent.putExtra("msg_id",msgId);
        intent.putExtra("state", state);
        intent.putExtra("ip",ip);
        intent.putExtra("ssid", ssid);
        if (macs!=null) {
            intent.putExtra("mac", macs);
        }
        context.sendBroadcast(intent);
    }

    void updateDeviceIpInfo(String mac, String ip, String dName)
    {
        Task currTask = BuzokuFuction.getInstance(this).getCurrTask();
        if(currTask!=null)
        {
            List<Device> deviceList = DataManager.getInstance(this).queryDeviceByMac(currTask.getId(),mac);
            if(deviceList!=null)
            {
                for (Device d:deviceList)
                {
                    if(d!=null)
                    {
                        d.setIpaddr(ip);
                        d.setIpInfoName(dName);
                        DataManager.getInstance(this).updateDevice(d);
                    }
                }
            }
        }

    }

}
