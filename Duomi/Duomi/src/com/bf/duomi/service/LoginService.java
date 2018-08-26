package com.bf.duomi.service;

import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.LoginResponse;

import android.content.Context;

public class LoginService extends BaseService<LoginRequest, LoginResponse> {

	public LoginService(Context context) {
		super(context);
	}

	@Override
	public LoginRequest newRequest() {
		return null;
	}

}
