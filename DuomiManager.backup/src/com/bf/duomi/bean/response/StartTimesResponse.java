package com.bf.duomi.bean.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.application.UserInfo;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.TOrderToJson;

public class StartTimesResponse extends BaseResponse {

	public int startTimes = 0;
	public int startPhoneNum = 0;
	
	public StartTimesResponse(){
		super();
	}
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jList = (JSONArray)result.get("timeslist");
				startPhoneNum = jList.length();
				startTimes = 0;
				for(int i=0;i<jList.length();i++)
				{
					JSONObject jobj = (JSONObject)jList.get(i);
					int times = Integer.valueOf((String)jobj.get("times"));
					startTimes += times;
				}

				Log.i("ezyx", "parse start times success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse start times failed");
				e.printStackTrace();
			} 
			
		}
	}
	
}
