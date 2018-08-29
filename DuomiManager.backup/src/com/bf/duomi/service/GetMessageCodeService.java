package com.bf.duomi.service;

import com.bf.duomi.bean.request.GetMessageCodeRequest;
import com.bf.duomi.bean.response.GetMessageCodeResponse;

import android.content.Context;

public class GetMessageCodeService extends BaseService<GetMessageCodeRequest, GetMessageCodeResponse> {

	public GetMessageCodeService(Context context) {
		super(context);
	}

	@Override
	public GetMessageCodeRequest newRequest() {
		return null;
	}

}
