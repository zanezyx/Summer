package com.zyx.duomimanager.bean.response;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Custom;
import com.zyx.duomimanager.entity.Wallet;


public class GetUserDetailResponse extends BaseResponse {

	private Custom custom;
	public Wallet wallet;
	
	public GetUserDetailResponse() {
		super();
	}

	public Custom getCustom() {
		return custom;
	}

	public void setCustom(Custom custom) {
		this.custom = custom;
	}
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				custom = Custom.getInstance();
				result = new JSONObject(resString);
				JSONObject jCustom = (JSONObject)result.get("custom");
				
				custom.id = (Integer)jCustom.get("id");
				custom.name = (String)jCustom.get("name");
				custom.password = (String)jCustom.get("password");
				custom.mobile = (String)jCustom.get("mobile");
				custom.gender = (Integer)jCustom.get("gender");
				custom.realyName = (String)jCustom.get("realyName");
				custom.province = (String)jCustom.get("province");
				custom.city = (String)jCustom.get("city");
				custom.country = (String)jCustom.get("country");
				custom.address = (String)jCustom.get("address");
				custom.country = (String)jCustom.get("country");
				custom.logo = (String)jCustom.get("logo");
				custom.level = (Integer)jCustom.get("level");
				custom.state = (Integer)jCustom.get("state");
				custom.cardId = (String)jCustom.get("cardId");
				Log.i("ezyx", "custom parse OK");
				
				
				wallet = Wallet.getInstance();
				JSONObject jWallet = (JSONObject)result.get("wallets");
				wallet.id = (Integer)jWallet.get("id");
				wallet.customId = (Integer)jWallet.get("customId");
				wallet.totalMoney = (Integer)jWallet.get("totalMoney");
				wallet.consumptionMoney = (Integer)jWallet.get("consumptionMoney");
				wallet.availableMoney = (Integer)jWallet.get("availableMoney");
				wallet.lockingMoney = (Integer)jWallet.get("lockingMoney");
				wallet.beans = (Integer)jWallet.get("beans");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
	
}
