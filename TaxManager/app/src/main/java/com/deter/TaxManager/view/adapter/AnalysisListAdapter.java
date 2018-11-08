package com.deter.TaxManager.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.FollowAnalysis;
import com.deter.TaxManager.model.StrikeAnalysis;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DateUtil;

import java.util.List;

/**
 * Created by zhongqiusheng on 2017/11/1 0001.
 */
public class AnalysisListAdapter extends CommonAdapter {

    private int color_analysis_isruning;
    private int color_analysis_finish;
    private int color_default_normal;
    private String status_analysis_isruning;
    private String status_analysis_finish;

    public AnalysisListAdapter(int mItemLayoutId, Context mContext, List mDatas) {
        super(mItemLayoutId, mContext, mDatas);
        status_analysis_finish = getString(R.string.analysis_status_finish);
        status_analysis_isruning = getString(R.string.analysis_status_isruning);
        color_analysis_isruning = getColor(R.color.color_FF0000);
        color_analysis_finish = getColor(R.color.tabs_selected);
        color_default_normal = getColor(R.color.ssid_pwd_library_text_color);
    }


    private String getString(int resID) {
        return mContext.getResources().getString(resID);
    }

    private int getColor(int resID) {
        try {
            return mContext.getResources().getColor(resID);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public void convert(ViewHolder viewHolder, Object item) {
        String taskName = "";
        int statu = -1;
        String startTimeStr = "";
        String stopTimeStr = "";
        String followAnalysisID="";
        if (item instanceof StrikeAnalysis) {
            StrikeAnalysis strikeAnalysis = (StrikeAnalysis) item;
            taskName = strikeAnalysis.getName();
            statu = strikeAnalysis.getStatus();
            startTimeStr = DateUtil.formatDate(strikeAnalysis.getStartTime());
            long stopTime = strikeAnalysis.getStopTime();
            if (stopTime > 0) {
                stopTimeStr = DateUtil.formatDate(stopTime);
            }
//            viewHolder.getView(R.id.task_time_number).setVisibility(View.GONE);
//            viewHolder.getView(R.id.task_number_name).setVisibility(View.GONE);
        }else if(item instanceof FollowAnalysis){
            FollowAnalysis followAnalysis = (FollowAnalysis) item;
            followAnalysisID = String.valueOf(followAnalysis.getId());
            taskName = APPUtils.formateTextToMacAddress(followAnalysis.getMac());;
            statu = 200;
            TextView textView = viewHolder.getView(R.id.task_number_name);
            textView.setVisibility(View.VISIBLE);
            textView.setText((viewHolder.getPosition() + 1) + ". ");
            startTimeStr = DateUtil.formatDate(followAnalysis.getStartTime());
            long stopTime = followAnalysis.getStopTime();
            if (stopTime > 0) {
                stopTimeStr = DateUtil.formatDate(stopTime);
            }
        }

        TextView taskNameTv = viewHolder.getView(R.id.task_name);
        taskNameTv.setText(taskName);

        TextView statuTv = viewHolder.getView(R.id.task_status);
        View progressBar = viewHolder.getView(R.id.task_progressbar);

        if (200 != statu) {
            statuTv.setText(status_analysis_isruning);
            if (-1 != color_analysis_isruning) {
                statuTv.setTextColor(color_analysis_isruning);
            }
            if (-1 != color_default_normal) {
                taskNameTv.setTextColor(color_default_normal);
            }
            progressBar.setVisibility(View.VISIBLE);
        } else {
            statuTv.setText(status_analysis_finish);
            if (-1 != color_analysis_finish) {
                statuTv.setTextColor(color_analysis_finish);
                taskNameTv.setTextColor(color_analysis_finish);
            }
            progressBar.setVisibility(View.GONE);
        }

        viewHolder.setText(R.id.task_start_time, startTimeStr);
        viewHolder.setText(R.id.task_end_time, stopTimeStr);

        ImageView choiceIv = viewHolder.getView(R.id.analysis_task_choice_iv);

        if (oprType == DELETE_MODE) {

            Object selectObject;
            if (!TextUtils.isEmpty(followAnalysisID)) {
                selectObject = selectListItems.get(followAnalysisID);
            } else {
                selectObject = selectListItems.get(taskName);
            }

            if (null != selectObject) {
                choiceIv.setImageResource(R.drawable.bottom_checkbox_select_icon);
            } else {
                choiceIv.setImageResource(R.drawable.bottom_checkbox_unselect_icon);
            }
            choiceIv.setVisibility(View.VISIBLE);
        } else {
            choiceIv.setVisibility(View.GONE);
        }

    }


    @Override
    public void radioChoice(Object selectItemObject) {
        String taskName = null;
        if (selectItemObject instanceof StrikeAnalysis) {
            StrikeAnalysis strikeAnalysis = (StrikeAnalysis) selectItemObject;
            taskName = strikeAnalysis.getName();
        } else if (selectItemObject instanceof FollowAnalysis) {
            FollowAnalysis followAnalysis = (FollowAnalysis) selectItemObject;
            taskName = String.valueOf(followAnalysis.getId());
        }

        if (null == taskName) {
            return;
        }

        Object device = selectListItems.get(taskName);

        if (null != device) {
            selectListItems.remove(taskName);
        } else {
            selectListItems.put(taskName, selectItemObject);
        }
        onRadioClick();
    }


    @Override
    public void checkAllChoice() {
        List<Object> list = mDatas;
        if (mDatas.size() != selectListItems.size()) {
            selectListItems.clear();

            for (Object device : list) {
                String taskName = null;
                if (device instanceof StrikeAnalysis) {
                    taskName = ((StrikeAnalysis) device).getName();
                } else if (device instanceof FollowAnalysis) {
                    taskName = String.valueOf(((FollowAnalysis) device).getId());
                }

                if (null != taskName) {
                    selectListItems.put(taskName, device);
                }
            }
        } else {
            selectListItems.clear();
        }

        onRadioClick();
    }



}
