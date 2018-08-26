package com.bf.duomi.activity;


import com.bf.duomimanager.R;
import com.bf.duomi.activity.ChangePasswordActivity;
import com.bf.duomi.activity.LoginActivity;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.ChangePasswordRequest;
import com.bf.duomi.bean.response.ChangePasswordResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.service.ChangePasswordService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

	
	EditText etPassword1;
	EditText etPassword2;
	EditText etPassword3;

	String password1;
	String password2;
	String password3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		
		if (UserInfo.getInstance().isLogin) {
			setContentView(R.layout.change_password_activity);
			init();
		} else {

			finish();
			Toast.makeText(ChangePasswordActivity.this, "您还没有登录，请先登录",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(ChangePasswordActivity.this, LoginActivity.class);
			ChangePasswordActivity.this.startActivity(intent);
			return;
		}

	}
	

	public void toBack(View v) {
		finish();
	}

	void init() {
		etPassword1 = (EditText) findViewById(R.id.etPassword1);
		etPassword2 = (EditText) findViewById(R.id.etPassword2);
		etPassword3 = (EditText) findViewById(R.id.etPassword3);

	}

	/**
	 * 修改密码
	 */
	public void toChangePassword(View v) {

		password1 = etPassword1.getText().toString();
		password2 = etPassword2.getText().toString();
		password3 = etPassword3.getText().toString();
		if (password1 == null || password1.equals("")) {
			AppUtil.showInfoShort(this, "请输入原密码");
			return;
		}
		if (password2 == null || password2.equals("")) {
			AppUtil.showInfoShort(this, "请输入新密码");
			return;
		}
		if (password3 == null || password3.equals("")) {
			AppUtil.showInfoShort(this, "请再次输入新密码");
			return;
		}
		if (!password3.equals(password2)) {
			AppUtil.showInfoShort(this, "两次输入新密码不一致");
			return;
		}

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		ChangePasswordResponse response = new ChangePasswordResponse();
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
				UserInfo.getInstance().customId, password1, password3);
		ChangePasswordService changePasswordService = new ChangePasswordService(
				this);
		changePasswordService.setRequest(changePasswordRequest);
		changePasswordService.setResponse(response);
		changePasswordService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {

				ChangePasswordResponse res = (ChangePasswordResponse) response;
				if (res == null)
					return;
				res.parseResString();
				if (res.isSuccess) {

					UserInfo.getInstance().password = password3;
					SharePreferenceManager.saveUserPassword(UserInfo
							.getInstance().password);
					AppUtil.showInfoShort(ChangePasswordActivity.this, "修改密码成功");
					finish();

				} else {
					AppUtil.showInfoShort(ChangePasswordActivity.this, "修改密码失败");
				}

			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				Log.i("ezyx", ">>>onFailed  修改密码失败");
				AppUtil.showInfoShort(ChangePasswordActivity.this, "修改密码失败");
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
}
