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
					p.attentionCount = (Integer)jobj.get("attentionCount");
					p.browseCount = (Integer)jobj.get("browseCount");
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					try {
						p.discountPrice = (Double) jobj.get("discountPrice");
					} catch (Exception e) {
						// TODO: handle exception
					}
					p.freezeStore = (Integer)jobj.get("freezeStore");
					p.id = (Integer) jobj.get("id");
					p.isMarketable = (Integer)jobj.get("isMarketable");
					p.logo = (String)jobj.get("logo");
					p.logoUrl = (String)jobj.get("logoUrl");
					p.marketPrice = (Double) jobj.get("marketPrice");
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					p.productType = (String)jobj.get("productType");
					p.saleCount = (Integer)jobj.get("saleCount");
					p.store = (Integer)jobj.get("store");
					p.imageCount = (Integer)jobj.get("imageCount");
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
