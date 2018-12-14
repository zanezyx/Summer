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
//        try {
//            //TableUtils.createTable(connectionSource, Device.class);
//
//        } catch (SQLException e) {
//            Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception:"+e.toString());
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.i(DtConstant.TAG, ">>>DB onUpgrade>>>");
        Log.i(DtConstant.TAG, ">>>DB onUpgrade>>>start i:"+i+" i1:"+i1);
//        if(i<30){
//            dropTables(sqLiteDatabase, connectionSource);
//            Log.i(DtConstant.TAG, ">>>DB onUpgrade>>> <30 OK");
//            return;
//        }
//        if(i<33){
//            try {
//                TableUtils.createTable(connectionSource, FollowAnalysis.class);
//                TableUtils.createTable(connectionSource, FollowAnalysisMac.class);
//                sqLiteDatabase.execSQL("ALTER TABLE 't_strike_nalysis' RENAME TO 't_strike_analysis'");
//                Log.i(DtConstant.TAG, ">>>DB onUpgrade>>> <33 OK");
//            } catch (SQLException e) {
//                Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception:"+e.toString());
//                e.printStackTrace();
//            } catch (Exception e) {
//                Log.i(DtConstant.TAG, ">>>DB onCreate>>>exception 111:"+e.toString());
//                e.printStackTrace();
//            }
//        }

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
//        try {
//            TableUtils.dropTable(connectionSource,Device.class,true);
//            onCreate(sqLiteDatabase,connectionSource);
//        } catch (SQLException e) {
//            Log.i(DtConstant.TAG, ">>>DB checkDatabase>>>exception:"+e.toString());
//            e.printStackTrace();
//        }
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
        return c;
    }

    public void execSQL(String sql)
    {
        if(mSqLiteDatabase==null)
        {
            mSqLiteDatabase = this.getWritableDatabase();
        }
        mSqLiteDatabase.execSQL(sql);
    }


}
