package com.wanda.mrb.intf.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBean {
	Pattern p = null;
	Matcher m = null;
	String str1 = "^[0-9]{1,20}$ "; // 校验是否全由数字组成
	String str2 = "^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$ "; // 校验登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串
	String str3 = "^(\\w){6,20}$ "; // 校验密码：只能输入6-20个字母、数字、下划线
	String str4 = "^[+]{0,1}(\\d){1,3}[   ]?([-]?((\\d)|[   ]){1,12})+$ ";// 校验普通电话、传真号码：可以“+”开头，除数字外，可含有“-”
	String str5 = "^[+]{0,1}(\\d){1,3}[   ]?([-]?((\\d)|[   ]){1,12})+$ "; // 校验手机号码
	String str6 = "^[a-zA-Z0-9   ]{3,6}$ "; // 校验邮政编码
	String str7 = "^[0-9.]{1,20}$ "; // ip
	String str8 = "^\\d+$ "; // 非负整数
	String str9 = "^[0-9]*[1-9][0-9]*$ "; // 正整数
	String str10 = "^[A-Za-z]+$ "; // 由26个英文字母组成的字符串
	String str11 = "^\\w+$ "; // 由数字、26个英文字母或者下划线组成的字符串
	String str12 = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$ "; // email
	String str13 = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$ ";// url
	String str14 = "[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2} ";
	String str15 = "^((((19|20)(([02468][048])|([13579][26]))\\-02\\-29))|((20[0-9][0-9])|(19[0-9][0-9]))\\-((((0[1-9])|(1[0-2]))\\-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))\\-31)|(((01,3-9])|(1[0-2]))\\-(29|30)))))$ ";	// date 如2004－06－06,非常精确，如2004－06－32可以查出非法，高兴吧！！
	String str16 = "^(\\d{4})\\-(\\d{2})\\-(\\d{2})   (\\d{2}):(\\d{2}):(\\d{2})$ "; // date and time 如2004－11－12 12：10：16
	String str17 = "^([\u4e00-\u9fa5]*) ";// 汉字

	public RegexBean() {
	}

	public boolean isDate(String str) {
		p = Pattern.compile(str15);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isEmail(String str) {
		p = Pattern.compile(str12);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isDateAndTime(String str) {
		p = Pattern.compile(str16);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isUserName(String str) {
		p = Pattern.compile(str2);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isPassword(String str) {
		p = Pattern.compile(str3);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isPhone(String str) {
		p = Pattern.compile(str4);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isMobile(String str) {
		p = Pattern.compile(str5);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isPost(String str) {
		p = Pattern.compile(str6);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isIP(String str) {
		p = Pattern.compile(str7);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isChinese(String str) {
		p = Pattern.compile(str17);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isURL(String str) {
		p = Pattern.compile(str13);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public boolean isDigit(String str) {
		p = Pattern.compile(str1);
		m = p.matcher(str);
		boolean b = m.matches();
		if (b)
			return true;
		else
			return false;
	}

	public static void main(String[] args) {
		RegexBean bb = new RegexBean();
		String str = "仗剑动 ";
		System.out.println(bb.isChinese(str));
	}
}