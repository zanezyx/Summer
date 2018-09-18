package com.zyx.jshop.entity;

import java.util.List;

public class ReceiveAddressMgr {
	
	static ReceiveAddressMgr receiveAddressMgr;
	
	public static ReceiveAddressMgr getInstance()
	{
		if(receiveAddressMgr==null)
			receiveAddressMgr = new ReceiveAddressMgr();
		return receiveAddressMgr;
	}
	
	public ReceiveAddress currReceiveAddress;
	public ReceiveAddress currUseReceiveAddress;
	public List<ReceiveAddress>  receiveAddressList;
	public int currDefault;
	
}
