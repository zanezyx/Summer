package com.zyx.duomimanager.activity;

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

import com.zyx.duomimanager.R;
import com.zyx.duomimanager.application.UserInfo;
import com.zyx.duomimanager.bean.request.AddressRequest;
import com.zyx.duomimanager.bean.request.ModifyAddressRequest;
import com.zyx.duomimanager.bean.response.AddressResponse;
import com.zyx.duomimanager.bean.response.ModifyAddressResponse;
import com.zyx.duomimanager.cascade.activity.AreaSelectActivity;
import com.zyx.duomimanager.commication.BaseRequest;
import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.ReceiveAddress;
import com.zyx.duomimanager.entity.ReceiveAddressMgr;
import com.zyx.duomimanager.service.AddressService;
import com.zyx.duomimanager.service.ModifyAddressService;
import com.zyx.duomimanager.util.AppUtil;
import com.zyx.duomimanager.util.NetListener;
import com.zyx.duomimanager.util.SharePreferenceManager;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class SupportParentsActivity extends Activity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.address_activity);
		init();
	}

	/**
	 * 初始化界面
	 */
	public void init() {

	}

    
}







