package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddShoppingCarRequest;
import com.bf.duomi.bean.request.DeleteShopCarRequest;
import com.bf.duomi.bean.response.AddShoppingCarResponse;
import com.bf.duomi.bean.response.DeleteShopCarResponse;

import android.content.Context;

public class DeleteShopCarService extends
		BaseService<DeleteShopCarRequest, DeleteShopCarResponse> {

	public DeleteShopCarService(Context context) {
		super(context);
	}

	@Override
	public DeleteShopCarRequest newRequest() {
		return null;
	}

}
