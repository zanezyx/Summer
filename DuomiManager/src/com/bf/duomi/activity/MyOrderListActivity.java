package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bf.duomimanager.R;
import com.bf.duomi.adapter.OneMonthAdapter;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.OneMonthRequest;
import com.bf.duomi.bean.response.OneMonthResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.TOrderToJson;
import com.bf.duomi.service.OneMonthService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyOrderListActivity extends Activity {

	Button btnUncompleteOrder;
	Button btnCompletedOrder;
	Button btnCancelOrder;

	private List<Map<String, Object>> mData1;
	private List<Map<String, Object>> mData2;
	private List<Map<String, Object>> mData3;
	private ListView lvUnCompletedOrder;
	private ListView lvCompletedOrder;
	private ListView lvCancelOrder;
	private Context context;

	private OneMonthResponse oneMonthResponse;
	private OneMonthRequest oneMonthRequest;
	private OneMonthService oneMonthService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		if (!UserInfo.getInstance().isLogin) {
			finish();
			Toast.makeText(this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivity(intent);
			return;
		}
		setContentView(R.layout.my_order_activity);
		context = this;
		init();
		// forTest();
		loadData();
	}

	void init() {
		btnCompletedOrder = (Button) findViewById(R.id.btCompletedOrder);
		btnUncompleteOrder = (Button) findViewById(R.id.btnUncompleteOrder);
		btnCancelOrder = (Button) findViewById(R.id.btnCanceledOrder);

		btnUncompleteOrder.setBackgroundResource(R.drawable.my_order_btn_bg2);
		btnCompletedOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnCancelOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnUncompleteOrder.setTextColor(Color.WHITE);
		btnCompletedOrder.setTextColor(getResources().getColor(
				R.color.greencolor));
		btnCancelOrder
				.setTextColor(getResources().getColor(R.color.greencolor));

		lvCompletedOrder = (ListView) findViewById(R.id.completedOrderList);
		lvUnCompletedOrder = (ListView) findViewById(R.id.uncompleteOrderList);
		lvCancelOrder = (ListView) findViewById(R.id.canceledOrderList);
		lvUnCompletedOrder.setVisibility(View.VISIBLE);
		lvCompletedOrder.setVisibility(View.INVISIBLE);
		lvCancelOrder.setVisibility(View.INVISIBLE);
	}

	public void toBack(View v) {
		finish();
	}

	public void toRemove(View v) {

	}

	public void clickUncompletedOrderButton(View v) {
		btnUncompleteOrder.setBackgroundResource(R.drawable.my_order_btn_bg2);
		btnCompletedOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnCancelOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnUncompleteOrder.setTextColor(Color.WHITE);
		btnCompletedOrder.setTextColor(getResources().getColor(
				R.color.greencolor));
		btnCancelOrder
				.setTextColor(getResources().getColor(R.color.greencolor));
		lvUnCompletedOrder.setVisibility(View.VISIBLE);
		lvCompletedOrder.setVisibility(View.INVISIBLE);
		lvCancelOrder.setVisibility(View.INVISIBLE);
	}

	public void clickCompletedOrderButton(View v) {
		btnUncompleteOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnCompletedOrder.setBackgroundResource(R.drawable.my_order_btn_bg2);
		btnCancelOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);

		btnUncompleteOrder.setTextColor(getResources().getColor(
				R.color.greencolor));
		btnCompletedOrder.setTextColor(Color.WHITE);
		btnCancelOrder
				.setTextColor(getResources().getColor(R.color.greencolor));
		lvUnCompletedOrder.setVisibility(View.INVISIBLE);
		lvCompletedOrder.setVisibility(View.VISIBLE);
		lvCancelOrder.setVisibility(View.INVISIBLE);
	}

	public void clickCanceledOrderButton(View v) {
		btnUncompleteOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnCompletedOrder.setBackgroundResource(R.drawable.my_order_btn_bg1);
		btnCancelOrder.setBackgroundResource(R.drawable.my_order_btn_bg2);
		btnUncompleteOrder.setTextColor(getResources().getColor(
				R.color.greencolor));
		btnCompletedOrder.setTextColor(getResources().getColor(
				R.color.greencolor));
		btnCancelOrder.setTextColor(Color.WHITE);
		lvUnCompletedOrder.setVisibility(View.INVISIBLE);
		lvCompletedOrder.setVisibility(View.INVISIBLE);
		lvCancelOrder.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private void getData() {
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();

		if (oneMonthResponse != null && oneMonthResponse.getData() != null
				&& oneMonthResponse.getData().size() > 0) {
			List<TOrderToJson> orderlist = oneMonthResponse.getData();
			for (TOrderToJson order : orderlist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderId", order.getId());
				map.put("productName", order.getOrderRemark());
				map.put("orderMoney", "￥" + order.getTotalPrice());
				map.put("name", "订单人姓名：" + order.name);
				map.put("mobile", "订单人手机号码：" + order.mobile);

				map.put("receiverName", "收货人姓名：" + order.receiverName);
				map.put("receiverMobile", "收货人手机号码：" + order.receiverMobile);
				map.put("receiverAddress", "收货人地址：" + order.receiverAddress);

				map.put("orderDate", "下单时间： " + order.publishDate);
				map.put("deliveryState", order.deliveryState);
				map.put("paymentType", order.paymentType);
				map.put("orderStatus", order.orderStatus);
				if (order.orderStatus == 0) {
					list1.add(map);
				} else if (order.orderStatus == 1) {
					list2.add(map);
				} else {
					list3.add(map);
				}

			}
			mData1 = list1;
			mData2 = list2;
			mData3 = list3;
		}

	}

	/**
	 * 通过网络获取数据
	 */

	@SuppressLint("NewApi")
	public void loadData() {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			oneMonthRequest = new OneMonthRequest();
			oneMonthRequest.setCustomId(UserInfo.getInstance().customId);
			oneMonthResponse = new OneMonthResponse();
			oneMonthService = new OneMonthService(this);
			oneMonthService.setRequest(oneMonthRequest);
			oneMonthService.setResponse(oneMonthResponse);
			oneMonthService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {

					OneMonthResponse res = (OneMonthResponse) response;
					res.parseResString();
					getData();
					if(mData1!=null)
					{
						Collections.reverse(mData1);
					}
					if(mData2!=null)
					{
						Collections.reverse(mData2);
					}
					if(mData3!=null)
					{
						Collections.reverse(mData3);
					}
					
					OneMonthAdapter adapter1 = new OneMonthAdapter(
							MyOrderListActivity.this, mData1);
					lvUnCompletedOrder.setAdapter(adapter1);

					OneMonthAdapter adapter2 = new OneMonthAdapter(
							MyOrderListActivity.this, mData2);
					lvCompletedOrder.setAdapter(adapter2);

					OneMonthAdapter adapter3 = new OneMonthAdapter(
							MyOrderListActivity.this, mData3);
					lvCancelOrder.setAdapter(adapter3);
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(context, "获取订单列表失败！");
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
			Log.e("oneMonthService error!", "oneMonthService onload error!");
		}
	}

	void forTest() {
		// mData = getDataForTest();
		// OneMonthAdapter adapter = new
		// OneMonthAdapter(MyOrderListActivity.this,
		// mData);
		// lvCompletedOrder.setAdapter(adapter);
	}

	/**
	 * 测试数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getDataForTest() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", 28);
		map.put("number", "编号： " + "12354");
		map.put("orderMoney", "￥： " + 208.00);
		map.put("productName", "苹果");
		map.put("orderDate", "2015-04-09");
		String state = "待处理";
		switch (0) {
		case 0:
			state = "等待付款";
			break;
		case 1:
			state = "待处理";
			break;
		case 2:
			state = "处理中";
			break;
		case 3:
			state = "完成";
			break;
		case 4:
			state = "作废";
			break;
		}
		map.put("orderstate", state);
		list.add(map);
		return list;

	}

	public void updateCancelOrderResult(int id) {
		int i = 0;
		for (i = 0; i < mData1.size(); i++) {
			int orderId = (Integer) mData1.get(i).get("orderId");
			if (orderId == id) {
				break;
			}
		}
		Map<String, Object> map = mData1.get(i);
		map.put("orderStatus", 2);
		mData3.add(map);
		mData1.remove(i);
		OneMonthAdapter adapter1 = new OneMonthAdapter(
				MyOrderListActivity.this, mData1);
		lvUnCompletedOrder.setAdapter(adapter1);

		OneMonthAdapter adapter3 = new OneMonthAdapter(
				MyOrderListActivity.this, mData3);
		lvCancelOrder.setAdapter(adapter3);
	}

//	@Override
//	protected Dialog onCreateDialog(int id)
//
//	{
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setMessage("Are you sure you want to exit?")
//		.setCancelable(false)
//		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//			}
//		})
//		.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int id) {
//				dialog.cancel();
//			}
//		});
//
//		AlertDialog alert = builder.create();
//		return alert;
//	}
}
