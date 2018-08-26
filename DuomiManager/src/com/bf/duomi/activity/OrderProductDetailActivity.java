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
import com.bf.duomi.bean.request.OrderDetailRequest;
import com.bf.duomi.bean.request.ProduceRequest;
import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.GetShoppingCarResponse;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.OrderDetailResponse;
import com.bf.duomi.bean.response.ProduceResponse;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Cart;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.OrderItem;
import com.bf.duomi.entity.OrderManager;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.Products;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.TOrderToJson;
import com.bf.duomi.service.OrderDetailService;
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
public class OrderProductDetailActivity extends BaseActivity {
	private Context mContext;
	private ListView productList;
	private List<Map<String, Object>> mData;
	OrderProductListAdapter adapter;
	private OrderDetailResponse orderDetailResponse;
	private OrderDetailRequest orderDetailRequest;
	private OrderDetailService orderDetailService;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.order_product_list_activity);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		init();
		loadData(id);
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

	private List<Map<String, Object>> getData(List<OrderItem> orderItems) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			for (OrderItem orderItem : orderItems) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", orderItem.productId);
				map.put("name", orderItem.productName);
				map.put("price", orderItem.productPrice);
				map.put("count", orderItem.amout);
				map.put("imgURl", orderItem.productImageUrl);
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

	

	/**
	 * 获取验证码
	 */
	@SuppressLint("NewApi")
	public void loadData(int orderId) {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			orderDetailRequest = new OrderDetailRequest();
			orderDetailRequest.setId(orderId);
			orderDetailResponse = new OrderDetailResponse();
			orderDetailService = new OrderDetailService(mContext);
			orderDetailService.setRequest(orderDetailRequest);
			orderDetailService.setResponse(orderDetailResponse);
			orderDetailService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {
					if (orderDetailResponse != null) {
						orderDetailResponse.parseResString();
						mData = getData(orderDetailResponse.orderItems);
						adapter = new OrderProductListAdapter(mContext, mData);
						productList.setAdapter(adapter);
					}
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(mContext, "获取订单详情失败！");
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
				}

				@Override
				public void onCancel() {
				}
			});
		} catch (Exception e) {
			Log.e("MyOrderDetailActivity error!",
					"MyOrderDetailActivity onload error!");
		}
	}
}
