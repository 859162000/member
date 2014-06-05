package com.wanda.ccs.member.ap2in;

import javax.servlet.http.HttpServletRequest;

public class AuthUserHelper {
	
	public final static String USER_PRFILE_HEADER_KEY = "ADK-User-Profile";
	
	public static final String VERSION_HEADER_KEY = "adk-ver";
	

	
	public final static String FROM_AP2_KEY = "Member.FROM_AP2";
	
	private static ThreadLocal<UserProfile> currentThreadUser = new ThreadLocal<UserProfile>();

	public static UserProfile getUser() {
		return currentThreadUser.get();
	}
	
	protected static void setUser(UserProfile user) {
		currentThreadUser.set(user);
	}
	
	protected static void removeUser() {
		currentThreadUser.remove();
	}
	
	public static String getUserHeader(HttpServletRequest request) {
		return request.getHeader(AuthUserHelper.USER_PRFILE_HEADER_KEY);
	}
	
	/**
	 * 设置当前请求是否来自AP2系统
	 * @param request
	 */
	public static void setFromAp2(HttpServletRequest request) {
		request.setAttribute(FROM_AP2_KEY, Boolean.TRUE);
	}
	
	public static boolean isFromAp2(HttpServletRequest request) {
		Boolean fromAp2 = (Boolean)request.getAttribute(FROM_AP2_KEY);
		if(fromAp2 != null && fromAp2 == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isFromAp2Header(HttpServletRequest request) {
		 String ap2VersionValue = request.getHeader(AuthUserHelper.VERSION_HEADER_KEY);
		 if(ap2VersionValue != null && ap2VersionValue.length() > 0) {
			 return true;
		 }
		 else {
			 return false;
		 }
	}
	
}
