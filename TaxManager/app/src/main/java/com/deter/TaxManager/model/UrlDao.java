package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class UrlDao {

    public Dao<URL, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<URL, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<URL,Integer> dao;

    public UrlDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(URL.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(URL url){
        try {
            return dao.create(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public URL query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(URL url){
        try {
            return dao.update(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delete(int id){
        try {
            return dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
