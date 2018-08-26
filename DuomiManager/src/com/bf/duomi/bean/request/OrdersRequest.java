package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 查询启动次数
 */
public class OrdersRequest extends BaseRequest {


	public OrdersRequest(String mobile, String password) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);

	}


	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
