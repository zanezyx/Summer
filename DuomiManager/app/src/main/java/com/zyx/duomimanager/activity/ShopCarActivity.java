package com.zyx.duomimanager.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyx.duomimanager.R;
import com.zyx.duomimanager.adapter.ShoppingCarAdapter;
import com.zyx.duomimanager.application.UserInfo;
import com.zyx.duomimanager.bean.request.DeleteShopCarRequest;
import com.zyx.duomimanager.bean.request.GetShoppingCarRequest;
import com.zyx.duomimanager.bean.response.DeleteShopCarResponse;
import com.zyx.duomimanager.bean.response.GetShoppingCarResponse;
import com.zyx.duomimanager.commication.BaseRequest;
import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Cart;
import com.zyx.duomimanager.entity.OrderManager;
import com.zyx.duomimanager.entity.ShopCartManager;
import com.zyx.duomimanager.entity.TOrderToJson;
import com.zyx.duomimanager.service.DeleteShopCarService;
import com.zyx.duomimanager.service.GetShoppingCarService;
import com.zyx.duomimanager.util.AppUtil;
import com.zyx.duomimanager.util.NetListener;

/**
 * 购物车
 * 
 * @author zyx
 * 
 */
public class ShopCarActivity extends BaseActivity {

	private LinearLayout layoutEmptyCart;
	private LinearLayout layoutCartList;
	private RelativeLayout layoutToOrder;

	private ListView shoppingCarList;
	private TextView tvTotalMoney;
	private ShoppingCarAdapter adapter;

	private Context mContext;
	private List<Map<String, Object>> mData;
	private GetShoppingCarResponse getShoppingCarResponse;
	private GetShoppingCarRequest getShoppingCarRequest;
	private GetShoppingCarService getShoppingCarService;

	private DeleteShopCarResponse deleteShopCarResponse;
	private DeleteShopCarRequest deleteShopCarRequest;
	private DeleteShopCarService deleteShopCarService;

	private CheckBox selectAll;
	private Button countbutton;
	private TextView produceTotal, produceMoney, tvEdit;
	private List<Integer> listItemID = new ArrayList<Integer>();
	private int[] produceID;
	private int[] produceNumber;
	private String produceName;
	private Integer totalmoney;
	public int selectIndex;
	public ItemClick mItemClick;
	private boolean isEditingMode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏

		if (!UserInfo.getInstance().isLogin) {
			finish();
			Toast.makeText(ShopCarActivity.this, "您还没有登录，请先登录",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(ShopCarActivity.this, LoginActivity.class);
			ShopCarActivity.this.startActivity(intent);
			return;
		}
		setContentView(R.layout.activity_shop_car);
		isEditingMode = false;
		selectIndex = -1;
		mItemClick = new ItemClick();
		init();
		// forTest();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mData == null) {
			onload();
		}
	}

	/**
	 * 初始化界面
	 */
	public void init() {

		selectAll = (CheckBox) this.findViewById(R.id.selectAll);
		layoutEmptyCart = (LinearLayout) this.findViewById(R.id.ll_empty_car);
		layoutCartList = (LinearLayout) this.findViewById(R.id.ll_cartList);
		layoutToOrder = (RelativeLayout) this.findViewById(R.id.rl_toOrder);
		shoppingCarList = (ListView) this.findViewById(R.id.shoppingcarlist);
		countbutton = (Button) findViewById(R.id.countbutton);
		produceTotal = (TextView) findViewById(R.id.total);
		produceMoney = (TextView) findViewById(R.id.producemoney);
		tvTotalMoney = (TextView) findViewById(R.id.tvTotalMoney);
		tvEdit = (TextView) findViewById(R.id.tvEdit);
		tvEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toEdit(arg0);
			}
		});
		mContext = this;
		setTotalMoneyText();
		countbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mData == null)
					return;
				if (mData.size() == 0)
					return;

				if (checkProductSelected()) {
					getSelectedCarts();
					TOrderToJson orderInfo = OrderManager.getInstance().currOrder;
					orderInfo.productPrice = (int) totalmoney;
					orderInfo.cartList.clear();
					orderInfo.cartList.addAll(ShopCartManager.getInstance().selectCartList);
					Intent intent = new Intent();
					intent.setClass(ShopCarActivity.this,
							EnsureOrderActivity.class);
					ShopCarActivity.this.startActivityForResult(intent, 0);
				} else {
					Toast.makeText(ShopCarActivity.this, "您还没有选择购买商品",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		selectAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					for (int i = 0; i < mData.size(); i++) {
						mData.get(i).put("selected", 1);
					}
				} else {
					for (int i = 0; i < mData.size(); i++) {
						mData.get(i).put("selected", 0);
					}
				}
				adapter = new ShoppingCarAdapter(mContext, mData, isEditingMode);
				shoppingCarList.setAdapter(adapter);
				setTotalMoneyText();
			}
		});
	}

	/**
	 * 列表选择事件
	 * 
	 * @author zyx
	 * 
	 */
	class ItemClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */

	private List<Map<String, Object>> getData(GetShoppingCarResponse res) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (res != null) {
				List<Cart> cartlist = res.shopCartList;
				for (Cart cart : cartlist) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", cart.id);
					map.put("productId", cart.productId);
					map.put("productLogo", cart.productLogo);
					map.put("name", cart.productName);
					map.put("total", cart.amout);
					map.put("price", cart.price);
					map.put("selected", 0);
					list.add(map);
				}
			}
			return list;
		} catch (Exception e) {
			Toast.makeText(ShopCarActivity.this, "获得购物数据错误！",
					Toast.LENGTH_SHORT).show();
		}
		return list;
	}

	/**
	 * 加载数据
	 */
	@SuppressLint("NewApi")
	public void onload() {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			getShoppingCarRequest = new GetShoppingCarRequest();
			// getShoppingCarRequest.setCustomId(Custom.getInstance().id);
			getShoppingCarRequest.setCustomId(UserInfo.getInstance().customId);
			getShoppingCarRequest.setPageNo(1);
			getShoppingCarResponse = new GetShoppingCarResponse();
			getShoppingCarService = new GetShoppingCarService(mContext);
			getShoppingCarService.setRequest(getShoppingCarRequest);
			getShoppingCarService.setResponse(getShoppingCarResponse);
			getShoppingCarService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {

					GetShoppingCarResponse res = (GetShoppingCarResponse) response;
					if (res == null)
						return;
					res.parseResString();
					ShopCartManager.getInstance().allCartList.clear();
					if (res.shopCartList != null) {
						ShopCartManager.getInstance().allCartList
								.addAll(res.shopCartList);
						mData = getData(res);

						if (mData != null) {
							if (mData.size() > 0) {
								layoutEmptyCart.setVisibility(View.GONE);
								layoutCartList.setVisibility(View.VISIBLE);
								layoutToOrder.setVisibility(View.VISIBLE);
								adapter = new ShoppingCarAdapter(mContext,
										mData, isEditingMode);
								shoppingCarList.setAdapter(adapter);
								setTotalMoneyText();
							} else {
								layoutEmptyCart.setVisibility(View.VISIBLE);
								layoutCartList.setVisibility(View.GONE);
								layoutToOrder.setVisibility(View.GONE);
							}
						}
					} else {
						layoutEmptyCart.setVisibility(View.VISIBLE);
						layoutCartList.setVisibility(View.GONE);
						layoutToOrder.setVisibility(View.GONE);
					}

				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(mContext, "获取购物车商品信息失败！");
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
					getShoppingCarResponse = (GetShoppingCarResponse) response;
					getShoppingCarService.setResponse(getShoppingCarResponse);
				}

				@Override
				public void onCancel() {
				}
			});
		} catch (Exception e) {
			Log.e("ShopingCarActivity error!",
					"ShopingCarActivity onload error!");
		}
	}

	/**
	 * 删除购物车信息
	 */
	@SuppressLint("NewApi")
	public void deleteShopCartProduct(final int id) {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			deleteShopCarRequest = new DeleteShopCarRequest();
			deleteShopCarRequest.setId(id);
			deleteShopCarResponse = new DeleteShopCarResponse();
			deleteShopCarService = new DeleteShopCarService(mContext);
			deleteShopCarService.setRequest(deleteShopCarRequest);
			deleteShopCarService.setResponse(deleteShopCarResponse);
			deleteShopCarService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {

					DeleteShopCarResponse res = (DeleteShopCarResponse) response;
					if (res == null)
						return;
					res.parseResString();
					if (res.isSuccess) {
						// mData.remove(selectIndex);
						// selectIndex = -1;
						// AppUtil.showInfoShort(mContext, "获取购物车商品信息成功！");
						adapter = new ShoppingCarAdapter(mContext, mData,
								isEditingMode);
						shoppingCarList.setAdapter(adapter);
						AppUtil.showInfoShort(mContext, "删除购物车商品信息成功！");
						for (int i = 0; i < mData.size(); i++) {
							int pid = (Integer) mData.get(i).get("id");
							if (pid == id) {
								mData.remove(i);
								break;
							}
						}

						ArrayList<Cart> allCartList = ShopCartManager
								.getInstance().allCartList;
						for (int i = 0; i < mData.size(); i++) {
							int pid = allCartList.get(i).id;
							if (pid == id) {
								allCartList.remove(i);
								break;
							}
						}
						adapter.mData = mData;
						// shoppingCarList.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						setTotalMoneyText();
					} else {
						AppUtil.showInfoShort(mContext, "删除购物车商品信息失败！");
					}

				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(mContext, "删除购物车商品信息失败！");
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
			Log.e("ShopingCarActivity error!",
					"ShopingCarActivity onload error!");
		}
	}

	public void toBack(View v) {
		finish();
	}

	public void toRemove(View v) {
		// deleteShopCar();
	}

	public void setTotalMoneyText() {

		if (mData != null) {
			int count = mData.size();
			if (count > 0) {

				totalmoney = 0;
				for (int i = 0; i < count; i++) {
					int selected = (Integer) mData.get(i).get("selected");
					if (selected != 0) {
						int number = (Integer) mData.get(i).get("total");
						Integer price = (Integer) mData.get(i).get("price");
						totalmoney += (number * price);
					}
				}
				tvTotalMoney.setText("￥" + totalmoney);
				return;

			}
		}
		String strTotal1 = String.format("%.2f", 0.00);
		tvTotalMoney.setText("￥" + strTotal1);
	}

	public void refreshListView() {
		shoppingCarList.setAdapter(adapter);
	}

	public void toHomePage(View v) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
	}

	public void toNearBy(View v) {
		Intent intent = new Intent();
		intent.setClass(this, SelectPaymentTypeActivity.class);
		this.startActivity(intent);
	}

	public void toProductClass(View v) {
		Intent intent = new Intent();
		intent.setClass(this, ProductClassActivity.class);
		this.startActivity(intent);
	}

	public void toShopCart(View v) {
		// Intent intent = new Intent();
		// intent.setClass(this,
		// ShopCarActivity.class);
		// this.startActivity(intent);
	}

	public void toPersonCenter(View v) {
		Intent intent = new Intent();
		intent.setClass(this, PersonInfoCenterActivity.class);
		this.startActivity(intent);
	}

	// public void toAddOrder(View v) {
	//
	// TOrderToJson orderInfo = OrderManager.getInstance().currOrder;
	// orderInfo.productPrice = totalmoney;
	// Intent intent = new Intent();
	// intent.setClass(this, EnsureOrderActivity.class);
	// this.startActivity(intent);
	// }

	/**
	 * 获取数据
	 * 
	 * @return
	 */

	private List<Map<String, Object>> getDataForTest() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 12);
		map.put("productLogo", "logo");
		map.put("name", "白菜");
		map.put("number", "123456");
		map.put("total", 2);
		map.put("price", 18.0);
		map.put("selected", 0);
		list.add(map);

		return list;

	}

	void forTest() {
		mData = getDataForTest();
		layoutEmptyCart.setVisibility(View.GONE);
		layoutCartList.setVisibility(View.VISIBLE);
		layoutToOrder.setVisibility(View.VISIBLE);

		if (mData != null) {
			adapter = new ShoppingCarAdapter(mContext, mData, isEditingMode);
			shoppingCarList.setAdapter(adapter);
		}
	}

	public void toEdit(View v) {

		isEditingMode = !isEditingMode;
		if (isEditingMode) {
			tvEdit.setText("完成");
		} else {
			tvEdit.setText("编辑");
		}
		if (mData != null) {
			adapter = new ShoppingCarAdapter(mContext, mData, isEditingMode);
			shoppingCarList.setAdapter(adapter);
		}
	}

	public boolean checkProductSelected() {
		int selectCount = 0;
		if (mData != null) {
			for (int i = 0; i < mData.size(); i++) {
				int selected = (Integer) mData.get(i).get("selected");
				if (selected != 0) {
					selectCount++;
				}
			}
		}
		if (selectCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void getSelectedCarts() {
		if (mData != null) {
			ArrayList<Cart> allCartList = ShopCartManager.getInstance().allCartList;
			ArrayList<Cart> selectCartList = ShopCartManager.getInstance().selectCartList;
			selectCartList.clear();
			for (int i = 0; i < mData.size(); i++) {
				int selected = (Integer) mData.get(i).get("selected");
				if (selected != 0) {
					selectCartList.add(allCartList.get(i));
				}
			}
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			finish();
			Intent intent = new Intent();
			intent.setClass(this, MyOrderListActivity.class);
			this.startActivity(intent);
			break;
		default:
			break;
		}
	}

}
