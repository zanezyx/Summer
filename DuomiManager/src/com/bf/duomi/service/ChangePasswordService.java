package com.bf.duomi.service;

import com.bf.duomi.bean.request.ChangePasswordRequest;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.ChangePasswordResponse;
import com.bf.duomi.bean.response.LoginResponse;

import android.content.Context;

public class ChangePasswordService extends BaseService<ChangePasswordRequest, ChangePasswordResponse> {

	public ChangePasswordService(Context context) {
		super(context);
	}

	@Override
	public ChangePasswordRequest newRequest() {
		return null;
	}

}
