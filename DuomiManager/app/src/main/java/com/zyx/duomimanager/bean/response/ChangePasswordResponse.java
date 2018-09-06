package com.zyx.duomimanager.bean.response;

import com.zyx.duomimanager.commication.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;



public class ChangePasswordResponse extends BaseResponse {
	
	public boolean isSuccess;
	
	public ChangePasswordResponse(){

		super();
		isSuccess = false;
	}
	
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				String s = (String)result.get("status");
				if(s.equals("ok"))
				{
					isSuccess = true;
				}else{
					isSuccess = false;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
