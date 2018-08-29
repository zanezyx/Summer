package com.zyx.duomimanager.bean.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;


public class UserNumResponse extends BaseResponse {

	public int userNum;
	public UserNumResponse(){
		super();
		userNum = 0;
	}
	
	
	public void parseResString()
	{

		if(resString!=null)
		{
			JSONObject result;
			userNum = 0;
			try {
				result = new JSONObject(resString);
				JSONArray jList = (JSONArray)result.get("userlist");
				userNum = jList.length();
				Log.i("ezyx", "parse user num success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse user num failed");
				e.printStackTrace();
			} 
			
		}
	}
	
}
