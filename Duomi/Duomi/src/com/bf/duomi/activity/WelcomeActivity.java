package com.bf.duomi.activity;


import com.bf.duomi.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * 欢迎界面
 * 
 * @author zyx
 * 
 */
public class WelcomeActivity extends Activity {

	private UserLoginTask mAuthTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不能横屏
		setContentView(R.layout.welcome_activity);
		try {
			Log.v("WelcomeActivity", "Start the register windows!");
			init();
		} catch (Exception e) {
			Log.e("init error", "WelcomeActivity init error!", e);
		}
	}

	/**
	 * 初始化界面
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		mAuthTask = new UserLoginTask();// 掉用后台进程
		mAuthTask.execute((Void) null);
	}

	
	/**
	 * 内部任务，进行异步登陆
	 * @author zyx
	 *
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
