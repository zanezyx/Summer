package com.deter.TaxManager.model;

import java.io.Serializable;

public class ChildrenInfo implements Serializable {

    public final static int DAUGHTER = 0;
    public final static int SON = 1;
    private String name;
    private TxDate txDate;
    private String identity;
    private String bornId;
    private int relationship;


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

    public String getBornId() {
        return bornId;
    }

    public void setBornId(String bornId) {
        this.bornId = bornId;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }
}
