package com.bf.duomi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.duomimanager.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.AddressRequest;
import com.bf.duomi.bean.request.ModifyAddressRequest;
import com.bf.duomi.bean.response.AddressResponse;
import com.bf.duomi.bean.response.ModifyAddressResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Address;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.ReceiveAddressMgr;
import com.bf.duomi.service.AddressService;
import com.bf.duomi.service.ModifyAddressService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;
import com.mrwujay.cascade.activity.AreaSelectActivity;

/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class AddressActivity extends Activity {
	
	private Context mContext;
	private AddressResponse addressResponse;
	private AddressRequest addressRequest;
	private AddressService addressService;
	private ModifyAddressResponse modifyAddressResponse;
	private ModifyAddressRequest modifyAddressRequest;
	private ModifyAddressService modifyAddressService;
	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private ReceiveAddress currAddress;
	private TextView tvArea;
	private TextView title;
	private boolean isAddNew = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.address_activity);
		try {
			init();
			//loadSpinner();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	public void init() {
		
		ReceiveAddressMgr rmgr = ReceiveAddressMgr.getInstance();
		if(rmgr.currReceiveAddress==null)
		{
			currAddress = new ReceiveAddress();
			isAddNew = true;
		}else{
			currAddress = rmgr.currReceiveAddress;
			isAddNew = false;
		}
		
		try {
			tvArea = (TextView)findViewById(R.id.tvArea);
			etReveiverName = (EditText) findViewById(R.id.reveiverName);
			etAddressDetail = (EditText) findViewById(R.id.addressDetail);
			mobile = (EditText) findViewById(R.id.phone);
			btnSubmit = (Button) findViewById(R.id.save);
			title = (TextView)findViewById(R.id.title);
			CheckBox cb = (CheckBox)findViewById(R.id.cbSetDefault);
			if(!isAddNew)
			{
				int defaultAddressId = SharePreferenceManager.getUserDefaultAddressId();
				if(defaultAddressId == currAddress.id.intValue())
				{
					cb.setChecked(true);
				}else{
					cb.setChecked(false);
				}
				etReveiverName.setText(currAddress.receiveName);
				etAddressDetail.setText(currAddress.address);
				mobile.setText(currAddress.mobile);
				tvArea.setText(currAddress.province+" "+currAddress.city+" "+currAddress.county);  
				title.setText("编辑收货地址");
			}else{
				cb.setChecked(false);
				title.setText("新增收货地址");
			}
			
		} catch (Exception e) {
			Log.e("AddressActivity error!", "初始化界面错误！");
		}
	}

	/**
	 * 提交添加
	 * 
	 * @throws Exception
	 */
	
	@SuppressLint("NewApi")
	public void addsubmit() throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		addressRequest = new AddressRequest();
		addressRequest.setContext(mContext);
		addressRequest.setCustomId(UserInfo.getInstance().customId);
//		addressRequest.setReceiveName(new String(currAddress.receiveName.getBytes(), "utf-8"));
//		addressRequest.setAddress1(new String(currAddress.address.getBytes(), "utf-8"));
//		addressRequest.setProvince(new String(currAddress.province.getBytes(), "utf-8"));
//		addressRequest.setCity(new String(currAddress.city.getBytes(), "utf-8"));
//		addressRequest.setCounty(new String(currAddress.county.getBytes(), "utf-8"));
		
		addressRequest.setReceiveName(currAddress.receiveName);
		addressRequest.setAddress1(currAddress.address);
		addressRequest.setProvince(currAddress.province);
		addressRequest.setCity(currAddress.city);
		addressRequest.setCounty(currAddress.county);
		
		addressRequest.setMobile(currAddress.mobile);
		addressResponse = new AddressResponse();
		addressService = new AddressService(mContext);
		addressService.setRequest(addressRequest);
		addressService.setResponse(addressResponse);
		addressService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				AddressResponse res = (AddressResponse)response;
				res.parseResString();
				if(res.isSuccess)
				{
					AppUtil.showInfoShort(mContext, "添加收货人地址成功！");
					finish();
				}else{
					AppUtil.showInfoShort(mContext, "添加收货人地址失败！");
				}
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "添加收货人地址失败！");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {
				addressResponse = (AddressResponse) response;
				addressService.setResponse(addressResponse);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	

	/**
	 * 提交修改
	 * 
	 * @throws Exception
	 */
	
	@SuppressLint("NewApi")
	public void modifySubmit() throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		modifyAddressRequest = new ModifyAddressRequest();
		modifyAddressRequest.setContext(mContext);
		modifyAddressRequest.setId(currAddress.id);
		modifyAddressRequest.setCustomId(UserInfo.getInstance().customId);
		modifyAddressRequest.setReceiveName(new String(currAddress.receiveName.getBytes(), "utf-8"));
		modifyAddressRequest.setAddress1(new String(currAddress.address.getBytes(), "utf-8"));
		modifyAddressRequest.setProvince(new String(currAddress.province.getBytes(), "utf-8"));
		modifyAddressRequest.setCity(new String(currAddress.city.getBytes(), "utf-8"));
		modifyAddressRequest.setCounty(new String(currAddress.county.getBytes(), "utf-8"));
		modifyAddressRequest.setMobile(currAddress.mobile);
		modifyAddressRequest.setTelephone(currAddress.mobile);
		modifyAddressResponse = new ModifyAddressResponse();
		modifyAddressService = new ModifyAddressService(mContext);
		modifyAddressService.setRequest(modifyAddressRequest);
		modifyAddressService.setResponse(modifyAddressResponse);
		modifyAddressService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {
				ModifyAddressResponse res = (ModifyAddressResponse)response;
				res.parseResString();
				if(res.isSuccess)
				{
					AppUtil.showInfoShort(mContext, "修改收货人地址成功！");
					finish();
				}else{
					AppUtil.showInfoShort(mContext, "修改收货人地址失败！");
				}
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "修改收货人地址失败！");
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
	
	
	
	public void toSelectArea(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, AreaSelectActivity.class);
        int requestCode = 0;  
        startActivityForResult(intent, requestCode);
	}
	
	
	public void toBack(View v)
	{
		finish();
	}
	
	public void toSubmit(View v)
	{
		boolean res = true;
		currAddress.receiveName = etReveiverName.getText().toString();
		currAddress.address = etAddressDetail.getText().toString();
		currAddress.mobile = mobile.getText().toString();
		if( currAddress.receiveName==null || currAddress.receiveName.equals(""))
		{
			res = false;
		}	
		if( currAddress.address==null || currAddress.address.equals(""))
		{
			res = false;
		}
		if( currAddress.mobile==null || currAddress.mobile.equals(""))
		{
			res = false;
		}
		if(!AppUtil.isMobileNO(currAddress.mobile))
		{
			res = false;
			AppUtil.showInfoShort(mContext, "请输入有效的手机号码");
			return;
		}
		if( currAddress.city==null || currAddress.city.equals(""))
		{
			res = false;
		}
		if( currAddress.county==null || currAddress.county.equals(""))
		{
			res = false;
		}
		if( currAddress.province==null || currAddress.province.equals(""))
		{
			res = false;
		}
		if(!res)
		{
			AppUtil.showInfoShort(mContext, "请选完善地址信息");
			return;
		}
		try {
			if(isAddNew)
			{
				addsubmit();
			}else{
				modifySubmit();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
    // 回调方法，从第二个页面回来的时候会执行这个方法  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

        // 根据上面发送过去的请求吗来区别  
        switch (requestCode) {  
        case 0:  
        	if(data!=null)
        	{
            	currAddress.province = data.getStringExtra("province");  
            	currAddress.city = data.getStringExtra("city");  
            	currAddress.county = data.getStringExtra("county");  
            	tvArea.setText(currAddress.province+" "+currAddress.city+" "+currAddress.county);  
        	}

            break;  
        case 2:  
            break;  
        default:  
            break;  
        }  
    }  
    
    public void toSetDefault(View v)
    {
    	
    	CheckBox cb = (CheckBox)v;
    	
    	if(isAddNew)
    	{
    		SharePreferenceManager.saveDefaultAddressId(-1);
    		SharePreferenceManager.saveDefaultAddress("");
    	}else{
        	if(cb.isChecked())
        	{
        		SharePreferenceManager.saveDefaultAddressId(currAddress.id);
        		SharePreferenceManager.saveDefaultAddress(currAddress.province+currAddress.city
        				+currAddress.county+currAddress.address);
        	}else{
        		SharePreferenceManager.saveDefaultAddressId(-1);
        		SharePreferenceManager.saveDefaultAddress("");
        	}
    	}


    }

    
}







