package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.AddOrderRequest;
import com.zyx.duomimanager.bean.response.AddOrderResponse;

public class AddOrderService extends BaseService<AddOrderRequest, AddOrderResponse> {

	public AddOrderService(Context context) {
		super(context);
	}

	@Override
	public AddOrderRequest newRequest() {
		return null;
	}

}
