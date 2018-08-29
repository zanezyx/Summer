package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.UserNumRequest;
import com.zyx.duomimanager.bean.response.UserNumResponse;

public class UserNumService extends BaseService<UserNumRequest, UserNumResponse> {

	public UserNumService(Context context) {
		super(context);
	}

	@Override
	public UserNumRequest newRequest() {
		return null;
	}

}
