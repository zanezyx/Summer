package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddOrderRequest;
import com.bf.duomi.bean.response.AddOrderResponse;

import android.content.Context;

public class AddOrderService extends BaseService<AddOrderRequest, AddOrderResponse> {

	public AddOrderService(Context context) {
		super(context);
	}

	@Override
	public AddOrderRequest newRequest() {
		return null;
	}

}
