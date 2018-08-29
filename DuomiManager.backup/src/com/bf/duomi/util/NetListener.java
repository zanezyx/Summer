package com.bf.duomi.util;

import com.bf.duomi.commication.BaseRequest;
import com.bf.duomi.commication.BaseResponse;

/**
 * 网络请求监听接口
 * @author Administrator
 *
 */
public interface NetListener {
	
	public void onPrepare();
    public void onLoading();
    public void onComplete(String respondCode,BaseRequest request,BaseResponse response);
    public void onLoadSuccess(BaseResponse response);
    public void onCancel();
    public void onFailed(Exception ex,BaseResponse response);

}
