package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class LocationDao {

    public Dao<Location, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<Location, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<Location,Integer> dao;

    public LocationDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(Location.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(Location location){
        try {
            return dao.create(location);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Location query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(Location location){
        try {
            return dao.update(location);
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
