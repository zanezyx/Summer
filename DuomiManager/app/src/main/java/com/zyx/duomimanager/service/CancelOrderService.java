package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.CancelOrderRequest;
import com.zyx.duomimanager.bean.response.CancelOrderResponse;

public class CancelOrderService extends BaseService<CancelOrderRequest, CancelOrderResponse> {

	public CancelOrderService(Context context) {
		super(context);
	}

	@Override
	public CancelOrderRequest newRequest() {
		return null;
	}

}
