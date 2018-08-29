package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.OrderDetailRequest;
import com.zyx.duomimanager.bean.response.OrderDetailResponse;

public class OrderListService extends
		BaseService<OrderDetailRequest, OrderDetailResponse> {

	public OrderListService(Context context) {
		super(context);
	}

	@Override
	public OrderDetailRequest newRequest() {
		return null;
	}

}
