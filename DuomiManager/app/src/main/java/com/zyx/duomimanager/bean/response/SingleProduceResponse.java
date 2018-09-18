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
			JSONObject result;
			try {
				result = new JSONObject(resString);
				JSONObject jProduct = (JSONObject) result.get("product");
				Product p = new Product();
				JSONObject jobj = jProduct;

				try {
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
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				product = p;
				Log.i("ezyx",
						"parse product and product images list success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("ezyx",
						"parse product and product images list failed");
				e.printStackTrace();
			}

		}
	}

}




