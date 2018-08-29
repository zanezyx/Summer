package com.bf.duomi.service;

import com.bf.duomi.bean.request.AddShoppingCarRequest;
import com.bf.duomi.bean.request.ModifyShoppingCarRequest;
import com.bf.duomi.bean.response.AddShoppingCarResponse;
import com.bf.duomi.bean.response.ModifyShoppingCarResponse;

import android.content.Context;

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
