package com.zyx.duomimanager.service;

;

import android.content.Context;

import com.zyx.duomimanager.bean.request.SelectAddressRequest;
import com.zyx.duomimanager.bean.response.SelectAddressResponse;

public class SelectAddressService extends BaseService<SelectAddressRequest, SelectAddressResponse> {

	public SelectAddressService(Context context) {
		super(context);
	}

	@Override
	public SelectAddressRequest newRequest() {
		return null;
	}

}
