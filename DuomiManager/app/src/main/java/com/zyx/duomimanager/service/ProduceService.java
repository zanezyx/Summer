package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ProduceRequest;
import com.zyx.duomimanager.bean.response.ProduceResponse;

public class ProduceService extends BaseService<ProduceRequest, ProduceResponse> {

	public ProduceService(Context context) {
		super(context);
	}

	@Override
	public ProduceRequest newRequest() {
		return null;
	}

}
