package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.UserOrdersRequest;
import com.zyx.duomimanager.bean.response.UserOrdersResponse;

public class UserOrdersService extends BaseService<UserOrdersRequest, UserOrdersResponse> {

	public UserOrdersService(Context context) {
		super(context);
	}

	@Override
	public UserOrdersRequest newRequest() {
		return null;
	}

}
