package com.deter.TaxManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class RentActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btBarqDate;
	private Button btRentStartDate;
	private Button btRentEndDate;
	private TextView tvArea;
	private TextView title;
	private DatePickerUtil datePickerUtil;
	private String strYear;
	private String strMonth;
	private String strDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		datePickerUtil = new DatePickerUtil(this);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_rent);
		setTitleText(R.string.fzba);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		strYear = getResources().getString(R.string.year);
		strMonth = getResources().getString(R.string.month);
		strDay = getResources().getString(R.string.day);
		btBarqDate = (Button)findViewById(R.id.btBarqDate);
		btBarqDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btBarqDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});

		btRentStartDate = (Button)findViewById(R.id.btRentStartDate);
		btRentStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btBarqDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});


		btRentEndDate = (Button)findViewById(R.id.btRentEndDate);
		btRentEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btBarqDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});
	}

	@Override
	public void initData() {

	}

	public void init() {

	}

    public void uploadRentContract(View view){
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







