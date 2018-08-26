package com.bf.duomi.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.duomi.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.AddOrderRequest;
import com.bf.duomi.bean.response.AddOrderResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.OrderManager;
import com.bf.duomi.entity.PaymentManager;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.ReceiveAddressMgr;
import com.bf.duomi.entity.TOrderToJson;
import com.bf.duomi.service.AddOrderService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;

/**
 * 确定订单
 * 
 * @author zyx
 * 
 */
public class EnsureOrderActivity extends BaseActivity {

	private LinearLayout toselectaddresslinerlayout;
	private TextView produceContent, totalmenoy, selectaddress, allProductPrice;
	private TextView tvProductCount;
	private TextView realAddress;
	private TextView tvPaymentTypeName;
	private EditText etName;
	private EditText etMobile;
	private String produceName;
	private double money;
	private Button pay;
	private Bundle bundle;
	private int addressID;
	private String address = "请选择地址";
	private Context mContext;
	private int[] produceID;
	private int[] produceNumber;
	private String name;
	private String mobile;
	private String receiverName;
	private String receiverMobile;
	private String receiverAddress;
	private AddOrderResponse addOrderResponse;
	private AddOrderRequest addOrderRequest;
	private AddOrderService addOrderService;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
			setContentView(R.layout.activity_ensure_order);
			mContext = this;
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ReceiveAddress ra = ReceiveAddressMgr.getInstance().currUseReceiveAddress;
		if(ra!=null)
		{
			String strAddress = "";
			strAddress+=ra.province;
			strAddress+=ra.city;
			strAddress+=ra.county;
			strAddress+=ra.address;
			strAddress+=" ";
			strAddress+=ra.receiveName;
			strAddress+=" ";
			strAddress+=ra.mobile;
			realAddress.setText(strAddress);
		}
		if(PaymentManager.getInstance().paymentType==0){
			tvPaymentTypeName.setText("货到付款");
		}else if(PaymentManager.getInstance().paymentType==1){
			tvPaymentTypeName.setText("支付宝付款");
		}else if(PaymentManager.getInstance().paymentType==2){
			tvPaymentTypeName.setText("微信付款");
		}else if(PaymentManager.getInstance().paymentType==3){
			tvPaymentTypeName.setText("银行卡付款");
		}
	}
	
	
	
	/**
	 * 初始化界面
	 */
	public void init() {
		try {
			etName = (EditText)findViewById(R.id.etName);
			etMobile = (EditText)findViewById(R.id.etMobile);
			tvProductCount = (TextView)findViewById(R.id.productCount);
			realAddress = (TextView)findViewById(R.id.realAddress);
			totalmenoy = (TextView)findViewById(R.id.totalmenoy);
			allProductPrice = (TextView)findViewById(R.id.productPrice);
			tvPaymentTypeName = (TextView)findViewById(R.id.tvPaymentTypeName);
			TOrderToJson orderInfo = OrderManager.getInstance().currOrder;
			orderInfo.deliveryFee = 0;
			orderInfo.totalPrice = orderInfo.productPrice + orderInfo.deliveryFee;
			totalmenoy.setText("￥" + orderInfo.totalPrice);
			allProductPrice.setText("￥" + orderInfo.productPrice);
			int productCount = 0;
			for(int i=0;i<orderInfo.cartList.size();i++)
			{
				productCount+=orderInfo.cartList.get(i).amout;
			}
			tvProductCount.setText("共"+productCount+"件商品");
		
		} catch (Exception e) {
			Toast.makeText(this, "进入选择确认订单界面失败！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 单击事件
	 * 
	 * @author lenovo
	 * 
	 */
	class onClick implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (arg0 == toselectaddresslinerlayout) {
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(EnsureOrderActivity.this,SelectAddressActivity.class);
				EnsureOrderActivity.this.startActivity(intent);
			}
//			if (arg0 == pay) {
//				try {
//					Bundle bundle = getIntent().getExtras();
//					String address = bundle.getString("name");
//					if (address != null && !"".equals(address)) {
//						addOrder();
//						
//					} else
//						Toast.makeText(EnsureOrderActivity.this, "请选择地址！",
//								Toast.LENGTH_LONG).show();
//
//				} catch (Exception e) {
//					Toast.makeText(mContext, "添加订单失败！", Toast.LENGTH_SHORT)
//							.show();
//				}
//			}

		}

	}

	
	
	
	
	
	/**
	 * 添加订单
	 * 
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public void addOrder() throws Exception {
		getCurrOrderInfo();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		TOrderToJson orderInfo = OrderManager.getInstance().currOrder;
		addOrderRequest = new AddOrderRequest();
		addOrderRequest.setContext(mContext);
		addOrderRequest.setCustomId(orderInfo.customId);
		addOrderRequest.setAddressId(orderInfo.addressId);
		addOrderRequest.setDeliveryFee(orderInfo.deliveryFee);
		addOrderRequest.setDeliveryTypeName(orderInfo.deliveryTypeName);
		addOrderRequest.setPaymentType(orderInfo.paymentType);
		addOrderRequest.setTotalPrice(""+orderInfo.totalPrice);
		addOrderRequest.setName(name);
		addOrderRequest.setMobile(mobile);
		addOrderRequest.setReceiverName(orderInfo.receiverName);
		addOrderRequest.setReceiverMobile(orderInfo.receiverMobile);
		addOrderRequest.setReceiverAddress(orderInfo.receiverAddress);
		
		String cartIds = "";
		for (int i = 0; i < orderInfo.cartList.size(); i++) {
			cartIds += ""+ orderInfo.cartList.get(i).id;
			if(i!=orderInfo.cartList.size()-1)
			{
				cartIds += ".";
			}
		}
		addOrderRequest.setCartIds(cartIds);
		addOrderResponse = new AddOrderResponse();
		addOrderService = new AddOrderService(mContext);
		addOrderService.setRequest(addOrderRequest);
		addOrderService.setResponse(addOrderResponse);
		addOrderService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				
				AddOrderResponse res = (AddOrderResponse)response;
				res.parseResString();
				if(res.isSuccess)
				{
					AppUtil.showInfoShort(mContext, "添加订单成功！");
					Intent intent =getIntent();
					setResult(RESULT_OK, intent);
					finish();
				}else{
					AppUtil.showInfoShort(mContext, "添加订单失败！");
				}	

			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "添加订单失败！");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {
				addOrderResponse = (AddOrderResponse) response;
				addOrderService.setResponse(addOrderResponse);
			}

			@Override
			public void onCancel() {
			}
		});
	}
	
	public void toBack(View v)
	{
		finish();
	}
	
	public void toSelectAddress(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, SelectAddressActivity.class);
		this.startActivity(intent);
	}
	
	public void toViewProducts(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, OrderProductListActivity.class);
		this.startActivity(intent);
	}
	
	
	public void toSubmitOrder(View v)
	{
		try {
			getCurrOrderInfo();
			TOrderToJson order = OrderManager.getInstance().currOrder;
			if(order.addressId==-1 || order.receiverName ==null
					|| order.receiverMobile==null
					|| order.receiverAddress==null)
			{
				AppUtil.showInfoShort(mContext, "请选择收货地址");
				return;
			}
			name = etName.getText().toString();
			mobile = etMobile.getText().toString();
			if(name.equals(""))
			{
				AppUtil.showInfoShort(mContext, "请输入订单人姓名");
				return;
			}
			if(mobile.equals(""))
			{
				AppUtil.showInfoShort(mContext, "请输入订单人手机号码");
				return;
			}
			if(!AppUtil.isMobileNO(mobile))
			{
				AppUtil.showInfoShort(mContext, "请输入正确的手机号码");
				return;
			}
			addOrder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void toSelectPaymentType(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, SelectPaymentTypeActivity.class);
		this.startActivity(intent);
	}
	
	
	void getCurrOrderInfo()
	{
		TOrderToJson orderInfo = OrderManager.getInstance().currOrder;
		ReceiveAddress ra = ReceiveAddressMgr.getInstance().currUseReceiveAddress;
		if(ra!=null)
		{
			orderInfo.addressId = ra.id;
			orderInfo.receiverName = ra.receiveName;
			orderInfo.receiverMobile = ra.mobile;
			orderInfo.receiverAddress = ra.province+ra.city+ra.county+ra.address;
		}else
		{
			orderInfo.addressId = -1;
			orderInfo.receiverName = null;
			orderInfo.receiverMobile = null;
			orderInfo.receiverAddress = null;
		}	
		orderInfo.customId = UserInfo.getInstance().customId;
		orderInfo.deliveryTypeName = "";
		orderInfo.paymentType = 0;
		orderInfo.totalPrice = orderInfo.productPrice + orderInfo.deliveryFee;
	}
	

}
