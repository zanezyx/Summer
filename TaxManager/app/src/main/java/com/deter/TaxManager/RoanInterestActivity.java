package com.deter.TaxManager;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class RoanInterestActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏


	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_loan_interest);
		setTitleText(R.string.interest_of_loans);
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







