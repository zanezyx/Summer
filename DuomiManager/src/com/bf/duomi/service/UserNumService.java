package com.bf.duomi.service;

import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.request.UserNumRequest;
import com.bf.duomi.bean.response.LoginResponse;
import com.bf.duomi.bean.response.UserNumResponse;

import android.content.Context;

public class UserNumService extends BaseService<UserNumRequest, UserNumResponse> {

	public UserNumService(Context context) {
		super(context);
	}

	@Override
	public UserNumRequest newRequest() {
		return null;
	}

}
