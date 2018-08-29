package com.zyx.duomimanager.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Cart;


public class GetShoppingCarResponse extends BaseResponse {

	public ArrayList<Cart> shopCartList;

	public GetShoppingCarResponse() {
		super();
	}
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				shopCartList = new ArrayList<Cart>();
				JSONArray jCartList = (JSONArray)result.get("cartItems");
				int count = jCartList.length();
				for(int i=0;i<count;i++)
				{
					JSONObject jCart = (JSONObject)jCartList.get(i);
					Cart cart = new Cart();
					cart.id = Integer.valueOf((String)jCart.get("id"));
					cart.productId = Integer.valueOf((String)jCart.get("productId"));
					cart.amout = Integer.valueOf((String)jCart.get("amout"));
					cart.customId = Integer.valueOf((String)jCart.get("customId"));
					cart.customMobile = (String)jCart.get("customMobile");
					cart.productName = (String)jCart.get("productName");
					cart.price = Integer.valueOf((String)jCart.get("price"));
					cart.productLogo = (String)jCart.get("productLogo");
					shopCartList.add(cart);
				}

				Log.i("ezyx", "parse shop cart list OK");
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("ezyx", "parse shop cart list failed");
			} 
			
		}
	}
}
