package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.AddShoppingCarRequest;
import com.zyx.duomimanager.bean.response.AddShoppingCarResponse;

public class AddShoppingCarService extends
		BaseService<AddShoppingCarRequest, AddShoppingCarResponse> {

	public AddShoppingCarService(Context context) {
		super(context);
	}

	@Override
	public AddShoppingCarRequest newRequest() {
		return null;
	}

}
