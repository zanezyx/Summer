package com.deter.TaxManager.model;

import java.io.Serializable;

public class InsuranceInfo implements Serializable {

    private TxDate startDate;
    private TxDate endDate;
    private int payPerMonth;

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

    public int getPayPerMonth() {
        return payPerMonth;
    }

    public void setPayPerMonth(int payPerMonth) {
        this.payPerMonth = payPerMonth;
    }
}
