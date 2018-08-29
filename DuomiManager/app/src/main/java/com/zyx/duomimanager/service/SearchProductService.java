package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.SearchProductRequest;
import com.zyx.duomimanager.bean.response.SearchProductResponse;

public class SearchProductService extends BaseService<SearchProductRequest, SearchProductResponse> {

	public SearchProductService(Context context) {
		super(context);
	}

	@Override
	public SearchProductRequest newRequest() {
		return null;
	}

}
