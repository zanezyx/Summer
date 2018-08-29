package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 查询启动次数
 */
public class UserNumRequest extends BaseRequest {


	public UserNumRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);

	}


	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
