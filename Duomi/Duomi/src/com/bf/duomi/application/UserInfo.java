package com.bf.duomi.application;

public class UserInfo {

	public static UserInfo userInfo;
	
	public static UserInfo getInstance()
	{
		if(userInfo==null)
		{
			userInfo = new UserInfo();
		}
		return userInfo;
	}
	
	public int customId;
	public String  moblile;
	public String  password;
	public boolean isLogin;
	public String name;
	public int count;
	public int quan;
	public int score;
	
	
}
