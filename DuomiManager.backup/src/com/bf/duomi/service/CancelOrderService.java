package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddOrderRequest;
import com.bf.duomi.bean.request.CancelOrderRequest;
import com.bf.duomi.bean.response.AddOrderResponse;
import com.bf.duomi.bean.response.CancelOrderResponse;

import android.content.Context;

public class CancelOrderService extends BaseService<CancelOrderRequest, CancelOrderResponse> {

	public CancelOrderService(Context context) {
		super(context);
	}

	@Override
	public CancelOrderRequest newRequest() {
		return null;
	}

}
