<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
String context;
if(fromAp2) {
	context = "/ap2/proxy";
}
else {
	context = request.getContextPath();
}
%>

<% if(fromAp2 == false) { %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>营销活动</title>

<jsp:include page="include.jsp">
<jsp:param name="context" value="<%=context%>"/>
<jsp:param name="groups" value="ap2-jquery,ap2-jquery-ui,,jqgrid,validation,ztree,datetime,multiselect,common,wrender"/>
</jsp:include>
<% } else { %>
<jsp:include page="include.jsp">
<jsp:param name="context" value="<%=context%>"/>
<jsp:param name="groups" value="jqgrid,validation,ztree,datetime,multiselect,common,wrender"/>
</jsp:include>
<% } %>

<style type="text/css">

</style>

<script type="text/javascript">

</script>

<% if(fromAp2 == false) { %>
</head>
<body>
<center>
<% } %>
<div id="contentArea" style="width:100%;">
	<table width="100%" align="center" border="1" cellspacing="1" cellpadding="1" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header">
				<td colspan="4" height="25px">营销活动明细统计帮助文档</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="50%" align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						营销活动明细统计主要用于对营销活动的反馈进行每天的实时统计和汇总。其中目标客群需要在客群管理中进行添加和计算完成后绑定到营销活动中。<font color="red">（注：当前统计是假定目标客群全部发送成功）</font>
					</p>
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						当营销活动开始执行后会在营销活动统计列表中生成一条汇总记录。同时可以通过查看按钮进入明细统计界面查看每天的实时营销情况统计。
						推荐人数：绑定目标客群中的推荐人数。如果目标客群中设置了对比组（推荐人数=客群目标客群数-对比组人数），否则为全部目标客群人数。<br/>
						对比组人数：绑定目标客群中的对比组人数。如果目标客群中设置了对比组（对比组人数=目标客群数-推荐人数），否则对比组人数为零。<br/>
						推荐响应率：根据推荐响应规则进行统计得出推荐响应人数与总推荐人数的占比。<font color="red">（注：如果未设置推荐响应规则时，则根据推荐人数从票房和卖品中同时统计得出推荐响应人数）</font><br/>
						对比组响应率：根据推荐响应规则进行统计得出对比组响应人数与总对比组人数的占比。<font color="red">（注：如果目标客群未设置对比组则占比为零）</font><br/>
						总响应率：根据推荐人数从票房和卖品中同时统计得出推荐响应人数与总推荐人数的占比。<font color="red">（注：如果未设置推荐响应规则，总响应率与推荐响应率一样）</font><br/>
						推荐响应收入：根据推荐响应规则进行统计得出推荐响应人数的收入。<font color="red">（注：如果未设置推荐响应规则时，则根据推荐人数从票房和卖品中同时统计得出推荐响应收入）</font><br/>
						对比组响应收入：根据推荐响应规则进行统计得出对比组响应收入。<font color="red">（注：如果目标客群未设置对比组则收入数据为零）</font><br/>
						总收入：根据推荐人数从票房和卖品中同时统计得出推荐响应收入。<font color="red">（注：如果未设置推荐响应规则，总响应率与推荐响应收入值一样）</font>
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/campaign/detail_list.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						通过营销活动明细界面可以查看每天的实时营销响应统计数据。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="480px" src="<%=context %>/segment/images/campaign/detail.png" alt="添加营销活动" />
				</td>
			</tr>
		</tbody>
	</table>
</div>
<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>