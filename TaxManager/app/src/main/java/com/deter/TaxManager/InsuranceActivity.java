package com.deter.TaxManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.InsuranceInfo;
import com.deter.TaxManager.model.RoanInterestInfo;
import com.deter.TaxManager.model.TxDate;


public class InsuranceActivity extends BaseActivity {

	private EditText etPayPerMonth;
	private Button btStartDate;
	private Button btEndDate;
	private DatePickerUtil datePickerUtil;
	private String strYear;
	private String strMonth;
	private String strDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		datePickerUtil = new DatePickerUtil(this);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_insurance);
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
		btStartDate = (Button)findViewById(R.id.btStartDate);
		btStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btStartDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});

		btEndDate = (Button)findViewById(R.id.btEndDate);
		btEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btEndDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
					}
				});
			}
		});
		etPayPerMonth = (EditText) findViewById(R.id.etPayPerMonth);
		DataManager.getInstance(this).initInsuranceInfo();
		InsuranceInfo info = DataManager.getInstance(this).getmInsuranceInfo();
		if(info!=null){
			if(info.getPayPerMonth()!=0){
				etPayPerMonth.setText(""+info.getPayPerMonth());
			}
			if(info.getStartDate()!=null){
				TxDate txDate = info.getStartDate();
				btStartDate.setText(txDate.getYear()+strYear
						+txDate.getMonth()+strMonth+txDate.getDay()+strDay);
			}
			if(info.getEndDate()!=null){
				TxDate txDate = info.getEndDate();
				btEndDate.setText(txDate.getYear()+strYear
						+txDate.getMonth()+strMonth+txDate.getDay()+strDay);
			}
		}

	}

	@Override
	public void initData() {

	}

	/**
	 * 初始化界面
	 */
	public void init() {

	}



	@Override
	protected void onStop() {
		super.onStop();
		saveInsuranceInfoToFile();
	}

	public void saveInsuranceInfoToFile(){
		final InsuranceInfo info = new InsuranceInfo();
		try {
			info.setPayPerMonth(Integer.parseInt(etPayPerMonth.getText().toString()));
		}catch (Exception e){
			Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile e:"+e.toString());
		}
		Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile 1");
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataManager.getInstance(InsuranceActivity.this).saveInsuranceInfo(info);
			}
		}).start();
	}



}







