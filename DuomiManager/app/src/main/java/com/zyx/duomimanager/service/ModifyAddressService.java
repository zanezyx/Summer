package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ModifyAddressRequest;
import com.zyx.duomimanager.bean.response.ModifyAddressResponse;

public class ModifyAddressService extends BaseService<ModifyAddressRequest, ModifyAddressResponse> {

	public ModifyAddressService(Context context) {
		super(context);
	}

	@Override
	public ModifyAddressRequest newRequest() {
		return null;
	}

}
