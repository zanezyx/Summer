package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class SsidDao {

    public Dao<SSID, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<SSID, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<SSID,Integer> dao;

    public SsidDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(SSID.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(SSID ssid){
        try {
            return dao.create(ssid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public SSID query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(SSID ssid){
        try {
            return dao.update(ssid);
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

    public int delete(SSID ssid){
        try {
            return dao.delete(ssid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
