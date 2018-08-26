package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddressRequest;
import com.bf.duomi.bean.request.DeleteAddressRequest;
import com.bf.duomi.bean.response.AddressResponse;
import com.bf.duomi.bean.response.DeleteAddressResponse;

import android.content.Context;

public class DeleteAddressService extends BaseService<DeleteAddressRequest, DeleteAddressResponse> {

	public DeleteAddressService(Context context) {
		super(context);
	}

	@Override
	public DeleteAddressRequest newRequest() {
		return null;
	}

}
