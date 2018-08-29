package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 修改购物车项信息
 */
public class ModifyShoppingCarRequest extends BaseRequest {



	private int id;
	private int number;

	public ModifyShoppingCarRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}


	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}
}
