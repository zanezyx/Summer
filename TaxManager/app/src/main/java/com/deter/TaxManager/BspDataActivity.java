package com.deter.TaxManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class BspDataActivity extends BaseActivity {

	private EditText etReveiverName, mobile, etAddressDetail;
	private Button btDate;
	private TextView tvArea;
	private TextView title;
	private DatePickerUtil datePickerUtil;
	private String strYear;
	private String strMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏
		datePickerUtil = new DatePickerUtil(this);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_bsp_data);
		setTitleText(R.string.bsp);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		strYear = getResources().getString(R.string.year);
		strMonth = getResources().getString(R.string.month);
		btDate = (Button)findViewById(R.id.btBzny);
		btDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
					@Override
					public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
						Log.i("tax", "RentActivity>>>initView>>>onClick");
						btDate.setText(year+strYear+monthOfYear+strMonth);
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







