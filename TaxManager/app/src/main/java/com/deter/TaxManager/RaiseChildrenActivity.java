package com.deter.TaxManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.RaiseChildrenInfo;
import com.deter.TaxManager.model.SupportParentInfo;


public class RaiseChildrenActivity extends BaseActivity {

	private EditText etMyAlimonyOfFirst;
	private EditText etOtherAlimonyOfFirst;
	private EditText etMyAlimonyOfSecond;
	private EditText etOtherAlimonyOfSecond;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 不能横屏

	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_raise_children);
		setTitleText(R.string.raise_children);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		DataManager.getInstance(this).initRaiseChildrenInfo();
		etMyAlimonyOfFirst = (EditText) findViewById(R.id.etAlimony);
		etOtherAlimonyOfFirst = (EditText) findViewById(R.id.etAlimonyOthers);
		etMyAlimonyOfSecond = (EditText) findViewById(R.id.etAlimonySecond);
		etOtherAlimonyOfSecond = (EditText) findViewById(R.id.etAlimonyOthersSecond);
		RaiseChildrenInfo info = DataManager.getInstance(this).getmRaiseChildrenInfo();
		if(info!=null){
			if(info.getMyAlimonyOfFirst()!=0){
				etMyAlimonyOfFirst.setText(""+info.getMyAlimonyOfFirst());
			}
			if(info.getOtherAlimonyOfFirst()!=0){
				etOtherAlimonyOfFirst.setText(""+info.getOtherAlimonyOfFirst());
			}
			if(info.getMyAlimonyOfSecond()!=0){
				etMyAlimonyOfSecond.setText(""+info.getMyAlimonyOfSecond());
			}
			if(info.getOtherAlimonyOfSecond()!=0){
				etOtherAlimonyOfSecond.setText(""+info.getOtherAlimonyOfSecond());
			}
		}
	}

	@Override
	public void initData() {
		init();
	}

	public void init() {

	}

	@Override
	protected void onStop() {
		super.onStop();
		saveRaiseChildrenInfoToFile();
	}

	public void saveRaiseChildrenInfoToFile(){
		final RaiseChildrenInfo info = new RaiseChildrenInfo();
		try {
			info.setMyAlimonyOfFirst(Integer.parseInt(etMyAlimonyOfFirst.getText().toString()));
			info.setOtherAlimonyOfFirst(Integer.parseInt(etOtherAlimonyOfFirst.getText().toString()));
			info.setMyAlimonyOfSecond(Integer.parseInt(etMyAlimonyOfSecond.getText().toString()));
			info.setOtherAlimonyOfSecond(Integer.parseInt(etOtherAlimonyOfSecond.getText().toString()));
		}catch (Exception e){
			Log.i("tax", "RaiseChildrenActivity>>>saveRaiseChildrenInfoToFile e:"+e.toString());
		}
		Log.i("tax", "RaiseChildrenActivity>>>saveRaiseChildrenInfoToFile 1");
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataManager.getInstance(RaiseChildrenActivity.this).saveRaiseChildrenInfo(info);
			}
		}).start();
	}


	public void uploadRaiseChildrenDoc1(View view){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	public void uploadRaiseChildrenDoc2(View view){
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
}







