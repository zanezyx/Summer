package com.bf.duomi.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.bf.duomimanager.R;
import com.bf.duomi.application.UserInfo;
import com.bf.duomi.bean.request.CountTimesRequest;
import com.bf.duomi.bean.request.IndexRequest;
import com.bf.duomi.bean.request.LoginRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.request.SettingInfoRequest;
import com.bf.duomi.bean.response.CountTimesResponse;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.LoginResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.bean.response.SettingInfoResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.News;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.RecommendProducts;
import com.bf.duomi.service.CountTimesService;
import com.bf.duomi.service.IndexService;
import com.bf.duomi.service.LoginService;
import com.bf.duomi.service.SearchProductService;
import com.bf.duomi.service.SettingInfoService;
import com.bf.duomi.tools.LoadImageUtil;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.DialogUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.PropertiesUtils;
import com.bf.duomi.util.SharePreferenceManager;
import com.bf.duomi.util.UpdateManager;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

/**
 * 主界面
 * 
 * @author zyx
 * 
 */
public class MainActivity extends BaseActivity {
	
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private EditText etMetaKeyWord;
	private LoginService loginService;
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	public LoadImageUtil loadImageUtil;
	private SettingInfoResponse response;
	private SettingInfoRequest request;
	private SettingInfoService settingInfoService;
	Map<String, Object> mMap;
	
	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.main_activity);
		countTimes();
		loadImageUtil = new LoadImageUtil();
		loadImageUtil.mInSampleSize = 2;
		mMap = new HashMap<String, Object>();
		etMetaKeyWord = (EditText)findViewById(R.id.etMetaKeyWord);
		requestInfo();
		if(!UserInfo.getInstance().isLogin)
		{
			if(SharePreferenceManager.isAutoLogin())
			{
				login();
			}
		}

		
	}

	void showImagePageView(ArrayList<Product> productList)
	{
		int maxSize = 5;
		
		if(productList==null)
			return;
		if(maxSize>productList.size())
		{
			maxSize = productList.size();
		}
		imageViews = new ArrayList<ImageView>();
		// 初始化图片资源
		for (int i = 0; i < maxSize; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setTag(productList.get(i).id);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String tag = (String)arg0.getTag();
					Integer id = (Integer)mMap.get(tag);
					if(id!=null)
					{
						Bundle bundle = new Bundle();
						bundle.putInt("id", id.intValue());
						Intent intent = new Intent();
						intent.putExtras(bundle);
						intent.setClass(MainActivity.this, ProductDetailActivity.class);
						MainActivity.this.startActivity(intent);
					}
				}
			});
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
		for(int i=0;i<maxSize;i++)
		{
			String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
			String url = URL + productList.get(i).logoUrl+1+".jpg";
			loadImageUtil.showImageAsyn(imageViews.get(i), url, 0);
			mMap.put(url, productList.get(i).id);
		}	
	}
	
	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 2,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

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
			if(position<dots.size())
			{
				currentItem = position;
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

	void requestInfo() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		IndexResponse response = new IndexResponse();
		IndexRequest indexRequest = new IndexRequest();
		IndexService indexService = new IndexService(this);
		indexService.setRequest(indexRequest);
		indexService.setResponse(response);
		indexService.request(new NetListener() {
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
				IndexResponse res = (IndexResponse) response;
				res.parseResString();
				showNewsImage();
				loadRecommendProductViews();
				try {
					onLoadSettingInfo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
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

	void loadRecommendProductViews() {
		int count = 0;
		LinearLayout ll = (LinearLayout) findViewById(R.id.llProducts);
		ll.removeAllViewsInLayout();
		count = RecommendProducts.getInstance().hotSalesList.size();
		Collections.shuffle(RecommendProducts.getInstance().hotSalesList);
		if (count>0) {
			for (int i = 0; i < count; i+=2) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.product_info, null);;
				Product p ;
				////
				if(i<RecommendProducts.getInstance().hotSalesList.size())
				{
					p = RecommendProducts.getInstance().hotSalesList.get(i);
					
					ImageView iv = (ImageView) layout.findViewById(R.id.ivImg1);
					ImageView iv11 = (ImageView) layout.findViewById(R.id.iv1);
					TextView tv1 = (TextView) layout.findViewById(R.id.name1);
					TextView tv2 = (TextView) layout.findViewById(R.id.price1);
//					TextView tv3 = (TextView) layout.findViewById(R.id.description);
					String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
					String url = URL + p.logoUrl+"/"+1+".jpg";
					loadImageUtil.showImageAsyn(iv, url, 0);
					tv2.setText("￥" + p.marketPrice);
					tv1.setText("" + p.name);
					//iv11.setVisibility(View.VISIBLE);
					// tv3.setText(p.description);
					ll.addView(layout);
					RelativeLayout rl1 = (RelativeLayout)layout.findViewById(R.id.rlProduct1);
					rl1.setTag(p.id);
					rl1.setOnClickListener(new OnClickListener() {// 就是这个监听没反应

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Bundle bundle = new Bundle();
							bundle.putInt("id", ((Integer)v.getTag()).intValue());
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(MainActivity.this, ProductDetailActivity.class);
							MainActivity.this.startActivity(intent);
						}
					});
				}

				if(i+1<RecommendProducts.getInstance().hotSalesList.size())
				{
					p = RecommendProducts.getInstance().hotSalesList.get(i+1);
//					LayoutInflater inflater1 = getLayoutInflater();
					ImageView iv1 = (ImageView) layout.findViewById(R.id.ivImg2);
					ImageView iv12 = (ImageView) layout.findViewById(R.id.iv2);
					TextView tv11 = (TextView) layout.findViewById(R.id.name2);
					TextView tv12 = (TextView) layout.findViewById(R.id.price2);
//					TextView tv3 = (TextView) layout.findViewById(R.id.description);
					String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
					String url1 = URL + p.logoUrl+"/"+1+".jpg";
					loadImageUtil.showImageAsyn(iv1, url1, 0);
					tv12.setText("￥" + p.marketPrice);
					tv11.setText("" + p.name);
					//iv12.setVisibility(View.VISIBLE);
//					tv3.setText(p.description);
					////
					RelativeLayout rl2 = (RelativeLayout)layout.findViewById(R.id.rlProduct2);
					rl2.setTag(p.id);
					rl2.setOnClickListener(new OnClickListener() {// 就是这个监听没反应

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Bundle bundle = new Bundle();
							bundle.putInt("id", ((Integer)v.getTag()).intValue());
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(MainActivity.this, ProductDetailActivity.class);
							MainActivity.this.startActivity(intent);
						}
					});
				}

			}
		}
	}

	void showNewsImage()
	{
		if(RecommendProducts.getInstance().newList!=null)
		{
			Collections.shuffle(RecommendProducts.getInstance().newList);
			showImagePageView(RecommendProducts.getInstance().newList);
//			for(int i=0;i<RecommendProducts.getInstance().newList.size();i++)
//			{
//				if(i<imageViews.size())
//				{
//					String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
//					String url = URL + RecommendProducts.getInstance().newList.get(i).logoUrl;
//					loadImageUtil.showImageAsyn(imageViews.get(i), url, R.drawable.index_img_1);
//				}
//			}	
		}
	}

	void searchProcucts(final String metaKeyWord) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		SearchProductResponse response = new SearchProductResponse();
		SearchProductRequest request = new SearchProductRequest();
		request.setMetaKeywords(metaKeyWord);
		SearchProductService searchService = new SearchProductService(this);
		searchService.setRequest(request);
		searchService.setResponse(response);
		searchService.request(new NetListener() {
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
				SearchProductResponse res = (SearchProductResponse) response;
				res.parseResString();
				Intent intent = new Intent();
				String s = "";
				s = "查找“"+metaKeyWord +"”"+"的商品";
				intent.putExtra("productTypeName", s);
				intent.setClass(MainActivity.this, ProductListActivity.class);
				MainActivity.this.startActivity(intent);
				
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
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

	
	
	
	public void toHomePage(View v)
	{
		
	}
	
	public void toNearBy(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this,
				SelectPaymentTypeActivity.class);
		MainActivity.this.startActivity(intent);
	}
	
	public void toProductClass(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				ProductClassActivity.class);
		MainActivity.this.startActivity(intent);
	}
	
	public void toShopCart(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this,
				ShopCarActivity.class);
		MainActivity.this.startActivity(intent);
	}
	
	public void toPersonCenter(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this,
				PersonInfoCenterActivity.class);
		MainActivity.this.startActivity(intent);
	}
	
	
	public void toSearch(View v)
	{
		String s = etMetaKeyWord.getText().toString();
		if(s!=null && !s.equals(""))
		{
			searchProcucts(s);
		}else{
			AppUtil.showInfoShort(this, "请输入文字进行搜索");
		}
		
//		Intent intent = new Intent();
//		intent.setClass(MainActivity.this, EnsureOrderActivity.class);
//		MainActivity.this.startActivity(intent);
	}
	
	

	/**
	 * 登陆事件
	 */
	public void login() {

		final String sMobile = SharePreferenceManager.getUserId();
		final String sPassword = SharePreferenceManager.getUserPassword();
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		LoginResponse response = new LoginResponse();
		
		LoginRequest loginRequest = new LoginRequest(sMobile, sPassword);
		loginService = new LoginService(this);
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
					Log.i("ezyx", "login success user id = "
							+ UserInfo.getInstance().customId);
					SharePreferenceManager.saveUserId(UserInfo
							.getInstance().moblile);
					SharePreferenceManager.saveUserPassword(UserInfo
							.getInstance().password);
				} else {
				}
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				Log.i("ezyx", ">>>onFailed  登录失败");
				AppUtil.showInfoShort(MainActivity.this, "登录失败");
				UserInfo.getInstance().isLogin = false;
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
	 * 加载应用配置数据
	 * 
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public void onLoadSettingInfo() throws Exception {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			response = new SettingInfoResponse();
			request = new SettingInfoRequest();
			settingInfoService = new SettingInfoService(this);
			settingInfoService.setRequest(request);
			settingInfoService.setResponse(response);
			settingInfoService.request(new NetListener() {
				@Override
				public void onPrepare() {
				}

				@Override
				public void onLoading() {
				}

				@Override
				public void onLoadSuccess(BaseResponse response) {
					if(response!=null)
					{
						response.parseResString();
						UpdateManager um = new UpdateManager(MainActivity.this);
						um.checkVersionUpdate(false);
					}
				}

				@Override
				public void onFailed(Exception ex, BaseResponse response) {
					//AppUtil.showInfoShort(mContext, "获取我的信息失败！");
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
			Log.e("PersonCenterActivity error!",
					"PersonCenterActivity onload error!");
		}
	}
	
	
//用于服务器记录启动次数
	void countTimes() {
		String IMEI = AppUtil.getIMEI(this);
		if(IMEI==null)
			return;
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		CountTimesResponse response = new CountTimesResponse();
		CountTimesRequest indexRequest = new CountTimesRequest(IMEI);
		CountTimesService indexService = new CountTimesService(this);
		indexService.setRequest(indexRequest);
		indexService.setResponse(response);
		indexService.request(new NetListener() {
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
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
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
