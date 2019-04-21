package com.deter.TaxManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.deter.TaxManager.utils.ToastUtils;


/**
 * 注册界面
 * @author zyx
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText mobile;
	private Button submit;
	private ImageView getvalidecode;
	private EditText valideCode;
	private EditText etPassword;
	private EditText etPasswordAgain;
	
	private Context mContext;
//	private GetMessageCodeResponse getMessageCodeResponse;
//	private GetMessageCodeRequest gtMessageCodeRequest;
//	private GetMessageCodeService getMessageCodeService;

	private String sMobile;
	private String sPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
		mContext = this;
	}

	@Override
	public void initView() {
		setContentView(R.layout.register_activity);
	}

	@Override
	public void initData() {
		init();
	}

	/**
	 * 界面初始化
	 */
	public void init() {
		try {
			mobile = (EditText) this.findViewById(R.id.mobile);
			submit = (Button) this.findViewById(R.id.submit);
			etPassword = (EditText) this.findViewById(R.id.etPassword);
			etPasswordAgain = (EditText) this.findViewById(R.id.etPasswordAgain);
			valideCode = (EditText) this.findViewById(R.id.validecode);
			getvalidecode = (ImageView) findViewById(R.id.getvalidecode);
			submit.setOnClickListener(new OnClick());
			getvalidecode.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					getMessageCode();
				}
			});

		} catch (Exception e) {
			Log.e("init", "register error!");
			Toast.makeText(this, "register", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 获取验证码
	 */
	@SuppressLint("NewApi")
	public void getMessageCode() {
		if (mobile.getText().toString() == null || "".equals(mobile.getText().toString())) {
//			AppUtil.showInfoShort(mContext, "请先输入手机号码！");
			return;
		} 

		try {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//					.detectDiskReads().detectDiskWrites().detectNetwork()
//					.penaltyLog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
//					.penaltyLog().penaltyDeath().build());
//			gtMessageCodeRequest = new GetMessageCodeRequest();
//			gtMessageCodeRequest.setMobile(mobile.getText().toString());
//			getMessageCodeService = new GetMessageCodeService(mContext);
//			getMessageCodeService.setRequest(gtMessageCodeRequest);
//			getMessageCodeService.request(new NetListener() {
//				@Override
//				public void onPrepare() {
//				}
//
//				@Override
//				public void onLoading() {
//				}
//
//				@Override
//				public void onLoadSuccess(BaseResponse response) {
//					AppUtil.showInfoShort(mContext, "验证码获取成功！");
//				}
//
//				@Override
//				public void onFailed(Exception ex, BaseResponse response) {
//					AppUtil.showInfoShort(mContext, "验证码获取失败！");
//				}
//
//				@Override
//				public void onComplete(String respondCode, BaseRequest request,
//						BaseResponse response) {
//					getMessageCodeResponse = (GetMessageCodeResponse) response;
//					getMessageCodeService.setResponse(getMessageCodeResponse);
//				}
//
//				@Override
//				public void onCancel() {
//				}
//			});
		} catch (Exception e) {
			Log.e("init",
					"ShopingCarActivity onload error!");
		}
	}

	void startRegister(final String mobile, final String password)
	{
		ToastUtils.showLong(mContext, RegisterActivity.this.getResources().getText(R.string.register_success));
		finish();
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
//		try {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//					.detectDiskReads().detectDiskWrites().detectNetwork()
//					.penaltyLog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
//					.penaltyLog().penaltyDeath().build());
//			RegisterResponse registerResponse = new RegisterResponse();
//			RegisterRequest registerRequest = new RegisterRequest(mobile, password);
//			RegisterService registerService = new RegisterService(mContext);
//			registerService.setRequest(registerRequest);
//			registerService.setResponse(registerResponse);
//			registerService.request(new NetListener() {
//				@Override
//				public void onPrepare() {
//				}
//
//				@Override
//				public void onLoading() {
//				}
//
//				@Override
//				public void onLoadSuccess(BaseResponse response) {
//					if(response.resString.contains("ok"))
//					{
//						AppUtil.showInfoShort(mContext, "注册成功！");
//						UserInfo.getInstance().moblile = mobile;
//						UserInfo.getInstance().password = password;
//						SharePreferenceManager.saveUserId(UserInfo.getInstance().moblile);
//						SharePreferenceManager.saveUserPassword(UserInfo.getInstance().password);
//						finish();
//						Intent intent = new Intent();
//						intent.setClass(RegisterActivity.this, LoginActivity.class);
//						startActivity(intent);
//					}else if(response.resString.contains("exist")){
//						AppUtil.showInfoShort(mContext, "账号已被注册");
//					}else{
//						AppUtil.showInfoShort(mContext, "注册失败");
//					}
//				}
//
//				@Override
//				public void onFailed(Exception ex, BaseResponse response) {
//					AppUtil.showInfoShort(mContext, "注册失败！");
//				}
//
//				@Override
//				public void onComplete(String respondCode, BaseRequest request,
//						BaseResponse response) {
//				}
//
//				@Override
//				public void onCancel() {
//				}
//			});
//		} catch (Exception e) {
//		}
	}
	
	
	/**
	 * 点击事件
	 * @author zyx
	 */
	class OnClick implements OnClickListener {
		private String mobile_v = null;
		private String validecode_v = null;

		public void onClick(View arg0) {
			
			if (arg0 == submit) {
				String password  = etPassword.getText().toString();
				String password1  = etPasswordAgain.getText().toString();

				if (mobile.getText() == null
						|| "".equals(mobile.getText().toString())) {
					ToastUtils.showLong(mContext, RegisterActivity.this.getResources().getText(R.string.input_phone));
				}else if(password==null || password.equals(""))
				{
					ToastUtils.showLong(mContext, RegisterActivity.this.getResources().getText(R.string.input_password));
				}else if(password1==null || password1.equals(""))
				{
					ToastUtils.showLong(mContext, RegisterActivity.this.getResources().getText(R.string.confirm_password));
				}else if(!password.equals(password1))
				{
					ToastUtils.showLong(mContext, RegisterActivity.this.getResources().getText(R.string.password_different));
				}
//				else if(!AppUtil.isMobileNO(mobile.getText().toString())){
//					AppUtil.showInfoShort(mContext, "请输入正确的手机号码");
//				}
				else {
					mobile_v = String.valueOf(mobile.getText());
//						validecode_v = String.valueOf(valideCode.getText());
					System.out.println("mobile_v =" + mobile_v);
					sMobile = mobile.getText().toString();
					sPassword = password;
					startRegister(sMobile, password);
				}

			}
		}
	}
	

	public void toBack(View v)
	{
		finish();
	}
	
	public void toLogin(View v)
	{
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
	}
	
}
