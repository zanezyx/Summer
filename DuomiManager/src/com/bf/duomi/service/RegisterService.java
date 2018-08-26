package com.bf.duomi.service;

import com.bf.duomi.bean.request.GetMessageCodeRequest;
import com.bf.duomi.bean.request.RegisterRequest;
import com.bf.duomi.bean.response.GetMessageCodeResponse;
import com.bf.duomi.bean.response.RegisterResponse;

import android.content.Context;

public class RegisterService extends BaseService<RegisterRequest, RegisterResponse> {

	public RegisterService(Context context) {
		super(context);
	}

	@Override
	public RegisterRequest newRequest() {
		return null;
	}

}
