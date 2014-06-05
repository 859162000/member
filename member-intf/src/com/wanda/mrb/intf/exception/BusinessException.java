package com.wanda.mrb.intf.exception;

public class BusinessException extends BaseException {
	private static final long serialVersionUID = 1L;

	public BusinessException(String respCode, String message) {
		super(respCode, message);
	}
}
