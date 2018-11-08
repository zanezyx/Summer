package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class AnalysisTaskDao {

    public Dao<AnalysisTask, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<AnalysisTask, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<AnalysisTask,Integer> dao;

    public AnalysisTaskDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(AnalysisTask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(AnalysisTask analysisTask){
        try {
            return dao.create(analysisTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public AnalysisTask query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(AnalysisTask analysisTask){
        try {
            return dao.update(analysisTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delete(long id){
        try {
            return dao.deleteById((int)id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delete(AnalysisTask analysisTask){
        try {
            return dao.delete(analysisTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
