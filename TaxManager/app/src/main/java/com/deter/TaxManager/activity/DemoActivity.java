package com.deter.TaxManager.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.network.BuzokuRestApi;

public class DemoActivity extends AppCompatActivity {

    Button btnStart;
    TextView tvSend;
    TextView tvReceive;
//    private SharedPreferences prefs;
//    private MessageSender sender;
//    private String serviceIp = null;
//    private String servicePort = null;

    public final static int CMD_START_LISTEN = 100;
    public final static int CMD_QUERY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
//        sender = MessageSender.getInstance();
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        serviceIp = prefs.getString("sip", "");
//        servicePort = prefs.getString("sport", "");
        initView();
    }

    void initView()
    {
        btnStart = (Button)findViewById(R.id.btnStart);
        tvReceive = (TextView)findViewById(R.id.tvReceive);
        tvSend = (TextView)findViewById(R.id.tvSend);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendCmd(CMD_START_LISTEN);
//                tvResult.setText("");
//                tvResult.append("ClientVersion\n");
//                result = BuzokuRestApi.RestGetClientVersion(apihost);
//                tvResult.append(getTimeString() + "\n" + result + "\n");
//
//                tvResult.append("Black List\n");
//                result = BuzokuRestApi.RestGetBlacklist(apihost);
//                tvResult.append(getTimeString() + "\n" + result + "\n");
//
//                tvResult.append("White List\n");
//                result = BuzokuRestApi.RestGetWhitelist(apihost);
//                tvResult.append(getTimeString() + "\n" + result + "\n");

                tvReceive.append("TopAP List\n");
                String result = BuzokuRestApi.RestGetTopApList(BuzokuRestApi.API_HOST);
                tvReceive.append("\n" + result + "\n");

//                tvResult.append("APListByBlack List\n");
//                result = BuzokuRestApi.RestGetAplistByBlackList(apihost);
//                tvResult.append(getTimeString() + "\n" + result + "\n");
//
//                tvResult.append("APTargetList for DEAUTH\n");
//                result = BuzokuRestApi.RestGetApTargetList(apihost);
//                tvResult.append(getTimeString() + "\n" + result + "\n");
//
//                tvResult.append("APTargetList By RSSI\n");
//                result = BuzokuRestApi.RestGetAPByRssi(apihost, "-40");
//                tvResult.append(getTimeString() + "\n" + result + "\n");

            }
        });


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);




        }
    };



}
