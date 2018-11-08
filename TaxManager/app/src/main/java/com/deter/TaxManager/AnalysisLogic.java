package com.deter.TaxManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.deter.TaxManager.model.AnalysisMac;
import com.deter.TaxManager.model.AnalysisTask;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.StrikeAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/10/11 0011.
 */
public class AnalysisLogic {

    public final static int TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST = 0x0081;
    public final static int TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST = 0x0057;
    public final static int TYPE_NOTIFY_COLLISION_ANALYSIS_FINISH = 0x0077;

    public final static int TYPE_NOTDATA_PROMPT = 0x0051;
    public final static int TYPE_DELETE_PROMPT = 0x0017;

    private static AnalysisLogic analysisLogic;

    private DataManager dataManager;

    private StrikeAnalysis oprStrikeAnalysis;
    private List<StrikeAnalysis> strikeAnalysisList;
    private List<AnalysisTask> selectTaskList;
    private List<AnalysisMac> analysisMacResultList;

    private List<AnalysisNotifyListener> analysisNotifyListenerList;
    private AnalysisNotDataListener notDataListener;

    private int analysisCount = 0;

    private List<DataManager.MacData> macDataList;//Accompany result datas

    public static AnalysisLogic getInstance() {
        if (null == analysisLogic) {
            analysisLogic = new AnalysisLogic();
        }
        return analysisLogic;
    }

    public List<AnalysisTask> getSelectTaskList() {
        if (null == selectTaskList) {
            selectTaskList = new ArrayList<>();
        }
        return selectTaskList;
    }

    public void setSelectTaskList(List<AnalysisTask> selectTaskList) {
        if (null != selectTaskList) {
            getSelectTaskList().clear();
            this.selectTaskList.addAll(selectTaskList);
        }
    }

    public StrikeAnalysis getCurOprStrikeAnalysis() {
        return oprStrikeAnalysis;
    }

    public void setCurStrikeAnalysis(StrikeAnalysis oprStrikeAnalysis) {
        analysisCount = 0;
        this.oprStrikeAnalysis = oprStrikeAnalysis;
    }

    public List<StrikeAnalysis> getStrikeAnalysisList() {
        if (null == strikeAnalysisList) {
            strikeAnalysisList = new ArrayList<>();
        }
        return strikeAnalysisList;
    }


    public List<DataManager.MacData> getMacDataList() {
        return macDataList;
    }


    public void setMacDataList(List<DataManager.MacData> macDatas) {
        this.macDataList = macDatas;
    }


    public void addNotifyListener(AnalysisNotifyListener analysisNotifyListener) {
        if (null == analysisNotifyListenerList) {
            analysisNotifyListenerList = new ArrayList<>();
        }
        analysisNotifyListenerList.add(analysisNotifyListener);
    }


    public void removeNotifyListener(AnalysisNotifyListener analysisNotifyListener) {
        if (null != analysisNotifyListenerList && analysisNotifyListenerList.contains(analysisNotifyListener)) {
            analysisNotifyListenerList.remove(analysisNotifyListener);
        }
    }


    private void notifyData(int code) {
        for (AnalysisNotifyListener analysisNotifyListener : analysisNotifyListenerList) {
            analysisNotifyListener.notifyData(code);
        }
    }


    public void setNotDataListener(AnalysisNotDataListener notDataListener) {
        this.notDataListener = notDataListener;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataManager.MSG_TASK_ANALYSE_OK:
                    analysisCount++;
                    long taskId = msg.arg1;
                    for (AnalysisTask analysisTask : selectTaskList) {
                        if (analysisTask.getTaskId() == taskId) {
                            analysisTask.setStatus(200);
                            break;
                        }
                    }

                    handler.removeCallbacks(notifyAnalysisTaskRunnable);
                    handler.postDelayed(notifyAnalysisTaskRunnable, 200);
                    break;


                case DataManager.MSG_STRIKE_ANALYSE_OVER:
                    analysisOver();
                    break;

                case TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST:
                    notifyData(TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST);
                    break;

                case TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST:
                    notifyData(TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST);
                    break;

            }
        }
    };

    public Handler getHandler() {
        return handler;
    }


    public void startStrikeAnalyse(Context context) {

        if (null == dataManager) {
            dataManager = DataManager.getInstance(context.getApplicationContext());
        }

        if (null == oprStrikeAnalysis || null == selectTaskList) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                analysisMacResultList = dataManager.startStrikeAnalyseExt(oprStrikeAnalysis, selectTaskList, handler);
            }
        }).start();

    }


    public void getStrikeAnalysis(Context context) {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(context.getApplicationContext());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StrikeAnalysis> list = dataManager.getAllStrikeAnalysisFromDb();
                if (null != list && !list.isEmpty()) {
                    strikeAnalysisList.clear();
                    Collections.reverse(list);
                    strikeAnalysisList.addAll(list);
                    handler.sendEmptyMessageDelayed(TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST, 200);
                }
            }
        }).start();
    }


    Runnable notifyAnalysisTaskRunnable = new Runnable() {
        @Override
        public void run() {
            notifyData(TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST);
        }
    };


    private void analysisOver() {
        if (null != selectTaskList && !selectTaskList.isEmpty() && analysisCount == selectTaskList.size()) {
            if (null == analysisMacResultList || analysisMacResultList.isEmpty()) {
                deleteStrikeAnalysisFromDb();
                notDataListener.notData();
                return;
            }
            oprStrikeAnalysis.setStatus(200);
            dataManager.updateStrikeAnalysisToDb(oprStrikeAnalysis);
            notifyData(TYPE_NOTIFY_COLLISION_ANALYSIS_FINISH);
        }
    }


    public void getAnalysisTaskListFromDb(Context context) {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(context.getApplicationContext());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == oprStrikeAnalysis) {
                    return;
                }
                List<AnalysisTask> analysisTasks = dataManager.getAnalysisTaskListFromDb(oprStrikeAnalysis.getId());
                if (null != analysisTasks && !analysisTasks.isEmpty()) {
                    selectTaskList.clear();
                    Collections.reverse(analysisTasks);
                    selectTaskList.addAll(analysisTasks);
                    handler.sendEmptyMessageDelayed(TYPE_NOTIFY_COLLISION_ANALYSIS_CHILD_LIST, 100);
                }
            }
        }).start();
    }


    public void deleteStrikeAnalysisFromDb(Context context, final List<StrikeAnalysis> deleteList) {
        if (null == dataManager) {
            dataManager = DataManager.getInstance(context.getApplicationContext());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (StrikeAnalysis strikeAnalysis : deleteList) {
                    if (null == strikeAnalysis) {
                        continue;
                    }
                    deleteStrikeAnalysisFromDb(strikeAnalysis);
                }

                handler.sendEmptyMessageDelayed(TYPE_NOTIFY_COLLISION_ANALYSIS_MAIN_LIST, 200);
            }
        }).start();
    }


    private void deleteStrikeAnalysisFromDb(StrikeAnalysis strikeAnalysis) {
        long id = strikeAnalysis.getId();
        dataManager.deleteStrikeAnalysisFromDb(strikeAnalysis);
        strikeAnalysisList.remove(strikeAnalysis);
        dataManager.deleteAnalysisTaskByAnalysisId(id);
        dataManager.deleteAnalysisMacByAnalysisId(id);
    }


    private void deleteStrikeAnalysisFromDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteStrikeAnalysisFromDb(getCurOprStrikeAnalysis());
            }
        }).start();
    }



    public interface AnalysisNotifyListener {
        void notifyData(int code);
    }


    public interface AnalysisNotDataListener {
        void notData();
    }


}
