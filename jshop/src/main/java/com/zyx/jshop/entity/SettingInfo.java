package com.zyx.jshop.entity;

import java.util.ArrayList;

public class SettingInfo {

	private static SettingInfo settingInfo;
	public String serviceMobile;
	public String latestVersionCode;
	public String latestVersionURL;
	
	SettingInfo()
	{
	}
	
	
	public static SettingInfo getInstance()
	{
		if(settingInfo==null)
		{
			settingInfo = new SettingInfo();
		}	
		return settingInfo;
	}
	
}
