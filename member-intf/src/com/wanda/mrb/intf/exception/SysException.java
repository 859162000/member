package com.wanda.mrb.intf.exception;

/**
 * This class is the user-defined system exception in exception handler module
 */
public class SysException extends BaseException {
	private static final long serialVersionUID = 1L;
	protected	Exception	originalEx = null;

	/**
	 * SystemException constructor comment.
	 * @param s java.lang.String
	 */
	public SysException(String respCode, String message) {
		super(respCode, message);
	}
	/**
	 * Insert the method's description here.
	 * @param respCode java.lang.String
	 * @param originalException java.lang.Exception
	 */
	public SysException(String respCode, Exception originalException) {
		super(respCode, originalException.getMessage());
		this.originalEx = originalException;
	}
	/**
	 * Insert the method's description here.
	 * @return java.lang.Exception
	 */
	public java.lang.Exception getOriginalException() {
		return originalEx;
	}
}
