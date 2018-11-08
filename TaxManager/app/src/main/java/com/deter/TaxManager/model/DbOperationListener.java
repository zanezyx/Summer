package com.deter.TaxManager.model;

import java.util.List;

/**
 * Created by yuxinz on 2017/7/20.
 */

public interface DbOperationListener {

    void onPre(int dbFuctionId);
    void onDone(int dbFfuctionId, int res, List<Object> resultList);
}
