package com.wanda.ccs.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	/**
	 * 通过正则表达式验证
	 * @param str
	 * 		     要验证的字段
	 * @param regular
	 * 		  正则表达式
	 * @return
	 * 若符合正则表达式，则返回true；否则返回false
	 */
	public static boolean validationByRegular(String str, String regular){
		Pattern p = Pattern.compile(regular);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
