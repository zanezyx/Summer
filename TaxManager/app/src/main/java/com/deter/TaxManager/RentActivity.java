package com.deter.TaxManager;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
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

	/**
	 * 初始化界面
	 */
	public void init() {

	}

    
}







