/**
 * 
 */
package com.zyx.duomimanager.commication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;



import android.content.Context;
import android.content.SharedPreferences;

import com.zyx.duomimanager.application.DmConstant;
import com.zyx.duomimanager.util.PropertiesUtils;
import com.zyx.duomimanager.util.SharePreferenceManager;

/**
 * 
 * @author lenovo
 * 
 */
public abstract class BaseRequest {

	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private REQUESTTYPE mRequestType;

	public REQUESTTYPE getmRequestType() {
		return mRequestType;
	}

	public void setmRequestType(REQUESTTYPE requestType) {
		this.mRequestType = requestType;
	}

	private REQUESTSHOWTYPE mShowType;

	public REQUESTSHOWTYPE getmShowType() {
		return mShowType;
	}

	public void setmShowType(REQUESTSHOWTYPE mShowType) {
		this.mShowType = mShowType;
	}

	public RequestLoadType mLoadType;

	/**
	 * @return the mLoadType
	 */
	public RequestLoadType getmLoadType() {
		return mLoadType;
	}

	/**
	 * @param mLoadType
	 *            the mLoadType to set
	 */
	public void setmLoadType(RequestLoadType mLoadType) {
		this.mLoadType = mLoadType;
	}

	/**
	 * 请求方式 get or post 默认为get
	 * 
	 * @author galis
	 * 
	 */
	public static enum REQUESTTYPE {

		GET, // 默认get方式
		POST // post方式
	}

	public static enum REQUESTSHOWTYPE {
		HIDE, // 默认隐藏
		DISPLAY// 展示加载框
	}

	/**
	 * 获取全部数据or加载更多
	 * 
	 * @author galis
	 * 
	 */
	public static enum RequestLoadType {
		ALL, MORE
	}

	private String imageFlag;

	public String getImageFlag() {
		return imageFlag;
	}

	public void setImageFlag(String imageFlag) {
		this.imageFlag = imageFlag;
	}

	// /单张照片
	private File bitmaps;

	public File getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(File bitmaps) {
		this.bitmaps = bitmaps;
	}

	public BaseRequest() {
		this(REQUESTTYPE.GET, REQUESTSHOWTYPE.HIDE);
	}

	public BaseRequest(REQUESTTYPE requestType, REQUESTSHOWTYPE showType) {
		setmRequestType(requestType);
		setmShowType(showType);
		setmLoadType(RequestLoadType.ALL);
	}

	/**
	 * 获取当前请求保存的URL for example
	 * ,http://192.168.2.9/property/i/rules/list.do?communityId=1
	 * 
	 * @return
	 */
	public abstract String getUrl();

	/**
	 * @return the sERVIER_URL
	 */
	public String getSERVIER_URL() {
		//String URL = PropertiesUtils.getProperties().getProperty("Service_URL");
		String URL = DmConstant.SERVIER_URL;
		return URL;
	}

	public String getPath() {
		String className = this.getClass().getSimpleName();
		String path = PropertiesUtils.getProperties().getProperty(className);
		return path;
	}

	public String getBaseParams() {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE);
		String communityId = sp.getString("communityId", "");
		String userId = sp.getString("userId", "");
		String propertyId = sp.getString("propertyId", "");
		return "&communityId=" + communityId + "&propertyId=" + propertyId
				+ "&userId=" + userId;
	}

	public String getUserIdParams() {
		SharedPreferences sp = SharePreferenceManager
				.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE);
		String userId = sp.getString("userId", "");
		return "userId=" + userId;
	}

	/**
	 * 包含images或image字段不会被反射为键值对
	 * 
	 * @return
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		Field[] fileds = this.getClass().getDeclaredFields();
		Method method;
		try {
			for (Field field : fileds) {
				if (field.getName().equals("path")
						|| field.getName().contains("image")) {
					continue;
				}
				StringBuilder nameBuilder = new StringBuilder(field.getName());
				nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "")
						.toUpperCase().charAt(0));
				method = this.getClass().getMethod("get" + nameBuilder);
				if (method.invoke(this) != null) {
					map.put(field.getName(), method.invoke(this) + "");
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 包括父类的 不过自反射public的属性
	 * 
	 * @return
	 */
	public String toAllParams() {
		StringBuilder builder = new StringBuilder();
		Field[] fileds = this.getClass().getFields();
		Method method;
		try {
			for (Field field : fileds) {
				if (field.getName().equals("path")) {
					continue;
				}
				StringBuilder nameBuilder = new StringBuilder(field.getName());
				nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "")
						.toUpperCase().charAt(0));
				method = this.getClass().getMethod("get" + nameBuilder);
				if (method.invoke(this) != null) {
					builder.append("&" + field.getName() + "="
							+ method.invoke(this));
				}
			}
			builder.deleteCharAt(0);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return builder.toString();

	}

	/**
	 * 反射request，获取拼接起来的参数
	 * 
	 * @return
	 */
	public String toParams() {
		StringBuilder builder = new StringBuilder();
		Field[] fileds = this.getClass().getDeclaredFields();
		Method method;
		try {
			for (Field field : fileds) {
				if (field.getName().equals("path")
						|| field.getName().contains("FINAL")) {
					continue;
				}
				StringBuilder nameBuilder = new StringBuilder(field.getName());
				nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "")
						.toUpperCase().charAt(0));
				method = this.getClass().getMethod("get" + nameBuilder);
				// if (method.invoke(this) != null) {
				try {
					builder.append("&"
							+ field.getName()
							+ "="
							+ URLEncoder.encode(method.invoke(this).toString(),
									"utf-8"));
				} catch (UnsupportedEncodingException e) {
					builder.append("&" + field.getName() + "="
							+ method.invoke(this));
					e.printStackTrace();
				}
				// }
			}

			if(!"".equals(builder.toString())){
				builder.replace(0, 1, "?");
			}
			// builder.deleteCharAt(0);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
