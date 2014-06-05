<%@ page import="java.util.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%
String groups = request.getParameter("groups");
String context = request.getParameter("context");
String[] groupArray = groups.split(",");
Set<String> groupSet = new HashSet<String>();
Collections.addAll(groupSet, groupArray);

if(groupSet.contains("jquery")) { 
%>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-1.8.3.js"></script>
<%
} if(groupSet.contains("jquery-ui")) {
%>
	<link rel="stylesheet" href="<%=context%>/jslib/jquery-ui/css/redmond/jquery-ui-1.9.2.custom.css">
	<script type="text/javascript" src="<%=context%>/jslib/jquery-ui/jquery-ui-1.9.2.custom.js"></script>
<%
} if(groupSet.contains("ap2-jquery")) {
%>
	<script language="javascript" src="<%=context%>/jslib/jquery-ap2/jquery-1.7.1.min.js"></script>
<%
} if(groupSet.contains("ap2-jquery-ui")) {
%>
	<link rel="stylesheet" id="css" type="text/css" href="<%=context%>/jslib/jquery-ap2/jquery-ui-1.8.16.custom.css" />
	<link rel="stylesheet" id="css" type="text/css" href="<%=context%>/jslib/jquery-ap2/tools.tooltip.css" />
	<script language="javascript" src="<%=context%>/jslib/jquery-ap2/jquery-ui-1.8.16.custom.min.js"></script>
	<script language="javascript" src="<%=context%>/jslib/jquery-ap2/tools.tooltip-1.1.3.min.js"></script>
<%
} if(groupSet.contains("layout")) {
%>
	<script src="<%=context%>/jslib/layout/jquery.layout.min.js" type="text/javascript"></script>
<%
} if(groupSet.contains("jqgrid")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/jqgrid/css/ui.jqgrid.css" />
	<script src="<%=context%>/jslib/jqgrid/i18n/grid.locale-cn.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
<%	
} if(groupSet.contains("ztree")) {
%>	
	<link rel="stylesheet" href="<%=context%>/jslib/ztree/css/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=context%>/jslib/ztree/jquery.ztree.all-3.5.js"></script>
<%		
} if(groupSet.contains("validation")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/validation/css/validator.css" />
	<script type="text/javascript" src="<%=context%>/jslib/validation/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/validation/localization/messages_cn.js"></script>
<%		
} if(groupSet.contains("fileupload")) {
%>
	<script src="<%=context%>/jslib/fileupload/jquery.ui.widget.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/fileupload/jquery.iframe-transport.js" type="text/javascript"></script>
	<script src="<%=context%>/jslib/fileupload/jquery.fileupload.js" type="text/javascript"></script>
<%		
} if(groupSet.contains("datetime")) {
%>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-ui/jquery.ui.datepk.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-ui/jquery.ui.datepicker-zh-CN.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-ui-timepicker-addon.js"></script>	
<%		
} if(groupSet.contains("multiselect")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/multiselect/jquery.multiselect.css"></link>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/multiselect/jquery.multiselect.filter.css"></link>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/multiselect/multiselect_prettify.css"></link>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jslib/multiselect/multiselect_style.css"></link>
	<script type="text/javascript" src="<%=context%>/jslib/multiselect/multiselect_prettify.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/multiselect/jquery.multiselect.min.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/multiselect/jquery.multiselect.filter.min.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/multiselect/multiselect_link_ajax.js"></script>
<%	
} if(groupSet.contains("common")) {
%>	
	<script type="text/javascript" src="<%=context%>/jslib/jquery.ba-resize.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-form.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/jquery-deserialize.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/json2.js"></script>
	<link rel="stylesheet" href="<%=context%>/jslib/common/css/common.css" type="text/css">
	<script type="text/javascript" src="<%=context%>/jslib/common/common.js"></script>	
<%		
} if(groupSet.contains("wrender")) {
%>
	<script type="text/javascript">
		$.wrender = $.wrender || {};
		$.wrender.context = '<%=context%>'; //must just after wrender.js be included and before other includes of wrender scripts 
	</script>
	<%--<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender-all.min.js"></script> --%>
	
	<%-- 下面为调试用的源码 --%>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.common.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.date.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.datetime.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.select.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.composite.js"></script>
	<script type="text/javascript" src="<%=context%>/jslib/wrender/wrender.tree.js"></script>
<%
}
%>