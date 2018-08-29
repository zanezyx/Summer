package com.bf.duomi.bean.response;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductImage;

public class ModifyAddressResponse extends BaseResponse {

	public boolean isSuccess;
	
	public ModifyAddressResponse() {
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
