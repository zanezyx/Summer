package com.deter.TaxManager.model;

import java.io.Serializable;

public class VocationInfo implements Serializable {

    private String raiseChild1ContactFilePath;
    private String raiseChild2ContactFilePath;
    private int myAlimonyOfFirst;
    private int otherAlimonyOfFirst;
    private int myAlimonyOfSecond;
    private int otherAlimonyOfSecond;

    public String getRaiseChild1ContactFilePath() {
        return raiseChild1ContactFilePath;
    }

    public void setRaiseChild1ContactFilePath(String raiseChild1ContactFilePath) {
        this.raiseChild1ContactFilePath = raiseChild1ContactFilePath;
    }

    public String getRaiseChild2ContactFilePath() {
        return raiseChild2ContactFilePath;
    }

    public void setRaiseChild2ContactFilePath(String raiseChild2ContactFilePath) {
        this.raiseChild2ContactFilePath = raiseChild2ContactFilePath;
    }

    public int getMyAlimonyOfFirst() {
        return myAlimonyOfFirst;
    }

    public void setMyAlimonyOfFirst(int myAlimonyOfFirst) {
        this.myAlimonyOfFirst = myAlimonyOfFirst;
    }

    public int getOtherAlimonyOfFirst() {
        return otherAlimonyOfFirst;
    }

    public void setOtherAlimonyOfFirst(int otherAlimonyOfFirst) {
        this.otherAlimonyOfFirst = otherAlimonyOfFirst;
    }

    public int getMyAlimonyOfSecond() {
        return myAlimonyOfSecond;
    }

    public void setMyAlimonyOfSecond(int myAlimonyOfSecond) {
        this.myAlimonyOfSecond = myAlimonyOfSecond;
    }

    public int getOtherAlimonyOfSecond() {
        return otherAlimonyOfSecond;
    }

    public void setOtherAlimonyOfSecond(int otherAlimonyOfSecond) {
        this.otherAlimonyOfSecond = otherAlimonyOfSecond;
    }
}
