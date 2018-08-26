package com.bf.duomi.service;

import com.bf.duomi.bean.request.GetUserDetailRequest;
import com.bf.duomi.bean.response.GetUserDetailResponse;

import android.content.Context;

public class GetUserDetailService extends
		BaseService<GetUserDetailRequest, GetUserDetailResponse> {

	public GetUserDetailService(Context context) {
		super(context);
	}

	@Override
	public GetUserDetailRequest newRequest() {
		return null;
	}

}
