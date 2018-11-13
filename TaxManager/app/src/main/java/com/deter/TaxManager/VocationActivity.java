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
public class VocationActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_vocation);
		setTitleText(R.string.zyzgjy);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initData() {

	}

	/**
	 * 初始化界面
	 */
	public void init() {

	}

    
}







