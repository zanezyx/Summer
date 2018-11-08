package com.deter.TaxManager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.AnalysisTask;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.DndMac;
import com.deter.TaxManager.model.FollowAnalysis;
import com.deter.TaxManager.model.FollowAnalysisMac;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.StrikeAnalysis;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.network.BuzokuFuction;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.network.FuctionListener;
import com.deter.TaxManager.utils.DownloadUtil;
import com.deter.TaxManager.utils.ExcelUtils;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.TimeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DeviceDbActivity extends AppCompatActivity {

    Button btnf1;
    Button btnf2;
    Button btnf3;
    Button btnf4;
    Button btnf5;
    Button btnf6;
    Button btnf7;
    Button btnf8;
    Button btnf9;
    Button btnf10;
    Button btnf11;
    Button btnf12;
    Button btnf13;
    Button btnf14;
    Button btnf15;

    Button btnf16;
    Button btnf17;
    Button btnf18;

    Button btnf19;
    Button btnf20;
    Button btnf21;
    Button btnf22;
    Button btnf23;
    TextView tvReceive;
    private SharedPreferences prefs;
    private String serviceIp = null;
    private String servicePort = null;

    public final static int CMD_START_LISTEN = 100;
    public final static int CMD_QUERY = 101;

    List<Device> deviceList = null;
    List<Object> dndList = null;
    static int count = 0;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvReceive.setText(""+msg.what+" "+msg.arg1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_db);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        serviceIp = prefs.getString("sip", "");
        servicePort = prefs.getString("sport", "");
        initView();
    }

    void initView()
    {
        btnf1 = (Button)findViewById(R.id.fuction1);
        btnf2 = (Button)findViewById(R.id.fuction2);
        btnf3 = (Button)findViewById(R.id.fuction3);
        btnf4 = (Button)findViewById(R.id.fuction4);
        btnf5 = (Button)findViewById(R.id.fuction5);
        btnf6 = (Button)findViewById(R.id.fuction6);
        btnf7 = (Button)findViewById(R.id.fuction7);
        btnf8 = (Button)findViewById(R.id.fuction8);
        btnf9 = (Button)findViewById(R.id.fuction9);
        btnf10 = (Button)findViewById(R.id.fuction10);
        btnf11 = (Button)findViewById(R.id.fuction11);
        btnf12 = (Button)findViewById(R.id.fuction12);
        btnf13 = (Button)findViewById(R.id.fuction13);
        btnf14 = (Button)findViewById(R.id.fuction14);
        btnf15 = (Button)findViewById(R.id.fuction15);

        btnf16 = (Button)findViewById(R.id.fuction16);
        btnf17 = (Button)findViewById(R.id.fuction17);
        btnf18 = (Button)findViewById(R.id.fuction18);

        btnf19 = (Button)findViewById(R.id.fuction19);
        btnf20 = (Button)findViewById(R.id.fuction20);
        btnf21 = (Button)findViewById(R.id.fuction21);
        btnf22 = (Button)findViewById(R.id.fuction22);
        btnf23 = (Button)findViewById(R.id.fuction23);

        tvReceive = (TextView)findViewById(R.id.tvReceive);
        btnf1.setText("start Listen");
        btnf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BuzokuFuction.getInstance(DeviceDbActivity.this).startListen("",0,0,true, new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("start listening....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("start listener result:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {

                    }
                });

            }
        });

        btnf2.setText("add topap");
        btnf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TopAP topAP = new TopAP();
                count++;
                topAP.setSsid("zyxtest"+count);
                topAP.setEncryption("WAP2");
                topAP.setPassword("123456");
                BuzokuFuction.getInstance(DeviceDbActivity.this).addToTopApList(topAP, new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("addToTopApList....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("addToTopApList:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {

                    }


                });
            }
        });

        btnf3.setText("pi n v");
        btnf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuzokuFuction.getInstance(DeviceDbActivity.this).queryCheckPiUpdate(new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("queryCheckPiUpdate....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("queryCheckPiUpdate:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {
                        if(resultList!=null)
                        {
                            String v = (String)resultList.get(0);
                            String b = (String)resultList.get(1);
                            tvReceive.setText("queryCheckPiUpdate Version:"+v+" b:"+b);
                        }else{
                            tvReceive.setText("queryCheckPiUpdate....failed");
                        }

                    }


                });
            }
        });

        btnf4.setText("pi w list");
        btnf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String str = BuzokuRestApi.RestGetTopApList("http://121.201.66.39:10005/api/v1");
//                Log.i(DtConstant.TAG, "db:123:"+str);
//                Log.i(DtConstant.TAG, "db:12355555555:"+str);

                BuzokuFuction.getInstance(DeviceDbActivity.this).queryDndList(new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("queryDndList....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {

                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {
                        String temp = "";
                        if(resultList!=null)
                        {
                            dndList = resultList;
                            for(Object o:resultList)
                            {
                                DndMac d = (DndMac) o;
                                temp+=d.toString();
                                temp+="\n";
                            }
                            tvReceive.setText(temp);
                        }
                    }
                });

            }
        });
        btnf5.setText("start deauth");
        btnf5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deviceList = new ArrayList<Device>();

                Device d = new Device();
                d.setRole("station");
                d.setMac("f48b3228a85b");
                d.setSsid("DT7");
                deviceList.add(d);

                Device d1 = new Device();
                d1.setRole("ap");
                d1.setSsid("DT11");
                deviceList.add(d1);

                Device d2 = new Device();
                d2.setRole("ap");
                d2.setSsid("DT15");
                deviceList.add(d2);

                Device d3 = new Device();
                d3.setRole("station");
                d3.setMac("f48b3228a85F");
                d3.setSsid("DT8");
                deviceList.add(d3);


                BuzokuFuction.getInstance(DeviceDbActivity.this).restartDeauth(new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("startDeauth....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("startDeauth:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {

                    }


                },deviceList,48, false);
            }
        });


        btnf6.setText("del w list");
        btnf6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dndList==null)
                    return;
                Object object = dndList.get(0);
                DndMac dnd = (DndMac)object;
                Log.i(DtConstant.TAG, "dnd mac:"+dnd.getMac());
                BuzokuFuction.getInstance(DeviceDbActivity.this).removeDndDeviceFromWhiteList(dnd, new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("removeDndDeviceFromWhiteList....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("removeDndDeviceFromWhiteList:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {

                    }


                });
            }
        });


        btnf7.setText("v update");
        btnf7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuzokuFuction.getInstance(DeviceDbActivity.this).queryCheckAppUpdate(new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("queryCheckAppUpdate....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        //tvReceive.setText("queryCheckAppUpdate:"+result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {
                        if(resultList!=null)
                        {
                            String v = (String)resultList.get(0);
                            String c = (String)resultList.get(1);
                            String p = (String)resultList.get(2);
                            String m = (String)resultList.get(3);
                            String n = (String)resultList.get(4);
                            tvReceive.setText("queryCheckAppUpdate v:"+v+"  c:"+c
                                + " p:"+p+" n:"+n +" m:"+m);
                        }

                    }


                });
            }
        });



        btnf8.setText("add topap");
        btnf8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSID ssid = new SSID();
                ssid.setSsid("bruce123"+count);
                ssid.setPassword("123456");
                ssid.setDescription("ccccc");
                ssid.setEncryption("WPA");
//                List<SSID>  ssids = new ArrayList<>();
//                ssids.add(ssid);
                BuzokuFuction.getInstance(DeviceDbActivity.this).addToSsidLib(ssid ,new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("addToSsidLib....");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("addToSsidLib:"+result);
                        count++;
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {

                    }


                });
            }
        });


        btnf9.setText("get ta db");
        btnf9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            List<TopAP> slist = DataManager.getInstance(DeviceDbActivity.this)
                    .getAllTopApFromDb();

            String temp = "";
            if(slist!=null)
            {
                for(TopAP s:slist)
                {
                    temp +=s.toString();
                    temp +="\n";
                    Log.i("zyxtest","getAllTopApFromDb:"+temp);
                }

            }
            tvReceive.setText(temp);
            }
        });


        btnf10.setText("add ana task");
        btnf10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnalysisTask s = new AnalysisTask();
                s.setTaskId(10000);
                DataManager.getInstance(DeviceDbActivity.this).addAnalysisTaskToDb(s);
                tvReceive.setText("add "+s.toString());
            }
        });

        btnf11.setText("strike");
        btnf11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for(long i=0;i<50;i++)
//                DataManager.getInstance(DeviceDbActivity.this).removeTopApFromDb(i);
                //tvReceive.setText("remove "+s.toString());
//                TimeUtils.getCurrentUtcTimeInString();
                StrikeAnalysis s = new StrikeAnalysis();
                List<Task> taskList = DataManager.getInstance(DeviceDbActivity.this).getTaskListFromDb();
                List<Task> tasks = new ArrayList<Task>();
                if(taskList!=null)
                {
                    int tcount = 0;
                    for(Task t:taskList)
                    {
                        if(t.getId()!=10000)
                        {
                            tasks.add(t);
                            tcount++;
                        }
                        if(tcount==4)
                            break;
                    }
                }
                Log.i(DtConstant.TAG, "strike>>>tasks size:"+tasks.size());
                DataManager.getInstance(DeviceDbActivity.this).startStrikeAnalyse(s,tasks,new Handler());

            }
        });

        btnf12.setText("start mitm");
        btnf12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            List<Device> devices = new ArrayList<Device>();
            Device d = new Device();
            d.setMac("f48b3228a85b");
            d.setRole("station");
            devices.add(d);

//                d = new Device();
//                d.setMac("f48b3228a85a");
//                d.setRole("station");
//                devices.add(d);

//            BuzokuFuction.getInstance(DeviceDbActivity.this).startMitm(devices, new FuctionListener() {
//                @Override
//                public void onPreExecute(int fuctionId) {
//                    tvReceive.setText("startMitm... ");
//                }
//
//                @Override
//                public void onPostExecute(int fuctionId, int result) {
//                    tvReceive.setText("startMitm: "+result);
//                }
//
//                @Override
//                public void onResult(int fuctionId, List<Object> resultList) {
//
//                }
//            });

            }
        });

        btnf13.setText("stop mitm");
        btnf13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuzokuFuction.getInstance(DeviceDbActivity.this).stopMitm(new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {
                        tvReceive.setText("stopMitm... ");
                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {
                        tvReceive.setText("stopMitm: " + result);
                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {
                    }
                });
            }
        });


        btnf14.setText("follow");
        btnf14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BuzokuFuction.getInstance(DeviceDbActivity.this).queryMitmState(new FuctionListener() {
//                    @Override
//                    public void onPreExecute(int fuctionId) {
//                        tvReceive.setText("queryMitmState... ");
//                    }
//
//                    @Override
//                    public void onPostExecute(int fuctionId, int result) {
//                        tvReceive.setText("queryMitmState: " + result);
//                    }
//
//                    @Override
//                    public void onResult(int fuctionId, List<Object> resultList) {
//                    }
//                });
                final long eTime = TimeUtils.getCurrentTimeInLong();
                final long sTime = TimeUtils.getCurrentTimeInLong()-5*24*60*60*1000;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataManager.getInstance(DeviceDbActivity.this).startFollowAnalyse(sTime,eTime,"ccb25560df2a", mHandler);
                    }
                }).start();

            }
        });


        btnf15.setText("follow mac");
        btnf15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<SpecialDevice> sdList = DataManager.getInstance(DeviceDbActivity.this).getWhiteListFromDb();
//                String temp = null;
//                for(SpecialDevice sp :sdList)
//                {
//                    temp += sp.toString();
//                    temp +="\n";
//                }
//                tvReceive.setText(temp);
                List<FollowAnalysisMac> followAnalysisMacs= DataManager.getInstance(DeviceDbActivity.this).getAllFollowAnalysisMacFromDb();
                String temp = null;
                for(FollowAnalysisMac sp :followAnalysisMacs)
                {
                    temp += sp.toString();
                    temp +="\n";
                }
                tvReceive.setText(temp);
            }
        });

        btnf16.setText("del task");
        btnf16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<SpecialDevice> sdList = DataManager.getInstance(DeviceDbActivity.this).getBlackListFromDb();
//                String temp = null;
//                for(SpecialDevice sp :sdList)
//                {
//                    temp += sp.toString();
//                    temp +="\n";
//                }
//                tvReceive.setText(temp);
                List<Task> deviceList= DataManager.getInstance(DeviceDbActivity.this).getTaskListFromDb();
                DataManager.getInstance(DeviceDbActivity.this).removeTaskListFromDb(deviceList);
            }
        });

        btnf17.setText("down file");
        btnf17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Device d = new Device();
//                d.setMac("12345678");

//                List<SpecialDevice> deviceList= DataManager.getInstance(DeviceDbActivity.this).queryMacInWhitelist("78");
//                String temp = null;
//                for(SpecialDevice sp :deviceList)
//                {
//                    temp += sp.toString();
//                    temp +="\n";
//                }
//                tvReceive.setText(temp);
                ///
//                DataManager.getInstance(DeviceDbActivity.this).addTempTaskToDb();
//                tvReceive.setText("addTempTaskToDb");
                ///
                //tcpClientTest();
//                DtDatabaseHelper.getInstance(DeviceDbActivity.this).execSQLTest();
                //String apkPath = Environment.getExternalStorageDirectory()+"/deter/Wifimonitor.apk";
                String downPath = "http://192.168.30.15/deter/apk/HKUSBApp.apk";
//                DownloadUtil.get().download(downPath, "deter", new DownloadUtil.OnDownloadListener() {
//                    @Override
//                    public void onDownloadSuccess() {
//                        //Utils.showToast(MainActivity.this, "????");
//                        //tvReceive.setText("success");
//                        Log.i("zyxtest","onDownloadSuccess");
//                    }
//                    @Override
//                    public void onDownloading(int progress) {
//                        //progressBar.setProgress(progress);
//                        //tvReceive.setText("download:"+progress+"%");
//                        Log.i("zyxtest","onDownloading p:"+progress+"%");
//                    }
//                    @Override
//                    public void onDownloadFailed() {
//                        //Utils.showToast(MainActivity.this, "????");
//                        //tvReceive.setText("fail");
//                        Log.i("zyxtest","onDownloadFailed");
//                    }
//                });

                BuzokuFuction.getInstance(DeviceDbActivity.this).startDownloadFile("/deter/apk/V1.4.1/app-debug.apk", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        Log.i("zyxtest","onDownloadSuccess");
                    }

                    @Override
                    public void onDownloading(int progress) {
                        Log.i("zyxtest","onDownloading p:"+progress+"%");
                    }

                    @Override
                    public void onDownloadFailed() {
                        Log.i("zyxtest","onDownloadFailed");
                    }
                });



            }
        });


        btnf18.setText("query 2");
        btnf18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Device d = new Device();
//                d.setMac("abcd5678");
//
//                List<SpecialDevice> deviceList = DataManager.getInstance(DeviceDbActivity.this).queryMacInBlacklist("78");
//                String temp = null;
//                for(SpecialDevice sp :deviceList)
//                {
//                    temp += sp.toString();
//                    temp +="\n";
//                }
//                tvReceive.setText(temp);

//                String vendorCn1  = DataManager.getInstance(DeviceDbActivity.this).queryVendor("fcf136");
//                String vendorCn2  = DataManager.getInstance(DeviceDbActivity.this).queryVendorCn("001882");
//                String vendorEn1  = DataManager.getInstance(DeviceDbActivity.this).queryVendorEn("fcf136");
//                String [] vendorEn2  = DataManager.getInstance(DeviceDbActivity.this).queryVendor("001882");
//                tvReceive.setText(vendorEn2[0]+"  "+vendorEn2[1]+"  ");
//                String a = Locale.getDefault().getLanguage();
//                tvReceive.setText(a);

                List<FollowAnalysis> followAnalysises= DataManager.getInstance(DeviceDbActivity.this).getAllFollowAnalysisFromDb();
                String temp = null;
                for(FollowAnalysis sp :followAnalysises)
                {
                    temp += sp.toString();
                    temp +="\n";
                }
                tvReceive.setText(temp);

            }
        });

        btnf19.setText("import vendor");
        btnf19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                List<Device> deviceList = DataManager.getInstance(DeviceDbActivity.this).getAllDeviceFromDb();
//                DataManager.getInstance(DeviceDbActivity.this).exportMacDataToFile(deviceList);
//                tvReceive.setText("OK");

                File file = new File(FileUtils.getSDPath()+"/oui_20170821_ch.xls");
                ExcelUtils.readMacVendor2DB(file, DeviceDbActivity.this);

            }
        });


        btnf20.setText("query ids");
        btnf20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 BuzokuFuction.getInstance(DeviceDbActivity.this).queryMacIdentitys("a1b2c3", new FuctionListener() {
                    @Override
                    public void onPreExecute(int fuctionId) {

                    }

                    @Override
                    public void onPostExecute(int fuctionId, int result) {

                    }

                    @Override
                    public void onResult(int fuctionId, List<Object> resultList) {
                        String temp = "";
                        if(resultList!=null)
                        {
                            for(Object o :resultList)
                            {
                                Identity dndMac = (Identity)o;
                                temp += dndMac.toString();
                                temp +="\n";
                            }
                        }

                        tvReceive.setText(temp);
                    }
                });

            }
        });

        btnf21.setText("query urls");
        btnf21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BuzokuFuction.getInstance(DeviceDbActivity.this).queryMacUrls("a1b2", new FuctionListener() {
//                    @Override
//                    public void onPreExecute(int fuctionId) {
//
//                    }
//
//                    @Override
//                    public void onPostExecute(int fuctionId, int result) {
//
//                    }
//
//                    @Override
//                    public void onResult(int fuctionId, List<Object> resultList) {
//                        String temp = "";
//                        if(resultList!=null)
//                        {
//                            for(Object o :resultList)
//                            {
//                                URL dndMac = (URL) o;
//                                temp += dndMac.toString();
//                                temp +="\n";
//                            }
//                        }
//
//                        tvReceive.setText(temp);
//                    }
//                });
                List<Device> dlist = new ArrayList<Device>();
                Device d = new Device();
                d.setRole("station");
                d.setMac("aabbccddeeff");
                dlist.add(d);
                Device d1 = new Device();
                d1.setRole("station");
                d1.setMac("aabbccddee22");
                dlist.add(d1);
//                String tmp = BuzokuFuction.getInstance(DeviceDbActivity.this).buildMitmPostString(
//                        dlist, "dt","123456","wpa2"
//                );
                String tmp = BuzokuFuction.getInstance(DeviceDbActivity.this).buildMitmPostString(
                        dlist, null,null,null
                );
                tvReceive.setText(tmp);
            }
        });


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };


    void tcpClientTest()
    {
        new Thread(){
            @Override
            public void run() {
                try{
                    Log.i("TcpMsgService","tcpClientTest>>>clicked");
                    Socket socket = new Socket("127.0.0.1",9000);
                    Log.i("TcpMsgService","tcpClientTest>>>Connected");
                    OutputStream os = socket.getOutputStream();
                    os.write("40020588aabbccddee12".getBytes());
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String data = br.readLine();
                    Log.i("TcpMsgService","tcpClientTest>>>"+data);

                    br.close();
                    is.close();
                    os.close();
                } catch (Exception e){
                    Log.i("TcpMsgService","tcpClientTest>>>exception:"+e.getMessage());
                }
            }
        }.start();
    }

}
