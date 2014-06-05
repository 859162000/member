<%@ page import="java.util.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%
String groups = request.getParameter("groups");
List groupList = java.util.Arrays.asList(groups.split(","));
String context = request.getContextPath();
if(groupList.contains("jquery")) { 
%>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-1.8.3.js"></script>
<%
} if(groupList.contains("jquery-ui")) {
%>
	<link rel="stylesheet" href="<%=context%>/jslib/jquery-ui/css/redmond/jquery-ui-1.9.2.custom.css">
	<script type="text/javascript" src="<%=context%>/jslib/jquery-ui/jquery-ui-1.9.2.custom.js"></script>
<%
} if(groupList.contains("layout")) {
%>
	<script src="<%=context%>/jslib/layout/jquery.layout.min.js" type="text/javascript"></script>
<%
} if(groupList.contains("jqgrid")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/jqgrid/css/ui.jqgrid.css" />
	<script src="<%=context%>/jslib/jqgrid/i18n/grid.locale-cn.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
<%	
} if(groupList.contains("ztree")) {
%>	
	<link rel="stylesheet" href="<%=context%>/jslib/ztree/css/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=context%>/jslib/ztree/jquery.ztree.all-3.5.js"></script>
<%		
} if(groupList.contains("validation")) {
%>
		<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/validation/css/validator.css" />
		<script type="text/javascript" src="<%=context%>/jslib/validation/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=context%>/jslib/validation/localization/messages_cn.js"></script>
<%		
} if(groupList.contains("fileupload")) {
%>
	<script src="<%=context%>/jslib/fileupload/jquery.ui.widget.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/fileupload/jquery.iframe-transport.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/fileupload/jquery.fileupload.js" type="text/javascript"></script>
<%	
} if(groupList.contains("common")) {
%>	
	<script type="text/javascript" src="<%=context%>/jslib/json2.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery.ba-resize.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-form.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-deserialize.js"></script>
	<link rel="stylesheet" href="<%=context%>/jslib/common/css/common.css" type="text/css">
	<script type="text/javascript" src="<%=context%>/jslib/common/common.js"></script>	
<%
}
%>