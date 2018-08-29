package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.DeleteShopCarRequest;
import com.zyx.duomimanager.bean.response.DeleteShopCarResponse;

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
