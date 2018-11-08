package com.deter.TaxManager.model;



import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class IdentityDao {

    public Dao<Identity, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<Identity, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<Identity,Integer> dao;

    public IdentityDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(Identity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(Identity identity){
        try {
            return dao.create(identity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Identity query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(Identity identity){
        try {
            return dao.update(identity);
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
