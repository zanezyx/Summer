package com.deter.TaxManager.model;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class StationDao {

    public Dao<Station, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<Station, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<Station,Integer> dao;

    public StationDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(Station.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(Station station){
        try {
            return dao.create(station);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Station query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(Station station){
        try {
            return dao.update(station);
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
