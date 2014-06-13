<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求

String context;
if(fromAp2) {
	context = "/ap2/proxy";
} else {
	context = request.getContextPath();
}
%>

<% if(fromAp2 == false) { %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>客群管理帮助文档</title>

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
				<td colspan="4" height="25px">客群管理帮助文档</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="50%" align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						客群管理系统主要通过添加筛选条件计算出会员客群。会员客群中分为普通客群和复合客群。<br/>
						普通客群：基本客群记录<br/>
						复合客群：将普通客群通过交、并、补操作组合成复合客群<br/>
					</p>
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						添加客群记录
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/segment/add.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						选择创建客群分为：普通客群、复合客群。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img src="<%=context %>/segment/images/segment/select.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						选择创建普通客群后进入客群创建界面。<br />
						排序：统计客群时根据哪个字段对筛选出来的会员进行排序。<br />
						对比组占比：将计算出来的客群数量根据占比进行计算得出。<br />
						可选条件：客群计算时主要筛选条件。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/segment/segment.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						普通客群创建成功后会显示在客群列表中。并在操作中列出当前可执行的操作：查看、导出、修改、删除。<br/>
						查看：查看当前客群记录。<br/>
						导出：客群计算完成后进行导出。<br/>
						修改：对创建的客群进行修改或重新计算。<br/>
						删除：删除客群记录。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/segment/segment_list.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						客群计算完成后点击导出按钮进入导出界面，在导出界面中勾选与会员相关的字段并点击确定进行导出。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img src="<%=context %>/segment/images/segment/export.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						对当前的客群记录进行修改操作并通过重新计算按钮触发客群计算操作。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/segment/edit.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						复合客群通过将多个普通客群进行交、并、补操作最后计算出复合客群。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img src="<%=context %>/segment/images/segment/comb.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						当客群计算出现错误时可通过进入编辑界面点击重新计算来触发客群计算操作。如果客群计算多次失败请与管理员联系。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img src="<%=context %>/segment/images/segment/error.png" alt="添加营销活动" />
					<img src="<%=context %>/segment/images/segment/edit.png" alt="添加营销活动" />
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