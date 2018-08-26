package com.bf.duomi.activity;

import com.bf.duomimanager.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.entity.SettingInfo;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.SharePreferenceManager;
import com.bf.duomi.util.UpdateManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.setting_activity);

		try {
			Log.v("RegisterActivity", "Start the register windows!");
			init();
		} catch (Exception e) {
			Log.e("init error", "RegisterActivity init error!", e);
		}

	}

	void init() {
		Button btn = (Button) findViewById(R.id.quitLogin);
		TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			tvVersion.setText(""+version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (UserInfo.getInstance().isLogin) {
			btn.setVisibility(View.VISIBLE);
		} else {
			btn.setVisibility(View.INVISIBLE);
		}
	}

	public void toBack(View v) {
		finish();
	}

	public void toChangePassword(View v) {
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, ChangePasswordActivity.class);
		SettingActivity.this.startActivity(intent);
	}

	public void toQuitLogin(View v) {
		if (UserInfo.getInstance().isLogin) {
			UserInfo.getInstance().isLogin = false;
			AppUtil.showInfoShort(this, "退出登录成功");
			finish();
		}
	}

	public void toAddressManager(View v) {
		Intent intent = new Intent();
		intent.setClass(this, SelectAddressActivity.class);
		this.startActivity(intent);
	}

	public void toContactUs(View v)
	{
		SettingInfo s = SettingInfo.getInstance();
		if(s.serviceMobile!=null)
		{
	        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+s.serviceMobile));  
	        startActivity(intent); 
		}else{
			AppUtil.showInfoShort(this, "获取联系信息失败！");
		}

	}
	
	
	public void toUpdateVersion(View v)
	{
		UpdateManager um = new UpdateManager(this);
		um.checkVersionUpdate(true);
	}
	
}
