package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;
import com.zyx.duomimanager.util.PropertiesUtils;

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

	public String getPath() {
		String path = "/login";
		return path;
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
