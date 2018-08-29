package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 删除地址
 */
public class DeleteAddressRequest extends BaseRequest {
	
	private int id;



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DeleteAddressRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}



}
