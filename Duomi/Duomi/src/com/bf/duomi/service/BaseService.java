package com.bf.duomi.service;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.ParameterizedType;

import org.apache.http.client.ClientProtocolException;

import com.bf.duomi.application.AppApplication;
import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.commication.HttpClient;
import com.bf.duomi.commication.BaseRequest.REQUESTSHOWTYPE;
import com.bf.duomi.component.LoadingDialog;
import com.bf.duomi.util.AppUtil;
import com.bf.duomi.util.GsonUtil;
import com.bf.duomi.util.NetListener;
import com.bf.duomi.util.SharePreferenceManager;
import com.bf.duomi.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * 基本服务类
 * 
 * @author bhl
 */
public abstract class BaseService<REQUEST extends BaseRequest, RESPOND extends BaseResponse> {

	private boolean isCanShow;

	private BaseService service;
	private Context context;
	private LoadingDialog dialog;
	private String tag;
	private String result;
	private AppApplication application;
	private REQUEST request;
	private Class<RESPOND> mResponseType;
	private NetListener listener;
	private Exception ex;
	private NetAsyncTask task;
	private DataLoadinStatusManager statusManager;
	private RESPOND response;

	public boolean isCanShow() {
		return isCanShow;
	}

	public void setCanShow(boolean isCanShow) {
		this.isCanShow = isCanShow;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setRequest(REQUEST request) {
		this.request = request;
		this.request.setContext(context);
	}

	public RESPOND getResponse() {
		return response;
	}

	@SuppressWarnings("unchecked")
	public void setResponse(BaseResponse response) {
		this.response = (RESPOND) response;
	}

	public void setCanShowDialog(boolean flag) {
		statusManager.setCanShowDialog(flag);
	}

	public abstract REQUEST newRequest();

	/**
	 * 
	 * @return the request
	 */
	public REQUEST getRequest() {
		return request;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseService(Context context) {
		this.context = context;
		tag = context.getClass().getName();
		application = (AppApplication) context.getApplicationContext();
		ParameterizedType pType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		mResponseType = (Class) pType.getActualTypeArguments()[1];
		request = newRequest(); // 以后都用setRequest主动设置吧，这样子不太好！！
		statusManager = DataLoadinStatusManager.getInstance(context, this
				.getClass().getSimpleName());
		isCanShow = true;
		service = this;
	}

	/**
	 * 发起请求
	 */
	public void request(NetListener listener) {
		if (request == null) {
			statusManager.printLog("request is null!!");
			return;
		}
		statusManager.printLog(request.getUrl() + "");

		this.listener = listener;
		if (task != null
				&& task.getStatus() == android.os.AsyncTask.Status.RUNNING) {
			return;
		}
		task = new NetAsyncTask(request);
		excuteTask(task);
	}

	public void cancelTask() {
		if (task != null) {
			task.cancel(true);
		}
	}

	@SuppressLint("NewApi")
	public void excuteTask(AsyncTask<Void, Void, integer> task) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}

	}

	/**
	 * 异步网络请求任务
	 * 
	 * @author lenovo
	 * 
	 */
	class NetAsyncTask extends AsyncTask<Void, Void, integer> {
		REQUEST request;

		LoadingDialog dialog;

		public NetAsyncTask(REQUEST request) {
			this.request = request;
		}

		@Override
		protected void onPreExecute() {
			ex = null;
			listener.onPrepare();
			statusManager.onPrepare();
			statusManager.onLoading();

			if (request.getmShowType() == REQUESTSHOWTYPE.DISPLAY
					&& dialog == null) {
				dialog = new LoadingDialog(context);
				dialog.showLoadingDialog(context.getResources().getString(
						R.string.loading_submit));
			}
		}

		@Override
		protected void onPostExecute(integer result) {
			super.onPostExecute(result);

			if (request.getmShowType() == REQUESTSHOWTYPE.DISPLAY
					&& dialog != null) {
				dialog.closeLoadingDialog();
				dialog = null;
			}

			if (response != null) {
				listener.onLoadSuccess(response);
				statusManager.onLoadSuccess(response);
			} else {
				listener.onFailed(ex, response);
				statusManager.onFailed(ex, response);
				if (AppUtil.isNetworkAvailable(context)) {
					Toast.makeText(context, "服务器连接异常", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(context, "当前网络不给力，请检查网络设置",
							Toast.LENGTH_SHORT).show();
				}
			}

			task = null;
		}

		@Override
		protected void onCancelled() {
			if (dialog != null) {
				dialog.closeLoadingDialog();
				dialog = null;
			}

			super.onCancelled();
			statusManager.onCancel();
		}

		@Override
		protected integer doInBackground(Void... params) {
			result = "";
			try {
				if (request.getmRequestType() == BaseRequest.REQUESTTYPE.GET) {
					Log.i("ezyx", ">>>net request  get url =" + request.getUrl());
					result = doNetworkTask();// 默认是get方法
				} else {
					Log.i("ezyx", ">>>net request  post url =" + request.getUrl());
					result = doNetworkTaskByPost();// post方式上传
				}
				Log.i("ezyx", ">>>net request  result =" + result);
//				response = GsonUtil.getGsonInstance().fromJson(result,
//						mResponseType);// Json格式化response
				response.resString = result;
				Log.i("ezyx", ">>>net request  resString =" + result);
				if (listener != null) {
					listener.onComplete(response.getResult(), request, response);
				}
				statusManager.onComplete(response.getResult(), request,
						response);

			}  catch (Exception e) {
				ex = e;
				e.printStackTrace();
			}
			if (ex != null) {
				Log.i("ezyx", ">>>net request  exception");
				response = null;
			}
			return null;
		}

	}

	protected String doNetworkTask() throws IOException,
			ClientProtocolException, InterruptedIOException {
		String content = "";
		String url = request.getUrl();
		if (application.isCmwap()) {
			content = HttpClient.getViaCmwap(url);
		} 
		else if (!application.isFast()) {
			content = HttpClient.getViaBadNetwork(url);
			 //content = HttpClient.get(url);
		} 
		else {
			content = HttpClient.get(url);
		}
		return content;
	}

	protected String doNetworkTaskByPost() throws IOException,
			ClientProtocolException, InterruptedIOException {
		// ArrayList<File> bitmaps = request.getBitmaps();
		File bitmaps = request.getBitmaps();
		String ret = HttpClient.getContentByPost(request.getUrl(),
				request.toMap(), bitmaps, request.getImageFlag());
		return ret;
	}

	public void saveUpdateTime(String updateTime) {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.UPDATETIME_PREFRENCE_DATABASE);
		Editor ed = sp.edit();
		ed.putString(this.getClass().toString(), updateTime);
		ed.commit();
	}

	public String getUpdateTime() {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.UPDATETIME_PREFRENCE_DATABASE);
		return sp.getString(this.getClass().toString(), "");
	}

	/***
	 * 保存邻居列表中的时间戳
	 */
	public void saveTS(long ts) {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.UPDATETIME_PREFRENCE_DATABASE);
		Editor ed = sp.edit();
		ed.putLong(this.getClass().toString(), ts);
		ed.commit();
	}

	public long getTs() {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.UPDATETIME_PREFRENCE_DATABASE);
		return sp.getLong(this.getClass().toString(), 0);
	}

}
