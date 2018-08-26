package com.bf.duomi.service;

import com.bf.duomi.bean.request.StartTimesRequest;
import com.bf.duomi.bean.response.StartTimesResponse;

import android.content.Context;

public class StartTimesService extends BaseService<StartTimesRequest, StartTimesResponse> {

	public StartTimesService(Context context) {
		super(context);
	}

	@Override
	public StartTimesRequest newRequest() {
		return null;
	}

}
