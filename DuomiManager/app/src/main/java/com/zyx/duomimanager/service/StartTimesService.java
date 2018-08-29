package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.StartTimesRequest;
import com.zyx.duomimanager.bean.response.StartTimesResponse;

public class StartTimesService extends BaseService<StartTimesRequest, StartTimesResponse> {

	public StartTimesService(Context context) {
		super(context);
	}

	@Override
	public StartTimesRequest newRequest() {
		return null;
	}

}
