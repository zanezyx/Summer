package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bf.duomi.activity.MainActivity;
import com.bf.duomi.activity.MyOrderListActivity;
import com.bf.duomi.activity.OrderProductDetailActivity;
import com.bf.duomi.activity.ProductDetailActivity;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.CancelOrderRequest;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.response.CancelOrderResponse;
import com.bf.duomi.bean.response.LoginResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.holder.OneMonthHolder;
import com.bf.duomi.service.CancelOrderService;
import com.bf.duomi.service.LoginService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;
import com.bf.duomimanager.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 浏览 自定义适配器
 * 
 * @author zyx
 * 
 */
public class OneMonthAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	public OneMonthAdapter(Context context, List<Map<String, Object>> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.mContext = context;
	}

	// 获得记录数
	@Override
	public int getCount() {
		if(mData != null){
			return mData.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * 重写记录
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		OneMonthHolder holder = null;
		if (convertView == null) {
			holder = new OneMonthHolder();
			convertView = mInflater.inflate(R.layout.onemonth_list_item_view,
					null);
			holder.productName = (TextView) convertView.findViewById(R.id.tvProductName);
			holder.orderMoney = (TextView) convertView.findViewById(R.id.tvTotalMoney);
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.mobile = (TextView) convertView.findViewById(R.id.tvMobile);
			holder.receiverAddress = (TextView) convertView.findViewById(R.id.tvReceiverAddress);
			holder.receiverMobile = (TextView) convertView.findViewById(R.id.tvReceiverMobile);
			holder.receiverName = (TextView) convertView.findViewById(R.id.tvReceiverName);
			holder.orderDate = (TextView) convertView.findViewById(R.id.tvorderDate);
			holder.viewDetail = (TextView) convertView.findViewById(R.id.tvViewDetail);
			holder.paymentType = (TextView) convertView.findViewById(R.id.tvPaymentType);
			holder.deliverystate = (TextView) convertView.findViewById(R.id.tvDeliveryState);
			holder.pay = (Button) convertView.findViewById(R.id.btnPay);
			holder.cancelOrder = (Button) convertView.findViewById(R.id.btnCancel);
			convertView.setTag(holder);
		} else {
			holder = (OneMonthHolder) convertView.getTag();
		}
		holder.productName.setText((String) mData.get(position).get("productName"));
		holder.orderMoney.setText(String.valueOf(mData.get(position).get("orderMoney")));
		holder.name.setText(String.valueOf(mData.get(position).get("name")));
		holder.mobile.setText(String.valueOf(mData.get(position).get("mobile")));
		holder.receiverName.setText(String.valueOf(mData.get(position).get("receiverName")));
		holder.receiverMobile.setText(String.valueOf(mData.get(position).get("receiverMobile")));
		holder.receiverAddress.setText(String.valueOf(mData.get(position).get("receiverAddress")));
		holder.orderDate.setText((String) mData.get(position).get("orderDate"));
		holder.viewDetail.setTag(mData.get(position).get("orderId"));
		holder.viewDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				Integer id = (Integer)arg0.getTag();
				bundle.putInt("id", id.intValue());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(mContext, OrderProductDetailActivity.class);
				mContext.startActivity(intent);
			}
		});
		Integer t1 = (Integer)mData.get(position).get("paymentType");

		if(t1!=null)
		{
			if(t1.intValue()==0)
			{
				holder.paymentType.setText("付款方式：货到付款");
			}else if(t1.intValue()==1)
			{
				holder.paymentType.setText("付款方式：支付宝付款");
			}else{
				holder.paymentType.setText("付款方式：银行卡付款");
			}
		}
		Integer t2 = (Integer)mData.get(position).get("deliveryState");
		if(t2!=null)
		{
			if(t2.intValue()==0)
			{
				holder.deliverystate.setText("发货状态：等待确认");
			}else
			{
				holder.deliverystate.setText("发货状态：已发货");
			}
		}
		if(t1==0)
		{
			holder.pay.setVisibility(View.INVISIBLE);
		}else{
			holder.pay.setVisibility(View.VISIBLE);
			holder.pay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//int id = (Integer)mData.get(position).get("orderId");
					//cacelOrder(id);
				}
			});
		}
		Integer t3 = (Integer)mData.get(position).get("orderStatus");
		if(t3==0)
		{
			holder.cancelOrder.setVisibility(View.VISIBLE);
			holder.cancelOrder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int id = (Integer)mData.get(position).get("orderId");
					//cacelOrder(id);
					showDialog(mContext, id);
				}
			});
		}else{
			holder.cancelOrder.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	

    //显示基本的AlertDialog  
    private void showDialog(Context context, final int orderId) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        //builder.setIcon(R.drawable.icon);  
        builder.setTitle("温馨提示");  
        builder.setMessage("是否确定取消订单？");  
        builder.setPositiveButton("确定",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	cacelOrder(orderId);
                    }  
                });  
        builder.setNegativeButton("取消",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        
                    }  
                });  
        builder.show();  
    }  
    
    

	/**
	 * 取消订单
	 */
	public void cacelOrder(final int id) {

		final String sMobile = SharePreferenceManager.getUserId();
		final String sPassword = SharePreferenceManager.getUserPassword();
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		CancelOrderResponse response = new CancelOrderResponse();
		
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId(id);
		CancelOrderService cancelOrderService = new CancelOrderService(mContext);
		cancelOrderService.setRequest(request);
		cancelOrderService.setResponse(response);
		cancelOrderService.request(new NetListener() {
			@Override
			public void onPrepare() {
			}

			@Override
			public void onLoading() {
			}

			@Override
			public void onLoadSuccess(BaseResponse response) {

				CancelOrderResponse res = (CancelOrderResponse) response;
				if (res == null)
					return;
				res.parseResString();
				if (res.isSuccess) {
					AppUtil.showInfoShort(mContext, "取消订单成功");
					((MyOrderListActivity)mContext).updateCancelOrderResult(id);
				} else {
					AppUtil.showInfoShort(mContext, "取消订单失败");
				}

			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				Log.i("ezyx", ">>>onFailed  取消订单失败");
				AppUtil.showInfoShort(mContext, "取消订单失败");
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
