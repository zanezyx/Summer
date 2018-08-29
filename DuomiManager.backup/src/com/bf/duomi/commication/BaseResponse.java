/**
 * 
 */
package com.bf.duomi.commication;


/**
 * @author galis
 * @date: 2014-3-7-下午4:48:36
 */
public class BaseResponse {

	/**
	 * 请求码列表
	 * 
	 * @author galis
	 * @date: 2014-3-7-下午4:49:27
	 */

	public String result = "";

	public String message = "";
	
	public String errorCode = "";

	public String resString;
	// 积分字段，并不是每个接口都有该字段
	public String pointContent;
	

	/**
	 * @return the pointContent
	 */
	public String getPointContent() {
		return pointContent;
	}

	
	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	/**
	 * @param pointContent
	 *            the pointContent to set
	 */
	public void setPointContent(String pointContent) {
		this.pointContent = pointContent;
	}
	
	
	

	/**
     * 
     */
	public BaseResponse() {
		super();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void parseResString()
	{
		
	}
	

}
