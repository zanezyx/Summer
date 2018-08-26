package com.bf.duomi.service;

import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.ProductSecondaryRequest;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.ProductSecondaryResponse;

import android.content.Context;

public class ProductSecondaryService extends BaseService<ProductSecondaryRequest, ProductSecondaryResponse> {

	public ProductSecondaryService(Context context) {
		super(context);
	}

	@Override
	public ProductSecondaryRequest newRequest() {
		return null;
	}

}
