package com.bf.duomi.commication;

import com.bf.duomi.util.PropertiesUtils;

/**
 * 继承父类，实现父类抽象方法
 * @author lenovo
 *
 */
public class MyRequest extends BaseRequest {

	/**
	 * 获取服务器地址
	 */
	public String getUrl() {
		return super.getSERVIER_URL();
	}
	
	/**
	 * 获取远程调用方法
	 * @param mothedName
	 * @return
	 */
	public String remoteMethod(final String mothedName){
		String mn = "";
		mn = PropertiesUtils.getProperties().getProperty(mothedName);
		return mn;
	}

}
