package com.wanda.ccs.jobhub.member.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberJobUtils {
	
	/**
	 * 验证手机号是否合法
	 * 
	 * @param mobile
	 *            手机号
	 * @return
	 */
	public static boolean checkMobileIsTrue(String mobile) {
		// 校验手机号是否合法
		String regExp = "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobile);
		if (!m.find()) {
			return false;
		} else {
			return true;
		}

	}
	
	/**
	 * 校验邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean chechEmail(String email) {
		// 校验邮箱地址是否合法
		String regExp = "(^[\\S]{0,0}$)|(^[\\S]*@[\\S]*.[\\S]*$)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(email);
		if (!m.find()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 校验是否是数据
	 * @param targetObject
	 * @return
	 */
	public static boolean checkIsNumber(String targetObject) {
		Pattern p = Pattern
				.compile("^\\d+(\\.\\d+)?$|^((-\\d+)|(0+))$|^((-\\d+)+(.[0-9]{1,4}))?$|^((\\d+)+(.[0-9]{1,4}))?$");
		Matcher matcher = p.matcher(((String) targetObject).replace(",", ""));
		return matcher.matches();
		
	}
}
