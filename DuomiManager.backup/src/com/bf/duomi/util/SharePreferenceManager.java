package com.bf.duomi.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * SharePreference管理器
 * 
 * @author Sunny
 * 
 */
public class SharePreferenceManager {

	public static final String UPDATETIME_PREFRENCE_DATABASE = "UPDATE_DB";// 更新时间shareprefernce

	public static final String USER_PREFRENCE_DATABASE = "USER_DB";// 用户shareprefernce

	public static final String USER_NICKNAME = "NICK_NAME";// ///用户昵称----sunny
															// 2014.4.7

	public static final String SING_IN = "SING_IN";// 保存当天签到

	public static final String LUCKY_AREA = "LUCKY_AREA";// //地区选择(省市区)

	public static final String PUSH_MSG = "PUSH_MSG";// 消息推送

	public static final String PUSH_MSG_PROPERTY = "PUSH_MSG_PROPERTY";// 物业中心规章制度消息推送

	public static final String PUSH_MSG_SETTING = "PUSH_MSG_SETTING";// 推送设置
	public static final String SOFTWARE_SETTING = "SOFTWARE_SETTING";// 设置shareprefernce

	public static final String UPLOAD_DATA = "UPLOAD_DATA";// 数据上报
	private static Context mContext;

	public static final String UUID = "UUID";
	public static final String USERID = "userId";
	public static final String USER_PASSWORD = "password";
	public static final String IS_AUTO_LOGIN = "isautologin";
	public static final String DEFAULT_ADDRESS_ID = "defaultaddressid";
	public static final String DEFAULT_ADDRESS = "defaultaddress";
	public static final String NICKNAME = "NICKNAME";
	public static final String CAN_SPEAK = "CAN_SPEAK";
	public static final String ISSHOW = "ISSHOW";

	private SharePreferenceManager() {

	}

	/**
	 * 在AppAPPliction中配置一次就行
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		if (mContext == null) {
			mContext = context;
		}
	}

	public static SharedPreferences getSharePreference(final String name) {
		SharedPreferences sp = mContext.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return sp;
	}

	// 获取会员编号
	public static String getUUID() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getString(UUID,
				"");
	}

	// 保存会员编号
	public static void saveUUID(String uuid) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putString(UUID, uuid).commit();
		setShowLoginDialog(true);
	}
	
	//是否弹出登录提示对话框
	public static void setShowLoginDialog(boolean isShow){
		SharePreferenceManager.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
		.putBoolean(ISSHOW, isShow).commit();
		
	}
	//是否弹出登录提示对话框
		public static boolean getShowLoginDialog(){
			return SharePreferenceManager.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE).
					getBoolean(ISSHOW, true);
			
		}

	// 获取userId
	public static String getUserId() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getString(
						SharePreferenceManager.USERID, null);
	}
	
	// 获取userId
	public static String getUserPassword() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getString(
						SharePreferenceManager.USER_PASSWORD, null);
	}
	
	// 获取userId
	public static boolean isAutoLogin() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getBoolean(
						SharePreferenceManager.IS_AUTO_LOGIN, false);
	}
	
	// 获取default address string
	public static String getUserDefaultAddress() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getString(
						SharePreferenceManager.DEFAULT_ADDRESS, null);
	}
	
	// 获取default address id
	public static Integer getUserDefaultAddressId() {
		return SharePreferenceManager.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE).getInt(
						SharePreferenceManager.DEFAULT_ADDRESS_ID, -1);
	}
	
	//保存default address string
	public static void saveDefaultAddress(String str) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putString(SharePreferenceManager.DEFAULT_ADDRESS, str).commit();

	}
	
	//保存default address id
	public static void saveDefaultAddressId(int id) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putInt(SharePreferenceManager.DEFAULT_ADDRESS_ID, id).commit();

	}
	
	//保存userId
	public static void saveUserId(String str) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putString(SharePreferenceManager.USERID, str).commit();

	}

	// 保存user password
	public static void saveUserPassword(String str) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putString(SharePreferenceManager.USER_PASSWORD, str).commit();

	}
	
	// 保存是否自动登录
	public static void saveUserAutoLogin(boolean isAutoLogin) {
		SharePreferenceManager
				.getSharePreference(
						SharePreferenceManager.USER_PREFRENCE_DATABASE).edit()
				.putBoolean(SharePreferenceManager.IS_AUTO_LOGIN, isAutoLogin).commit();

	}
	
	//保存昵称
	public static void saveNickName(String nickName){
		SharePreferenceManager
		.getSharePreference(
				SharePreferenceManager.USER_PREFRENCE_DATABASE)
		.edit()
		.putString(SharePreferenceManager.NICKNAME,
				nickName).commit();
	}
	
	//获取NickName
	public static String getNickName(){
		return SharePreferenceManager.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE).getString(
				NICKNAME, "");
	}
	
	
	//保存是否可以发言
	public static void saveSpeak(int state){
		SharePreferenceManager.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE)
		.edit()
		.putInt(SharePreferenceManager.CAN_SPEAK, state).commit();
	}
	
	//获取发言状态
	public static int getSpeak(){
		return SharePreferenceManager.getSharePreference(SharePreferenceManager.USER_PREFRENCE_DATABASE)
				.getInt(SharePreferenceManager.CAN_SPEAK, 0);
	}

	public static void save(String dbName, Object object) {
		Field[] fileds = object.getClass().getDeclaredFields();
		Method method;
		try {
			Editor editor = getSharePreference(dbName).edit();
			for (Field field : fileds) {
				StringBuilder nameBuilder = new StringBuilder(field.getName());
				nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "")
						.toUpperCase().charAt(0));
				method = object.getClass().getMethod("get" + nameBuilder);
				if (method.invoke(object) != null) {
					editor.putString(field.getName(), method.invoke(object)
							+ "");
				}
			}
			editor.commit();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/****
	 * 保存注册，登录信息
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setPrefString(Context context, final String key,
			final String value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	

}
