package com.deter.TaxManager;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.StrikeAnalysis;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.utils.DensityUtil;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.BasePopupWindow;
import com.deter.TaxManager.view.adapter.AnalysisListAdapter;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.dateview.CalendarPopupWindow;
import com.deter.TaxManager.view.dateview.CustomDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/9/27 0027.
 */
public class CollisionFragment extends Fragment implements CalendarPopupWindow.ClickDateListener, AnalysisLogic.AnalysisNotifyListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, MainActivity.OnkeyBackDownListener, AnalysisLogic.AnalysisNotDataListener {

    private EditText inputAnalysisEt;
    private TextView startAnalysisTimeTv;
    private TextView endAnalysisTimeTv;
    private Button cancelAnalysisBtn;
    private Button confirmAnalysisBtn;

    private Button startAnalysisBtn;
    private ListView analysisListview;
    private View emptyView;
    View contentView;
    private DataManager dataManager;

    private CustomDate curDate;
    private String startTimePrompt;
    private String endTimePrompt;
    private long startTimeStamp;
    private long endTimeStamp;

    private RelativeLayout bottomChoiceView;
    private CheckBox bottomCheckChoice;
    private Button deleteSelectBtn;

    private BasePopupWindow startAnalysisPopupWindow;

    private CalendarPopupWindow calendarPopupWindow;

    AnalysisLogic analysisLogic;
    private AnalysisListAdapter commonAdapter;
    List<StrikeAnalysis> strikeAnalysisList;

    private Dialog promptDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != analysisLogic) {
            analysisLogic.setNotDataListener(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_collision_analysis_layout, container, false);
        emptyView = contentView.findViewById(R.id.analysis_emptyView);
        initViews();
        return contentView;
    }


    private void initViews() {
        startAnalysisBtn = (Button) contentView.findViewById(R.id.start_analysis_btn);
        analysisListview = (ListView) contentView.findViewById(R.id.analysis_listview);
        analysisListview.setOnItemClickListener(this);
        analysisListview.setOnItemLongClickListener(this);
        analysisListview.setEmptyView(emptyView);

        bottomChoiceView = (RelativeLayout) contentView.findViewById(R.id.bottom_choice_view);
        bottomCheckChoice = (CheckBox) contentView.findViewById(R.id.bottom_check_choice);
        deleteSelectBtn = (Button) contentView.findViewById(R.id.folders_select_btn);
        bottomCheckChoice.setOnClickListener(mClickListene);
        deleteSelectBtn.setOnClickListener(mClickListene);

        ImageView emptyIv = (ImageView) emptyView.findViewById(R.id.empty_iv);
        TextView emptyTv = (TextView) emptyView.findViewById(R.id.empty_tv);
        emptyIv.setImageResource(R.drawable.empty_collision_analys);
        emptyTv.setText(R.string.empty_collision_analys_str);

        startAnalysisBtn.setOnClickListener(mClickListene);
        initData();
    }


    private void initData() {
        curDate = new CustomDate();

        if (null == analysisLogic) {
            analysisLogic = AnalysisLogic.getInstance();
        }

        analysisLogic.addNotifyListener(this);
        analysisLogic.setNotDataListener(this);
        strikeAnalysisList = analysisLogic.getStrikeAnalysisList();

        commonAdapter = new AnalysisListAdapter(R.layout.analysis_task_item, getActivity(), strikeAnalysisList);
        oprTypeStyle(CommonAdapter.NORMAL_MODE);
        commonAdapter.setDeleteModeView(bottomCheckChoice, deleteSelectBtn);
        analysisListview.setAdapter(commonAdapter);

        analysisLogic.getStrikeAnalysis(getActivity());
        //startTimePrompt = getString(R.string.start_analysis_time_prompt);
        //endTimePrompt = getString(R.string.end_analysis_time_prompt);
        startTimePrompt = curDate.toString() + " 00:00:00";
        endTimePrompt = curDate.toString() + " 23:59:59";
        startTimeStamp = Long.valueOf(DateUtil.getTimestamp(startTimePrompt, DateUtil.FORMAT_YMDHMS));
        endTimeStamp = Long.valueOf(DateUtil.getTimestamp(endTimePrompt, DateUtil.FORMAT_YMDHMS));
    }


    @Override
    public void notifyData(int code) {
        if (AnalysisLogic.TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST == code) {
            commonAdapter.notifyDataSetChanged();
        } else if (AnalysisLogic.TYPE_NOTIFY_COLLISION_ANALYSIS_FINISH == code) {
            boolean isPlayEnable = APPUtils.isCollisionWarningToneEnable(getActivity());
            if (isPlayEnable) {
                APPUtils.play(getActivity().getApplicationContext(), APPConstans.ACCOMPANT_TONE_TYPE);
            }
            analysisLogic.getStrikeAnalysis(getActivity());
        }
        onBackClick();
    }


    @Override
    public void notData() {
        showPromptDialog(getResources().getString(R.string.analysis_not_data_prompt), AnalysisLogic.TYPE_NOTDATA_PROMPT);
    }

    private View.OnClickListener mClickListene = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_analysis_btn:
                    showAnalysisPopupWindow();
                    break;

                case R.id.confirm_analysis_btn:
                    startAnalysisOpration();
                    break;

                case R.id.cancel_analysis_btn:
                    hideAnalysisPopupWindow();
                    break;

                case R.id.start_analysis_time_tv:
                    showCalendarPopupWindow(startAnalysisTimeTv, APPConstans.TYPE_SELECT_START_TIME);
                    break;
                case R.id.end_analysis_time_tv:
                    showCalendarPopupWindow(endAnalysisTimeTv, APPConstans.TYPE_SELECT_END_TIME);
                    break;

                case R.id.btn_reminder_cancel:
                    dimissPromptDialog();
                    break;

                case R.id.btn_reminder_comfirm:
                    deleteAnalysisDataFromDB();
                    dimissPromptDialog();
                    break;

                case R.id.folders_select_btn:
                    if (commonAdapter.getSelectListItems().size() > 1) {
                        showPromptDialog(getResources().getString(R.string.delete_analysis_prompt), AnalysisLogic.TYPE_DELETE_PROMPT);
                    } else {
                        deleteAnalysisDataFromDB();
                    }
                    break;

                case R.id.bottom_check_choice:
                    commonAdapter.checkAllChoice();
                    break;

            }
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (strikeAnalysisList.isEmpty()) {
            return;
        }
        StrikeAnalysis strikeAnalysis = strikeAnalysisList.get(position);
        if (null == strikeAnalysis) {
            return;
        }

        if (CommonAdapter.DELETE_MODE == commonAdapter.getOprType()) {
            commonAdapter.radioChoice(strikeAnalysis);
        } else {
            if (!strikeAnalysisList.isEmpty()) {
                AnalysisLogic.getInstance().setCurStrikeAnalysis(strikeAnalysis);
                if (strikeAnalysis.getStatus() != 200) {
                    Intent intent = new Intent(getActivity(), AnalysisTaskActivity.class);
                    intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_ANALYSIS_TASK_LIST);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CollisionAnalysisResultActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (commonAdapter.getOprType() == CommonAdapter.NORMAL_MODE) {
            oprTypeStyle(CommonAdapter.DELETE_MODE);
        }
        return true;
    }

    private void showAnalysisPopupWindow() {
        if (hideAnalysisPopupWindow()) {
            return;
        }

        if (null == startAnalysisPopupWindow) {
            startAnalysisPopupWindow = new BasePopupWindow(getActivity(), R.layout.dialog_start_analysis_layout, true) {
                @Override
                public void convert(View contentView) {
                    inputAnalysisEt = getViewById(R.id.input_analysis_et);
                    startAnalysisTimeTv = getViewById(R.id.start_analysis_time_tv);
                    startAnalysisTimeTv.setText(startTimePrompt);
                    startAnalysisTimeTv.setTag(startTimeStamp);
                    endAnalysisTimeTv = getViewById(R.id.end_analysis_time_tv);
                    endAnalysisTimeTv.setText(endTimePrompt);
                    endAnalysisTimeTv.setTag(endTimeStamp);
                    cancelAnalysisBtn = getViewById(R.id.cancel_analysis_btn);
                    confirmAnalysisBtn = getViewById(R.id.confirm_analysis_btn);

                    startAnalysisTimeTv.setOnClickListener(mClickListene);
                    endAnalysisTimeTv.setOnClickListener(mClickListene);
                    cancelAnalysisBtn.setOnClickListener(mClickListene);
                    confirmAnalysisBtn.setOnClickListener(mClickListene);
                }
            };

            startAnalysisPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            startAnalysisPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//
//                }
//            });
        }

        startAnalysisPopupWindow.showAsDropDown(startAnalysisBtn);
    }


    private void startAnalysisOpration() {
        String inputName = inputAnalysisEt.getText().toString();
        if (TextUtils.isEmpty(inputName.trim())) {
            showToast(R.string.input_analysis_name_prompt);
            return;
        }

//        String selectStartTimeTv = startAnalysisTimeTv.getText().toString();
//        if (selectStartTimeTv.equals(startTimePrompt)) {
//            showToast(R.string.start_analysis_time_prompt);
//            return;
//        }

//        String selectEndTimeTv = endAnalysisTimeTv.getText().toString();
//        if (selectEndTimeTv.equals(endTimePrompt)) {
//            showToast(R.string.end_analysis_time_prompt);
//            return;
//        }

        if (null == dataManager) {
            dataManager = DataManager.getInstance(getActivity());
        }
        int count = dataManager.getStrikeAnalysisCountByName(inputName);
        if (count > 0) {
            ToastUtils.showShort(getActivity(), getString(R.string.analysis_name_repetition_prompt));
            return;
        }

        long startTime = (long) startAnalysisTimeTv.getTag();
        long endTime = (long) endAnalysisTimeTv.getTag();

        inputAnalysisEt.getText().clear();
        startAnalysisTimeTv.setText(startTimePrompt);
        startAnalysisTimeTv.setTag(startTimeStamp);
        endAnalysisTimeTv.setText(endTimePrompt);
        endAnalysisTimeTv.setTag(endTimeStamp);

        StrikeAnalysis strikeAnalysis = new StrikeAnalysis();
        strikeAnalysis.setName(inputName);
        strikeAnalysis.setStartTime(startTime);
        strikeAnalysis.setStopTime(endTime);

        AnalysisLogic.getInstance().setCurStrikeAnalysis(strikeAnalysis);
        goToStarMapListActivity();

        hideAnalysisPopupWindow();
    }


    private void goToStarMapListActivity() {
        Intent intent = new Intent(getActivity(), StarMapTaskListActivity.class);
        intent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_START_MAP_TASK_LIST_BY_TIME);
        startActivity(intent);
    }


    private void showCalendarPopupWindow(View parentView, int selectType) {
        hideSoftInputView();
        if (hideCalendarPopupWindow()) {
            return;
        }

        if (null == calendarPopupWindow) {
            calendarPopupWindow = new CalendarPopupWindow(getActivity());
            calendarPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //calendarPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            calendarPopupWindow.setClickDateListener(this);
        }
        calendarPopupWindow.setSelect_time_type(selectType);
        int[] arrayOfInt2 = new int[2];
        parentView.getLocationOnScreen(arrayOfInt2);
        calendarPopupWindow.showAtLocation(contentView, 0, arrayOfInt2[0], arrayOfInt2[1] + DensityUtil.dip2px(getActivity().getApplicationContext(), 30.0F));
    }


    @Override
    public void clickDate(String timeStr, long startTimeStamp, long endTimeStamp, int selectType) {
        if (TextUtils.isEmpty(timeStr)) {
            return;
        }

        if (null != startAnalysisTimeTv && selectType == APPConstans.TYPE_SELECT_START_TIME) {
            startAnalysisTimeTv.setText(timeStr);
            startAnalysisTimeTv.setTag(startTimeStamp);
        }

        if (null != endAnalysisTimeTv && selectType == APPConstans.TYPE_SELECT_END_TIME) {
            endAnalysisTimeTv.setText(timeStr);
            endAnalysisTimeTv.setTag(endTimeStamp);
        }

    }

    private void showToast(int stringID) {
        ToastUtils.showShort(getActivity().getApplicationContext(), getResources().getString(stringID));
    }


    private boolean hideAnalysisPopupWindow() {
        if (null != startAnalysisPopupWindow && startAnalysisPopupWindow.isShowing()) {
            startAnalysisPopupWindow.dismiss();
            return true;
        }
        return false;
    }


    private boolean hideCalendarPopupWindow() {
        if (null != calendarPopupWindow && calendarPopupWindow.isShowing()) {
            calendarPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    private void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(inputAnalysisEt.getWindowToken(), 0);
        }
    }


    private void showPromptDialog(String prompt, int type) {
        dimissPromptDialog();
        if (AnalysisLogic.TYPE_NOTDATA_PROMPT == type) {
            promptDialog = DialogUtils.showConfirmDialog(getActivity(), prompt, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dimissPromptDialog();
                    analysisLogic.getStrikeAnalysis(getActivity());
                }
            });
        } else if (AnalysisLogic.TYPE_DELETE_PROMPT == type) {
            promptDialog = DialogUtils.showConfirmAndCancelDialog(getActivity(), prompt, mClickListene, mClickListene);
        }
    }


    private void dimissPromptDialog() {
        if (null != promptDialog && promptDialog.isShowing()) {
            promptDialog.dismiss();
            promptDialog = null;
        }
    }


    private void oprTypeStyle(int oprType) {
        if (CommonAdapter.NORMAL_MODE == oprType) {
            commonAdapter.setOprType(CommonAdapter.NORMAL_MODE);
            bottomChoiceView.setVisibility(View.GONE);
            bottomCheckChoice.setChecked(false);
            deleteSelectBtn.setEnabled(false);
            startAnalysisBtn.setEnabled(true);
        } else {
            commonAdapter.setOprType(CommonAdapter.DELETE_MODE);
            bottomChoiceView.setVisibility(View.VISIBLE);
            startAnalysisBtn.setEnabled(false);
        }
    }


    private void deleteAnalysisDataFromDB() {
        HashMap<String, Object> selectItems = commonAdapter.getSelectListItems();
        List<StrikeAnalysis> deleteList = new ArrayList<>();
        for (String key : selectItems.keySet()) {
            Object object = selectItems.get(key);
            if (null == object) {
                continue;
            }

            if (object instanceof StrikeAnalysis) {
                deleteList.add((StrikeAnalysis) object);
            }
        }

        analysisLogic.deleteStrikeAnalysisFromDb(getActivity(), deleteList);
    }


    @Override
    public boolean onBackClick() {
        if (CommonAdapter.DELETE_MODE == commonAdapter.getOprType()) {
            commonAdapter.getSelectListItems().clear();
            oprTypeStyle(CommonAdapter.NORMAL_MODE);
            return true;
        }

        return false;
    }
}
