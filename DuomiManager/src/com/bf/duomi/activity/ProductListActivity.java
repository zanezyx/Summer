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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bf.duomimanager.R;
import com.bf.duomi.adapter.ProduceTypeAdapter;
import com.bf.duomi.adapter.ProductAdapter;
import com.bf.duomi.bean.request.ProduceRequest;
import com.bf.duomi.bean.request.ProduceTypeRequest;
import com.bf.duomi.bean.request.SearchProductRequest;
import com.bf.duomi.bean.response.IndexResponse;
import com.bf.duomi.bean.response.ProduceResponse;
import com.bf.duomi.bean.response.ProduceTypeResponse;
import com.bf.duomi.bean.response.SearchProductResponse;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Product;
import com.bf.duomi.entity.ProductType;
import com.bf.duomi.entity.Products;
import com.bf.duomi.service.ProduceService;
import com.bf.duomi.service.ProduceTypeService;
import com.bf.duomi.service.SearchProductService;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.DialogUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;


/**
 * 产品列表
 * 
 * @author zyx
 * 
 */
public class ProductListActivity extends BaseActivity {
	private Context mContext;
	private ListView productList;
	private ListView secondProducetypeList;
	private List<Map<String, Object>> mData;
	private EditText etMetaKeyWord;
	private TextView tvTip;
	private ProduceTypeResponse produceTypeResponse;
	private ProduceTypeRequest producetypeRequest;
	private ProduceTypeService producetypeService;

	private ProduceResponse productResponse;
	private ProduceRequest productRequest;
	private ProduceService productService;
	ProductAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		setContentView(R.layout.product_list_activity);
		init();
		// load();
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

		String typeName = getIntent().getStringExtra("productTypeName");
		TextView tvName = (TextView) findViewById(R.id.tvTypeName);
		tvTip = (TextView) findViewById(R.id.tvTip);
		tvName.setText(typeName);
		productList = (ListView) this.findViewById(R.id.productlist);
		// producetypeList.setOnItemClickListener(new itemClick());
		etMetaKeyWord = (EditText) findViewById(R.id.etMetaKeyWord);
		mData = getData();
		if(mData.size()==0)
		{
			tvTip.setVisibility(View.VISIBLE);
			tvTip.setText("很抱歉，没有你想要查找的商品");
		}else{
			tvTip.setVisibility(View.INVISIBLE);
			adapter = new ProductAdapter(mContext, mData);
			productList.setAdapter(adapter);
		}
	}

	/**
	 * 列表选择事件
	 * 
	 * @author zyx
	 * 
	 */
	// class itemClick implements ListView.OnItemClickListener {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Map<String, Object> producetype = mData.get(arg2);
	// int id = (Integer) producetype.get("id");
	//
	// adapter.selectIndex = arg2;
	// productList.setAdapter(adapter);
	// try {
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

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
				
				// AppUtil.showInfoShort(mContext, "获取产品类型列表成功!");
			}

			@Override
			public void onFailed(Exception ex, BaseResponse response) {
				AppUtil.showInfoShort(mContext, "获取产品类型列表失败!");
			}

			@Override
			public void onComplete(String respondCode, BaseRequest request,
					BaseResponse response) {
				produceTypeResponse = (ProduceTypeResponse) response;
				producetypeService.setResponse(produceTypeResponse);
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
			List<Product> ptl = Products.getInstance().productList;
			if (ptl != null && !ptl.isEmpty()) {
				int i = 0;
				for (Product pt : ptl) {
					map = new HashMap<String, Object>();
					map.put("id", pt.getId());
					map.put("name", pt.getName());
					map.put("price", pt.marketPrice);
					map.put("imgURl", pt.logoUrl);
					list.add(map);
					i++;
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void toBack(View v) {
		finish();
	}


}
