package com.deter.TaxManager.model;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class MacVendorDao {

    public Dao<MacVendor, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<MacVendor, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<MacVendor,Integer> dao;

    public MacVendorDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(MacVendor.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(MacVendor macVendor){
        try {
            return dao.create(macVendor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public MacVendor query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(MacVendor macVendor){
        try {
            return dao.update(macVendor);
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
