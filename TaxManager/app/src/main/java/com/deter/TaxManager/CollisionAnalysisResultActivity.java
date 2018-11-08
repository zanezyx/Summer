package com.deter.TaxManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.model.AnalysisMac;
import com.deter.TaxManager.model.AnalysisTask;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.StrikeAnalysis;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.view.CHScrollView;
import com.deter.TaxManager.view.adapter.CommonAdapter;
import com.deter.TaxManager.view.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/10/17 0017.
 */
public class CollisionAnalysisResultActivity extends BaseActivity {

    private final int code_init_type = 0x0051;

    private LinearLayout hearContentLinearLayout;

    private DataManager dataManager;
    private AnalysisLogic analysisLogic;
    private StrikeAnalysis oprStrikeAnalysis;
    private List<AnalysisTask> analysisTaskList;
    private List<AnalysisMac> analysisMacList;
    private List<List<String>> alldatas;
    private CommonAdapter collisionAnalysisResultAdapter;

    private View division_line_view;

    LinearLayout.LayoutParams divisionViewLayoutParams;
    LinearLayout.LayoutParams textViewLayoutParams;

    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case code_init_type:
                    TextView textView=getView(R.id.analysis_mac_title);
                    textView.setText("MAC");
                    String statistics_title = getResources().getString(R.string.analysis_result_statistics_mac_count);
                    generateTextView(null, hearContentLinearLayout, statistics_title, null);
                    for (AnalysisTask analysisTask : analysisTaskList) {
                        generateTextView(null, hearContentLinearLayout, analysisTask.getTaskName(), analysisTask);
                    }
                    mListView.setAdapter(collisionAnalysisResultAdapter);
                    division_line_view.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    @Override
    public void initView() {
        setContentView(R.layout.activity_collision_result_layout);
        division_line_view = getView(R.id.division_line_view);
        division_line_view.setVisibility(View.GONE);
        setTitleText(R.string.analysis_result);
        textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        divisionViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        divisionViewLayoutParams.width = 3;
        mListView = getView(R.id.scroll_list);
        CHScrollView headerScroll = getView(R.id.item_scroll_title);
        hearContentLinearLayout = getView(R.id.item_scroll_title_content_linearlayout);
        mHScrollViews.add(headerScroll);
    }


    @Override
    public void initData() {
        analysisTaskList = new ArrayList<>();
        analysisMacList = new ArrayList<>();
        alldatas = new ArrayList<>();

        if (null == analysisLogic) {
            analysisLogic = AnalysisLogic.getInstance();
        }

        if (null == dataManager) {
            dataManager = DataManager.getInstance(this);
        }

        oprStrikeAnalysis = analysisLogic.getCurOprStrikeAnalysis();

        int resID = R.layout.activity_collision_result_item_layout;
        collisionAnalysisResultAdapter = new CollisionAnalysisResultAdapter(resID, CollisionAnalysisResultActivity.this, alldatas);

        mhandler.postDelayed(loadDataRunnable, 10);
    }


    Runnable loadDataRunnable = new Runnable() {

        @Override
        public void run() {
            final long analysisID = oprStrikeAnalysis.getId();
            if (-1 != analysisID) {
                List<AnalysisMac> analysisMacs = dataManager.getAnalysisMacListFromDb(analysisID);
                Collections.reverse(analysisMacs);
                analysisMacList.addAll(analysisMacs);
                List<AnalysisTask> analysisTasks = dataManager.getAnalysisTaskListFromDb(analysisID);
                Collections.reverse(analysisTasks);
                analysisTaskList.addAll(analysisTasks);

                for (AnalysisMac analysisMac : analysisMacList) {
                    List<String> contentStr = new ArrayList<>();

                    contentStr.add(analysisMac.getMac());
                    contentStr.add(String.valueOf(analysisMac.getTimes()));

                    String task_ids = analysisMac.getTaskIds();

                    for (AnalysisTask analysisTask : analysisTaskList) {
                        String taskID = String.valueOf(analysisTask.getTaskId());
                        if (task_ids.contains(taskID)) {
                            contentStr.add(taskID);
                        } else {
                            contentStr.add("");
                        }
                    }
                    alldatas.add(contentStr);
                }

                mhandler.sendEmptyMessageDelayed(code_init_type, 50);

            }
        }
    };




    private void generateTextView(List<View> list, LinearLayout linearLayout, String content,AnalysisTask analysisTask) {
        TextView textView = new TextView(this);
        textView.setTextSize(16f);
        if (null != analysisTask) {
            String startTime = "(" + DateUtil.formatDate(analysisTask.getTaskStartTime());
            String stopTime = "";

            if (analysisTask.getTaskStopTime() > 0) {
                stopTime = DateUtil.formatDate(analysisTask.getTaskStopTime()) + ")";
            } else {
                startTime += ")";
            }

            String htmlStr = content + "<br /><small>" + startTime + "</small>";
            if (!TextUtils.isEmpty(stopTime)) {
                htmlStr += "<br /><small>" + stopTime + "</small>";
            }

            textView.setPadding(5,0,5,0);
            textView.setText(Html.fromHtml(htmlStr));
        } else {
            textView.setText(content);
        }

        textViewLayoutParams.width = 240;
        textView.setLayoutParams(textViewLayoutParams);
        textView.setGravity(Gravity.CENTER);

        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.color.color_EBEBEB);
        view.setLayoutParams(divisionViewLayoutParams);

        linearLayout.addView(view);
        linearLayout.addView(textView);
        if (null != list) {
            list.add(textView);
        }
    }

    LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private void generateImageView(List<View> list, LinearLayout linearLayout) {
        ImageView imageView = new ImageView(this);
        imgLp.width = 240;
        imgLp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imgLp);
        imageView.setImageResource(R.drawable.exist_collision_task);

        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.color.color_EBEBEB);
        view.setLayoutParams(divisionViewLayoutParams);

        linearLayout.addView(view);
        linearLayout.addView(imageView);
        if (null != list) {
            list.add(imageView);
        }
    }


    public void addHViews(final CHScrollView hScrollView) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            if (scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (CHScrollView scrollView : mHScrollViews) {
            if (mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }


    class CollisionAnalysisResultAdapter extends CommonAdapter {

        public CollisionAnalysisResultAdapter(int mItemLayoutId, Context mContext, List mDatas) {
            super(mItemLayoutId, mContext, mDatas);
        }

        @Override
        public void convert(ViewHolder viewHolder, Object item) {
            List<String> contentStr = (List<String>) item;
            String mac = APPUtils.formateTextToMacAddress(contentStr.get(0));
            viewHolder.setText(R.id.item_title, mac);

            CHScrollView chScrollView = viewHolder.getView(R.id.item_scroll);
            addHViews(chScrollView);

            List<View> viewList;
            LinearLayout contentLinearLayout = viewHolder.getView(R.id.item_scroll_title_content_linearlayout);

            if (null == contentLinearLayout.getTag()) {
                viewList = new ArrayList<>();
                for (int i = 1; i < contentStr.size(); i++) {
                    if (i == 1) {
                        generateTextView(viewList, contentLinearLayout, contentStr.get(i),null);
                    } else {
                        String hasArise = contentStr.get(i);
                        generateImageView(viewList, contentLinearLayout);
                        if (TextUtils.isEmpty(hasArise)) {
                            viewList.get(i - 1).setVisibility(View.INVISIBLE);
                        } else {
                            viewList.get(i - 1).setVisibility(View.VISIBLE);
                        }
                    }
                }
                contentLinearLayout.setTag(viewList);
            } else {
                viewList = (List<View>) contentLinearLayout.getTag();
                for (int i = 0; i < viewList.size(); i++) {
                    String content = contentStr.get(i + 1);
                    if (i == 0) {
                        TextView textView = (TextView) viewList.get(i);
                        textView.setText(content);
                    } else {
                        if (TextUtils.isEmpty(content)) {
                            viewList.get(i).setVisibility(View.INVISIBLE);
                        } else {
                            viewList.get(i).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

        }
    }


    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(CollisionAnalysisResultActivity.this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(loadDataRunnable);
    }
}
