package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 
 */
public class ProductSecondaryRequest extends BaseRequest {

	public int type;
	
	public ProductSecondaryRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
