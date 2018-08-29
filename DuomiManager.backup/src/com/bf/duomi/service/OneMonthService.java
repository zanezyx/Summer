package com.bf.duomi.service;

import com.bf.duomi.bean.request.OneMonthRequest;
import com.bf.duomi.bean.response.OneMonthResponse;

import android.content.Context;

public class OneMonthService extends BaseService<OneMonthRequest, OneMonthResponse> {

	public OneMonthService(Context context) {
		super(context);
	}

	@Override
	public OneMonthRequest newRequest() {
		return null;
	}

}
