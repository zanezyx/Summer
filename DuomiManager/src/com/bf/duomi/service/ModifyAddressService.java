package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddressRequest;
import com.bf.duomi.bean.request.ModifyAddressRequest;
import com.bf.duomi.bean.response.AddressResponse;
import com.bf.duomi.bean.response.ModifyAddressResponse;

import android.content.Context;

public class ModifyAddressService extends BaseService<ModifyAddressRequest, ModifyAddressResponse> {

	public ModifyAddressService(Context context) {
		super(context);
	}

	@Override
	public ModifyAddressRequest newRequest() {
		return null;
	}

}
