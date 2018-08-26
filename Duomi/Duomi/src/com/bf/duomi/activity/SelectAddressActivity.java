package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.duomi.R;
import com.bf.duomi.adapter.SelectAddressAdapter;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.AddressRequest;
import com.bf.duomi.bean.request.DeleteAddressRequest;
import com.bf.duomi.bean.request.SelectAddressRequest;
import com.bf.duomi.bean.response.AddressResponse;
import com.bf.duomi.bean.response.DeleteAddressResponse;
import com.bf.duomi.bean.response.SelectAddressResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.ReceiveAddressMgr;
import com.bf.duomi.service.AddressService;
import com.bf.duomi.service.DeleteAddressService;
import com.bf.duomi.service.SelectAddressService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;


/**
 * 收货地址管理
 * 
 * @author zyx
 * 
 */
public class SelectAddressActivity extends BaseActivity {
	private Context mContext;
	private ListView addressList;
	private List<Map<String, Object>> mData;
	private SelectAddressResponse selectAddressResponse;
	private SelectAddressRequest selectAddressRequest;
	private SelectAddressService selectAddressService;

	private DeleteAddressResponse deleteAddressResponse;
	private DeleteAddressRequest deleteAddressRequest;
	private DeleteAddressService deleteAddressService;
	
	private ImageView addbutton;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		if(!UserInfo.getInstance().isLogin)
		{
			Toast.makeText(this, "您还没有登录，请先登录",Toast.LENGTH_SHORT).show();
			finish();
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivity(intent);
			return;
		}
		ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
		rmgr.currDefault = SharePreferenceManager.getUserDefaultAddressId();
		setContentView(R.layout.selectaddress_activity);
		try {
			bundle = this.getIntent().getExtras();
			init();
			//forTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			load();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	public void init() {
		try {
			addressList = (ListView) this.findViewById(R.id.addresslist);
			addressList.setOnItemClickListener(new itemClick());
			addbutton = (ImageView) this.findViewById(R.id.addbutton);
			addbutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
					rmgr.currReceiveAddress = null;
					Intent intent = new Intent();
					intent.setClass(SelectAddressActivity.this,
							AddressActivity.class);
					SelectAddressActivity.this.startActivity(intent);
				}
			});
		} catch (Exception e) {
			Log.e("SelectAddressActivity error!", "初始化界面错误！");
		}
	}

	/**
	 * 列表选择事件
	 * 
	 * @author lenovo
	 * 
	 */
	class itemClick implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				List<ReceiveAddress> addresslist = selectAddressResponse.getData();
				
				ReceiveAddress r1 = addresslist.get(arg2);
				
//				Map<String, Object> address = mData.get(arg2);
//				int id = (Integer) address.get("id");
//				if (bundle == null) {
//					bundle = new Bundle();
//				}
//				bundle.putInt("addressid", id);
//				bundle.putString("name", address.get("address").toString());
				
				
//				ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
//				rmgr.currReceiveAddress = r1;
//				Intent intent = new Intent();
//				intent.setClass(SelectAddressActivity.this,AddressActivity.class);
//				SelectAddressActivity.this.startActivity(intent);
				
				ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
				rmgr.currUseReceiveAddress = new ReceiveAddress();
				rmgr.currUseReceiveAddress.id = r1.id;
				rmgr.currUseReceiveAddress.address = r1.address;
				rmgr.currUseReceiveAddress.province = r1.province;
				rmgr.currUseReceiveAddress.city = r1.city;
				rmgr.currUseReceiveAddress.county = r1.county;
				rmgr.currUseReceiveAddress.receiveName = r1.receiveName;
				rmgr.currUseReceiveAddress.mobile = r1.mobile;
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载
	 * 
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public void load() throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		selectAddressRequest = new SelectAddressRequest();
		selectAddressRequest.setContext(mContext);
		selectAddressRequest.setCustomId(UserInfo.getInstance().customId);
		selectAddressResponse = new SelectAddressResponse();
		selectAddressService = new SelectAddressService(mContext);
		selectAddressService.setRequest(selectAddressRequest);
		selectAddressService.setResponse(selectAddressResponse);
		selectAddressService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				SelectAddressResponse res = (SelectAddressResponse)response;
				res.parseResString();
				mData = getData();
				SelectAddressAdapter adapter = new SelectAddressAdapter(
						mContext, mData);
				addressList.setAdapter(adapter);
				addressList.setOnItemClickListener(new itemClick());
				//AppUtil.showInfoShort(mContext, "获取地址成功！");
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "获取地址列表失败！");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {
//				selectAddressResponse = (SelectAddressResponse) response;
//				selectAddressService.setResponse(selectAddressResponse);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public void deleteAddress(final Integer id) throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setContext(mContext);
		deleteAddressRequest.setId(id);
		deleteAddressResponse = new DeleteAddressResponse();
		deleteAddressService = new DeleteAddressService(mContext);
		deleteAddressService.setRequest(deleteAddressRequest);
		deleteAddressService.setResponse(deleteAddressResponse);
		deleteAddressService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				DeleteAddressResponse res = (DeleteAddressResponse)response;
				res.parseResString();
				if(res.isSuccess)
				{
					AppUtil.showInfoShort(mContext, "删除地址成功！");
					if(mData!=null)
					{
						for(int i=0;i<mData.size();i++)
						{
							int aid = (Integer)mData.get(i).get("id");
							if(id.intValue()==aid)
							{
								mData.remove(i);
								SelectAddressAdapter adapter = new SelectAddressAdapter(
										mContext, mData);
								addressList.setAdapter(adapter);
								break;
							}
						}
					}
				}else{
					AppUtil.showInfoShort(mContext, "删除地址失败！");
				}
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "删除地址失败！");
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
	
	
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (selectAddressResponse != null) {
			List<ReceiveAddress> addresslist = selectAddressResponse.getData();
			if (addresslist != null && !addresslist.isEmpty()) {
				int i = 1;
				for (ReceiveAddress address : addresslist) {
					map = new HashMap<String, Object>();
					map.put("id", address.getId());
					map.put("name", address.receiveName);
					map.put("phone", address.mobile);
					map.put("address", address.address);
					map.put("area",  address.getProvince() + " "
							+ address.getCity() + " " + address.getCountry());
					list.add(map);
					i++;
				}
			}
		}
		return list;
	}

	private void getDataForTest() {
		selectAddressResponse = new SelectAddressResponse();
		selectAddressResponse.addressList = new ArrayList<ReceiveAddress>();
		
		ReceiveAddress r1 = new ReceiveAddress();
		r1.receiveName = "郑新";
		r1.mobile = "13603021940";
		r1.address = "宝珠花园";
		r1.province = "广东省";
		r1.city = "深圳市";
		r1.county = "南山";
		selectAddressResponse.addressList.add(r1);
		mData = getData();
		return;
	}

	
	
	void forTest()
	{
		getDataForTest();
		SelectAddressAdapter adapter = new SelectAddressAdapter(mContext, mData);
		addressList.setAdapter(adapter);
		addressList.setOnItemClickListener(new itemClick());
	}
	
	public void toAddAddress(View v)
	{
		ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
		rmgr.currReceiveAddress = null;
		Intent intent = new Intent();
		intent.setClass(SelectAddressActivity.this,AddressActivity.class);
		SelectAddressActivity.this.startActivity(intent);
	}
	
	
	public void toBack(View v)
	{
		finish();
	}
	
	public void editAddress(int index)
	{
		List<ReceiveAddress> addresslist = selectAddressResponse.getData();
		
		ReceiveAddress r1 = addresslist.get(index);

		ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
		rmgr.currReceiveAddress = r1;
		Intent intent = new Intent();
		intent.setClass(SelectAddressActivity.this,AddressActivity.class);
		SelectAddressActivity.this.startActivity(intent);
	}
}
