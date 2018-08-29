package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.SingleProduceRequest;
import com.zyx.duomimanager.bean.response.SingleProduceResponse;

public class SingleProduceService extends BaseService<SingleProduceRequest, SingleProduceResponse> {

	public SingleProduceService(Context context) {
		super(context);
	}

	@Override
	public SingleProduceRequest newRequest() {
		return null;
	}

}
