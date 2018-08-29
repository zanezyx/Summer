package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.LoginRequest;
import com.zyx.duomimanager.bean.response.LoginResponse;

public class LoginService extends BaseService<LoginRequest, LoginResponse> {

	public LoginService(Context context) {
		super(context);
	}

	@Override
	public LoginRequest newRequest() {
		return null;
	}

}
