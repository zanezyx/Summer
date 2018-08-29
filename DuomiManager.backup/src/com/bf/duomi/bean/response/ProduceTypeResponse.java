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

public class ProduceTypeResponse extends BaseResponse {
	
	private List<ProductType> productTypeList;
	
	public ProduceTypeResponse(){
		super();
	}

	public List<ProductType> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductType> productTypeList) {
		this.productTypeList = productTypeList;
	}
	
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jProductTypeList = (JSONArray)result.get("productTypeList");
				productTypeList = new ArrayList<ProductType>();
				for(int i=0;i<jProductTypeList.length();i++)
				{
					ProductType ptype = new ProductType();
					JSONObject jobj = (JSONObject)jProductTypeList.get(i);
					ptype.id = Integer.valueOf((String)jobj.get("id"));
					ptype.code = (String)jobj.get("code");
					ptype.content =  (String)jobj.get("content");
					ptype.state = Integer.valueOf( (String)jobj.get("state"));
					ptype.name =  (String)jobj.get("name");
					ptype.remark =  (String)jobj.get("remark");
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
