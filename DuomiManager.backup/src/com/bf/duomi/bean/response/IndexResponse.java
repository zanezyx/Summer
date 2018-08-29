package com.bf.duomi.bean.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.CreateDate;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.IndexNew;
import com.bf.duomi.entity.News;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.RecommendProducts;
import com.google.gson.JsonObject;

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
					Product p = new Product();
					JSONObject jobj = (JSONObject)jIndexNewList.get(i);
					p.attentionCount = Integer.valueOf((String)jobj.get("attentionCount"));
					p.browseCount = Integer.valueOf((String)jobj.get("browseCount"));
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					try {
						p.discountPrice = Integer.valueOf((String)jobj.get("discountPrice"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					p.freezeStore = Integer.valueOf((String)jobj.get("freezeStore"));
					p.id = Integer.valueOf((String)jobj.get("id"));
					p.isMarketable = Integer.valueOf((String)jobj.get("isMarketable"));
					p.logo = (String)jobj.get("logo");
					p.logoUrl = (String)jobj.get("logoUrl");
					p.marketPrice = Integer.valueOf((String)jobj.get("marketPrice"));
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					p.norm = (String)jobj.get("norm");
					p.productType = Integer.valueOf((String)jobj.get("productType"));
					p.saleCount = Integer.valueOf((String)jobj.get("saleCount"));
					p.store = Integer.valueOf((String)jobj.get("store"));
					p.imageCount = Integer.valueOf((String)jobj.get("imageCount"));
					recommendProducts.newList.add(p);
				}
				

				if(recommendProducts.hotSalesList!=null)
				{
					recommendProducts.hotSalesList.clear();
				}
				
				for(int i=0;i<arrayProduct.length();i++)
				{
					Product p = new Product();
					JSONObject jobj = (JSONObject)arrayProduct.get(i);
					p.attentionCount = Integer.valueOf((String)jobj.get("attentionCount"));
					//p.beansPrice = (Integer)jobj.get("beansPrice");
					p.browseCount = Integer.valueOf((String)jobj.get("browseCount"));
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					try {
						p.discountPrice = Integer.valueOf((String)jobj.get("discountPrice"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					p.freezeStore = Integer.valueOf((String)jobj.get("freezeStore"));
					p.id = Integer.valueOf((String)jobj.get("id"));
					p.isMarketable = Integer.valueOf((String)jobj.get("isMarketable"));
					p.logo = (String)jobj.get("logo");
					p.logoUrl = (String)jobj.get("logoUrl");
					p.marketPrice = Integer.valueOf((String)jobj.get("marketPrice"));
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					p.norm = (String)jobj.get("norm");
					p.productType = Integer.valueOf((String)jobj.get("productType"));
					p.saleCount = Integer.valueOf((String)jobj.get("saleCount"));
					p.store = Integer.valueOf((String)jobj.get("store"));
					p.imageCount = Integer.valueOf((String)jobj.get("imageCount"));
					recommendProducts.hotSalesList.add(p);
				}
				Log.i("ezyx", "parse news list and recommend product list success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse news list and recommend product list failed");
				e.printStackTrace();
			} 
			
		}
	}
	
}




