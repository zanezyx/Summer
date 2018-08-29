package com.bf.duomi.activity;

import com.bf.duomimanager.R;
import com.bf.duomi.bean.request.PaymentRequest;
import com.bf.duomi.bean.response.PaymentResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.service.PaymentService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 支付界面
 * 
 * @author bhl
 * 
 */
public class PaymentActivity extends BaseActivity {

	private Context mContext;
	private int orderId;
	private PaymentResponse paymentResponse;
	private PaymentRequest paymentRequest;
	private PaymentService paymentService;

	private Button countbutton;	
	private TextView totalmoney;
	
	private double money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.activity_payment);
		init();
	}

	/**
	 * 初始化界面
	 */
	public void init() {
		Bundle bundle = this.getIntent().getExtras();
		orderId = bundle.getInt("orderID");
		money = bundle.getDouble("money");
		countbutton = (Button) findViewById(R.id.countbutton);
		totalmoney = (TextView) findViewById(R.id.totalmoney);
		totalmoney.setText(String.valueOf(money));
		mContext = this;
		countbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toSubmit();
			}
		});
	}

	/**
	 * 提交支付
	 */
	@SuppressLint("NewApi")
	public void toSubmit() {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			paymentRequest = new PaymentRequest();
			paymentRequest.setCustomId(1);
			paymentRequest.setId(orderId);
			paymentService = new PaymentService(mContext);
			paymentService.setRequest(paymentRequest);
			paymentService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {
					AppUtil.showInfoShort(mContext, "恭喜您，订单支付成功！");
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(mContext, "订单支付失败！");
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
					paymentResponse = (PaymentResponse) response;
					paymentService.setResponse(paymentResponse);
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
}
