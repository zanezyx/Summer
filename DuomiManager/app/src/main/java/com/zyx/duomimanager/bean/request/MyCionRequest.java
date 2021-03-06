package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author bhl
 * @anotion 登录
 */
public class MyCionRequest extends BaseRequest {

	private int customId;

	public MyCionRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

}
