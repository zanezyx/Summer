package com.zyx.duomimanager.activity;

import java.util.HashMap;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.zyx.duomimanager.R;
import com.zyx.duomimanager.bean.request.AllOrdersRequest;
import com.zyx.duomimanager.bean.request.StartTimesRequest;
import com.zyx.duomimanager.bean.request.UserNumRequest;
import com.zyx.duomimanager.bean.response.AllOrdersResponse;
import com.zyx.duomimanager.bean.response.StartTimesResponse;
import com.zyx.duomimanager.bean.response.UserNumResponse;
import com.zyx.duomimanager.commication.BaseRequest;
import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.service.AllOrdersService;
import com.zyx.duomimanager.service.StartTimesService;
import com.zyx.duomimanager.service.UserNumService;
import com.zyx.duomimanager.util.NetListener;

public class FirstActivity extends Activity{
	
	TextView tvTimes;
	TextView tvPhoneNum;
	TextView tvUserNum;
	TextView tvOrderNum;
	EditText etProductId;
	EditText etMobile;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.first_activity);
		init();
		
	}
	
	void init()
	{
		tvTimes = (TextView)findViewById(R.id.tvQueryTimes);
		tvPhoneNum = (TextView)findViewById(R.id.tvQueryPhoneNum);
		tvUserNum = (TextView)findViewById(R.id.tvQueryUserNum);
		tvOrderNum = (TextView)findViewById(R.id.tvQueryOrderNum);
		etProductId = (EditText)findViewById(R.id.etQueryProduct); 
		etMobile = (EditText)findViewById(R.id.etMobile); 
	}
	
	

	public void toQueryStartTimes(View v) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		StartTimesResponse response = new StartTimesResponse();
		StartTimesRequest indexRequest = new StartTimesRequest();
		StartTimesService indexService = new StartTimesService(this);
		indexService.setRequest(indexRequest);
		indexService.setResponse(response);
		indexService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				if (response == null)
					return;
				StartTimesResponse res = (StartTimesResponse) response;
				res.parseResString();
				tvTimes.setText(""+res.startTimes);
				tvPhoneNum.setText(""+res.startPhoneNum);

			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {

			}

			@Override
			public void onCancel() {
			}
		});
	}
	
	
	public void toQueryUserNum(View v) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		UserNumResponse response = new UserNumResponse();
		UserNumRequest indexRequest = new UserNumRequest();
		UserNumService indexService = new UserNumService(this);
		indexService.setRequest(indexRequest);
		indexService.setResponse(response);
		indexService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				if (response == null)
					return;
				UserNumResponse res = (UserNumResponse) response;
				res.parseResString();
				tvUserNum.setText(""+res.userNum);
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {

			}

			@Override
			public void onCancel() {
			}
		});
	}
	


	public void toQueryOrderNum(View v) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		AllOrdersResponse response = new AllOrdersResponse();
		AllOrdersRequest indexRequest = new AllOrdersRequest();
		AllOrdersService indexService = new AllOrdersService(this);
		indexService.setRequest(indexRequest);
		indexService.setResponse(response);
		indexService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				if (response == null)
					return;
				AllOrdersResponse res = (AllOrdersResponse) response;
				res.parseResString();
				tvOrderNum.setText(""+res.orderNum);
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {

			}

			@Override
			public void onCancel() {
			}
		});
	}

	public void toQueryAllOrder(View v) {
		Intent intent = new Intent();
		intent.setClass(this, AllOrderListActivity.class);
		this.startActivity(intent);
	}
	

	public void toQueryOrderByUser(View v) {
		String mobile = etMobile.getText().toString();

		if(mobile==null || mobile.equals(""))
			return;
		
		Bundle bundle = new Bundle();
		bundle.putString("mobile", mobile);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this, UserOrderListActivity.class);
		this.startActivity(intent);
	}
	
	
	
	
	/**
	 * 查询产品
	 * 
	 * @throws Exception
	 */
	public void toQueryProduct(View v) {
		String sid = etProductId.getText().toString();
		Integer id = null;
		try {
			id = Integer.valueOf(sid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(id==null)
			return;
		Bundle bundle = new Bundle();
		bundle.putInt("id", id.intValue());
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this, ProductDetailActivity.class);
		this.startActivity(intent);
		
	}
	
	
	

	
}
