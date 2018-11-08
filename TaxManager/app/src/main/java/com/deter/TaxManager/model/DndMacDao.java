package com.deter.TaxManager.model;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class DndMacDao {

    public Dao<DndMac, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<DndMac, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<DndMac,Integer> dao;

    public DndMacDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(DndMac.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(DndMac dndMac){
        try {
            return dao.create(dndMac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public DndMac query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(DndMac dndMac){
        try {
            return dao.update(dndMac);
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
