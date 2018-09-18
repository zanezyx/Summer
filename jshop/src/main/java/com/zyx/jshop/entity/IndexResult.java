package com.zyx.jshop.entity;

import java.util.List;

public class IndexResult {

    private List<Product> newList = null;

    private List<Product> hotSales = null;

    public IndexResult() {

    }

    public IndexResult(List<Product> newList, List<Product> hotSales) {
        this.newList = newList;
        this.hotSales = hotSales;
    }

    public List<Product> getNewList() {
        return newList;
    }

    public void setNewList(List<Product> newList) {
        this.newList = newList;
    }

    public List<Product> getHotSales() {
        return hotSales;
    }

    public void setHotSales(List<Product> hotSales) {
        this.hotSales = hotSales;
    }
}

