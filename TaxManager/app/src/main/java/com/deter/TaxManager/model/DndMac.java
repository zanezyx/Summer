package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/8/10.
 */
@DatabaseTable(tableName = DndMac.TABLE_NAME)
public class DndMac {

    public static final String TABLE_NAME = "t_dnd_mac";

    //空的构造方法一定要有，否则数据库会创建失败
    public DndMac() {
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "mac")
    private String mac;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "vender")
    private String vender;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }




    @Override
    public String toString() {
        String str = ""+id+" "+mac+" "+" ";
        return str;
    }


}
