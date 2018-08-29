package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 订单列表
 */
public class AllOrdersRequest extends BaseRequest {


	public AllOrdersRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
