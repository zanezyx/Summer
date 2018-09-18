package com.zyx.jshop.entity;

import java.util.ArrayList;

public class PaymentManager {


	private static PaymentManager paymentManager;
	
	//0:货到付款 1:支付宝 2:微信 3:银行卡
	public int paymentType;
	
	
	PaymentManager()
	{
		paymentType=0;
	}
	
	
	public static PaymentManager getInstance()
	{
		if(paymentManager==null)
		{
			paymentManager = new PaymentManager();
		}	
		return paymentManager;
	}
	
	
}
