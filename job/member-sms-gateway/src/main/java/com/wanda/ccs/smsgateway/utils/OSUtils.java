package com.wanda.ccs.smsgateway.utils;

public abstract class OSUtils {

	public static String getEnterKey() {
		return WINDOW_ENTER;
	}

	// Window 下 Telnet 回车标志
	private static String WINDOW_ENTER = "\r\n";
}
