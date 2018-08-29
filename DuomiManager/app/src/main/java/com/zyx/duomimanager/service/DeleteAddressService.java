package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.DeleteAddressRequest;
import com.zyx.duomimanager.bean.response.DeleteAddressResponse;

public class DeleteAddressService extends BaseService<DeleteAddressRequest, DeleteAddressResponse> {

	public DeleteAddressService(Context context) {
		super(context);
	}

	@Override
	public DeleteAddressRequest newRequest() {
		return null;
	}

}
