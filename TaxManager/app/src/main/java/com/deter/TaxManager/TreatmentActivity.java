package com.deter.TaxManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TreatmentActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_treatment);
		setTitleText(R.string.treatment_of_diseases);
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

    public void uploadDoc1(View view){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	public void uploadDoc2(View view){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			String path = uri.getPath();
			Toast.makeText(this,path+requestCode,Toast.LENGTH_SHORT).show();

		}
	}

	public void addNewTreatmentBill(View view){

		Intent intent1 = new Intent(this, AddTreatmentBillActivity.class);
		startActivity(intent1);
	}

}







