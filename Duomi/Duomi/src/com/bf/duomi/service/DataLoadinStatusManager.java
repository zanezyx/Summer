package com.bf.duomi.service;

import java.util.HashMap;
import java.util.Map;

import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.util.GsonUtil;
import com.bf.duomi.util.NetListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 数据加载状态管理类
 * @author Sunny
 *
 */
public class DataLoadinStatusManager implements NetListener {
    
    public boolean isDebug;
    public boolean isCanShowDialog;//能否弹窗显示？
    public boolean isLoading;
    
    public static final String SUCCESS_RESULT = "000";//返回成功
    public static final String FAILD_RESULT = "001";//返回失败
    public static final String FAILD_PARAMS = "002";//参数错误
    public static final String FAILD_USER_VITIFY = "003";//用户身份验证失败
    public static final String ABNORMAL_DATABASE = "004";//数据库异常
    public static final String VITIFY_CODE = "005";//验证码有误
    public static final String INVITE_MSG_FAILD = "006";//文件上传异常
    public static final String FAILD_INVITE_CODE = "007";//邀请码错误
    public static final String FAILD_MSG_SEND = "008";//短信邀请码发送失败
    
    public static final String FAILED_AUTHOR =  "010" ;//权限认证失败
    public static final String UNCORRECT_LOGIN_INFO = "011";//登录信息不正确
    
    public static final String IS_REGISTERED = "021";//该用户已经注册
    public static final String IS_NOT_REGISTERED = "022";//该用户还未注册
    
    
    private String tag = "DataLoadingStatusManager";
    private String title;
    private Context mContext;
    
    public void setTitle(String title) {
        this.title = title;
    }

//    private static DataLoadinStatusManager statusManager;
    
    private Map<String,String> initResultMap;
    
    private ProgressDialog loadingDialog;
    
    private DataLoadinStatusManager()
    {
        
    }
    
    /**
     * 
     * @param context
     * @param tag
     * @return
     */
    public static DataLoadinStatusManager getInstance(Context context,String tag)
    {
        DataLoadinStatusManager statusManager = new DataLoadinStatusManager();
        statusManager.mContext = context;
        statusManager.initResultMap();
        statusManager.initLoadingDialog();
        statusManager.isDebug = Boolean.parseBoolean("true");
        statusManager.isCanShowDialog = false;
        statusManager.isLoading = false;
        statusManager.title = "加载";
        statusManager.tag = tag;
        return statusManager;
    }
    
    /**
     * 设置是否能够弹窗显示
     * @param isCanShowDialog
     */
    public void setCanShowDialog(boolean isCanShowDialog) {
        this.isCanShowDialog = isCanShowDialog;
    }

    /**
     * 初始化结果标志map
     */
    public void initResultMap()
    {
        initResultMap = new HashMap<String, String>();
        initResultMap.put(SUCCESS_RESULT, "返回成功");
        initResultMap.put(FAILD_RESULT, "返回失败");
        initResultMap.put(FAILD_PARAMS, "参数错误");
        initResultMap.put(FAILD_USER_VITIFY, "用户身份验证失败");
        initResultMap.put(ABNORMAL_DATABASE, "数据库异常");
        initResultMap.put(VITIFY_CODE, "验证码有误");
        initResultMap.put(INVITE_MSG_FAILD, "文件上传异常");
        initResultMap.put(FAILD_INVITE_CODE, "邀请码错误");
        initResultMap.put(FAILD_MSG_SEND, "短信邀请码发送失败");
        initResultMap.put(FAILED_AUTHOR, "权限认证失败");
        initResultMap.put(UNCORRECT_LOGIN_INFO, "登录信息不正确");
        
    }
    
    /**
     * 初始化加载框
     */
    public void initLoadingDialog()
    {
        loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setCanceledOnTouchOutside(false); //不能点击
        loadingDialog.setTitle("正在"+title+"!!");
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("sdfsdfsdf");
    }
    
    public void showToast(String msg)
    {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onComplete(String respondCode, BaseRequest request,
            BaseResponse response) {
        if(isCanShowDialog)
        {
            showToast(initResultMap.get(response.getResult()));
        }
        printLog("result:"+GsonUtil.getGsonInstance().toJson(response));
        printLog("-----"+title+"完成");
    }

    @Override
    public void onLoading() {
        if(isCanShowDialog&&isLoading==false)
        {
            loadingDialog.show();
            isLoading = true;
        }
        printLog(title+"ing-----");
    }

    @Override
    public void onPrepare() {
        printLog("准备"+title+"-----");
    }

    @Override
    public void onCancel() {
        onLoadSuccess(null);
        printLog("-----取消"+title);
    }

    @Override
    public void onFailed(Exception ex,BaseResponse response) {
        printLog(ex.toString());
        printLog("-----加载数据失败");
    }

    @Override
    public void onLoadSuccess(BaseResponse response) {
//        if(isCanShowDialog)
//        {
//            loadingDialog.dismiss();
//            isLoading = false;
//        }
    }
    
    public void printLog(String msg) {
        
        if(isDebug)
        {
            Log.i(tag,msg);
        }
            
    }


}
