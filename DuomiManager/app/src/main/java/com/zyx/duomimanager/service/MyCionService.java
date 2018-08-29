package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.MyCionRequest;
import com.zyx.duomimanager.bean.response.MyCionResponse;

public class MyCionService extends BaseService<MyCionRequest, MyCionResponse> {

	public MyCionService(Context context) {
		super(context);
	}

	@Override
	public MyCionRequest newRequest() {
		return null;
	}

}
