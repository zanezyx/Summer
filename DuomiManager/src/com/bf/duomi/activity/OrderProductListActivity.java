package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.duomimanager.R;
import com.bf.duomi.adapter.OrderProductListAdapter;
import com.bf.duomi.adapter.ProduceTypeAdapter;
import com.bf.duomi.adapter.ProductAdapter;
import com.bf.duomi.bean.request.ProduceRequest;
import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.GetShoppingCarResponse;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.ProduceResponse;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Cart;
import com.bf.duomi.entity.OrderManager;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.Products;
import com.bf.duomi.entity.TOrderToJson;
import com.bf.duomi.service.ProduceService;
import com.bf.duomi.service.ProduceTypeService;
import com.bf.duomi.service.SearchProductService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.DialogUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;

/**
 * 订单商品列表
 * 
 * @author zyx
 * 
 */
public class OrderProductListActivity extends BaseActivity {
	private Context mContext;
	private ListView productList;
	private List<Map<String, Object>> mData;
	OrderProductListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.order_product_list_activity);
		init();
		// load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/**
	 * 初始化界面
	 */
	public void init() {

		productList = (ListView) this.findViewById(R.id.productlist);
		// producetypeList.setOnItemClickListener(new itemClick());
		mData = getData();
		adapter = new OrderProductListAdapter(mContext, mData);
		productList.setAdapter(adapter);

	}

	/**
	 * 列表选择事件
	 * 
	 * @author zyx
	 * 
	 */
	// class itemClick implements ListView.OnItemClickListener {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Map<String, Object> producetype = mData.get(arg2);
	// int id = (Integer) producetype.get("id");
	//
	// adapter.selectIndex = arg2;
	// productList.setAdapter(adapter);
	// try {
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }


	
	/**
	 * 获取数据
	 * 
	 * @return
	 */

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			List<Cart> cartlist = OrderManager.getInstance().currOrder.cartList;;
			for (Cart cart : cartlist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", cart.productId);
				map.put("name", cart.productName);
				map.put("price", cart.price);
				map.put("count", cart.amout);
				map.put("imgURl", cart.productLogo);
				list.add(map);
			}

			return list;
		} catch (Exception e) {
			Toast.makeText(this, "获得购物数据错误！",Toast.LENGTH_SHORT).show();
		}
		return list;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getDataForTest() {
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;

			map = new HashMap<String, Object>();
			map.put("id", 1);
			map.put("name", "白菜");
			map.put("price", "30.3");
			map.put("count", "50");
			map.put("imgURl", "image:");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("id", 1);
			map.put("name", "白菜");
			map.put("price", "30.3");
			map.put("count", "50");
			map.put("imgURl", "image:");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("id", 1);
			map.put("name", "白菜");
			map.put("price", "30.3");
			map.put("count", "50");
			map.put("imgURl", "image:");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("id", 1);
			map.put("name", "白菜");
			map.put("price", "30.3");
			map.put("count", "50");
			map.put("imgURl", "image:");
			list.add(map);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void toBack(View v) {
		finish();
	}

}
