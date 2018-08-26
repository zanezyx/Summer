package com.bf.duomi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.duomi.activity.ProductDetailActivity;
import com.bf.duomi.activity.ShopCarActivity;
import com.bf.duomi.bean.request.ModifyShoppingCarRequest;
import com.bf.duomi.bean.response.AddShoppingCarResponse;
import com.bf.duomi.bean.response.ModifyShoppingCarResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Cart;
import com.bf.duomi.entity.ShopCartManager;
import com.bf.duomi.holder.ShoppingCarHolder;
import com.bf.duomi.service.ModifyShoppingCarService;
import com.bf.duomi.tools.LoadImageUtil;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.PropertiesUtils;
import com.bf.duomi.R;


/**
 * 自定义适配器
 * 
 * @author zyx
 * 
 */
public class ShoppingCarAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private Context mContext;
	private ShoppingCarHolder holder = null;
	private LoadImageUtil loadImageUtil;
	public int selectIndex;
	public boolean isEditingMode;
	private ModifyShoppingCarResponse modifyShoppingCarResponse;
	private ModifyShoppingCarRequest modifyShoppingCarRequest;
	private ModifyShoppingCarService modifyShoppingCarService;
	
	
	
	public ShoppingCarAdapter(Context context, List<Map<String, Object>> mData, boolean isEditingMode) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.mContext = context;
		loadImageUtil = new LoadImageUtil();
		loadImageUtil.mInSampleSize = 4;
		loadImageUtil.adapter = this;
		selectIndex = -1;
		this.isEditingMode = isEditingMode;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				holder = new ShoppingCarHolder();
				convertView = mInflater.inflate(R.layout.shoppingcar_list_item_view, null);
				holder.producePicture = (ImageView) convertView.findViewById(R.id.producepicture);
				//holder.producePicture.setTag(0);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.number = (TextView) convertView .findViewById(R.id.count);
				holder.state = (TextView) convertView .findViewById(R.id.stockstate);
				holder.total = (EditText) convertView.findViewById(R.id.total);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.reduction = (Button) convertView.findViewById(R.id.Reduction);
				holder.add = (Button) convertView.findViewById(R.id.add);
				holder.deleteBtn = (Button)convertView.findViewById(R.id.btnDelete);
				holder.selected = (CheckBox) convertView.findViewById(R.id.select);
				holder.position = position;
				convertView.setTag(holder);
//				mData.get(position).put("imageView", holder.producePicture);
			} else {
				holder = (ShoppingCarHolder) convertView.getTag();
			}
			convertView . setClickable ( true );
			convertView . setOnClickListener (new ItemClick());
			final int p = position;
			String sname = (String)mData.get(p).get("name");
			holder.name.setText(sname);
			holder.number.setText("X"+mData.get(p).get("total").toString());
			holder.total.setText(mData.get(p).get("total").toString());
			Integer dprice = (Integer)(mData.get(p).get("price"));
			holder.price.setText("￥"+ dprice);
			holder.reduction.setTag(p);
			holder.add.setTag(p);
			holder.reduction.setOnClickListener(new onReductionClick(holder.total));
			holder.add.setOnClickListener(new onAddClick(holder.total));
			holder.deleteBtn.setTag(mData.get(p).get("id"));
			holder.deleteBtn.setOnClickListener(new DeleteBtnClick());
			int selected = (Integer)mData.get(p).get("selected");
			if(selected==0)
			{
				holder.selected.setChecked(false);
			}else{
				holder.selected.setChecked(true);
			}	
			holder.selected.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					if (cb.isChecked()) {
						//cb.setSelected(true);
						mData.get(p).put("selected", 1);
					} else {
						//cb.setSelected(false);
						mData.get(p).put("selected", 0);
					}
					ShopCarActivity activity = (ShopCarActivity)(ShoppingCarAdapter.this.mContext);
					activity.setTotalMoneyText();
				}
			});
			
			RelativeLayout rlInfo = (RelativeLayout)convertView.findViewById(R.id.rlInfo);
			RelativeLayout rlEdit = (RelativeLayout)convertView.findViewById(R.id.rlEdit);
			if(isEditingMode)
			{
				rlInfo.setVisibility(View.INVISIBLE);
				rlEdit.setVisibility(View.VISIBLE);
			}else{
				rlInfo.setVisibility(View.VISIBLE);
				rlEdit.setVisibility(View.INVISIBLE);
			}
//			if(position==getCount()-1)
//			{
//				try {
//					Message message = new Message();   
//	                message.what = 0;   
//	                ((ShopCarActivity)mContext).myHandler.sendMessage(message);   
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
	        if(holder.producePicture==null)
	        {
	        	holder.producePicture = (ImageView)convertView.findViewById(R.id.producepicture);
	        	//mData.get(position).put("imageView", holder.image);
	        }
			showProductImage(position, holder.producePicture); 
			return convertView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 点击事件
	 */
	class onAddClick implements OnClickListener {
		EditText total;
		public onAddClick(final EditText total) {
			this.total = total;
		}

		@Override
		public void onClick(View arg0) {
			try {
				int position = (Integer) arg0.getTag();
				int number = (Integer)mData.get(position).get("total");
//				mData.get(position).put(
//						"total",
//						Integer.valueOf(mData.get(position).get("total")
//								.toString()) + 1);
//				total.setText(mData.get(position).get("total")
//						.toString());
//				ShopCarActivity activity = (ShopCarActivity)(ShoppingCarAdapter.this.mContext);
//				activity.setTotalMoneyText();
				number+=1;
				//total.setText(mData.get(position).get("total").toString());
				modifyShoppingCar(position, number);
			} catch (Exception e) {
				Log.e("onAddClick error!", "单击错误");
			}
		}
	}

	/**
	 * 点击事件
	 */
	class onReductionClick implements OnClickListener {
		EditText total;
		onReductionClick(final EditText total){
			this.total = total;
		}
		@Override
		public void onClick(View arg0) {
			try {
				int position = (Integer) arg0.getTag();
				int number = (Integer)mData.get(position).get("total");
				if (number > 1) {
					number-=1;
					//total.setText(mData.get(position).get("total").toString());
					modifyShoppingCar(position, number);
				}
			} catch (Exception e) {
				Log.e("onReductionClick error!", "单击错误");
			}
		}
	}

	class ItemClick implements OnClickListener  {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			selectIndex = (Integer) ((ShoppingCarHolder) arg0.getTag()).add.getTag();;
			Bundle bundle = new Bundle();
			bundle.putInt("id", (Integer)mData.get(selectIndex).get("productId"));
			bundle.putBoolean("enterFromShopCart", true);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(mContext, ProductDetailActivity.class);
			mContext.startActivity(intent);
		}
	}

	class DeleteBtnClick implements OnClickListener  {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			int id = (Integer)arg0.getTag();
			((ShopCarActivity)mContext).deleteShopCartProduct(id);
		}
	}

	
	
	
	/**
	 * 修改购物车
	 */
	public void modifyShoppingCar(final int position, final int number) {

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			modifyShoppingCarRequest = new ModifyShoppingCarRequest();
			modifyShoppingCarRequest.setId((Integer)mData.get(position).get("id"));
			modifyShoppingCarRequest.setNumber(number);
			modifyShoppingCarResponse = new ModifyShoppingCarResponse();
			modifyShoppingCarService = new ModifyShoppingCarService(mContext);
			modifyShoppingCarService.setRequest(modifyShoppingCarRequest);
			modifyShoppingCarService.setResponse(modifyShoppingCarResponse);
			modifyShoppingCarService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {

					if (response == null)
						return;
					ModifyShoppingCarResponse res = (ModifyShoppingCarResponse) response;
					res.parseResString();
					if (res.isSuccess) {
						mData.get(position).put("total", number);
						ArrayList<Cart> allCartList = ShopCartManager.getInstance().allCartList;
						allCartList.get(position).amout = number;
						ShoppingCarAdapter.this.notifyDataSetChanged();
						ShopCarActivity activity = (ShopCarActivity)(ShoppingCarAdapter.this.mContext);
						activity.setTotalMoneyText();
						//AppUtil.showInfoShort(mContext, "修改购物车成功！");
					} else {
						AppUtil.showInfoShort(mContext, "修改购物车失败！");
					}

				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(mContext, "添加购物车失败！");
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
				}

				@Override
				public void onCancel() {
				}
			});
		} catch (Exception e) {
			Log.v("updateTextView error!", "updateTextView error");
		}
	}
	
	public void showProductImage(int position , ImageView iv) {
		if (mData != null) {
			//ImageView iv = (ImageView) mData.get(position).get("imageView");
			if (iv != null) {
				String logoUrl = mData.get(position).get("productLogo").toString();
				String URL = PropertiesUtils.getProperties().getProperty(
						"Service_URL");
				String url = URL + logoUrl + "1.jpg";
				loadImageUtil.showImageAsyn(iv, url, 0);
			}
		}
	}
	
}
