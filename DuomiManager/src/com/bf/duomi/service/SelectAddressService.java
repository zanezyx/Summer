package com.bf.duomi.service;

import com.bf.duomi.bean.request.SelectAddressRequest;
import com.bf.duomi.bean.response.SelectAddressResponse;

import android.content.Context;

public class SelectAddressService extends BaseService<SelectAddressRequest, SelectAddressResponse> {

	public SelectAddressService(Context context) {
		super(context);
	}

	@Override
	public SelectAddressRequest newRequest() {
		return null;
	}

}
