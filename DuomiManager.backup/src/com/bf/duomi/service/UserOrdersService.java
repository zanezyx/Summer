package com.bf.duomi.service;

import com.bf.duomi.bean.request.OneMonthRequest;
import com.bf.duomi.bean.request.UserOrdersRequest;
import com.bf.duomi.bean.response.OneMonthResponse;
import com.bf.duomi.bean.response.UserOrdersResponse;

import android.content.Context;

public class UserOrdersService extends BaseService<UserOrdersRequest, UserOrdersResponse> {

	public UserOrdersService(Context context) {
		super(context);
	}

	@Override
	public UserOrdersRequest newRequest() {
		return null;
	}

}
