package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.CountTimesRequest;
import com.zyx.duomimanager.bean.response.CountTimesResponse;

public class CountTimesService extends BaseService<CountTimesRequest, CountTimesResponse> {

	public CountTimesService(Context context) {
		super(context);
	}

	@Override
	public CountTimesRequest newRequest() {
		return null;
	}

}
