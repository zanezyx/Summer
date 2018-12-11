package com.deter.TaxManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.InsuranceInfo;
import com.deter.TaxManager.model.SalaryInfo;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class BspActivity extends BaseActivity {

	private EditText etSalary;
	private EditText etUnemploymentInsurance;
	private EditText etEndowment;
	private EditText etMedicalInsurance;
	private EditText etSubsidy;
	private EditText etFundCharges;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏


	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_bsp);
		setTitleText(R.string.bsp);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		etSalary = (EditText) findViewById(R.id.etSalary);
		etUnemploymentInsurance = (EditText) findViewById(R.id.etUnemployment);
		etEndowment = (EditText) findViewById(R.id.etEndowment);
		etMedicalInsurance = (EditText) findViewById(R.id.etMedical);
		etSubsidy = (EditText) findViewById(R.id.etSubsidy);
		etFundCharges = (EditText) findViewById(R.id.etGjj);

		DataManager.getInstance(this).initSalaryInfo();
		SalaryInfo info = DataManager.getInstance(this).getmSalaryInfo();
		if(info!=null){
			if(info.getSalary()!=0){
				etSalary.setText(""+info.getSalary());
			}
			if(info.getUnemploymentInsurance()!=0){
				etUnemploymentInsurance.setText(""+info.getUnemploymentInsurance());
			}
			if(info.getEndowment()!=0){
				etEndowment.setText(""+info.getEndowment());
			}
			if(info.getMedicalInsurance()!=0){
				etMedicalInsurance.setText(""+info.getMedicalInsurance());
			}
			if(info.getSubsidy()!=0){
				etSubsidy.setText(""+info.getSubsidy());
			}
			if(info.getFundCharges()!=0){
				etFundCharges.setText(""+info.getFundCharges());
			}
		}

	}




	@Override
	protected void onStop() {
		super.onStop();
		saveSalaryInfoToFile();
	}



	public void saveSalaryInfoToFile(){
		final SalaryInfo info = new SalaryInfo();
		try {
			info.setSalary(Integer.parseInt(etSalary.getText().toString()));
			info.setUnemploymentInsurance(Integer.parseInt(etUnemploymentInsurance.getText().toString()));
			info.setEndowment(Integer.parseInt(etEndowment.getText().toString()));
			info.setMedicalInsurance(Integer.parseInt(etMedicalInsurance.getText().toString()));
			info.setSubsidy(Integer.parseInt(etSubsidy.getText().toString()));
			info.setFundCharges(Integer.parseInt(etFundCharges.getText().toString()));
		}catch (Exception e){
			Log.i("tax", "BspActivity>>>saveSalaryInfoToFile e:"+e.toString());
		}
		Log.i("tax", "BspActivity>>>saveSalaryInfoToFile 1");
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataManager.getInstance(BspActivity.this).saveSalaryInfo(info);

			}
		}).start();
	}



	@Override
	public void initData() {

	}

	/**
	 * 初始化界面
	 */
	public void init() {

	}

    public void toBspData(View view){
		Intent intent8 = new Intent(this, BspDataActivity.class);
		startActivity(intent8);
	}
}







