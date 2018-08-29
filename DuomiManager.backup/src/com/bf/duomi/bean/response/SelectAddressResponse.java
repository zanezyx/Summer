package com.bf.duomi.bean.response;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.Products;
import com.bf.duomi.entity.ReceiveAddress;

public class SelectAddressResponse extends BaseResponse {

	
	
	public List<ReceiveAddress> addressList;

	public SelectAddressResponse() {
		super();
	}

	public List<ReceiveAddress> getData() {
		return addressList;
	}

	public void setData(List<ReceiveAddress> data) {
		this.addressList = data;
	}


	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jAddressList = (JSONArray)result.get("addressList");
				addressList = new ArrayList<ReceiveAddress>();
				for(int i=0;i<jAddressList.length();i++)
				{
					ReceiveAddress p = new ReceiveAddress();
					JSONObject jobj = (JSONObject)jAddressList.get(i);
					p.id = Integer.valueOf((String)jobj.get("id"));
					p.customId = Integer.valueOf((String)jobj.get("customId"));
					p.province = (String)jobj.get("province");
					p.city = (String)jobj.get("city");
					p.county = (String)jobj.get("county");
					p.address = (String)jobj.get("address");
					p.receiveName = (String)jobj.get("receiveName");
					String name;
					try {
						name = new String(p.receiveName.getBytes(), "utf-8");
						int e = 9;
						e=80;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p.mobile = (String)jobj.get("mobile");
					p.telephone = (String)jobj.get("telephone");
					addressList.add(p);
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



//private Integer ;
//private Integer ;
//private String ;
//private String ;
//private String ;
//private String ;
//private String ;
//private String ;
//private String ;
//private String ;
//private String ;
