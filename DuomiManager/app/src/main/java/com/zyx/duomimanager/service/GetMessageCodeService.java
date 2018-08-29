package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.GetMessageCodeRequest;
import com.zyx.duomimanager.bean.response.GetMessageCodeResponse;

public class GetMessageCodeService extends BaseService<GetMessageCodeRequest, GetMessageCodeResponse> {

	public GetMessageCodeService(Context context) {
		super(context);
	}

	@Override
	public GetMessageCodeRequest newRequest() {
		return null;
	}

}
