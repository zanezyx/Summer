package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddressRequest;
import com.bf.duomi.bean.response.AddressResponse;

import android.content.Context;

public class AddressService extends BaseService<AddressRequest, AddressResponse> {

	public AddressService(Context context) {
		super(context);
	}

	@Override
	public AddressRequest newRequest() {
		return null;
	}

}
