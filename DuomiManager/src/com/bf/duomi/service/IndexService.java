package com.bf.duomi.service;

import com.bf.duomi.bean.request.IndexRequest;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.LoginResponse;

import android.content.Context;

public class IndexService extends BaseService<IndexRequest, IndexResponse> {

	public IndexService(Context context) {
		super(context);
	}

	@Override
	public IndexRequest newRequest() {
		return null;
	}

}
