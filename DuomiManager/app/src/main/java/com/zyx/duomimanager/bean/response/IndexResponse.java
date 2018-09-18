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
					Product p = new Product();
					JSONObject jobj = (JSONObject)jIndexNewList.get(i);
					p.attentionCount = (Integer)jobj.get("attentionCount");
					p.browseCount = (Integer)jobj.get("browseCount");
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					p.discountPrice = (Double) jobj.get("discountPrice");
					p.freezeStore = (Integer)jobj.get("freezeStore");
					p.id = (Integer) jobj.get("id");
					p.isMarketable = (Integer)jobj.get("isMarketable");
					try {
						Object logo = jobj.get("logo");
						if(logo!=null){
							p.logo = (String)logo;
						}
						Object logoUrl = jobj.get("logoUrl");
						if(logoUrl!=null){
							p.logoUrl = (String)logoUrl;
						}

					} catch (Exception e) {
						// TODO: handle exception
						Log.i("ezyx", "parse news list 1 failed e:"+e.toString());
						e.printStackTrace();
					}
					if(p.logoUrl==null || p.logoUrl.equals("")||p.logoUrl.equalsIgnoreCase("null")){
						p.logoUrl= "/images/product/"+p.id+"/";
						Log.i("ezyx", "parseResString>>>p.logoUrl:"+p.logoUrl);
					}
					p.marketPrice = (Double) jobj.get("marketPrice");
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					//p.productType = (String)jobj.get("productType");
					//p.saleCount = (Integer)jobj.get("saleCount");
					p.store = (Integer)jobj.get("store");
					p.imageCount = (Integer)jobj.get("imageCount");
					recommendProducts.newList.add(p);
				}

				if(recommendProducts.hotSalesList!=null){
					recommendProducts.hotSalesList.clear();
				}
				
				for(int i=0;i<arrayProduct.length();i++)
				{
					Product p = new Product();
					JSONObject jobj = (JSONObject)arrayProduct.get(i);
					p.attentionCount = (Integer)jobj.get("attentionCount");
					p.browseCount = (Integer)jobj.get("browseCount");
					p.code = (String)jobj.get("code");
					p.description = (String)jobj.get("description");
					p.discountPrice = (Double) jobj.get("discountPrice");
					p.freezeStore = (Integer)jobj.get("freezeStore");
					p.id = (Integer) jobj.get("id");
					p.isMarketable = (Integer)jobj.get("isMarketable");
					try {
						Object logo = jobj.get("logo");
						if(logo!=null){
							p.logo = (String)logo;
						}
						Object logoUrl = jobj.get("logoUrl");
						if(logoUrl!=null){
							p.logoUrl = (String)logoUrl;
						}
					} catch (Exception e) {
						// TODO: handle exception
						Log.i("ezyx", "parse news list 1 failed e:"+e.toString());
						e.printStackTrace();
					}
					if(p.logoUrl==null || p.logoUrl.equals("")||p.logoUrl.equalsIgnoreCase("null")){
						p.logoUrl= "/images/product/"+p.id+"/";
						Log.i("ezyx", "parseResString>>>p.logoUrl:"+p.logoUrl);
					}
					p.marketPrice = (Double) jobj.get("marketPrice");
					p.metaKeywords = (String)jobj.get("metaKeywords");
					p.name = (String)jobj.get("name");
					//p.productType = (String)jobj.get("productType");
					//p.saleCount = (Integer)jobj.get("saleCount");
					p.store = (Integer)jobj.get("store");
					p.imageCount = (Integer)jobj.get("imageCount");
					recommendProducts.hotSalesList.add(p);
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




