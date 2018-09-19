package com.zyx.duomimanager.bean.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Product;
import com.zyx.duomimanager.entity.ProductType;


public class SingleProduceResponse extends BaseResponse {

	private Product product;
	private ProductType productType;

	public SingleProduceResponse() {
		super();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public void parseResString() {
		if (resString != null) {
			try {
				JSONObject	jsonObject = new JSONObject(resString);
				JSONObject jProduct = (JSONObject) jsonObject.get("result");
				if(jProduct!=null){
					product = Product.parseJson(jProduct);
				}

			}catch (Exception e){
				product = null;
			}

		}
	}

}




