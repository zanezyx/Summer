package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class AnalysisMacDao {

    public Dao<AnalysisMac, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<AnalysisMac, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<AnalysisMac,Integer> dao;

    public AnalysisMacDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(AnalysisMac.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(AnalysisMac analysisMac){
        try {
            return dao.create(analysisMac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public AnalysisMac query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(AnalysisMac analysisMac){
        try {
            return dao.update(analysisMac);
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

    public int delete(AnalysisMac analysisMac){
        try {
            return dao.delete(analysisMac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
