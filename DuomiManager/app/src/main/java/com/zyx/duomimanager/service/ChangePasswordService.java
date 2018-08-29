package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ChangePasswordRequest;
import com.zyx.duomimanager.bean.response.ChangePasswordResponse;

public class ChangePasswordService extends BaseService<ChangePasswordRequest, ChangePasswordResponse> {

	public ChangePasswordService(Context context) {
		super(context);
	}

	@Override
	public ChangePasswordRequest newRequest() {
		return null;
	}

}
