package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.ProductSecondaryRequest;
import com.zyx.duomimanager.bean.response.ProductSecondaryResponse;

public class ProductSecondaryService extends BaseService<ProductSecondaryRequest, ProductSecondaryResponse> {

	public ProductSecondaryService(Context context) {
		super(context);
	}

	@Override
	public ProductSecondaryRequest newRequest() {
		return null;
	}

}
