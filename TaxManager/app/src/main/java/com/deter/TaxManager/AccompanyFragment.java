package com.deter.TaxManager;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.deter.TaxManager.model.FollowAnalysis;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.utils.DensityUtil;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.BasePopupWindow;
import com.deter.TaxManager.view.Progressview.RoundProgressBarWidthNumber;
import com.deter.TaxManager.view.adapter.AnalysisListAdapter;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.dateview.CalendarPopupWindow;
import com.deter.TaxManager.view.dateview.CustomDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by zhongqiusheng on 2017/9/27 0027.
 */
public class AccompanyFragment extends Fragment implements View.OnClickListener,CalendarPopupWindow.ClickDateListener,MainActivity.OnkeyBackDownListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private final static int TYPE_NOTIFY_ACCOMPANY_LIST = 0x0055;
    private final static int TYPE_NOTIFY_REPETITION_DATA_PROMPT = 0X0017;

    private DataManager dataManager;
    private Button startAnalysisBtn;

    private View analysis_input_info_view;
    private EditText inputAnalysisEt;
    private TextView startAnalysisTimeTv;
    private TextView endAnalysisTimeTv;
    private Button cancelAnalysisBtn;
    private Button confirmAnalysisBtn;

    private ListView analysisListview;
    private View emptyView;
    private RelativeLayout bottomChoiceView;
    private CheckBox bottomCheckChoice;
    private Button deleteSelectBtn;

    private CustomDate curDate;
    private String startTimePrompt;
    private String endTimePrompt;
    private long startTimeStamp;
    private long endTimeStamp;

    private CalendarPopupWindow calendarPopupWindow;
    private RoundProgressBarWidthNumber progressBarWidthNumber;
    private BasePopupWindow popupWindow;
    private Dialog promptDialog;

    View contentView;

    AnalysisLogic analysisLogic;
    private AnalysisListAdapter commonAdapter;
    List<FollowAnalysis> followAnalysisList;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case DataManager.MSG_FOLLOW_ANALYSE_NO_MAC:
                case DataManager.MSG_FOLLOW_ANALYSE_NO_TASK:
                    if (null != popupWindow && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    showPromptDialog(getResources().getString(R.string.analysis_not_data_prompt), AnalysisLogic.TYPE_NOTDATA_PROMPT);
                    break;

                case DataManager.MSG_FOLLOW_ANALYSE_PERCENT:
                    int percent = msg.arg1;
                    if (null == popupWindow) {
                        showProgressPopwindow(percent);
                    } else if (!popupWindow.isShowing()) {
                        showProgressPopwindow(percent);
                    } else {
                        progressBarWidthNumber.setProgress(percent);
                        if (percent == 100 && null != popupWindow && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                    break;

                case DataManager.MSG_FOLLOW_ANALYSE_SUCCESSS:
                    analysisCompletes(msg.arg1);
                    break;

                case TYPE_NOTIFY_ACCOMPANY_LIST:
                    if (CommonAdapter.NORMAL_MODE == commonAdapter.getOprType()) {
                        commonAdapter.notifyDataSetChanged();
                    }
                    onBackClick();
                    break;

                case TYPE_NOTIFY_REPETITION_DATA_PROMPT:
                    inputAnalysisEt.getText().clear();
                    String prompt = getResources().getString(R.string.accompany_repetition_prompt,"【"+(msg.arg1 + 1)+"】");
                    showPromptDialog(prompt, AnalysisLogic.TYPE_NOTDATA_PROMPT);
                    break;

            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_accompany_analysis_layout, container, false);
        initViews();
        return contentView;
    }


    private void initViews() {
        emptyView = contentView.findViewById(R.id.analysis_emptyView);
        ImageView emptyIv = (ImageView) emptyView.findViewById(R.id.empty_iv);
        TextView emptyTv = (TextView) emptyView.findViewById(R.id.empty_tv);
        emptyIv.setImageResource(R.drawable.empty_accompany_analys);
        emptyTv.setText(R.string.empty_accompany_analys_str);

        startAnalysisBtn = (Button) contentView.findViewById(R.id.start_analysis_btn);
        startAnalysisBtn.setOnClickListener(this);

        analysis_input_info_view = getViewById(R.id.analysis_input_info_view);
        analysis_input_info_view.setVisibility(View.GONE);
        analysis_input_info_view.setOnClickListener(this);
        inputAnalysisEt = getViewById(R.id.input_analysis_mac_et);
        inputAnalysisEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                String t = inputAnalysisEt.getText().toString();
//                String editable = inputAnalysisEt.getText().toString();
//                String str = StringFilter(editable.toString());
//                if (!editable.equals(str)) {
//                    inputAnalysisEt.setText(str);
//                    inputAnalysisEt.setSelection(str.length()); //光标置后
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //String str = APPUtils.formateMacAddressToText(s.toString());
                //inputAnalysisEt.setText(str);
            }
        });

        startAnalysisTimeTv = getViewById(R.id.start_analysis_time_tv);
        startAnalysisTimeTv.setText(startTimePrompt);
        startAnalysisTimeTv.setTag(startTimeStamp);
        endAnalysisTimeTv = getViewById(R.id.end_analysis_time_tv);
        endAnalysisTimeTv.setText(endTimePrompt);
        endAnalysisTimeTv.setTag(endTimeStamp);

        cancelAnalysisBtn = getViewById(R.id.cancel_analysis_btn);
        confirmAnalysisBtn = getViewById(R.id.confirm_analysis_btn);
        cancelAnalysisBtn.setOnClickListener(this);
        confirmAnalysisBtn.setOnClickListener(this);

        startAnalysisTimeTv.setOnClickListener(this);
        endAnalysisTimeTv.setOnClickListener(this);

        analysisListview = (ListView) contentView.findViewById(R.id.analysis_listview);
        analysisListview.setOnItemClickListener(this);
        analysisListview.setOnItemLongClickListener(this);
        analysisListview.setEmptyView(emptyView);
        emptyView.setOnClickListener(this);
        contentView.findViewById(R.id.content_view).setOnClickListener(this);

        bottomChoiceView = (RelativeLayout) contentView.findViewById(R.id.bottom_accompany_choice_view);
        bottomCheckChoice = (CheckBox) contentView.findViewById(R.id.bottom_check_choice);
        deleteSelectBtn = (Button) contentView.findViewById(R.id.folders_select_btn);
        bottomCheckChoice.setOnClickListener(this);
        deleteSelectBtn.setOnClickListener(this);

        initData();
    }


    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    private void initData() {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(getActivity());
        }
        curDate = new CustomDate();
        startTimePrompt = curDate.toString() + " 00:00:00";
        endTimePrompt = curDate.toString() + " 23:59:59";
        startTimeStamp = Long.valueOf(DateUtil.getTimestamp(startTimePrompt, DateUtil.FORMAT_YMDHMS));
        endTimeStamp = Long.valueOf(DateUtil.getTimestamp(endTimePrompt, DateUtil.FORMAT_YMDHMS));
        startAnalysisTimeTv.setText(startTimePrompt);
        startAnalysisTimeTv.setTag(startTimeStamp);
        endAnalysisTimeTv.setText(endTimePrompt);
        endAnalysisTimeTv.setTag(endTimeStamp);

        if (null == followAnalysisList) {
            followAnalysisList = new ArrayList<>();
        }

        commonAdapter = new AnalysisListAdapter(R.layout.analysis_task_item, getActivity(), followAnalysisList);
        oprTypeStyle(CommonAdapter.NORMAL_MODE);
        commonAdapter.setDeleteModeView(bottomCheckChoice, deleteSelectBtn);
        analysisListview.setAdapter(commonAdapter);

        mhandler.postDelayed(loadDataRunnable, 200);

    }


    Runnable loadDataRunnable = new Runnable() {

        @Override
        public void run() {
            List<FollowAnalysis> list = dataManager.getAllFollowAnalysisFromDb();
            if (null != list) {
                followAnalysisList.clear();
                if (!list.isEmpty()) {
                    Collections.reverse(list);
                    followAnalysisList.addAll(list);
                }
                mhandler.sendEmptyMessageDelayed(TYPE_NOTIFY_ACCOMPANY_LIST, 200);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.analysis_input_info_view:
                hideInfoView();
                break;

            case R.id.start_analysis_btn:
                if (View.VISIBLE == analysis_input_info_view.getVisibility()) {
                    analysis_input_info_view.setVisibility(View.GONE);
                } else {
                    analysis_input_info_view.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.start_analysis_time_tv:
                showCalendarPopupWindow(startAnalysisTimeTv, APPConstans.TYPE_SELECT_START_TIME);
                break;
            case R.id.end_analysis_time_tv:
                showCalendarPopupWindow(endAnalysisTimeTv, APPConstans.TYPE_SELECT_END_TIME);
                break;

            case R.id.cancel_analysis_btn:
                hideInfoView();
                break;

            case R.id.confirm_analysis_btn:
                startAnalysisOpration();
                hideInfoView();
                break;


            case R.id.folders_select_btn:
                if (commonAdapter.getSelectListItems().size() > 1) {
                    showPromptDialog(getResources().getString(R.string.delete_analysis_prompt),AnalysisLogic.TYPE_DELETE_PROMPT);
                } else {
                    deleteAnalysisDataFromDB();
                }

                break;

            case R.id.bottom_check_choice:
                commonAdapter.checkAllChoice();
                break;

            case R.id.content_view:
            case R.id.btn_reminder_cancel:
            case R.id.analysis_emptyView:
                dimissPromptDialog();
                break;

            case R.id.btn_reminder_comfirm:
                deleteAnalysisDataFromDB();
                dimissPromptDialog();
                break;

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (followAnalysisList.isEmpty()) {
            return;
        }
        FollowAnalysis followAnalysis = followAnalysisList.get(position);
        if (null == followAnalysis) {
            return;
        }

        if (CommonAdapter.DELETE_MODE == commonAdapter.getOprType()) {
            commonAdapter.radioChoice(followAnalysis);
        } else {
            goTOResultActivity((int) followAnalysis.getId());
        }

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (commonAdapter.getOprType() == CommonAdapter.NORMAL_MODE) {
            oprTypeStyle(CommonAdapter.DELETE_MODE);
        }
        return true;
    }



    private void hideInfoView() {
        if (null != analysis_input_info_view && View.VISIBLE == analysis_input_info_view.getVisibility()) {
            analysis_input_info_view.setVisibility(View.GONE);
        }
    }


    private void startAnalysisOpration() {

        String inputName = inputAnalysisEt.getText().toString().trim().toLowerCase();
        if (TextUtils.isEmpty(inputName.trim())) {
            showToast(R.string.accompany_input_analysis_mac_prompt);
            return;
        }

        final String newinputName = APPUtils.formateMacAddressToText(inputName);

        if (newinputName.length() < 12) {
            showToast(R.string.accompany_input_analysis_full_mac_prompt);
            return;
        }
        //final String inputName = "c4b8b5df8cc9";

        final long startTime = (long) startAnalysisTimeTv.getTag();
        final long endTime = (long) endAnalysisTimeTv.getTag();

        //final long eTime = TimeUtils.getCurrentTimeInLong();
        //final long sTime = TimeUtils.getCurrentTimeInLong() - 5 * 24 * 60 * 60 * 1000;

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isExist = isExistInAccompanyList(newinputName, startTime, endTime);
                if (isExist) {
                    return;
                }
                List<DataManager.MacData> macDataList = dataManager.startFollowAnalyse(startTime, endTime, newinputName, mhandler);//f48b3228a85b
                AnalysisLogic.getInstance().setMacDataList(macDataList);
            }
        }).start();


        inputAnalysisEt.getText().clear();
        startAnalysisTimeTv.setText(startTimePrompt);
        startAnalysisTimeTv.setTag(startTimeStamp);
        endAnalysisTimeTv.setText(endTimePrompt);
        endAnalysisTimeTv.setTag(endTimeStamp);
    }


    private boolean isExistInAccompanyList(String mac, long startTime, long stopTime) {
        List<FollowAnalysis> findAnalysisList = dataManager.queryFollowAnalysis(mac, startTime, stopTime);
        if (null != findAnalysisList && !findAnalysisList.isEmpty()) {
            FollowAnalysis followAnalysis = findAnalysisList.get(0);
            if (null != followAnalysis && !followAnalysisList.isEmpty()) {
                for (int i = 0; i < followAnalysisList.size(); i++) {
                    if (followAnalysis.getId() == followAnalysisList.get(i).getId()) {
                        Message message = new Message();
                        message.what = TYPE_NOTIFY_REPETITION_DATA_PROMPT;
                        message.arg1 = i;
                        mhandler.sendMessage(message);
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private void deleteAnalysisDataFromDB() {
        HashMap<String, Object> selectItems = commonAdapter.getSelectListItems();
        if (!selectItems.isEmpty()) {
            final HashMap<String, Object> deleteItems = new HashMap<>();
            deleteItems.putAll(selectItems);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String key : deleteItems.keySet()) {
                        Object object = deleteItems.get(key);
                        if (null == object) {
                            continue;
                        }

                        if (object instanceof FollowAnalysis) {
                            FollowAnalysis followAnalysis = (FollowAnalysis) object;
                            dataManager.deleteFollowAnalysisFromDb(followAnalysis);
                            dataManager.deleteFollowAnalysisMacFromDb(followAnalysis.getId());
                        }
                    }
                    mhandler.removeCallbacks(loadDataRunnable);
                    mhandler.postDelayed(loadDataRunnable, 200);
                }
            }).start();
        }

    }


    private void showCalendarPopupWindow(View parentView,int selectType) {

        hideSoftInputView();
        if (hideCalendarPopupWindow()) {
            return;
        }

        if (null == calendarPopupWindow) {
            calendarPopupWindow = new CalendarPopupWindow(getActivity());
            calendarPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
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


    private boolean hideCalendarPopupWindow() {
        if (null != calendarPopupWindow && calendarPopupWindow.isShowing()) {
            calendarPopupWindow.dismiss();
            return true;
        }
        return false;
    }


    private void showProgressPopwindow(int percent) {
        if (null == popupWindow) {
            popupWindow = new BasePopupWindow(getActivity(), R.layout.accompany_result_progress_layout, false) {
                @Override
                public void convert(View contentView) {
                    progressBarWidthNumber = getViewById(R.id.analysis_progressbar_view);
                }
            };

            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        progressBarWidthNumber.setProgress(percent);

        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    private void analysisCompletes(int accompany_id) {
        List<DataManager.MacData> macDataList = AnalysisLogic.getInstance().getMacDataList();
        if (null == macDataList || macDataList.isEmpty()) {
            showPromptDialog(getResources().getString(R.string.analysis_not_data_prompt), AnalysisLogic.TYPE_NOTDATA_PROMPT);
            return;
        }
        boolean isPlayEnable = APPUtils.isAccompanyWarningToneEnable(getActivity());
        if (isPlayEnable) {
            APPUtils.play(getActivity().getApplicationContext(), APPConstans.ACCOMPANT_TONE_TYPE);
        }
        goTOResultActivity(accompany_id);

        mhandler.removeCallbacks(loadDataRunnable);
        mhandler.postDelayed(loadDataRunnable, 2000);
    }


    private void goTOResultActivity(int accompany_id) {
        Intent intent = new Intent(getActivity(), AccompanyAnalysisResultActivity.class);
        intent.putExtra("accompany_id", accompany_id);
        startActivity(intent);
    }


    private void showPromptDialog(String prompt, int type) {
        dimissPromptDialog();

        if (AnalysisLogic.TYPE_NOTDATA_PROMPT == type) {
            promptDialog = DialogUtils.showConfirmDialog(getActivity(), prompt, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dimissPromptDialog();
                }
            });
        } else if (AnalysisLogic.TYPE_DELETE_PROMPT == type) {
            promptDialog = DialogUtils.showConfirmAndCancelDialog(getActivity(), prompt, this, this);
        }

    }



    private void dimissPromptDialog() {
        if (null != promptDialog && promptDialog.isShowing()) {
            promptDialog.dismiss();
            promptDialog = null;
        }
    }


    private void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(inputAnalysisEt.getWindowToken(), 0);
        }
    }

    private void showToast(int stringID) {
        ToastUtils.showShort(getActivity().getApplicationContext(), getResources().getString(stringID));
    }

    public <T extends View> T getViewById(int layoutID) {
        return (T) contentView.findViewById(layoutID);
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

    @Override
    public boolean onBackClick() {
        if (CommonAdapter.DELETE_MODE == commonAdapter.getOprType()){
            commonAdapter.getSelectListItems().clear();
            oprTypeStyle(CommonAdapter.NORMAL_MODE);
            return true;
        }
        return false;
    }

}
