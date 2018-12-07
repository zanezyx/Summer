package com.deter.TaxManager.model;

import java.io.Serializable;

public class SupportParentInfo implements Serializable {

    private String contactFilePath;
    private int myAlimony;
    private int otherAlimony1;
    private int otherAlimony2;

    public int getMyAlimony() {
        return myAlimony;
    }

    public void setMyAlimony(int myAlimony) {
        this.myAlimony = myAlimony;
    }

    public int getOtherAlimony1() {
        return otherAlimony1;
    }

    public void setOtherAlimony1(int otherAlimony1) {
        this.otherAlimony1 = otherAlimony1;
    }

    public int getOtherAlimony2() {
        return otherAlimony2;
    }

    public void setOtherAlimony2(int otherAlimony2) {
        this.otherAlimony2 = otherAlimony2;
    }

    public String getContactFilePath() {
        return contactFilePath;
    }

    public void setContactFilePath(String contactFilePath) {
        this.contactFilePath = contactFilePath;
    }
}
