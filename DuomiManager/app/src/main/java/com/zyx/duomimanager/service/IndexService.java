package com.zyx.duomimanager.service;



import android.content.Context;

import com.zyx.duomimanager.bean.request.IndexRequest;
import com.zyx.duomimanager.bean.response.IndexResponse;

public class IndexService extends BaseService<IndexRequest, IndexResponse> {

	public IndexService(Context context) {
		super(context);
	}

	@Override
	public IndexRequest newRequest() {
		return null;
	}

}
