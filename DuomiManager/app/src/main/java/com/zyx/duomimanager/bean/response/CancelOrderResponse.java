package com.zyx.duomimanager.bean.response;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;


public class CancelOrderResponse extends BaseResponse {

	public boolean isSuccess;
	private int orderId;

	public CancelOrderResponse() {
		super();
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public void parseResString() {
		if (resString != null) {
			if(resString.contains("success"))
			{
				isSuccess = true;
			}else{
				isSuccess = false;
			}

		}
		
	}

}
