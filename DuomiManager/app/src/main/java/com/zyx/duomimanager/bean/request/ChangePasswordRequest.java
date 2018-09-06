package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion update password
 */
public class ChangePasswordRequest extends BaseRequest {

	private int  id;
	private String password;
	private String pwdnew;

	public ChangePasswordRequest(int id, String password, String pwdNew) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
		this.id = id;
		this.password = password;
		this.pwdnew = pwdNew;
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

	public String getPath(){
		return "/updatepwd";
	}

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}

	public String getPwdnew() {
		return pwdnew;
	}

	public void setPwdnew(String pwdnew) {
		this.pwdnew = pwdnew;
	}
}
