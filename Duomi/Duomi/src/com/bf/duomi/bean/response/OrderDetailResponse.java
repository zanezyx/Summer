package com.bf.duomi.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Cart;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.OrderItem;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.Products;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.TOrderToJson;

public class OrderDetailResponse extends BaseResponse {

	public List<OrderItem> orderItems;

	public OrderDetailResponse() {
		super();
	}

	public void parseResString()
	{
		if(resString!=null)
		{
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONArray jProductList = (JSONArray)result.get("orderItems");
				orderItems = new ArrayList<OrderItem>();;
				for(int i=0;i<jProductList.length();i++)
				{
					OrderItem p = new OrderItem();
					JSONObject jobj = (JSONObject)jProductList.get(i);
					p.id = Integer.valueOf((String)jobj.get("id"));
					p.productId = Integer.valueOf((String)jobj.get("productId"));
					p.productName = (String)jobj.get("productName");
					p.productImageUrl = (String)jobj.get("productImageUrl");
					p.amout = Integer.valueOf((String)jobj.get("amout"));
					p.orderId = Integer.valueOf((String)jobj.get("orderId"));
					p.productPrice = Integer.valueOf((String)jobj.get("productPrice"));
					orderItems.add(p);
				}

				Log.i("ezyx", "parse main orderItem list success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("ezyx", "parse main orderItem list failed");
				e.printStackTrace();
			} 
			
		}
	}



}
