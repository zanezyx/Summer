package com.bf.duomi.service;

import com.bf.duomi.bean.request.CountTimesRequest;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.CountTimesResponse;
import com.bf.duomi.bean.response.LoginResponse;

import android.content.Context;

public class CountTimesService extends BaseService<CountTimesRequest, CountTimesResponse> {

	public CountTimesService(Context context) {
		super(context);
	}

	@Override
	public CountTimesRequest newRequest() {
		return null;
	}

}
