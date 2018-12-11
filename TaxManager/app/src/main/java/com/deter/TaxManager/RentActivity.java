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

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.InsuranceInfo;
import com.deter.TaxManager.model.RentInfo;
import com.deter.TaxManager.model.TxDate;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class RentActivity extends BaseActivity {

	private EditText etAddress;
	private EditText etPayPerMonth;
	private Button btBarqDate;
	private Button btRentStartDate;
	private Button btRentEndDate;
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
						btRentStartDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
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
						btRentEndDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});
		etAddress = (EditText) findViewById(R.id.etRentAddress);
		etPayPerMonth = (EditText) findViewById(R.id.etRent);
		DataManager.getInstance(this).initRentInfo();
		RentInfo info = DataManager.getInstance(this).getmRentInfo();
		if(info!=null){
			if(info.getAddress()!=null){
				etAddress.setText(""+info.getAddress());
			}
			if(info.getPayPerMonth()!=0){
				etPayPerMonth.setText(""+info.getPayPerMonth());
			}
			if(info.getStartDate()!=null){
				TxDate txDate = info.getStartDate();
				btRentStartDate.setText(txDate.getYear()+strYear
						+txDate.getMonth()+strMonth+txDate.getDay()+strDay);
			}

			if(info.getEndDate()!=null){
				TxDate txDate = info.getEndDate();
				btRentEndDate.setText(txDate.getYear()+strYear
						+txDate.getMonth()+strMonth+txDate.getDay()+strDay);
			}
			if(info.getBackupDate()!=null){
				TxDate txDate = info.getBackupDate();
				btBarqDate.setText(txDate.getYear()+strYear
						+txDate.getMonth()+strMonth+txDate.getDay()+strDay);
			}
		}

	}

	@Override
	public void initData() {

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

	@Override
	protected void onStop() {
		super.onStop();
		saveRentInfoToFile();
	}

	public void saveRentInfoToFile(){
		final RentInfo info = new RentInfo();
		try {
			info.setAddress(etAddress.getText().toString());
			info.setPayPerMonth(Integer.parseInt(etPayPerMonth.getText().toString()));
		}catch (Exception e){
			Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile e:"+e.toString());
		}
		Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile 1");
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataManager.getInstance(RentActivity.this).saveRentInfo(info);
			}
		}).start();
	}
}







