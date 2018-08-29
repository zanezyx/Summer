package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.MyMoneyRequest;
import com.zyx.duomimanager.bean.response.MyMoneyResponse;

public class MyMoneyService extends BaseService<MyMoneyRequest, MyMoneyResponse> {

	public MyMoneyService(Context context) {
		super(context);
	}

	@Override
	public MyMoneyRequest newRequest() {
		return null;
	}

}
