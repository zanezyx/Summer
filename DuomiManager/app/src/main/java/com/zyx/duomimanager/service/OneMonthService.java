package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.OneMonthRequest;
import com.zyx.duomimanager.bean.response.OneMonthResponse;

public class OneMonthService extends BaseService<OneMonthRequest, OneMonthResponse> {

	public OneMonthService(Context context) {
		super(context);
	}

	@Override
	public OneMonthRequest newRequest() {
		return null;
	}

}
