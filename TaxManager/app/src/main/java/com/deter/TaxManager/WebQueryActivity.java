package com.deter.TaxManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.URL;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.view.adapter.WebQueryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18 0018.
 */
public class WebQueryActivity extends BaseActivity {

    private final int ref_code = 0x0035;

    DataManager dataManager;

    private ArrayList<HashMap<String, Object>> totalList = new ArrayList<HashMap<String, Object>>();

    private TextView title;

    private ListView webQueryListview;

    private WebQueryAdapter webQueryAdapter;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ref_code:
                    Collections.reverse(totalList);
                    webQueryAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_web_query);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.web_query_result_title_str);
        webQueryListview = (ListView) findViewById(R.id.web_query_listview);
        TextView emptyView = (TextView) findViewById(R.id.emptyView);
        webQueryListview.setEmptyView(emptyView);
    }

    @Override
    public void initData() {
        webQueryAdapter = new WebQueryAdapter(R.layout.web_query_list_item, this, totalList);
        webQueryListview.setAdapter(webQueryAdapter);
        mhandler.post(loadDataRunnable);
    }


    Runnable loadDataRunnable = new Runnable() {

        @Override
        public void run() {
            String mac = getIntent().getExtras().getString("deviceMac");
            if (null == dataManager) {
                dataManager = DataManager.getInstance(getApplicationContext());
            }

            if (null != dataManager) {
                List<URL> urlList = dataManager.queryUrl(mac);
                if (null != urlList && !urlList.isEmpty()) {
                    for (URL url : urlList) {
                        String curUrlTime = DateUtil.getChatTime(url.getTime(),WebQueryActivity.this.getApplicationContext());
                        boolean isExistType = isExistTypeInList(url, curUrlTime);

                        if (!isExistType) {
                            generateMap(url, curUrlTime);
                        }
                    }
                }
            }
            //createFakeData();
            mhandler.sendEmptyMessage(ref_code);
        }
    };


    private boolean isExistTypeInList(URL url, String curUrlTime) {
        boolean isExistType = false;
        for (HashMap<String, Object> stringObjectHashMap : totalList) {
            String urlTime = stringObjectHashMap.get("time").toString();
            if (curUrlTime.equals(urlTime)) {
                List<URL> urls = (List<URL>) stringObjectHashMap.get("urls");
                if (null != urls) {
                    urls.add(url);
                }
                isExistType = true;
                return isExistType;
            }
        }
        return isExistType;
    }


    private void generateMap(URL url, String time) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("time", time);
        List<URL> urls = new ArrayList<>();
        urls.add(url);
        map.put("urls", urls);
        totalList.add(map);
    }

    private void createFakeData() {
        List<URL> urls = new ArrayList<>();

        URL yearDay = new URL();
        yearDay.setTime(1503204555L);
        yearDay.setUrl("www.philips.com");
        urls.add(yearDay);

        URL yearDay1 = new URL();
        yearDay1.setTime(1503204055L);
        yearDay1.setUrl("www.aoc.com");
        urls.add(yearDay1);

        URL yearDay2 = new URL();
        yearDay2.setTime(1503203255L);
        yearDay2.setUrl("www.linjia.com");
        urls.add(yearDay2);

        URL yearDay3 = new URL();
        yearDay3.setTime(1503203160L);
        yearDay3.setUrl("www.coolhear.com");
        urls.add(yearDay3);

        URL urlToday = new URL();
        urlToday.setTime(1503283575L);
        urlToday.setUrl("www.android.com");
        urls.add(urlToday);

        URL urlToday1 = new URL();
        urlToday1.setTime(1503283421L);
        urlToday1.setUrl("www.sztop.com");
        urls.add(urlToday1);

        URL urlToday2 = new URL();
        urlToday2.setTime(1503283400L);
        urlToday2.setUrl("www.dell.com");
        urls.add(urlToday2);


        for (URL url : urls) {
            String curUrlTime = DateUtil.getChatTime(url.getTime(),this.getApplicationContext());
            boolean isExistType = isExistTypeInList(url, curUrlTime);

            if (!isExistType) {
                generateMap(url, curUrlTime);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(loadDataRunnable);
    }
}
