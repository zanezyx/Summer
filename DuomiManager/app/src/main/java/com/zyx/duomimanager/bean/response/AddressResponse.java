package com.zyx.duomimanager.bean.response;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;


public class AddressResponse extends BaseResponse {

	public boolean isSuccess;
	
	public AddressResponse() {
		super();
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
