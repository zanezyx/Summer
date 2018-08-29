package com.zyx.duomimanager.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.ProductSecondary;


public class ProductSecondaryResponse extends BaseResponse {
	
	private List<ProductSecondary> productTypeList;
	
	public ProductSecondaryResponse(){
		super();
	}

	public List<ProductSecondary> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductSecondary> productTypeList) {
		this.productTypeList = productTypeList;
	}
	
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jProductTypeList = (JSONArray)result.get("productSecondarys");
				productTypeList = new ArrayList<ProductSecondary>();
				for(int i=0;i<jProductTypeList.length();i++)
				{
					ProductSecondary ptype = new ProductSecondary();
					JSONObject jobj = (JSONObject)jProductTypeList.get(i);
					ptype.id = (Integer)jobj.get("id");
					ptype.code = (String)jobj.get("code");
					ptype.name =  (String)jobj.get("name");
					ptype.productType =  (Integer)jobj.get("productType");
					productTypeList.add(ptype);
				}

				Log.i("ezyx", "parse main product type list success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse main product type list failed");
				e.printStackTrace();
			} 
			
		}
	}
	
	
	
	
}
