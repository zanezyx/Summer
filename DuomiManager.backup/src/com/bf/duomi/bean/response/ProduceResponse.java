package com.bf.duomi.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.Products;

public class ProduceResponse extends BaseResponse {

	public List<Product> productList;
	public List<Product> data;
	public List<String> productTypeNameList;
	
	public ProduceResponse() {
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
					try {
						p.marketPrice = Integer.valueOf((String)jobj.get("marketPrice"));
					} catch (Exception e) {
						// TODO: handle exception
						p.marketPrice = 0;
					}
					
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					p.norm = (String)jobj.get("norm");
					p.productType = Integer.valueOf((String)jobj.get("productType"));
					p.saleCount = Integer.valueOf((String)jobj.get("saleCount"));
					p.store = Integer.valueOf((String)jobj.get("store"));
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
