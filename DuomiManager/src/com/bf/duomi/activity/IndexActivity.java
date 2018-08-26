package com.bf.duomi.activity;


import com.bf.duomimanager.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class IndexActivity extends ActivityGroup {

	// 定义FragmentTabHost对象
	private TabHost mTabHost;
	private RadioGroup mTabRg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.index_activity);
		initView();
	}

	private void initView() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setFocusable(true);
		mTabHost.setup();
		// 此处是我解决异常加的一行代码,如果继承Activity的话可将此行注释
		mTabHost.setup(IndexActivity.this.getLocalActivityManager());

		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// 为每一个Tab按钮设置图标、文字和内容
		TabSpec tabSpec = mTabHost.newTabSpec("").setIndicator("indicator")
				.setContent(intent);
		// 将Tab按钮添加进Tab选项卡中
		mTabHost.addTab(tabSpec);

		intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// 为每一个Tab按钮设置图标、文字和内容
		TabSpec tabSpec1 = mTabHost.newTabSpec("").setIndicator("indicator1")
				.setContent(intent);
		// 将Tab按钮添加进Tab选项卡中
		mTabHost.addTab(tabSpec1);

		intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// 为每一个Tab按钮设置图标、文字和内容
		TabSpec tabSpec2 = mTabHost.newTabSpec("").setIndicator("indicator2")
				.setContent(intent);
		// 将Tab按钮添加进Tab选项卡中
		mTabHost.addTab(tabSpec2);

		intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// 为每一个Tab按钮设置图标、文字和内容
		TabSpec tabSpec3 = mTabHost.newTabSpec("").setIndicator("indicator3")
				.setContent(intent);
		// 将Tab按钮添加进Tab选项卡中
		mTabHost.addTab(tabSpec3);

		intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// 为每一个Tab按钮设置图标、文字和内容
		TabSpec tabSpec4 = mTabHost.newTabSpec("").setIndicator("indicator4")
				.setContent(intent);
		// 将Tab按钮添加进Tab选项卡中
		mTabHost.addTab(tabSpec4);
		mTabHost.setCurrentTab(0);
		mTabRg = (RadioGroup) findViewById(R.id.main_radiogroup);
		mTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				System.out.println("&&&&&&&&&&&&&&&&&&checkedID=" + arg1
						+ "&&&&&&&&&&&&&&&&&&");
				switch (arg1) {
				case R.id.workspace:
					mTabHost.setCurrentTab(0);
					break;
				case R.id.workspace1:
					mTabHost.setCurrentTab(1);
					break;
				case R.id.entertainment2:

					mTabHost.setCurrentTab(2);
					break;
				case R.id.tools3:

					mTabHost.setCurrentTab(3);
					break;

				default:
					break;
				}

			}
		});

	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public void onBackPressed() {
	}

	// @Override
	// public void backEvent() {
	// Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
	// }

}
