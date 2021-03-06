package com.deter.TaxManager.model;

import java.io.Serializable;

public class ParentInfo implements Serializable {

    public final static int MONTHER = 0;
    public final static int FATHER = 1;
    private String name;
    private TxDate txDate;
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

    public TxDate getTxDate() {
        return txDate;
    }

    public void setTxDate(TxDate txDate) {
        this.txDate = txDate;
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
