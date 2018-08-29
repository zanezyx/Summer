package com.bf.duomi.service;

import com.bf.duomi.bean.request.AllOrdersRequest;
import com.bf.duomi.bean.request.OneMonthRequest;
import com.bf.duomi.bean.request.UserOrdersRequest;
import com.bf.duomi.bean.response.AllOrdersResponse;
import com.bf.duomi.bean.response.OneMonthResponse;
import com.bf.duomi.bean.response.UserOrdersResponse;

import android.content.Context;

public class AllOrdersService extends BaseService<AllOrdersRequest, AllOrdersResponse> {

	public AllOrdersService(Context context) {
		super(context);
	}

	@Override
	public AllOrdersRequest newRequest() {
		return null;
	}

}
