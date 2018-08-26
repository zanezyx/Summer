package com.bf.duomi.service;

import com.bf.duomi.bean.request.GetShoppingCarRequest;
import com.bf.duomi.bean.response.GetShoppingCarResponse;

import android.content.Context;

public class GetShoppingCarService extends BaseService<GetShoppingCarRequest, GetShoppingCarResponse> {

	public GetShoppingCarService(Context context) {
		super(context);
	}

	@Override
	public GetShoppingCarRequest newRequest() {
		return null;
	}

}
