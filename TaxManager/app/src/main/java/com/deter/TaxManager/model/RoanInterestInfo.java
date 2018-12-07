package com.deter.TaxManager.model;

import java.io.Serializable;

public class RoanInterestInfo implements Serializable {

    private String roanContactFilePath;
    private String roanPayFilePath;
    private int roanInterest;

    public String getRoanContactFilePath() {
        return roanContactFilePath;
    }

    public void setRoanContactFilePath(String roanContactFilePath) {
        this.roanContactFilePath = roanContactFilePath;
    }

    public String getRoanPayFilePath() {
        return roanPayFilePath;
    }

    public void setRoanPayFilePath(String roanPayFilePath) {
        this.roanPayFilePath = roanPayFilePath;
    }

    public int getRoanInterest() {
        return roanInterest;
    }

    public void setRoanInterest(int roanInterest) {
        this.roanInterest = roanInterest;
    }
}
