package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class FollowAnalysisDao {

    public Dao<FollowAnalysis, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<FollowAnalysis, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<FollowAnalysis,Integer> dao;

    public FollowAnalysisDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(FollowAnalysis.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(FollowAnalysis followAnalysis){
        try {
            return dao.create(followAnalysis);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public FollowAnalysis query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(FollowAnalysis followAnalysis){
        try {
            return dao.update(followAnalysis);
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

    public int delete(FollowAnalysis followAnalysis){
        try {
            return dao.delete(followAnalysis);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
