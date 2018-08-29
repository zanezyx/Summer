package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.AddressRequest;
import com.zyx.duomimanager.bean.response.AddressResponse;

public class AddressService extends BaseService<AddressRequest, AddressResponse> {

	public AddressService(Context context) {
		super(context);
	}

	@Override
	public AddressRequest newRequest() {
		return null;
	}

}
