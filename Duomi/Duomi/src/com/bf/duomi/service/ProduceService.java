package com.bf.duomi.service;

import com.bf.duomi.bean.request.ProduceRequest;
import com.bf.duomi.bean.response.ProduceResponse;

import android.content.Context;

public class ProduceService extends BaseService<ProduceRequest, ProduceResponse> {

	public ProduceService(Context context) {
		super(context);
	}

	@Override
	public ProduceRequest newRequest() {
		return null;
	}

}
