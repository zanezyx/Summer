package com.deter.TaxManager;

import android.content.Intent;
import android.widget.ListView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.FollowAnalysisMac;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/10/24 0024.
 */
public class AccompanyAnalysisResultActivity extends BaseActivity {

    private ListView accompanyResultListview;

    private List<Object> allDataList = new ArrayList<>();
    private CommonAdapter<Object> commonAdapter;

    private DataManager dataManager;

    @Override
    public void initView() {
        setContentView(R.layout.activity_accompany_result_layout);
        setTitleText(R.string.analysis_result);
        accompanyResultListview = getView(R.id.accompany_result_listview);
    }

    @Override
    public void initData() {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(this);
        }

        Intent intent = getIntent();
        if (null == intent && !intent.getExtras().containsKey("accompany_id")) {
            finish();
            return;
        }

        long accompany_id = intent.getExtras().getInt("accompany_id");

        List<FollowAnalysisMac> analysisMacs = dataManager.getFollowAnalysisMacById(accompany_id);
        if (null != analysisMacs && !analysisMacs.isEmpty()) {
            allDataList.add("title");
            allDataList.addAll(analysisMacs);

            commonAdapter = new CommonAdapter<Object>(R.layout.activity_accompany_result_item_layout, this, allDataList) {
                @Override
                public void convert(ViewHolder viewHolder, Object item) {
                    String mac = null;
                    String count = null;
                    if (item instanceof FollowAnalysisMac) {
                        FollowAnalysisMac analysisMac = (FollowAnalysisMac) item;
                        mac = APPUtils.formateTextToMacAddress(analysisMac.getMac());
                        count = analysisMac.getAppearTimes() + "/" + analysisMac.getTotalTimes();
                    } else {
                        mac = "MAC";
                        count = getResources().getString(R.string.accompany_result_count_title);
                    }
                    if (null != mac) {
                        viewHolder.setText(R.id.mac_item_tv, mac);
                    }

                    if (null != count) {
                        viewHolder.setText(R.id.item_count, count);
                    }
                }
            };

            accompanyResultListview.setAdapter(commonAdapter);
        }

    }


}
