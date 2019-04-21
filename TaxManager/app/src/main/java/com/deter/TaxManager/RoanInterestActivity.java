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
import com.deter.TaxManager.model.RaiseChildrenInfo;
import com.deter.TaxManager.model.RoanInterestInfo;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class RoanInterestActivity extends BaseActivity {

	private EditText etRoanInterest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏


	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_loan_interest);
		setTitleText(R.string.interest_of_loans);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		etRoanInterest = (EditText) findViewById(R.id.etRoanInterest);
		DataManager.getInstance(this).initRoanInterestInfo();
		RoanInterestInfo info = DataManager.getInstance(this).getmRoanInterestInfo();
		if(info!=null){
			if(info.getRoanInterest()!=0){
				etRoanInterest.setText(""+info.getRoanInterest());
			}
		}
	}

	@Override
	public void initData() {
		init();
	}


	public void init() {

	}


	public void uploadRoanContract(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);

    }

    public void uploadRoanPaymentCredentials(View view){

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


	@Override
	protected void onStop() {
		super.onStop();
		saveRoanInterestInfoToFile();
	}

	public void saveRoanInterestInfoToFile(){
		final RoanInterestInfo info = new RoanInterestInfo();
		try {
			info.setRoanInterest(Integer.parseInt(etRoanInterest.getText().toString()));
		}catch (Exception e){
			Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile e:"+e.toString());
		}
		Log.i("tax", "RoanInterestActivity>>>saveRoanInterestInfoToFile 1");
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataManager.getInstance(RoanInterestActivity.this).saveRoanInterestInfo(info);
			}
		}).start();
	}

}







