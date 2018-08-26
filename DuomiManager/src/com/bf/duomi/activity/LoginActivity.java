package com.bf.duomi.activity;

import com.bf.duomimanager.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.LoginResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.service.LoginService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 登录界面
 * 
 * @author zyx
 * 
 */
@SuppressLint("NewApi")
public class LoginActivity extends Activity {

	private String userName;
	private String password;

	private EditText userNameView;
	private EditText passwordView;
	private CheckBox cbAutoLogin;
	private LoginService loginService;
	private Context mContext;

	private TextView toregister;
	private LoginResponse loginResponse;

	String sMobile;
	String sPassword;
	boolean  isAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.login_activity);
		isAutoLogin = SharePreferenceManager.isAutoLogin();
		sMobile = SharePreferenceManager.getUserId();
		sPassword = SharePreferenceManager.getUserPassword();
		try {
			Log.v("RegisterActivity", "Start the register windows!");
			init();
		} catch (Exception e) {
			Log.e("init error", "RegisterActivity init error!", e);
		}
	}

	/**
	 * 初始化界面
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		cbAutoLogin = (CheckBox) findViewById(R.id.cbAutoLogin);
		userNameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		if (sMobile != null) {
			userNameView.setText(sMobile);
		}
		if (sPassword != null) {
			passwordView.setText(sPassword);
		}
		toregister = (TextView) findViewById(R.id.toregister);

		passwordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							login();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						login();
					}
				});

		toregister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
		
		if(isAutoLogin)
		{
			cbAutoLogin.setChecked(true);
		}else{
			cbAutoLogin.setChecked(false);
		}
		cbAutoLogin.setOnCheckedChangeListener(listener);
	}

	/**
	 * 登陆事件
	 */
	public void login() {
		userNameView.setError(null);
		passwordView.setError(null);
		userName = userNameView.getText().toString();
		password = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// 判断用户名是否为空
		if (TextUtils.isEmpty(userName)) {
			userNameView.setError(getString(R.string.error_field_required));
			focusView = userNameView;
			cancel = true;
		}
		// 判断密码是否为空
		if (TextUtils.isEmpty(password)) {
			passwordView.setError(getString(R.string.error_field_required));
			focusView = passwordView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			if(!AppUtil.isMobileNO(userName)){
				AppUtil.showInfoShort(mContext, "请输入正确的手机号码");
				return;
			}
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			LoginResponse response = new LoginResponse();
			LoginRequest loginRequest = new LoginRequest(userName, password);
			loginService = new LoginService(mContext);
			loginService.setRequest(loginRequest);
			loginService.setResponse(response);
			loginService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {

					LoginResponse res = (LoginResponse) response;
					if (res == null)
						return;
					res.parseResString();
					if (UserInfo.getInstance().isLogin) {
						finish();
						Log.i("ezyx", "login success user id = "
								+ UserInfo.getInstance().customId);
						SharePreferenceManager.saveUserId(UserInfo
								.getInstance().moblile);
						SharePreferenceManager.saveUserPassword(UserInfo
								.getInstance().password);
						if (cbAutoLogin.isChecked()) {
							SharePreferenceManager.saveUserAutoLogin(true);
						} else {
							SharePreferenceManager.saveUserAutoLogin(false);
						}

						AppUtil.showInfoShort(mContext, "登录成功");
					} else {
						AppUtil.showInfoShort(mContext, "登录失败");
						
					}

				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					Log.i("ezyx", ">>>onFailed  登录失败");
					AppUtil.showInfoShort(mContext, "登录失败");
					UserInfo.getInstance().isLogin = false;
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
					loginResponse = (LoginResponse) response;
					loginService.setResponse(loginResponse);
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}

	public void toBack(View v) {
		finish();
	}

	public void toRegister(View v) {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	public void toRetrivePassword(View v) {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RetrivePasswordActivity.class);
		startActivity(intent);
	}

	CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			CheckBox checkBox = (CheckBox) buttonView;
			if(checkBox.isChecked())
			{
				SharePreferenceManager.saveUserAutoLogin(true);
			}else{
				SharePreferenceManager.saveUserAutoLogin(false);
			}

		}

	};
}
