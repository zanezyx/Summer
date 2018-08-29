package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.PaymentRequest;
import com.zyx.duomimanager.bean.response.PaymentResponse;

public class PaymentService extends BaseService<PaymentRequest, PaymentResponse> {

	public PaymentService(Context context) {
		super(context);
	}

	@Override
	public PaymentRequest newRequest() {
		return null;
	}

}
