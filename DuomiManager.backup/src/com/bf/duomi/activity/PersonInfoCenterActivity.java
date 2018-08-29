package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bf.duomimanager.R;
import com.bf.duomi.adapter.PersonCenterAdapter;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.GetUserDetailRequest;
import com.bf.duomi.bean.request.SettingInfoRequest;
import com.bf.duomi.bean.request.SingleProduceRequest;
import com.bf.duomi.bean.response.GetUserDetailResponse;
import com.bf.duomi.bean.response.SettingInfoResponse;
import com.bf.duomi.bean.response.SingleProduceResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.SettingInfo;
import com.bf.duomi.entity.Wallet;
import com.bf.duomi.service.GetUserDetailService;
import com.bf.duomi.service.SettingInfoService;
import com.bf.duomi.service.SingleProduceService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.DialogUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;


/**
 * 个人中心
 * 
 * @author zyx
 * 
 */
public class PersonInfoCenterActivity extends BaseActivity {
	private List<Map<String, Object>> mData1, mData2, mData3;
	private ListView list1, list2, list3;

	private Context mContext;
	private TextView points, level, name, tvUserId;
	private TextView tvNumber;
	private TextView tvCount;
	private TextView tvYouHuiQuan;
	LinearLayout linearLayout1;
	RelativeLayout relativeLayout2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		mContext = this;
		setContentView(R.layout.personinfocenter_activity);
		linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
		relativeLayout2 = (RelativeLayout)findViewById(R.id.relativeLayout2);
		tvUserId = (TextView) this.findViewById(R.id.tvUserId);
		tvNumber = (TextView) this.findViewById(R.id.tvScore);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(UserInfo.getInstance().isLogin)
		{
			linearLayout1.setVisibility(View.GONE);
			relativeLayout2.setVisibility(View.VISIBLE);
			tvUserId.setText("用户 "+UserInfo.getInstance().moblile+" 已登录");
			tvNumber.setText(""+UserInfo.getInstance().score);

		} else {
			linearLayout1.setVisibility(View.VISIBLE);
			relativeLayout2.setVisibility(View.GONE);
		}
		
	}
	

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


		return list;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData2() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		return list;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData3() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


		return list;
	}

	/**
	 * 列表
	 * 
	 * @author zyx
	 * 
	 */
	public class onClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 1) {

			}
			if (arg2 == 0) {
				if (!UserInfo.getInstance().isLogin) {
					DialogUtil dialogUtil = new DialogUtil();
					dialogUtil.showAlertDialog(PersonInfoCenterActivity.this);

				} else {
				}
			}
		}

	}

	
	
	public void toLogin(View v)
	{
		Intent intent = new Intent();
		intent.setClass(PersonInfoCenterActivity.this,
				LoginActivity.class);
		PersonInfoCenterActivity.this.startActivity(intent);
	}
	
	public void toRegister(View v)
	{
		Intent intent = new Intent();
		intent.setClass(PersonInfoCenterActivity.this,
				RegisterActivity.class);
		PersonInfoCenterActivity.this.startActivity(intent);
	}
	
	
	public void toSetting(View v)
	{
		Intent intent = new Intent();
		intent.setClass(PersonInfoCenterActivity.this,
				SettingActivity.class);
		PersonInfoCenterActivity.this.startActivity(intent);
	}
	
	
	public void toMyOrder(View v)
	{
		Intent intent = new Intent();
		intent.setClass(PersonInfoCenterActivity.this,
				MyOrderListActivity.class);
		PersonInfoCenterActivity.this.startActivity(intent);
	}
	
	
	public void toHomePage(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				MainActivity.class);
		this.startActivity(intent);
	}
	
	public void toNearBy(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				SelectPaymentTypeActivity.class);
		this.startActivity(intent);
	}
	
	public void toProductClass(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				ProductClassActivity.class);
		this.startActivity(intent);
	}
	
	public void toShopCart(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				ShopCarActivity.class);
		this.startActivity(intent);
	}
	
	public void toPersonCenter(View v)
	{
//		Intent intent = new Intent();
//		intent.setClass(this,
//				PersonInfoCenterActivity.class);
//		this.startActivity(intent);
	}
	
	public void toContactUs(View v)
	{
		SettingInfo s = SettingInfo.getInstance();
		if(s.serviceMobile!=null)
		{
	        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+s.serviceMobile));  
	        startActivity(intent); 
		}else{
			AppUtil.showInfoShort(mContext, "获取联系信息失败！");
		}

	}
	
}








