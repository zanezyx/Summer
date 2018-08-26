package com.bf.duomi.service;

import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.SearchProductResponse;

import android.content.Context;

public class SearchProductService extends BaseService<SearchProductRequest, SearchProductResponse> {

	public SearchProductService(Context context) {
		super(context);
	}

	@Override
	public SearchProductRequest newRequest() {
		return null;
	}

}
