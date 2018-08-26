package com.bf.duomi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class AppUtil {

	// 压缩后图片最大为200K
	public final static int MAXSIZE = 400 * 1024;
	public static final String ACTION_CROP = "com.android.camera.action.CROP";
	public static final String IMAGE_FILE_LOCATION_PATH = Environment
			.getExternalStorageDirectory() + "/temp.jpg";
	public static final String IMAGE_FILE_LOCATION_URI = "file://"
			+ IMAGE_FILE_LOCATION_PATH;
	public static final int TAKE_BIG_PICTURE = 1;
	public static final int TAKE_SMALL_PICTURE = 2;
	public static final int CROP_BIG_PICTURE = 3;
	public static final int CROP_SMALL_PICTURE = 4;
	public static final int CHOOSE_BIG_PICTURE = 5;
	public static final int CHOOSE_SMALL_PICTURE = 6;

	public static final String MORE_DATA = "No more data";

	// 头像宽
	public static final int HEAD_W = 540;
	// 头像高
	public static final int HEAD_H = 540;
	// 个人资料与随手拍图片宽
	public static final int ME_INFO_W = 540;
	// 个人资料与随手拍图片高
	public static final int ME_INFO_H = 640;
	// 拼车图片宽
	public static final int BUS_INFO_W = 640;
	// 拼车图片高
	public static final int BUS_INFO_H = 480;

	public static int produceID = 0; // 商品ID

	public static boolean isSuccess;
	public static boolean isModifySuccess;// 编辑头像，修改昵称成功标记

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断字符是否为空
	 * 
	 * @param result
	 * @return
	 */
	public static boolean isNull(String result) {
		if ("".equals(result)) {
			return true;
		} else if (result == null) {
			return true;
		} else if ("".equals(result.trim())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Toast弹出提示
	 */
	public static void showInfoShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast弹出提示
	 */
	public static void showInfoLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/****
	 * 判断是否有网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断应用程序在前台还是后台运行
	 */
	public static boolean isRunningForeground(Context mContext) {
		String packageName = mContext.getPackageName();
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
		if (taskInfo.size() > 0) {
			System.out.println("应用包名packageName：：" + packageName
					+ "\ntopActivityClassName::"
					+ taskInfo.get(0).topActivity.getPackageName());
			if (packageName
					.equals(taskInfo.get(0).topActivity.getPackageName())) {
				Log.i("info", "应用程序-----》》isRunningForeGround");
				return true;
			}
		}
		Log.i("info", "应用程序-----》》isNotRunningForeGround");
		return false;

	}

	public static String getTopActivityName(Context mContext2) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) mContext2
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = activityManager
				.getRunningTasks(100);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/*****
	 * sunny 2014.4.1 拍照上传
	 */
	public static File createNewFile() {
		File file = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/temple";// //保存路径
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			file = new File(saveDir, System.currentTimeMillis() + ".jpg");
			file.delete();
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 手动设置listView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 图片压缩方法
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 600f;// 这里设置高度为300f
		float ww = 360f;// 这里设置宽度为240f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 图片压缩方法
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length > MAXSIZE) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 临时保存方法
	 * 
	 * */
	public static File saveBitmap(Bitmap bm) {
		// File folder = new File("/storage/sdcard0/yunlai/");
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/topsky/");
		if (folder.exists()) {
			folder.delete();
		}

		folder.mkdirs();

		// File f = new File("/storage/sdcard0/yunlai/", new
		// Date().getTime()+".jpg");
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/topsky/", new Date().getTime() + ".jpg");

		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return f;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Intent cropImageUri(String action, Uri uri, String requestType) {
		Intent intent = new Intent(action);
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 2，头像， 3，资料图片集
		if ("2".equals(requestType)) {
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", HEAD_W);
			intent.putExtra("outputY", HEAD_W);
		} else if ("3".equals(requestType)) {
			intent.putExtra("aspectX", 7);
			intent.putExtra("aspectY", 8);
			intent.putExtra("outputX", ME_INFO_W);
			intent.putExtra("outputY", ME_INFO_H);
		} else {
			intent.putExtra("aspectX", 4);
			intent.putExtra("aspectY", 3);
			intent.putExtra("outputX", BUS_INFO_W);
			intent.putExtra("outputY", BUS_INFO_H);
		}

		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	/****
	 * 判断手机号格式是否正确
	 * 
	 * @param name
	 * @param length
	 * @author sunny
	 * @return
	 */
	public static boolean phoneTypeIsWrong(Context context, String msg,
			TextView view) {
		boolean isWrong = false;
		String str = view.getText().toString();
		if (!str.matches("^1(3|5|7|8)[0-9]{9}$")) {
			Toast.makeText(context, msg + "格式错误", Toast.LENGTH_SHORT).show();
			isWrong = true;
		}
		return isWrong;
	}

	public static String deleteSpecialChar(String str) {
		// 只允许字母和数字: String regEx = "[^a-zA-Z0-9]";
		try {
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			return m.replaceAll("").trim();
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}

	}

	public static Bitmap getUrlImage(String url) {
		InputStream i = null;
		try {
			i = getUrlStreamWithHttpClient(url);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static InputStream getUrlStreamWithHttpClient(String url_str)
			throws ClientProtocolException, IOException {
		return getUrlStreamWithHttpClient(url_str, "");
	}

	/**
	 * use HttpClient to download, can ignore 401 crash error
	 * 
	 * @param url_str
	 * @param user_agent
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static InputStream getUrlStreamWithHttpClient(String url_str,
			String user_agent) throws ClientProtocolException, IOException {
		HttpClient hc;
		if (user_agent != null && !user_agent.equals("")) {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
			// HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
			// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
			// HttpClientParams.setRedirecting(httpParams, true);
			HttpProtocolParams.setUserAgent(httpParams, user_agent);
			hc = new DefaultHttpClient(httpParams);
		} else
			hc = new DefaultHttpClient();

		HttpGet get = new HttpGet(url_str);
		HttpResponse response = hc.execute(get);
		return response.getEntity().getContent();
	}

	public static void bitmap2File(Bitmap b, String imageFile) {
		try {
			if (!createFileFolder(imageFile))
				return;

			FileOutputStream out = new FileOutputStream(imageFile);
			b.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			// Log.i("==========>", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean createFileFolder(String filename) {
		File folder = new File(AppUtil.getFilePath(filename));
		if (!folder.exists())
			if (!folder.mkdirs())
				return false;
		return true;
	}

	/**
	 * "/sdcard/txt" return "/sdcard"
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFilePath(String filename) {
		int dot = filename.lastIndexOf("/");
		if (dot == -1)
			return "";
		return filename.substring(0, dot);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeigth(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getProduceID() {
		return produceID;
	}

	public static void setProduceID(int produceID) {
		AppUtil.produceID = produceID;
	}

	// 判断是否是手机号码
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("[1][358]\\d{9}");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();
	}

	public static String getIMEI(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context  
				  .getSystemService(Context.TELEPHONY_SERVICE);  
		String IMEI = telephonyManager.getDeviceId(); 
		return IMEI;
	}

}
