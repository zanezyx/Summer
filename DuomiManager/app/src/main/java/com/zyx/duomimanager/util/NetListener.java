package com.zyx.duomimanager.util;


import com.zyx.duomimanager.commication.BaseRequest;
import com.zyx.duomimanager.commication.BaseResponse;

/**
 * 网络请求监听接口
 * @author Administrator
 *
 */
public interface NetListener {
	
	public void onPrepare();
    public void onLoading();
    public void onComplete(String respondCode, BaseRequest request, BaseResponse response);
    public void onLoadSuccess(BaseResponse response);
    public void onCancel();
    public void onFailed(Exception ex, BaseResponse response);

}
