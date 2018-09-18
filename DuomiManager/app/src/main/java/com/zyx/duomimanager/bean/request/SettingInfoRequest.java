package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author  zyx
 * @anotion 
 */
public class SettingInfoRequest extends BaseRequest {

	public SettingInfoRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

	@Override
	public String getPath() {
		return "/settings";
	}
}
