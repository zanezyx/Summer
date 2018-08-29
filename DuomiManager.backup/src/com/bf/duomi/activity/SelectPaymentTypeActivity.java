package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bf.duomimanager.R;
import com.bf.duomi.activity.ProductClassActivity.itemClick;
import com.bf.duomi.adapter.ProduceTypeAdapter;
import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.PaymentManager;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.service.ProduceTypeService;
import com.bf.duomi.service.SearchProductService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class SelectPaymentTypeActivity extends Activity {
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.payment_type_activity);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	}

	
	public void toBack(View v)
	{
		finish();
	}
	
	
	public void toSetPayCash(View v)
	{
		PaymentManager.getInstance().paymentType=0;
		finish();
	}
	
	
}







