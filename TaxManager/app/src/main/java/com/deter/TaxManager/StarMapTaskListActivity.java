package com.deter.TaxManager;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deter.TaxManager.model.AnalysisTask;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.StrikeAnalysis;
import com.deter.TaxManager.model.Task;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.adapter.BlackWhiteListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */
public class StarMapTaskListActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    final int init_data_code = 0x0021;

    final int delete_data_code = 0x0051;

    final int least_choise_code = 0x0033;

    private int fromType;

    private ImageView topDeleteIv;

    private RelativeLayout bottomChoiceView;
    private CheckBox bottomCheckChoice;
    private Button deleteSelectBtn;


    private DataManager dataManager;

    private List<Object> totalList;

    private ListView startMapTaskListview;

    private BlackWhiteListAdapter starMapTaskAdapter;

    private String analysisLimitStr;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case init_data_code:
                    if (null == totalList) {
                        totalList = new ArrayList<>();
                    } else {
                        totalList.clear();
                    }

                    List<Object> list = (List<Object>) msg.obj;
                    if (!list.isEmpty()) {
                        Collections.reverse(list);
                        totalList.addAll(list);
                    } else {
                        topDeleteIv.setVisibility(View.GONE);
                        if (fromType != APPConstans.DETAIL_START_MAP_TASK_LIST) {
                            bottomChoiceView.setVisibility(View.GONE);
                        }
                    }
                    starMapTaskAdapter.notifyDataSetChanged();
                    break;

                case delete_data_code:
                    totalList.clear();
                    List<Object> deleteList = (List<Object>) msg.obj;
                    if (!deleteList.isEmpty()) {
                        //Collections.reverse(deleteList);
                        totalList.addAll(deleteList);
                    }

                    if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST) {
                        onBackOpr();
                    } else {
                        deleteSelectBtn.setEnabled(false);
                        oprTypeStyle(BlackWhiteListAdapter.DELETE_MODE);
                        if (totalList.isEmpty()) {
                            bottomChoiceView.setVisibility(View.GONE);
                        }
                    }

                    break;

                case least_choise_code:
                    ToastUtils.showShort(StarMapTaskListActivity.this, getResources().getString(R.string.least_choise_task_prompt));
                    break;

            }

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_star_map_task_list);

        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackOpr();
            }
        });

        topDeleteIv =getView(R.id.search);
        topDeleteIv.setImageResource(R.drawable.start_map_task_top_delete_icon);
        topDeleteIv.setVisibility(View.VISIBLE);
        topDeleteIv.setOnClickListener(this);

        bottomChoiceView = getView(R.id.bottom_choice_view);
        bottomCheckChoice = getView(R.id.bottom_check_choice);
        deleteSelectBtn = getView(R.id.folders_select_btn);

        startMapTaskListview = getView(R.id.start_map_task_list);
        TextView emptyView = getView(R.id.emptyView);
        startMapTaskListview.setEmptyView(emptyView);

        startMapTaskListview.setOnItemClickListener(this);
        bottomCheckChoice.setOnClickListener(this);
        deleteSelectBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                oprTypeStyle(BlackWhiteListAdapter.DELETE_MODE);
                break;

            case R.id.bottom_check_choice:
                if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                    if (starMapTaskAdapter.getCount() != starMapTaskAdapter.getSelectListItems().size()) {
                        if (starMapTaskAdapter.getCount() >= 10 || starMapTaskAdapter.getSelectListItems().size() == 10) {
                            if (null == analysisLimitStr) {
                                analysisLimitStr = StarMapTaskListActivity.this.getResources().getString(R.string.analysis_select_limit_prompt);
                            }
                            ToastUtils.showShort(StarMapTaskListActivity.this, analysisLimitStr);
                            bottomCheckChoice.setChecked(false);
                            return;
                        }
                    }
                }
                starMapTaskAdapter.checkAllChoice();
                break;

            case R.id.folders_select_btn:
                deleteSelectItem();
                break;
        }
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        int resID=-1;
        if (null != intent) {

            fromType = intent.getIntExtra(APPConstans.KEY_DETAIL_TYPE, -1);

            if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST || fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                setTitleText(R.string.star_map_task_list_title);
                resID = R.layout.star_map_list_item;
            } else if (fromType == APPConstans.DETAIL_SSID) {
                setTitleText(R.string.title_ssid_pwd_library_manager_list_str);
                resID = R.layout.black_white_list_item;
            } else if (fromType == APPConstans.DETAIL_TOP_AP_LIST) {
                setTitleText(R.string.title_top_list_manager_list_str);
                resID = R.layout.black_white_list_item;
            }

            if (null == totalList) {
                totalList = new ArrayList<>();
            } else {
                totalList.clear();
            }

            if (null != startMapTaskListview) {
                starMapTaskAdapter = new BlackWhiteListAdapter(resID, StarMapTaskListActivity.this, totalList);
                starMapTaskAdapter.setDeleteModeView(bottomCheckChoice, deleteSelectBtn);

                startMapTaskListview.setAdapter(starMapTaskAdapter);
                //starMapTaskAdapter.setOprType(BlackWhiteListAdapter.NORMAL_MODE);
                if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST) {
                    oprTypeStyle(BlackWhiteListAdapter.NORMAL_MODE);
                } else {
                    oprTypeStyle(BlackWhiteListAdapter.DELETE_MODE);
                }

            }

            new Thread(loadDataRunnable).start();

        }

    }


    Runnable loadDataRunnable = new Runnable() {

        @Override
        public void run() {
            if (null==dataManager){
                dataManager = DataManager.getInstance(getApplicationContext());
            }
            List<Object> list = getListData();
            Message msg = mHandler.obtainMessage();
            msg.what = init_data_code;
            msg.obj = list;
            mHandler.sendMessage(msg);
        }
    };


    private List<Object> getListData() {
        List<Object> returnList = new ArrayList<>();
        if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST) {
            List<Task> taskList = dataManager.getTaskListFromDb();
            if (null!=taskList && !taskList.isEmpty()) {
                returnList.addAll(taskList);
            }
        } else if (fromType == APPConstans.DETAIL_SSID) {
            List<SSID> ssidList = dataManager.getAllSsidFromDb();
            if (null!=ssidList && !ssidList.isEmpty()) {
                returnList.addAll(ssidList);
            }
        } else if (fromType == APPConstans.DETAIL_TOP_AP_LIST) {
            List<TopAP> topAPList = dataManager.getAllTopApFromDb();
            if (null!=topAPList && !topAPList.isEmpty()) {
                returnList.addAll(topAPList);
            }
        } else if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
            StrikeAnalysis strikeAnalysis = AnalysisLogic.getInstance().getCurOprStrikeAnalysis();
            if (null != strikeAnalysis) {
                List<Task> taskList = dataManager.queryTaskInTime(strikeAnalysis.getStartTime(), strikeAnalysis.getStopTime());
                if (null != taskList && !taskList.isEmpty()) {
                    returnList.addAll(taskList);
                }
            }
        }

        return returnList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != totalList && !totalList.isEmpty()) {
            Object device = totalList.get(position);
            if (null != device) {

                if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST) {
                    Intent intent=new Intent(StarMapTaskListActivity.this,TaskDetailActivity.class);
                    Task task= (Task) device;
                    Log.i("zyxtest", "onItemClick>>>taskId:"+task.getId());
                    intent.putExtra("task_id",task.getId());
                    startActivity(intent);
                    return;
                }

                //删除模式下的点击action
                if (BlackWhiteListAdapter.DELETE_MODE == starMapTaskAdapter.getOprType()) {
                    if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                        if (starMapTaskAdapter.getSelectListItems().size() == 10) {//can select up to 10 tasks
                            if (device instanceof Task) {
                                Task itm = (Task) device;
                                String key = Long.toString(itm.getStartTime());
                                if (null == starMapTaskAdapter.getSelectListItems().get(key)) {
                                    if (null == analysisLimitStr) {
                                        analysisLimitStr = StarMapTaskListActivity.this.getResources().getString(R.string.analysis_select_limit_prompt);
                                    }
                                    ToastUtils.showShort(StarMapTaskListActivity.this, analysisLimitStr);
                                    return;
                                }
                            }
                        }
                    }
                    starMapTaskAdapter.radioChoice(device);
                }
            }
        }
    }

    private void deleteSelectItem() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Object> selectItems = starMapTaskAdapter.getSelectListItems();

                List<AnalysisTask> taskList = null;
                if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                    taskList = new ArrayList<AnalysisTask>();
                }

                for (String key : selectItems.keySet()) {
                    if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST) {
                        Task findDevice = (Task) selectItems.get(key);
                        dataManager.removeTaskFromDb(findDevice);
                        if (totalList.contains(findDevice)) {
                            totalList.remove(findDevice);
                        }
                    } else if (fromType == APPConstans.DETAIL_SSID) {
                        SSID findDevice = (SSID) selectItems.get(key);
                        dataManager.removeSsidFromDb(findDevice);
                        if (totalList.contains(findDevice)) {
                            totalList.remove(findDevice);
                        }
                    } else if (fromType == APPConstans.DETAIL_TOP_AP_LIST) {
                        TopAP findDevice = (TopAP) selectItems.get(key);
                        dataManager.removeTopApFromDb(findDevice);
                        if (totalList.contains(findDevice)) {
                            totalList.remove(findDevice);
                        }
                    } else if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                        Task findDevice = (Task) selectItems.get(key);

                        AnalysisTask analysisTask = new AnalysisTask();
                        analysisTask.setTaskId(findDevice.getId());
                        if (null == findDevice.getAddress() || TextUtils.isEmpty(findDevice.getAddress())) {
                            String taskName = getString(R.string.longitude_latitude, String.valueOf(findDevice.getLongitude()), String.valueOf(findDevice.getLatitude()));
                            analysisTask.setTaskName(taskName);
                        } else {
                            analysisTask.setTaskName(findDevice.getAddress());
                        }
                        analysisTask.setTaskStartTime(findDevice.getStartTime());
                        analysisTask.setTaskStopTime(findDevice.getStopTime());

                        taskList.add(analysisTask);
                    }

                }

                if (fromType == APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME) {
                    if (taskList.size() < 2) {
                        mHandler.sendEmptyMessage(least_choise_code);
                        return;
                    }
                    AnalysisLogic.getInstance().setSelectTaskList(taskList);
                    Intent intent = new Intent(StarMapTaskListActivity.this, AnalysisTaskActivity.class);
                    intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME);
                    startActivity(intent);
                    finish();
                    return;
                }

                //List<SpecialDevice> list = getListData();
                List<Object> list = new ArrayList<Object>();
                list.addAll(totalList);
                Message msg = mHandler.obtainMessage();
                msg.what = delete_data_code;
                msg.obj = list;
                mHandler.sendMessageDelayed(msg, 100);

            }
        }).start();

    }


    private void oprTypeStyle(int oprType) {
        if (BlackWhiteListAdapter.NORMAL_MODE == oprType) {
            if (!totalList.isEmpty()){
                topDeleteIv.setVisibility(View.VISIBLE);
            }
            starMapTaskAdapter.setOprType(BlackWhiteListAdapter.NORMAL_MODE);
            bottomChoiceView.setVisibility(View.GONE);
            bottomCheckChoice.setChecked(false);
            deleteSelectBtn.setEnabled(false);
        } else {
            topDeleteIv.setVisibility(View.GONE);
            starMapTaskAdapter.setOprType(BlackWhiteListAdapter.DELETE_MODE);
            bottomChoiceView.setVisibility(View.VISIBLE);
        }
    }


    private void onBackOpr() {

        if (fromType != APPConstans.DETAIL_START_MAP_TASK_LIST) {
            if (starMapTaskAdapter.getSelectListItems().size() > 0) {
                starMapTaskAdapter.getSelectListItems().clear();
                starMapTaskAdapter.notifyDataSetChanged();
                deleteSelectBtn.setEnabled(false);
                bottomCheckChoice.setChecked(false);
            } else {
                finish();
            }
            return;
        }

        if (BlackWhiteListAdapter.DELETE_MODE == starMapTaskAdapter.getOprType()) {
            starMapTaskAdapter.getSelectListItems().clear();
            oprTypeStyle(BlackWhiteListAdapter.NORMAL_MODE);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackOpr();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
