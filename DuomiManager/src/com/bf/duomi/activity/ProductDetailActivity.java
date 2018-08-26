package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.List;

import com.bf.duomimanager.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.AddShoppingCarRequest;
import com.bf.duomi.bean.request.SingleProduceRequest;
import com.bf.duomi.bean.response.AddShoppingCarResponse;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.SingleProduceResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductImage;
import com.bf.duomi.entity.SettingInfo;
import com.bf.duomi.service.AddShoppingCarService;
import com.bf.duomi.service.SingleProduceService;
import com.bf.duomi.tools.LoadImageUtil;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.PropertiesUtils;
import com.bf.duomi.util.SharePreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {

	
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private RadioGroup rg;
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	
	private SingleProduceRequest sproduceRequest;
	private SingleProduceService sProduceService;
	private SingleProduceResponse sProduceResponse;

	private AddShoppingCarResponse aresponse;
	private AddShoppingCarRequest request;
	private AddShoppingCarService service;
	
	
	private Product product;
	private List<ProductImage> productImgList;
	
	private int productNumber;
	private TextView tvProductNumber;
	private TextView tvProductId;//商品编号
	private TextView tvProductName;
	private TextView tvProductDescription;
	private TextView tvDiscountPrice;
	private TextView tvOrignPrice;
	public LoadImageUtil loadImageUtil;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.activity_product_detail);
		loadImageUtil = new LoadImageUtil();
		loadImageUtil.mInSampleSize = 2;
		productNumber = 1;
		tvProductId = (TextView)findViewById(R.id.tvProductId);
		tvProductNumber = (TextView)findViewById(R.id.tvProductNumber);
		tvProductNumber.setText(""+productNumber);
		tvProductName =  (TextView)findViewById(R.id.tvProductName);;
		tvProductDescription=  (TextView)findViewById(R.id.tvProductDescription);
		tvDiscountPrice=  (TextView)findViewById(R.id.tvDiscountPrice);
		tvOrignPrice=  (TextView)findViewById(R.id.tvOrignPrice);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		Boolean isEnterFromOrder = (Boolean)bundle.getBoolean("enterFromOrder");
		Boolean isEnterFromShopCart = (Boolean)bundle.getBoolean("enterFromShopCart");
		if(isEnterFromOrder!=null)
		{
			if(isEnterFromOrder)
			{
				RelativeLayout rl = (RelativeLayout)findViewById(R.id.rlShopCart);
				rl.setVisibility(View.GONE);
			}
		}
		if(isEnterFromShopCart!=null)
		{
			if(isEnterFromShopCart)
			{
				RelativeLayout rl = (RelativeLayout)findViewById(R.id.rlShopCart);
				rl.setVisibility(View.GONE);
			}
		}
		load(id);
	}
	
	
	void showImages(Product p)
	{
		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < p.imageCount; i++) {
			ImageView imageView = new ImageView(this);
			//imageView.setImageResource(R.drawable.index_img_1);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
//		for(int i=0;i<product.imageCount;i++)
//		{
//			if(i<imageViews.size())
//			{
//				String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
//				String url = URL + product.logoUrl+(1)+".jpg";
//				if(imageViews.get(0)!=null && url!=null)
//				{
//					loadImageUtil.showImageAsyn(imageViews.get(0), url, 0);
//				}
//			}
//		}	
	}
	
	
	
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			if(position<dots.size())
			{
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				oldPosition = position;
			}
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
			String url = URL + product.logoUrl+(arg1+1)+".jpg";
			if(imageViews.get(arg1)!=null && url!=null)
			{
				loadImageUtil.showImageAsyn(imageViews.get(arg1), url, 0);
			}
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	
	public void toBack(View v)
	{
		finish();
	}
	
	
	/**
	 * 加载数据
	 * 
	 * @throws Exception
	 */
	public void load(final int id) {
		try {
			sproduceRequest = new SingleProduceRequest();
			sproduceRequest.setContext(this);
			sproduceRequest.setId(id);
			sProduceResponse = new SingleProduceResponse();
			sProduceService = new SingleProduceService(this);
			sProduceService.setRequest(sproduceRequest);
			sProduceService.setResponse(sProduceResponse);
			sProduceService.request(new NetListener() {
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
					SingleProduceResponse res = (SingleProduceResponse) response;
					res.parseResString();
					product = res.getProduct();
					if(product!=null)
					{
						//AppUtil.showInfoShort(ProductDetailActivity.this, "获取产品信息成功");
					}else{
						AppUtil.showInfoShort(ProductDetailActivity.this, "获取产品信息失败");
						return;
					}
					if(product.id!=null)
					{
						tvProductId.setText("商品编号:"+product.id);
					}
					if(product.name!=null)
					{
						tvProductName.setText(product.name);
					}
					if(product.description!=null)
					{
						tvProductDescription.setText(product.description);
					}
					if(product.discountPrice!=null)
					{
						tvDiscountPrice.setText("￥"+product.marketPrice);
					}
					
					if(product.beansPrice!=null)
					{
						tvOrignPrice.setText("￥"+product.beansPrice);
					}
					showImages(product);
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(ProductDetailActivity.this, "获取产品信息失败");
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
			Log.e("produceDetail error!", "produceDetail load data error!");
		}
	}
	
	

	/**
	 * 添加购物车
	 */
	public void toAddShoppingCar() {
		if(product==null)
		{
			AppUtil.showInfoShort(ProductDetailActivity.this, "获取产品信息失败");
			return;
		}
		if(productNumber<=0)
		{
			AppUtil.showInfoShort(ProductDetailActivity.this, "请重新设置购买数量");
			return;
		}	
		try {
			request = new AddShoppingCarRequest();
			request.setContext(ProductDetailActivity.this);
			request.setCustomId(UserInfo.getInstance().customId);
			request.setCustomMobile(UserInfo.getInstance().moblile);
			request.setProductId(product.id);
			request.setProductName(product.name);
			request.setProductLogo(product.logoUrl);
			request.setPrice(product.discountPrice);
			request.setAmout(productNumber);
			aresponse = new AddShoppingCarResponse();
			service = new AddShoppingCarService(ProductDetailActivity.this);
			service.setRequest(request);
			service.setResponse(aresponse);
			service.request(new NetListener() {
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
					AddShoppingCarResponse res = (AddShoppingCarResponse) response;
					res.parseResString();
					if(res.isSuccess)
					{
						Intent intent = new Intent();
						intent.setClass(ProductDetailActivity.this, ShopCarActivity.class);
						ProductDetailActivity.this.startActivity(intent);
						AppUtil.showInfoShort(ProductDetailActivity.this, "添加购物车成功！");
						finish();
					}else{
						AppUtil.showInfoShort(ProductDetailActivity.this, "添加购物车失败！");
					}
					
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					AppUtil.showInfoShort(ProductDetailActivity.this, "添加购物车失败！");
				}

				@Override
				public void onComplete(String respondCode, BaseRequest request,
						BaseResponse response) {
					aresponse = (AddShoppingCarResponse) response;
					service.setResponse(aresponse);
				}

				@Override
				public void onCancel() {
				}
			});
		} catch (Exception e) {
			Log.v("updateTextView error!", "updateTextView error");
		}
	}
	
	public void addNumber(View v)
	{
		productNumber++;
		tvProductNumber.setText(""+productNumber);
	}
	
	
	public void reduceNumber(View v)
	{
		if(productNumber>0)
		{
			productNumber--;
			tvProductNumber.setText(""+productNumber);
		}
		
	}
	
	
	public void addToCart(View v)
	{
		if(!UserInfo.getInstance().isLogin)
		{
			finish();
			Toast.makeText(this, "您还没有登录，请先登录",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivity(intent);
			return;
		}
		toAddShoppingCar();
	}
	
	
	public void toShopCart(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,ShopCarActivity.class);
		this.startActivity(intent);
		finish();
	}
	
	public void toContact(View v)
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
	
}
