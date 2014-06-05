package com.wanda.member.upgrade.exception;

public class UnSupportLevelException extends Exception {

	public UnSupportLevelException(String memLevel) {
		super(memLevel+" is unsupport");
	}

}
