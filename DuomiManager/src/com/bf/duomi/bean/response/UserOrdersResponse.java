package com.bf.duomi.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.Products;
import com.bf.duomi.entity.TOrderToJson;

public class UserOrdersResponse extends BaseResponse {

	public int orderNum;
	private List<TOrderToJson> orderList;

	public UserOrdersResponse() {
		super();
		orderNum = 0;
	}

	public List<TOrderToJson> getData() {
		return orderList;
	}

	public void setData(List<TOrderToJson> data) {
		this.orderList = data;
	}

	
	
	
	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			orderNum = 0;
			try {
				result = new JSONObject(resString);
				JSONArray jOrderList = (JSONArray)result.get("orderList");
				orderList = new ArrayList<TOrderToJson>();
				orderNum = jOrderList.length(); 
				for(int i=0;i<jOrderList.length();i++)
				{
					TOrderToJson p = new TOrderToJson();
					JSONObject jobj = (JSONObject)jOrderList.get(i);
					p.id = Integer.valueOf((String)jobj.get("id"));
					p.deliverySN = (String)jobj.get("deliverySN");
					p.orderStatus = Integer.valueOf((String)jobj.get("orderStatus"));
					p.paymentStatus = Integer.valueOf((String)jobj.get("paymentStatus"));
					p.deliveryTypeName = (String)jobj.get("deliveryTypeName");
					p.paymentType = Integer.valueOf((String)jobj.get("paymentType"));
					p.deliveryFee = Integer.valueOf((String)jobj.get("deliveryFee"));
					p.orderRemark = (String)jobj.get("orderRemark");
					p.publishDate = (String)jobj.get("publishDate");
					p.totalPrice = Integer.valueOf((String)jobj.get("totalPrice"));
					p.addressId = Integer.valueOf((String)jobj.get("addressId"));
					p.customId = Integer.valueOf((String)jobj.get("customId"));
					p.deliveryState = Integer.valueOf((String)jobj.get("deliveryState"));
					p.name = (String)jobj.get("name");
					p.mobile = (String)jobj.get("mobile");
					p.receiverAddress = (String)jobj.get("receiverAddress");
					p.receiverMobile = (String)jobj.get("receiverMobile");
					p.receiverName = (String)jobj.get("receiverName");
					orderList.add(p);
				}

				Log.i("ezyx", "parse main product list success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse main product list failed");
				e.printStackTrace();
			} 
			
		}
	}
	
}
