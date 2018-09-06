package com.zyx.duomimanager.bean.response;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.application.UserInfo;
import com.zyx.duomimanager.commication.BaseResponse;


public class LoginResponse extends BaseResponse {

	
	public LoginResponse(){
		super();
	}
	
	
	public void parseResString()
	{
		if (resString != null) {
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONObject jobj = (JSONObject) result.get("result");
				UserInfo p = UserInfo.getInstance();

				try {
					p.customId = (Integer)jobj.get("id");
					p.moblile = (String)jobj.get("mobile");
					p.password = (String)jobj.get("password");
					p.name = (String)jobj.get("name");
					p.score = (Integer) jobj.get("score");
					p.isLogin = true;
					Log.i("ezyx","parse user success");
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("ezyx","parse user failed 1 e:"+e.toString());
					UserInfo.getInstance().isLogin = false;
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx","parse user failed 2 e:"+e.toString());
				UserInfo.getInstance().isLogin = false;
				e.printStackTrace();
			}

		}
	}
	
}
