package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.GetUserDetailRequest;
import com.zyx.duomimanager.bean.response.GetUserDetailResponse;

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
