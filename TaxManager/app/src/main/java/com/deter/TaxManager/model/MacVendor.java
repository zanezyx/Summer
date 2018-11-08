package com.deter.TaxManager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yuxinz on 2017/8/10.
 */
@DatabaseTable(tableName = MacVendor.TABLE_NAME)
public class MacVendor {

    public static final String TABLE_NAME = "t_mac_vendor";

    //空的构造方法一定要有，否则数据库会创建失败
    public MacVendor() {
    }

    public String getVendercn() {
        return vendercn;
    }

    public void setVendercn(String vendercn) {
        this.vendercn = vendercn;
    }

    public String getVenderen() {
        return venderen;
    }

    public void setVenderen(String venderen) {
        this.venderen = venderen;
    }

    @DatabaseField(columnName = "mac", uniqueIndex = true, canBeNull = false)
    private String mac;

    @DatabaseField(columnName = "vendercn")
    private String vendercn;

    @DatabaseField(columnName = "venderen")
    private String venderen;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        String str = ""+mac+" "+vendercn+" "+venderen+" ";
        return str;
    }


}
