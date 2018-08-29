package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ProduceTypeRequest;
import com.zyx.duomimanager.bean.response.ProduceTypeResponse;

public class ProduceTypeService extends BaseService<ProduceTypeRequest, ProduceTypeResponse> {

	public ProduceTypeService(Context context) {
		super(context);
	}

	@Override
	public ProduceTypeRequest newRequest() {
		return null;
	}

}
