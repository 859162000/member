package com.wanda.ccs.member.ap2in;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AuthUserFilter implements Filter {
	
	private static Log log = LogFactory.getLog(AuthUserFilter.class);
	
	private UserProfile testUserProfile =  null;

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			String ups = getInitParam(filterConfig, "testUserProfile");
			if(ups != null) {
				testUserProfile = new UserProfile(ups);
				log.info("Test User Profile loaded from the web.xml configuration! loginId=" + testUserProfile.getId());
			}
		} 
		catch (NamingException nfe) {
			log.info("Test User Profile Could not be retrieved from JNDI! Cause by: " + nfe.getMessage());
		}
		catch (UnsupportedEncodingException e) {
			throw new ServletException("Failed to create testUserProfile!", e);
		}

	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response= (HttpServletResponse) servletResponse;
		
		
		UserProfile userProfile = null;
		
		try {
			boolean fromAp2 = AuthUserHelper.isFromAp2Header(request); //通过头信息中标志，判断请求是否是通过AP2发送过来的
			if(fromAp2) {
				AuthUserHelper.setFromAp2(request);//设置Request标志，标志该请求是否是通过AP2发送的
	
				String userProfileStr = AuthUserHelper.getUserHeader(request);
				if(userProfileStr != null) {
					userProfile = new UserProfile(userProfileStr);
					AuthUserHelper.setUser(userProfile);
				}
			}
			
			//如果设置测试账号，设置其为当前登录用户
			if(userProfile == null && testUserProfile != null) {
				userProfile = testUserProfile;
				AuthUserHelper.setUser(userProfile);
				log.info("The test User Profile has been set in current request. loginId=" + testUserProfile.getId());
			}
			
			if(isAjaxRequest(request) && userProfile == null) {
				//不存在用户信息，表示用户已经退出或者session失效。需要进行退出登录操作。
				outUnauthorized(response);//对于AJAX请求，输出特殊登出标志
			}
			else {
				//TODO 建议加入redirect机制或者，需要区分是否是集成请求(fromAp2==true)采取不同措施。
				chain.doFilter(request, response);
			}
		}
		finally {
			//清除当前request用户信息
			if(userProfile != null) {
				AuthUserHelper.removeUser();
			}
		}
	}
	
	public void destroy() { 

	}

	/**
	 * Get parameter from the web.xml.
	 * 
	 * @param paramName the parameter name, if it not exists, the name "jndiParamName" will be try again.
	 *        and the value of "jndiParamName" will try to lookup from the JNDI.
	 * @return
	 * @throws ServletException
	 */
	private String getInitParam(FilterConfig filterConfig, String paramName) throws NamingException {
		String paramValue = filterConfig.getInitParameter(paramName);
		if(paramValue == null) {
			//If can not get from the paramName, try append 'jdni' before the paramName and retrieve again.
			String first = paramName.substring(0, 1).toUpperCase();
			String jndiParamName = "jndi" + first + paramName.substring(1);
			String jndiName = filterConfig.getInitParameter(jndiParamName);
			Context initContext = new InitialContext();
			paramValue = (String)initContext.lookup(jndiName);
		}
		
		if(paramValue != null) {
			paramValue = paramValue.trim();
		}
		
		return paramValue;
	}
	
	
	/**
	 * 输出特殊登出标志, 数据为JSON格式
	 * 内容如：{level:"ERROR", message:"logout"}
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request)	 {
		String uri = request.getRequestURI();
		if(uri != null && uri.endsWith(".do")) {
			return true;
		}
		
		return false;
	}
	
	public void outUnauthorized(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setStatus(403);
		
		PrintWriter out = response.getWriter();
		try {
			out.println("{\"level\":\"ERROR\", \"message\":\"logout\"}");
		} finally {
			out.close(); 
		}

	}
	
}
