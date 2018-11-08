package com.deter.TaxManager;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.model.AnalysisTask;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.adapter.ViewHolder;

/**
 * Created by zhongqiusheng on 2017/10/11 0011.
 */
public class AnalysisTaskActivity extends BaseActivity implements AnalysisLogic.AnalysisNotifyListener, AnalysisLogic.AnalysisNotDataListener {

    private ListView analysisListview;

    private CommonAdapter<AnalysisTask> commonAdapter;

    AnalysisLogic analysisLogic;

    private int color_analysis_isruning;
    private int color_analysis_finish;
    private int color_default_normal;
    private String status_analysis_isruning;
    private String status_analysis_finish;

    private boolean hasRefresh;

    private int fromType;

    private Dialog promptDialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_analysis_task);
        setTitleText(R.string.analysis_detail);
        analysisListview = (ListView) findViewById(R.id.analysis_listview);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            fromType = intent.getIntExtra(APPConstans.KEY_DETAIL_TYPE, -1);

            status_analysis_finish = getResources().getString(R.string.analysis_status_finish);
            status_analysis_isruning = getResources().getString(R.string.analysis_status_isruning);
            color_analysis_isruning = getResources().getColor(R.color.color_FF0000);
            color_analysis_finish = getResources().getColor(R.color.tabs_selected);
            color_default_normal = getResources().getColor(R.color.ssid_pwd_library_text_color);

            analysisLogic = AnalysisLogic.getInstance();
            analysisLogic.addNotifyListener(this);
            analysisLogic.setNotDataListener(this);

            commonAdapter = new CommonAdapter<AnalysisTask>(R.layout.analysis_task_item, AnalysisTaskActivity.this, AnalysisLogic.getInstance().getSelectTaskList()) {
                @Override
                public void convert(ViewHolder viewHolder, AnalysisTask item) {
                    TextView taskName = viewHolder.getView(R.id.task_name);
                    taskName.setText(item.getTaskName());
                    TextView statuTv = viewHolder.getView(R.id.task_status);
                    View progressBar = viewHolder.getView(R.id.task_progressbar);
                    if (200!=item.getStatus()) {
                        statuTv.setText(status_analysis_isruning);
                        statuTv.setTextColor(color_analysis_isruning);
                        taskName.setTextColor(color_default_normal);
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        statuTv.setText(status_analysis_finish);
                        statuTv.setTextColor(color_analysis_finish);
                        taskName.setTextColor(color_analysis_finish);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    viewHolder.setText(R.id.task_start_time, DateUtil.formatDate(item.getTaskStartTime()));
                    long stopTime = item.getTaskStopTime();
                    if (stopTime > 0) {
                        viewHolder.setText(R.id.task_end_time, DateUtil.formatDate(stopTime));
                    } else {
                        viewHolder.setText(R.id.task_end_time, "");
                    }
                }
            };

            analysisListview.setAdapter(commonAdapter);

            if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                analysisLogic.startStrikeAnalyse(this);
            } else if (fromType == APPConstans.DETAIL_ANALYSIS_TASK_LIST) {
                analysisLogic.getAnalysisTaskListFromDb(this);
            }
        }

    }


    @Override
    public void notifyData(int code) {
        if (AnalysisLogic.TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST == code) {
            commonAdapter.notifyDataSetChanged();
        } else if (AnalysisLogic.TYPE_NOTIFY_COLLISION_ANALYSIS_FINISH == code) {
            hasRefresh = true;
            goToAnalysisResultActivity();
        }
    }


    @Override
    public void notData() {
        showPromptDialog(getResources().getString(R.string.analysis_not_data_prompt));
    }

    public void goToAnalysisResultActivity() {
        Intent intent = new Intent(this, CollisionAnalysisResultActivity.class);
        startActivity(intent);
        finish();
    }


    private void showPromptDialog(String prompt) {
        dimissPromptDialog();
        promptDialog = DialogUtils.showConfirmDialog(this, prompt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analysisLogic.getStrikeAnalysis(AnalysisTaskActivity.this);
                hasRefresh = true;
                dimissPromptDialog();
                finish();
            }
        });
    }


    private void dimissPromptDialog() {
        if (null != promptDialog && promptDialog.isShowing()) {
            promptDialog.dismiss();
            promptDialog = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!hasRefresh) {
            analysisLogic.getStrikeAnalysis(this);
        }
        analysisLogic.removeNotifyListener(this);
    }


}
