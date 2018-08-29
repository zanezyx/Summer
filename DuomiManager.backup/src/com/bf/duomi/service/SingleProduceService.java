package com.bf.duomi.service;

import com.bf.duomi.bean.request.SingleProduceRequest;
import com.bf.duomi.bean.response.SingleProduceResponse;

import android.content.Context;

public class SingleProduceService extends BaseService<SingleProduceRequest, SingleProduceResponse> {

	public SingleProduceService(Context context) {
		super(context);
	}

	@Override
	public SingleProduceRequest newRequest() {
		return null;
	}

}
