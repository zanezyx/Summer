package com.bf.duomi.service;

import com.bf.duomi.bean.request.MyCionRequest;
import com.bf.duomi.bean.response.MyCionResponse;

import android.content.Context;

public class MyCionService extends BaseService<MyCionRequest, MyCionResponse> {

	public MyCionService(Context context) {
		super(context);
	}

	@Override
	public MyCionRequest newRequest() {
		return null;
	}

}
