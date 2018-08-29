package com.zyx.duomimanager.bean.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;

public class AddShoppingCarResponse extends BaseResponse {

	public boolean isSuccess;
	
	public AddShoppingCarResponse() {
		super();
		isSuccess = false;
	}

	

	public void parseResString() {
		if (resString != null) {
			if(resString.contains("success"))
			{
				isSuccess = true;
			}else{
				isSuccess = false;
			}
		}else{
			isSuccess = false;
		}
	}
}
