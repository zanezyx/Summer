package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author bhl
 * @anotion 登录
 */
public class MyMoneyRequest extends BaseRequest {

	private int customId;

	public MyMoneyRequest() {
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
