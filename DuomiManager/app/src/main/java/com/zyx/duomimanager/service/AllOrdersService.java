package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.AllOrdersRequest;
import com.zyx.duomimanager.bean.response.AllOrdersResponse;

public class AllOrdersService extends BaseService<AllOrdersRequest, AllOrdersResponse> {

	public AllOrdersService(Context context) {
		super(context);
	}

	@Override
	public AllOrdersRequest newRequest() {
		return null;
	}

}
