package com.bf.duomi.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.CreateDate;
import com.bf.duomi.entity.IndexNew;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductImage;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.SettingInfo;

public class SettingInfoResponse extends BaseResponse {


	public SettingInfoResponse() {
		super();
	}


	public void parseResString() {
		if (resString != null) {
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONObject jSettingInfo = (JSONObject) result.get("settingInfo");
				SettingInfo p = SettingInfo.getInstance();

				try {
					p.latestVersionCode = (String)jSettingInfo.get("latestVersionCode");
					p.serviceMobile = (String)jSettingInfo.get("serviceMobile");
					p.latestVersionURL = (String)jSettingInfo.get("latestVersionURL");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Log.i("ezyx","parse setting info success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx","parse setting info failed");
				e.printStackTrace();
			}

		}
	}

}




