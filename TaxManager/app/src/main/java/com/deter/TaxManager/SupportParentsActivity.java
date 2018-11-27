package com.deter.TaxManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.R;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class SupportParentsActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_support_parents);
		setTitleText(R.string.zylr);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initData() {
		init();
	}

	public void init() {

	}

	public void uploadSupportDoc(View view){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
//			if ("file".equalsIgnoreCase(uri.getScheme())){
//				path = uri.getPath();
//				tv.setText(path);
//				Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
//				return;
//			}
			String path = uri.getPath();
			//String path = getRealPathFromURI(this, uri);
			//tv.setText(path);
			Toast.makeText(this,path,Toast.LENGTH_SHORT).show();

		}
	}


}







