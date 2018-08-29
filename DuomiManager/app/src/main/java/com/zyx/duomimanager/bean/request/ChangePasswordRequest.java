package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 修改密码
 */
public class ChangePasswordRequest extends BaseRequest {

	private int  id;
	private String password;
	private String password1;

	public ChangePasswordRequest(int id, String password, String password1) {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
		this.id = id;
		this.password = password;
		this.password1 = password1;
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



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getPassword1() {
		return password1;
	}



	public void setPassword1(String password1) {
		this.password1 = password1;
	}

}
