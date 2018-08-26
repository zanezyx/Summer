package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 统计启动次数
 */
public class CountTimesRequest extends BaseRequest {

	private String imei;

	public CountTimesRequest(String imei) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
		this.imei = imei;

	}


	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}


	public String getImei() {
		return imei;
	}


	public void setImei(String imei) {
		this.imei = imei;
	}

}
