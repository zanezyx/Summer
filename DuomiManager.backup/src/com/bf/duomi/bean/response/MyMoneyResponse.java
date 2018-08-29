package com.bf.duomi.bean.response;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Wallet;

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
