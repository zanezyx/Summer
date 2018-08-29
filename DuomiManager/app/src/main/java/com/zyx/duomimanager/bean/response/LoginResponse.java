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
//		if(resString!=null)
//		{
//			try {
//
//				if(resString.equals(""))
//				{
//					custom.isLogined =false;
//				}else{
//					
//					custom = Custom.getInstance();
//					Integer temp = Integer.valueOf(resString);
//					custom.setId(temp);
//					custom.isLogined =true;
//				}
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
//		}
		
		if (resString != null) {
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONObject jobj = (JSONObject) result.get("user");
				UserInfo p = UserInfo.getInstance();

				try {
					p.customId = Integer.valueOf((String)jobj.get("id"));
					p.moblile = (String)jobj.get("mobile");
					p.password = (String)jobj.get("password");
					p.name = (String)jobj.get("name");
					p.score = Integer.valueOf((String)jobj.get("score"));
					p.isLogin = true;

				} catch (Exception e) {
					// TODO: handle exception
					UserInfo.getInstance().isLogin = false;
					e.printStackTrace();
				}
				Log.i("ezyx","parse user success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx","parse user failed");
				UserInfo.getInstance().isLogin = false;
				e.printStackTrace();
			}

		}
	}
	
}
