package com.wanda.mrb.intf.exception;

@SuppressWarnings("serial")
public class BaseException extends Exception {
	protected String respCode = ""; // 异常响应码
	protected String message = "";  // 异常说明
//	protected ResponseCode responseCodeObj = null;

	/**
	 * BaseException constructor comment.
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public BaseException(String respCode, String message) {
		super(respCode);
		this.respCode = respCode;
		this.message = message;

//		try {
//			this.responseCodeObj = ResponseCode.load(
//					(java.sql.Connection) null, respCode);
//		} catch (Exception ex) {
//		}
//		if (responseCodeObj == null) {
//			responseCodeObj = ResponseCode.getUndefinedRespCode();
//		}
	}

	/**
	 * Insert the method's description here. Creation date: (2003-2-25 18:41:15)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRespCode() {
		return respCode;
	}
	public java.lang.String getMessage() {
		return message;
	}

//	public java.lang.String getUserMessage() {
//		return responseCodeObj.getUserMessage();
//	}
//
//	public java.lang.String getSystemMessage() {
//		return responseCodeObj.getSystemMessage();
//	}
//
//	public String toString() {
//		if (responseCodeObj.getSystemMessage().equalsIgnoreCase("")) {
//			return responseCodeObj.getUserMessage();
//		} else {
//			return responseCodeObj.getSystemMessage();
//		}
//	}

}
