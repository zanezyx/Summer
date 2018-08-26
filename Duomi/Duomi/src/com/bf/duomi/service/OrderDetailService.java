package com.bf.duomi.service;

import com.bf.duomi.bean.request.OrderDetailRequest;
import com.bf.duomi.bean.response.OrderDetailResponse;

import android.content.Context;

public class OrderDetailService extends
		BaseService<OrderDetailRequest, OrderDetailResponse> {

	public OrderDetailService(Context context) {
		super(context);
	}

	@Override
	public OrderDetailRequest newRequest() {
		return null;
	}

}
