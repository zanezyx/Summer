package com.deter.TaxManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class VocationActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btnSubmit;
	private TextView tvArea;
	private TextView title;
	private Spinner mSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_vocation);
		setTitleText(R.string.zyzgjy);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mSpinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter =
				ArrayAdapter.createFromResource(this, R.array.deduction_spiner_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new VocationActivity.SpinnerListener());
	}

	@Override
	public void initData() {

	}

	public void init() {

	}

	class SpinnerListener implements android.widget.AdapterView.OnItemSelectedListener{


		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			String selected = parent.getItemAtPosition(position).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			System.out.println("nothingSelect");
		}
	}


	public void uploadVocationTranningDoc(View view){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			String path = uri.getPath();
			Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
		}
	}
}







