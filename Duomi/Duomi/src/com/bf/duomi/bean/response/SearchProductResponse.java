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
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.Products;

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
				JSONArray jProductList = (JSONArray)result.get("productList");
				productList = Products.getInstance().productList;
				productList.clear();
				for(int i=0;i<jProductList.length();i++)
				{
					Product p = new Product();
					JSONObject jobj = (JSONObject)jProductList.get(i);
					p.attentionCount = Integer.valueOf((String)jobj.get("attentionCount"));
					p.browseCount = Integer.valueOf((String)jobj.get("browseCount"));
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					p.discountPrice = Integer.valueOf((String)jobj.get("discountPrice"));
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
					productList.add(p);
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

//05-11 13:26:03.774: I/ezyx(18894): >>>net request  resString ={"result":"没有更多数据","message":"没有更多数据",
//		"productList":["白糯米","荞麦米"]}




