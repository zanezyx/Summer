package com.bf.duomi.activity;


import com.bf.duomi.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class RetrivePasswordActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.retrive_password_activity);
//			init();

	}
	
	
	public void toBack(View v)
	{
		finish();
	}
	
	
	public void toGetValideCode(View v)
	{
		
	}
	
}
