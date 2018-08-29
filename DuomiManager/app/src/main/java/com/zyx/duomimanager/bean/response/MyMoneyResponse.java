package com.zyx.duomimanager.bean.response;


import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Wallet;

public class MyMoneyResponse extends BaseResponse {

	private Wallet wallet;
	
	public MyMoneyResponse() {
		super();
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

}
