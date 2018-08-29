package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.RegisterRequest;
import com.zyx.duomimanager.bean.response.RegisterResponse;

public class RegisterService extends BaseService<RegisterRequest, RegisterResponse> {

	public RegisterService(Context context) {
		super(context);
	}

	@Override
	public RegisterRequest newRequest() {
		return null;
	}

}
