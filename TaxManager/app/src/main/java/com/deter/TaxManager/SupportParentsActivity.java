package com.deter.TaxManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deter.TaxManager.R;
import com.deter.TaxManager.model.BaseInfo;
import com.deter.TaxManager.model.ChildrenInfo;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.ParentInfo;
import com.deter.TaxManager.model.SupportParentInfo;


/**
 * 地址信息
 * 
 * @author zyx
 * 
 */
public class SupportParentsActivity extends BaseActivity {

	private EditText etMyAlimony;
	private EditText etOtherAlimony1;
	private EditText etOtherAlimony2;



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_support_parents);
		setTitleText(R.string.zylr);
		setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		etMyAlimony = (EditText) findViewById(R.id.etMyAlimony);
		etOtherAlimony1 = (EditText) findViewById(R.id.etOtherAlimony1);
		etOtherAlimony2 = (EditText) findViewById(R.id.etOtherAlimony2);
		DataManager.getInstance(this).initSupportParentInfo();
		SupportParentInfo info = DataManager.getInstance(this).getmSupportParentInfo();
		if(info!=null){
			if(info.getMyAlimony()!=0){
				etMyAlimony.setText(""+info.getMyAlimony());
			}
			if(info.getOtherAlimony1()!=0){
				etOtherAlimony1.setText(""+info.getOtherAlimony1());
			}
			if(info.getOtherAlimony2()!=0) {
				etOtherAlimony2.setText("" + info.getOtherAlimony2());
			}
		}
	}

	@Override
	public void initData() {

	}

    @Override
    protected void onStop() {
        super.onStop();
        saveSupportParentInfoToFile();
    }

	public void uploadSupportDoc(View view){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
//			if ("file".equalsIgnoreCase(uri.getScheme())){
//				path = uri.getPath();
//				tv.setText(path);
//				Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
//				return;
//			}
			String path = uri.getPath();
			//String path = getRealPathFromURI(this, uri);
			//tv.setText(path);
			Toast.makeText(this,path,Toast.LENGTH_SHORT).show();

		}
	}



    public void saveSupportParentInfoToFile(){
        final SupportParentInfo info = new SupportParentInfo();
        try {
            info.setMyAlimony(Integer.parseInt(etMyAlimony.getText().toString()));
            info.setOtherAlimony1(Integer.parseInt(etOtherAlimony1.getText().toString()));
            info.setOtherAlimony2(Integer.parseInt(etOtherAlimony2.getText().toString()));
        }catch (Exception e){
            Log.i("tax", "MyInfoPagerAdapter>>>saveSupportParentInfoToFile e:"+e.toString());
        }
        Log.i("tax", "MyInfoPagerAdapter>>>saveSupportParentInfoToFile 1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(SupportParentsActivity.this).saveSupportParentInfo(info);
            }
        }).start();
        Log.i("tax", "SupportParentsActivity>>>saveSupportParentInfoToFile");
    }


}







