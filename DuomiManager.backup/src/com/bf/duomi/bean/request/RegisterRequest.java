package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 注册
 */
public class RegisterRequest extends BaseRequest {

	private String name;
	private String mobile;
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIsAgree() {
		return isAgree;
	}


	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}


	private String password;
	private String isAgree;
	
	
	public RegisterRequest(String mobile, String password) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
		this.name = "unknown";
		this.mobile = mobile;
		this.password = password;
		this.isAgree = "1";
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
