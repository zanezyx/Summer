package com.bf.duomi.service;

import com.bf.duomi.bean.request.MyMoneyRequest;
import com.bf.duomi.bean.response.MyMoneyResponse;

import android.content.Context;

public class MyMoneyService extends BaseService<MyMoneyRequest, MyMoneyResponse> {

	public MyMoneyService(Context context) {
		super(context);
	}

	@Override
	public MyMoneyRequest newRequest() {
		return null;
	}

}
