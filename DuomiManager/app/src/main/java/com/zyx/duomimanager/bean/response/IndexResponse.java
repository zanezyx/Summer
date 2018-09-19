package com.zyx.duomimanager.bean.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.News;
import com.zyx.duomimanager.entity.Product;
import com.zyx.duomimanager.entity.RecommendProducts;


public class IndexResponse extends BaseResponse {
	
	private News news;
	private RecommendProducts recommendProducts;

	
	public IndexResponse(){
		super();
		news = News.getInstance();
		recommendProducts = RecommendProducts.getInstance();
	}


	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jIndexNewList = (JSONArray)result.get("newList");
				JSONArray arrayProduct = (JSONArray)result.get("hotSales");
				
				if(recommendProducts.newList!=null)
				{
					recommendProducts.newList.clear();
				}
				
				for(int i=0;i<jIndexNewList.length();i++)
				{
					JSONObject jobj = (JSONObject)jIndexNewList.get(i);
					Product p = Product.parseJson(jobj);
					if(p!=null){
						recommendProducts.newList.add(p);
					}
				}

				if(recommendProducts.hotSalesList!=null){
					recommendProducts.hotSalesList.clear();
				}
				
				for(int i=0;i<arrayProduct.length();i++)
				{
					JSONObject jobj = (JSONObject)arrayProduct.get(i);
					Product p = Product.parseJson(jobj);
					if(p!=null){
						recommendProducts.hotSalesList.add(p);
					}
				}
				Log.i("ezyx", "parse news list and recommend product list success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse news list and recommend product list failed e:"+e.toString());
				e.printStackTrace();
			}
		}
	}
	
}




