package com.deter.TaxManager.model;

import android.content.Context;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.utils.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by yuxinz on 2017/7/13.
 */

public class DtDatabaseHelper  extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "db_dtwireless";
    //app v1.9 : db v30
    //app v2.0 : db v33

    private static final int DB_VERSION = 33;
    public SQLiteDatabase mSqLiteDatabase;
    private static DtDatabaseHelper instance;
    Context mContext;

    private DtDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }
    public static DtDatabaseHelper getInstance(Context context){
        if(instance == null){
            synchronized (DtDatabaseHelper.class){
                if(instance == null){
                    instance = new DtDatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.i(DtConstant.TAG, ">>>DB onCreate>>>start");
        mSqLiteDatabase = sqLiteDatabase;
        try {
            TableUtils.createTable(connectionSource, Device.class);
            TableUtils.createTable(connectionSource, Identity.class);
            TableUtils.createTable(connectionSource, Location.class);
            TableUtils.createTable(connectionSource, SSID.class);
            TableUtils.createTable(connectionSource, Station.class);
            TableUtils.createTable(connectionSource, URL.class);
            TableUtils.createTable(connectionSource, SpecialDevice.class);
            TableUtils.createTable(connectionSource, DndMac.class);
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, TopAP.class);
            TableUtils.createTable(connectionSource, StrikeAnalysis.class);
            TableUtils.createTable(connectionSource, AnalysisTask.class);
            TableUtils.createTable(connectionSource, AnalysisMac.class);
            TableUtils.createTable(connectionSource, FollowAnalysis.class);
            TableUtils.createTable(connectionSource, FollowAnalysisMac.class);
        } catch (SQLException e) {
            Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception:"+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.i(DtConstant.TAG, ">>>DB onUpgrade>>>");
        Log.i(DtConstant.TAG, ">>>DB onUpgrade>>>start i:"+i+" i1:"+i1);
        if(i<30){
            dropTables(sqLiteDatabase, connectionSource);
            Log.i(DtConstant.TAG, ">>>DB onUpgrade>>> <30 OK");
            return;
        }
        if(i<33){
            try {
                TableUtils.createTable(connectionSource, FollowAnalysis.class);
                TableUtils.createTable(connectionSource, FollowAnalysisMac.class);
                sqLiteDatabase.execSQL("ALTER TABLE 't_strike_nalysis' RENAME TO 't_strike_analysis'");
                Log.i(DtConstant.TAG, ">>>DB onUpgrade>>> <33 OK");
            } catch (SQLException e) {
                Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception:"+e.toString());
                e.printStackTrace();
            } catch (Exception e) {
                Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception 111:"+e.toString());
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DtConstant.TAG, ">>>DB onDowngrade>>>start");
//        super.onDowngrade(db, oldVersion, newVersion);
        Log.i(DtConstant.TAG, ">>>DB onDowngrade>>>oldVersion:"+oldVersion+" newVersion"+newVersion);


    }


    void dropTables(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource)
    {
        Log.i(DtConstant.TAG, ">>>DB checkDatabase>>>start");
        try {
            TableUtils.dropTable(connectionSource,Device.class,true);
            TableUtils.dropTable(connectionSource,Identity.class,true);
            TableUtils.dropTable(connectionSource,Location.class,true);
            TableUtils.dropTable(connectionSource,SSID.class,true);
            TableUtils.dropTable(connectionSource,Station.class,true);
            TableUtils.dropTable(connectionSource,URL.class,true);
            TableUtils.dropTable(connectionSource,DndMac.class,true);
            TableUtils.dropTable(connectionSource,SpecialDevice.class,true);
            TableUtils.dropTable(connectionSource,Task.class,true);
            TableUtils.dropTable(connectionSource,TopAP.class,true);
            TableUtils.dropTable(connectionSource,StrikeAnalysis.class,true);
            TableUtils.dropTable(connectionSource,AnalysisTask.class,true);
            TableUtils.dropTable(connectionSource,AnalysisMac.class,true);
            TableUtils.dropTable(connectionSource,FollowAnalysis.class,true);
            TableUtils.dropTable(connectionSource,FollowAnalysisMac.class,true);

            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            Log.i(DtConstant.TAG, ">>>DB checkDatabase>>>exception:"+e.toString());
            e.printStackTrace();
        }
    }

    void dropTables()
    {
        dropTables(this.getWritableDatabase(), this.getConnectionSource());
    }

    public Dao getDao(Class clazz) throws SQLException {
        return super.getDao(clazz);
    }

    public Cursor rawQuery(String sql)
    {
        if(mSqLiteDatabase==null)
        {
            mSqLiteDatabase = this.getWritableDatabase();
        }
        Cursor c = null;
        try {
            c = mSqLiteDatabase.rawQuery(sql, new String[]{});
        }catch (Exception e){
            Log.i(DtConstant.TAG, ">>>DB rawQuery>>>exception:"+e.toString());
        }

//        Cursor c = mSqLiteDatabase.rawQuery("SELECT mac, role, task_id FROM t_device WHERE mac IN (SELECT mac FROM t_special_device )", new String[]{});
//        while (c.moveToNext()) {
//            //long _id = c.getLong(c.getColumnIndex("id"));
//            String address = c.getString(c.getColumnIndex("mac"));
//            String role = c.getString(c.getColumnIndex("role"));
//            String taskId = c.getString(c.getColumnIndex("task_id"));
//            //long startTime = c.getLong(c.getColumnIndex("start_time"));
//            //Log.i("DtDatabaseHelper", "id=>" + _id + ", address=>" + address + ", startTime=>" + startTime);
//            Log.i("test","DtDatabaseHelper address=>" + address+" role=>"+role +" taskId=>"+taskId);
//        }
        return c;
    }

    public void execSQL(String sql)
    {
        if(mSqLiteDatabase==null)
        {
            mSqLiteDatabase = this.getWritableDatabase();
        }
        mSqLiteDatabase.execSQL(sql);
        //Cursor c = mSqLiteDatabase.rawQuery("SELECT mac FROM t_device WHERE task_id IN (SELECT id FROM t_task )", new String[]{});
//        Cursor c = mSqLiteDatabase.rawQuery("SELECT mac, role, task_id FROM t_device WHERE mac IN (SELECT mac FROM t_special_device )", new String[]{});
//        while (c.moveToNext()) {
//            //long _id = c.getLong(c.getColumnIndex("id"));
//            String address = c.getString(c.getColumnIndex("mac"));
//            String role = c.getString(c.getColumnIndex("role"));
//            String taskId = c.getString(c.getColumnIndex("task_id"));
//            //long startTime = c.getLong(c.getColumnIndex("start_time"));
//            //Log.i("DtDatabaseHelper", "id=>" + _id + ", address=>" + address + ", startTime=>" + startTime);
//            Log.i("test","DtDatabaseHelper address=>" + address+" role=>"+role +" taskId=>"+taskId);
//        }
    }


}
