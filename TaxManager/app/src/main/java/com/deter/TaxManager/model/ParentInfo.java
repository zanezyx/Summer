package com.deter.TaxManager.model;

import java.io.Serializable;
import java.util.Date;

public class ParentInfo implements Serializable {

    public final static int MONTHER = 0;
    public final static int FATHER = 1;
    private String name;
    private Date bornDate;
    private String identity;
    private String carrier;
    private int relationship;


    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}
