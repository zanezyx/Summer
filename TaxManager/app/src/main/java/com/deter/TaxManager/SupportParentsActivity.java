package com.deter.TaxManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.deter.TaxManager.R;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class SupportParentsActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_support_parents);
		setTitleText(R.string.zylr);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initData() {
		init();
	}

	/**
	 * 初始化界面
	 */
	public void init() {

	}

    
}







