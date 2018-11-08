package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;


import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class DeviceDao {

    public Dao<Device, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<Device, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<Device,Integer> dao;
    Context context;

    public DeviceDao(Context context) {
        this.context = context;
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(Device.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(Device device){
        try {
            return dao.create(device);
        } catch (SQLException e) {
            DtDatabaseHelper.getInstance(context).dropTables();
            e.printStackTrace();
        }
        return -1;
    }
    public Device query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(Device device){
        try {
            return dao.update(device);
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
