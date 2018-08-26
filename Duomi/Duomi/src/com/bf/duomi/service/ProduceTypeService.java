package com.bf.duomi.service;

import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.response.ProduceTypeResponse;

import android.content.Context;

public class ProduceTypeService extends BaseService<ProduceTypeRequest, ProduceTypeResponse> {

	public ProduceTypeService(Context context) {
		super(context);
	}

	@Override
	public ProduceTypeRequest newRequest() {
		return null;
	}

}
