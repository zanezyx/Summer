package com.zyx.duomimanager.service;


import android.content.Context;

import com.zyx.duomimanager.bean.request.SettingInfoRequest;
import com.zyx.duomimanager.bean.response.SettingInfoResponse;

public class SettingInfoService extends BaseService<SettingInfoRequest, SettingInfoResponse> {

	public SettingInfoService(Context context) {
		super(context);
	}

	@Override
	public SettingInfoRequest newRequest() {
		return null;
	}

}
