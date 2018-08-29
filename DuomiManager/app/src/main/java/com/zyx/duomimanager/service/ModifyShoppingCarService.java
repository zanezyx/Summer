package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ModifyShoppingCarRequest;
import com.zyx.duomimanager.bean.response.ModifyShoppingCarResponse;

public class ModifyShoppingCarService extends
		BaseService<ModifyShoppingCarRequest, ModifyShoppingCarResponse> {

	public ModifyShoppingCarService(Context context) {
		super(context);
	}

	@Override
	public ModifyShoppingCarRequest newRequest() {
		return null;
	}

}
