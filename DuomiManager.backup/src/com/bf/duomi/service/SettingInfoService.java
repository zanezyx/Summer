package com.bf.duomi.service;

import com.bf.duomi.bean.request.SettingInfoRequest;
import com.bf.duomi.bean.request.SingleProduceRequest;
import com.bf.duomi.bean.response.SettingInfoResponse;
import com.bf.duomi.bean.response.SingleProduceResponse;

import android.content.Context;

public class SettingInfoService extends BaseService<SettingInfoRequest, SettingInfoResponse> {

	public SettingInfoService(Context context) {
		super(context);
	}

	@Override
	public SettingInfoRequest newRequest() {
		return null;
	}

}
