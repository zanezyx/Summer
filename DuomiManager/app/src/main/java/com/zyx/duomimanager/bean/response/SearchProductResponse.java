package com.zyx.duomimanager.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Product;
import com.zyx.duomimanager.entity.Products;


public class SearchProductResponse extends BaseResponse {
	
	public List<Product> productList;
	
	public SearchProductResponse(){
		super();
	}

	
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jProductList = (JSONArray)result.get("result");
				productList = Products.getInstance().productList;
				productList.clear();
				for(int i=0;i<jProductList.length();i++)
				{
					JSONObject jobj = (JSONObject)jProductList.get(i);
					if(jobj!=null){
						Product p = Product.parseJson(jobj);
						productList.add(p);
					}
				}
				Log.i("ezyx", "parse main product list success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse main product list failed");
				e.printStackTrace();
			} 
			
		}
	}
}





