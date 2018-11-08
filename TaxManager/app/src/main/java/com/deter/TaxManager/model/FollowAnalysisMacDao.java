package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class FollowAnalysisMacDao {

    public Dao<FollowAnalysisMac, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<FollowAnalysisMac, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<FollowAnalysisMac,Integer> dao;

    public FollowAnalysisMacDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(FollowAnalysisMac.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(FollowAnalysisMac followAnalysisMac){
        try {
            return dao.create(followAnalysisMac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public FollowAnalysisMac query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(FollowAnalysisMac followAnalysisMac){
        try {
            return dao.update(followAnalysisMac);
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

    public int delete(FollowAnalysisMac followAnalysisMac){
        try {
            return dao.delete(followAnalysisMac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
