package com.deter.TaxManager.model;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class TaskDao {

    public Dao<Task, Integer> getDao() {
        return dao;
    }

    public void setDao(Dao<Task, Integer> dao) {
        this.dao = dao;
    }

    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<Task,Integer> dao;

    public TaskDao(Context context) {
        try {
            dao = DtDatabaseHelper.getInstance(context).getDao(Task.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(Task task){
        try {
            return dao.create(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Task query(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updata(Task task){
        try {
            return dao.update(task);
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
