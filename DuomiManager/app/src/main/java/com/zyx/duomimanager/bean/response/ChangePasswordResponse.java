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
//			JSONObject result;
//			try {
//				custom = Custom.getInstance();
//				result = new JSONObject(resString);
//				Integer temp = (Integer)result.get("customId");
//				custom.setId(temp);
//				String s = (String)result.get("result");
//				if(s.equals("success"))
//				{
//					custom.isLogined =true;
//				}else{
//					custom.isLogined =false;
//				}
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			if(resString.contains("0"))
			{
				isSuccess = true;
			}else
			{
				isSuccess = false;
			}
		}
	}
	
}
