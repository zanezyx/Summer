package com.bf.duomi.service;

import com.bf.duomi.bean.request.PaymentRequest;
import com.bf.duomi.bean.response.PaymentResponse;

import android.content.Context;

public class PaymentService extends BaseService<PaymentRequest, PaymentResponse> {

	public PaymentService(Context context) {
		super(context);
	}

	@Override
	public PaymentRequest newRequest() {
		return null;
	}

}
