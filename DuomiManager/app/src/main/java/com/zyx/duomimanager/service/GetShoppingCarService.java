package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.GetShoppingCarRequest;
import com.zyx.duomimanager.bean.response.GetShoppingCarResponse;

public class GetShoppingCarService extends BaseService<GetShoppingCarRequest, GetShoppingCarResponse> {

	public GetShoppingCarService(Context context) {
		super(context);
	}

	@Override
	public GetShoppingCarRequest newRequest() {
		return null;
	}

}
