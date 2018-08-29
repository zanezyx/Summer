package com.zyx.duomimanager.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zyx.duomimanager.entity.SettingInfo;

public class UpdateManager {

	
	public final static int UPDATA_CLIENT = 0;
	public final static int GET_UNDATAINFO_ERROR = 1;
	public final static int DOWN_ERROR = 2;
	Context mContext;

	public UpdateManager(Context context)
	{
		mContext = context;
	}
	
	
	
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				mContext.getPackageName(), 0);
		return packInfo.versionName;
	}

	public static File getFileFromServer(String path, ProgressDialog pd)throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),"Duomi.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(mContext.getApplicationContext(), "获取服务器更新信息失败",
						Toast.LENGTH_LONG).show();
				// LoginMain();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(mContext.getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG)
						.show();
				// LoginMain();
				break;
			}
		}
	};
	
	/* 
	 *  
	 * 弹出对话框通知用户更新程序  
	 *  
	 * 弹出对话框的步骤： 
	 *  1.创建alertDialog的builder.   
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
	 *  3.通过builder 创建一个对话框 
	 *  4.对话框show()出来   
	 */  
	protected void showUpdataDialog() {  
	    Builder builer = new Builder(mContext) ;
	    builer.setTitle("版本升级");  
	    builer.setMessage("检测到有新版本，是否更新版本");  
	    //当点确定按钮时从服务器上下载 新的apk 然后安装    
	    builer.setPositiveButton("确定", new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	            Log.i("ezyx","下载apk,更新");  
	            downLoadApk();  
	        }     
	    });  
	    //当点取消按钮时进行登录   
	    builer.setNegativeButton("取消", new OnClickListener() {  
	        public void onClick(DialogInterface dialog, int which) {  
	            // TODO Auto-generated method stub   
	            //LoginMain();  
	        }  
	    });  
	    AlertDialog dialog = builer.create();  
	    dialog.show();  
	} 

	
	/* 
	 * 从服务器中下载APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //进度条对话框   
	    pd = new  ProgressDialog(mContext);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	        	if(SettingInfo.getInstance().latestVersionURL!=null)
	        	{
		            try {  
		                File file = getFileFromServer(SettingInfo.getInstance().latestVersionURL, pd);  
		                sleep(3000);  
		                installApk(file);  
		                pd.dismiss(); //结束掉进度条对话框   
		            } catch (Exception e) {  
		                Message msg = new Message();  
		                msg.what = DOWN_ERROR;  
		                handler.sendMessage(msg);  
		                e.printStackTrace();  
		            }  
	        	}else{
	                Message msg = new Message();  
	                msg.what = GET_UNDATAINFO_ERROR;  
	                handler.sendMessage(msg); 
	        	}

	        }}.start();  
	}

	
	//安装apk    
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //执行动作   
	    intent.setAction(Intent.ACTION_VIEW);  
	    //执行的数据类型   
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了    
	    mContext.startActivity(intent);  
	}  
	
	
	public void checkVersionUpdate(boolean tag)
	{
		try {
			if(SettingInfo.getInstance().latestVersionCode==null)
			{
				if(tag)
				{
					Toast.makeText(mContext.getApplicationContext(), "获取不到最新版本信息",Toast.LENGTH_LONG).show();
				}
				return;
			}
			if(!SettingInfo.getInstance().latestVersionCode.equals(getVersionName()))
			{
                Message msg = new Message();  
                msg.what = UPDATA_CLIENT;  
                handler.sendMessage(msg);  
			}else{
				if(tag)
				{
					Toast.makeText(mContext.getApplicationContext(), "当前版本是最新版本",Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(tag)
			{
				Toast.makeText(mContext.getApplicationContext(), "当前版本是最新版本",Toast.LENGTH_LONG).show();
			}
			e.printStackTrace();
		}
	}
}

