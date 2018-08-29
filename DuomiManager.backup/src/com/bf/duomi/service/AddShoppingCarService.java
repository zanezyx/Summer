package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddShoppingCarRequest;
import com.bf.duomi.bean.response.AddShoppingCarResponse;

import android.content.Context;

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
