<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ page import="javax.servlet.http.*,java.util.*" %>

<%
StringBuilder buf = new StringBuilder();
//Enumeration names = session.getAttributeNames();
Enumeration names = request.getHeaderNames();
while(names.hasMoreElements()) {
	String attrName = (String)names.nextElement();
	String value = request.getHeader(attrName);
	buf.append(attrName).append(" : ");
	buf.append(value).append("<br/>\n");
}
%>

<%-- 
//import com.aggrepoint.adk.plugin.WinletUserProfile;
WinletUserProfile profile = (WinletUserProfile)session.getAttribute("com.aggrepoint.ae.plugin.AEUserEngine.UserProfile");
Enumeration names2 = profile.getPublicPropertyNames();
while(names2.hasMoreElements()) {
	String attrName = (String)names2.nextElement();
	buf.append(attrName).append(" : ");
	String propValue = profile.getProperty(attrName);
	buf.append(propValue).append("<br/>\n");
}
--%>


<h1>Request Header</h1>
<h2>
<%=buf.toString() %>
</h2>
