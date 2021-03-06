package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author bhl
 */
public class OrderListRequest extends BaseRequest {

	private int id;

	public OrderListRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
