package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author bhl
 * @anotion 登录
 */
public class LoginRequest extends BaseRequest {

	private String mobile;
	private String password;

	public LoginRequest(String mobile, String password) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
		this.mobile = mobile;
		this.password = password;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
