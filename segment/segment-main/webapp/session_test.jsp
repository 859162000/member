<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ page import="javax.servlet.http.*,
java.util.*,
com.aggrepoint.adk.plugin.WinletUserProfile" %>

<%-- Could be used AP2 Web Application Only --%>

<%
StringBuilder buf = new StringBuilder();
Enumeration names = session.getAttributeNames();
while(names.hasMoreElements()) {
	String attrName = (String)names.nextElement();
	buf.append(attrName).append(" : ");
	Object attrObj = session.getAttribute(attrName);
	buf.append(attrObj.getClass().getCanonicalName()).append("<br/>\n");
}
%>

<% 
StringBuilder userProfileBuf = new StringBuilder();
WinletUserProfile profile = (WinletUserProfile)session.getAttribute("com.aggrepoint.ae.plugin.AEUserEngine.UserProfile");
if(profile != null) {
	Enumeration names2 = profile.getPublicPropertyNames();
	while(names2.hasMoreElements()) {
		String attrName = (String)names2.nextElement();
		userProfileBuf.append(attrName).append(" : ");
		String propValue = profile.getProperty(attrName);
		userProfileBuf.append(propValue).append("<br/>\n");
	}
}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Test Only</title>
</head>
<body>
<h1>Session Values</h1>
<h2>
<%=buf.toString() %>
</h2>

<h1>User Profile Values</h1>
<h2>
<%=userProfileBuf.toString() %>
</h2>
</body>
</html>
