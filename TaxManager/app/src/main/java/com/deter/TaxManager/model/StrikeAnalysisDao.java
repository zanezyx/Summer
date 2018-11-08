package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class StrikeAnalysisDao {

    public Dao<StrikeAnalysis, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<StrikeAnalysis, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<StrikeAnalysis,Integer> dao;

    public StrikeAnalysisDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(StrikeAnalysis.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(StrikeAnalysis strikeAnalysis){
        try {
            return dao.create(strikeAnalysis);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public StrikeAnalysis query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(StrikeAnalysis strikeAnalysis){
        try {
            return dao.update(strikeAnalysis);
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

    public int delete(StrikeAnalysis strikeAnalysis){
        try {
            return dao.delete(strikeAnalysis);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
