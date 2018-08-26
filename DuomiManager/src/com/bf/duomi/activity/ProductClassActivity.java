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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bf.duomimanager.R;
import com.bf.duomi.adapter.ProduceTypeAdapter;
import com.bf.duomi.bean.request.ProduceRequest;
import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.ProductSecondaryRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.ProduceResponse;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.ProductSecondaryResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductSecondary;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.RecommendProducts;
import com.bf.duomi.service.ProduceService;
import com.bf.duomi.service.ProduceTypeService;
import com.bf.duomi.service.ProductSecondaryService;
import com.bf.duomi.service.SearchProductService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.DialogUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;

/**
 * 分类
 * 
 * @author zyx
 * 
 */
public class ProductClassActivity extends BaseActivity {
	private Context mContext;
	private ListView producetypeList;
	private ListView secondProducetypeList;
	private List<Map<String, Object>> mData;
	private List<Map<String, Object>> mSecondaryData;
	private List<Map<String, Object>> mSeconeData;
	private EditText etMetaKeyWord;
	private ProduceTypeResponse produceTypeResponse;
	private ProduceTypeRequest producetypeRequest;
	private ProduceTypeService producetypeService;

	private ProduceResponse productResponse;
	private ProduceRequest productRequest;
	private ProduceService productService;
	
	private ProductSecondaryResponse productSecondaryResponse;
	private ProductSecondaryRequest productSecondaryRequest;
	private ProductSecondaryService productSecondaryService;
	
	ProduceTypeAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.product_class_activity);
		try {
			init();
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	
	
	/**
	 * 初始化界面
	 */
	public void init() {
		try {
			etMetaKeyWord = (EditText)findViewById(R.id.etMetaKeyWord);
		} catch (Exception e) {
			Log.e("producetypeActivity error!", "初始化界面错误！");
		}
	}

	/**
	 * 列表选择事件
	 * 
	 * @author zyx
	 * 
	 */
	class itemClick implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Map<String, Object> producetype = mData.get(arg2);
			int id = (Integer) producetype.get("id");
			
			adapter.selectIndex = arg2;
			producetypeList.setAdapter(adapter);
//			Boolean isChecked = (Boolean)mData.get(arg2).get("check");
//			mData.get(arg2).put("check", !isChecked);
			
//			RelativeLayout rl = (RelativeLayout)arg1;
//			rl.setBackgroundColor(getResources().getColor(R.color.greencolor));
			
//			Bundle bundle = new Bundle();
//			bundle.putInt("id", id);
//			bundle.putString("name", producetype.get("name").toString());
//			Intent intent = new Intent();
//			intent.putExtras(bundle);
//			intent.setClass(ProductClassActivity.this, ViewProduceActivity.class);
//			ProductClassActivity.this.startActivity(intent);
			try {
				//currMainMenuIndex = arg2;
				//loadSecondProductType(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * 列表选择事件
	 * 
	 * @author zyx
	 * 
	 */
	class itemClick1 implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			
			Map<String, Object> producetype = mSeconeData.get(arg2);
			int id = (Integer) producetype.get("id");
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			bundle.putString("name", producetype.get("name").toString());
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(ProductClassActivity.this, ProductDetailActivity.class);
			ProductClassActivity.this.startActivity(intent);
		}
	}
	
	
	
	/**
	 * 加载一级菜单
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
		producetypeRequest = new ProduceTypeRequest();
		produceTypeResponse = new ProduceTypeResponse();
		producetypeService = new ProduceTypeService(mContext);
		producetypeService.setRequest(producetypeRequest);
		producetypeService.setResponse(produceTypeResponse);
		producetypeService.request(new NetListener() {
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
				ProduceTypeResponse res = (ProduceTypeResponse) response;
				res.parseResString();
				mData = getData();
				loadProductTypeListViews();
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "获取产品类型列表失败!");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,BaseResponse response) {
				produceTypeResponse = (ProduceTypeResponse) response;
				producetypeService.setResponse(produceTypeResponse);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	
	

	/**
	 * 加载二级菜单
	 * 
	 * @throws Exception
	 */
	
	@SuppressLint("NewApi")
	public void loadProductSecondary(int id) throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		productSecondaryRequest = new ProductSecondaryRequest();
		productSecondaryRequest.setType(id);
		productSecondaryResponse = new ProductSecondaryResponse();
		productSecondaryService = new ProductSecondaryService(mContext);
		productSecondaryService.setRequest(productSecondaryRequest);
		productSecondaryService.setResponse(productSecondaryResponse);
		productSecondaryService.request(new NetListener() {
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
				productSecondaryResponse = (ProductSecondaryResponse) response;
				productSecondaryResponse.parseResString();
				mSecondaryData = getSecondaryData();
				int index = currMainMenuIndex;
				View v = (View)mData.get(currMainMenuIndex).get("secondView");
				Button b1 = (Button)v.findViewById(R.id.btn1);
				Button b2 = (Button)v.findViewById(R.id.btn2);
				Button b3 = (Button)v.findViewById(R.id.btn3);
				Button b4 = (Button)v.findViewById(R.id.btn4);
				Button b5 = (Button)v.findViewById(R.id.btn5);
				Button b6 = (Button)v.findViewById(R.id.btn6);
				Button b7 = (Button)v.findViewById(R.id.btn7);
				Button b8 = (Button)v.findViewById(R.id.btn8);
				Button b9 = (Button)v.findViewById(R.id.btn9);
				
				Button [] btns = {b1,b2,b3,b4,b5,b6,b7,b8,b9};
				for(int i=0;i<mSecondaryData.size();i++)
				{
					if(i<9)
					{
						btns[i].setVisibility(View.VISIBLE);
						btns[i].setText((String)mSecondaryData.get(i).get("name"));
						int id = (Integer)mSecondaryData.get(i).get("id");
						btns[i].setTag(id*1000+i);
						btns[i].setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								try {
									currSecondMenuIndex = ((Integer)arg0.getTag())%1000;
									//loadSecondProductType(((Integer)arg0.getTag())/1000);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						//loadSecondProductType
					}
				}
				
//				ProduceTypeAdapter adapter = new ProduceTypeAdapter(mContext,mSeconeData, false);
//				secondProducetypeList.setAdapter(adapter);
				//AppUtil.showInfoShort(mContext, "获取产品类型列表成功!");
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "获取产品类型列表失败!");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,BaseResponse response) {
//				productResponse = (ProduceResponse) response;
//				productService.setResponse(productResponse);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	
	int currSecondMenuIndex;
	
	

	/**
	 * 加载三级菜单
	 * 
	 * @throws Exception
	 */
	
	@SuppressLint("NewApi")
	public void loadProducts(int id) throws Exception {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		productRequest = new ProduceRequest();
		productRequest.setTypeId(id);
		productResponse = new ProduceResponse();
		productService = new ProduceService(mContext);
		productService.setRequest(productRequest);
		productService.setResponse(productResponse);
		productService.request(new NetListener() {
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
				ProduceResponse res = (ProduceResponse) response;
				res.parseResString();
				Intent intent = new Intent();
				String typeName = (String)mData.get(currMainMenuIndex).get("name");
				intent.putExtra("productTypeName", typeName);
				intent.setClass(ProductClassActivity.this, ProductListActivity.class);
				ProductClassActivity.this.startActivity(intent);
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "获取产品类型列表失败!");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,BaseResponse response) {
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
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
//			Integer pictureID[] = new Integer[] { R.drawable.picture_1,
//					R.drawable.picture_2, R.drawable.picture_3,
//					R.drawable.picture_4, R.drawable.picture_5,
//					R.drawable.picture_6, R.drawable.picture_7,
//					R.drawable.picture_8, R.drawable.picture_9,
//					R.drawable.picture_10, R.drawable.picture_11,
//					R.drawable.picture_12, R.drawable.picture_13 };
			if (produceTypeResponse != null) {
				List<ProductType> ptl = produceTypeResponse.getProductTypeList();
				if (ptl != null && !ptl.isEmpty()) {
					int i = 0;
					for (ProductType pt : ptl) {
						map = new HashMap<String, Object>();
						map.put("id", pt.getId());
						map.put("name", pt.getName());
//						map.put("number", pictureID[i]);
						map.put("info", pt.getContent());
						list.add(map);
						i++;
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getSecondaryData() {
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;

			if (productSecondaryResponse != null) {
				List<ProductSecondary> ptl = productSecondaryResponse.getProductTypeList();
				if (ptl != null && !ptl.isEmpty()) {
					int i = 0;
					for (ProductSecondary pt : ptl) {
						map = new HashMap<String, Object>();
						map.put("id", pt.id);
						map.put("name", pt.name);
						map.put("code", pt.code);
						map.put("productType", pt.productType);
						list.add(map);
						i++;
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getDataOfSecondType() {
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			if (productResponse != null) {
				List<Product> ptl = productResponse.productList;
				if (ptl != null && !ptl.isEmpty()) {
					int i = 0;
					for (Product pt : ptl) {
						map = new HashMap<String, Object>();
//						map.put("id", pt.getId());
						map.put("name", pt.name);
						map.put("id", pt.id);
						list.add(map);
						i++;
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public void toBack(View v)
	{
		finish();
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
		Intent intent = new Intent();
		intent.setClass(this,
				PersonInfoCenterActivity.class);
		this.startActivity(intent);
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
				intent.setClass(ProductClassActivity.this, ProductListActivity.class);
				ProductClassActivity.this.startActivity(intent);
				
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

	int currMainMenuIndex;
	
	void loadProductTypeListViews() {
		if(mData==null && mData.size()==0)
			return;
		int count = 0;
		LinearLayout ll = (LinearLayout) findViewById(R.id.llProductTypes);
		ll.removeAllViewsInLayout();
		count = mData.size();
		if (count>0) {
			for (int i = 0; i < count; i++) {
				Map<String, Object> map = mData.get(i);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.product_type_info, null);
				TextView tv1 = (TextView) layout.findViewById(R.id.main);
				tv1.setText(""+map.get("name"));
				map.put("secondView", layout);
				ll.addView(layout);
				layout.setTop(i);
				int c = (Integer)map.get("id");
				layout.setTag(i*1000+c);
				layout.setOnClickListener(new OnClickListener() {// 就是这个监听没反应

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						currMainMenuIndex = ((Integer)v.getTag()).intValue()/1000;
//						LinearLayout tv1 = (LinearLayout) v.findViewById(R.id.llSecond);
//						ImageView iv = (ImageView)v.findViewById(R.id.indactor);
//						
//						if(tv1.getVisibility() == View.VISIBLE)
//						{
//							iv.setImageResource(R.drawable.right_arrow);
//							tv1.setVisibility(View.GONE);
//						}else{
//							
//							try {
//								loadProductSecondary(((Integer)v.getTag()).intValue()%1000);
//								//iv.setImageResource(R.drawable.down_arrow);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							tv1.setVisibility(View.VISIBLE);
//						}
						try {
							loadProducts(((Integer) v.getTag()).intValue() % 1000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
	

}
