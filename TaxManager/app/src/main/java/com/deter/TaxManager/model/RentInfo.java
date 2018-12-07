package com.deter.TaxManager.model;

import java.io.Serializable;

public class RentInfo implements Serializable {

    private String rentContactFilePath;
    private String address;
    private TxDate startDate;
    private TxDate endDate;
    private TxDate backupDate;
    private int payPerMonth;

    public String getRentContactFilePath() {
        return rentContactFilePath;
    }

    public void setRentContactFilePath(String rentContactFilePath) {
        this.rentContactFilePath = rentContactFilePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TxDate getStartDate() {
        return startDate;
    }

    public void setStartDate(TxDate startDate) {
        this.startDate = startDate;
    }

    public TxDate getEndDate() {
        return endDate;
    }

    public void setEndDate(TxDate endDate) {
        this.endDate = endDate;
    }

    public TxDate getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(TxDate backupDate) {
        this.backupDate = backupDate;
    }

    public int getPayPerMonth() {
        return payPerMonth;
    }

    public void setPayPerMonth(int payPerMonth) {
        this.payPerMonth = payPerMonth;
    }
}
