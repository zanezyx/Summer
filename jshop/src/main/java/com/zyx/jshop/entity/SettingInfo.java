package com.zyx.jshop.entity;

import java.util.ArrayList;

public class SettingInfo {

	public String serviceMobile;
	public String latestVersionCode;
	public String latestVersionURL;
	
	public SettingInfo()
	{
	}
	


	public String getServiceMobile() {
		return serviceMobile;
	}

	public void setServiceMobile(String serviceMobile) {
		this.serviceMobile = serviceMobile;
	}

	public String getLatestVersionCode() {
		return latestVersionCode;
	}

	public void setLatestVersionCode(String latestVersionCode) {
		this.latestVersionCode = latestVersionCode;
	}

	public String getLatestVersionURL() {
		return latestVersionURL;
	}

	public void setLatestVersionURL(String latestVersionURL) {
		this.latestVersionURL = latestVersionURL;
	}
}
