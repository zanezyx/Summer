package com.deter.TaxManager.model;

import java.io.Serializable;
import java.util.Date;

public class BaseInfo implements Serializable {

    private String name;
    private Date bornDate;
    private String taxpayerId;
    private String carrier;
    private String deformityId;
    private String martyrId;
    private String marriageId;
    private boolean isOnlyChild;
    private String address;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDeformityId() {
        return deformityId;
    }

    public void setDeformityId(String deformityId) {
        this.deformityId = deformityId;
    }

    public String getMartyrId() {
        return martyrId;
    }

    public void setMartyrId(String martyrId) {
        this.martyrId = martyrId;
    }

    public String getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(String marriageId) {
        this.marriageId = marriageId;
    }

    public boolean isOnlyChild() {
        return isOnlyChild;
    }

    public void setOnlyChild(boolean onlyChild) {
        isOnlyChild = onlyChild;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
