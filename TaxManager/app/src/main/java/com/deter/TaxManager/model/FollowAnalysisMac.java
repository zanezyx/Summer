package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/7/14.
 */

@DatabaseTable(tableName = FollowAnalysisMac.TABLE_NAME)
public class FollowAnalysisMac {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "analysis_id")
    private long analysisId;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "total_times")
    private int totalTimes;

    @DatabaseField(columnName = "appear_times")
    private int appearTimes;

    @DatabaseField(columnName = "task_ids")
    private String taskIds;//总数，task id

    @DatabaseField(columnName = "appear_task_ids")
    private String appearTaskIds;//伴随次数，task id


    public static final String TABLE_NAME = "t_follow_analysis_mac";

    //空的构造方法一定要有，否则数据库会创建失败
    public FollowAnalysisMac() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(long analysisId) {
        this.analysisId = analysisId;
    }

    public String getAppearTaskIds() {
        return appearTaskIds;
    }

    public void setAppearTaskIds(String appearTaskIds) {
        this.appearTaskIds = appearTaskIds;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public int getAppearTimes() {
        return appearTimes;
    }

    public void setAppearTimes(int appearTimes) {
        this.appearTimes = appearTimes;
    }

    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+taskIds+" "+appearTaskIds;
        return str;
    }


}
