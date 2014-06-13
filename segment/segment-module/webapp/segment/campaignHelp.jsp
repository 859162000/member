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
<title>营销活动管理</title>

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
				<td colspan="4" height="25px">营销活动管理帮助文档</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="50%" align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						营销活动管理系统主要用于对营销活动的反馈进行每天的实时统计和汇总。其中目标客群需要在客群管理中进行添加和计算完成后绑定到营销活动中。统计方式分为：票房营销统计、卖品营销统计、会员营销统计三种。<font color="red">（注：当前统计是假定目标客群全部发送成功）</font>
						票房营销统计：在推荐影片相关信息时使用。<br/>
						卖品营销统计：在推荐卖品相关信息时使用。<br/>
						会员营销统计：当没有配置推荐响应规则时，统计时系统默认会根据绑定的营销客群通过票房和卖品两种情况同时统计得出结果。<br/>
					</p>
				</td>
			</tr>
			<td align="left">
				<p style="font-size: 14px;padding-left: 10px;">
					添加营销活动记录。
				</p>
			</td>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/campaign/add.png" alt="添加营销活动" />
				</td>
			</tr>
			<td align="left">
				<p style="font-size: 14px;padding-left: 10px;">
					填写营销活动基本信息。<br/>
					<font color="red">注：绑定的目标客群要在客群管理系统中进行配置计算。</font>
				</p>
			</td>
			<tr>
				<td align="center">
					<img width="1000px" height="450px" src="<%=context %>/segment/images/campaign/base_info.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						推荐响应配置可分为三种情况：票房交易、卖品交易、无推荐响应配置。<br/>
						票房交易：在推荐影片相关信息时使用。<font color="red">注意：不能和卖品推荐同时出现。</font><br/>
						卖品交易：在推荐卖品相关信息时使用。<font color="red">注意：不能和影片推荐同时出现。</font><br/>
						无推荐响应配置（会员交易）：当没有配置推荐响应规则时，统计时系统默认会根据绑定的营销客群通过票房和卖品两种情况同时统计得出结果。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="450px" src="<%=context %>/segment/images/campaign/cri.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						填写完营销活动信息后，如果没有配置推荐响应规则时会在保存时弹出提示框给予提示。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="450px" src="<%=context %>/segment/images/campaign/save.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						保存成功后会返回到营销活动列表界面。在操作中列出当前可进行的操作。从左至右依次为：查看、生效、修改、删除。<br/>
						查看：查看当前营销活动记录。<br/>
						生效：提交当前营销活动信息状态为生效，主要用于将于该活动绑定的资源进行确定后进行统计计算。<font color="red">注意：只有当记录状态为生效后才能进行统计</font><br/>
						修改：对当前营销活动记录进行修改。<br/>
						删除：删除当前营销活动记录。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/campaign/edit.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						提交成功后当前营销活动状态将变为生效。在操作中列出当前可进行的操作。从左至右依次为：查看、生效、修改、删除。<br/>
						查看：查看当前营销活动记录。<br/>
						生效：提交当前营销活动信息状态为生效，主要用于将于该活动绑定的资源进行确定后进行统计计算。<font color="red">注意：只有当记录状态为生效后才能进行统计</font><br/>
						修改：对当前营销活动记录进行修改。<br/>
						删除：删除当前营销活动记录。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/campaign/commit.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						当营销活动到达开始执行时，记录状态会变成<font color="red">执行</font>。这时在操作中列出当前可进行的操作从左至右依次为：查看、修改。<br/>
						查看：查看当前营销活动记录。<br/>
						修改：对当前正在执行的营销活动记录的结束日期进行修改。
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="420px" src="<%=context %>/segment/images/campaign/add_time.png" alt="添加营销活动" />
				</td>
			</tr>
			<tr>
				<td align="left">
					<p style="font-size: 14px;padding-left: 10px;">
						对当前的营销活动结束日期进行修改，主要用于延长活动的统计时间。<font color="red">注意：结束日期不能小于原始结束日期</font>
					</p>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img width="1000px" height="450px" src="<%=context %>/segment/images/campaign/edit_time.png" alt="添加营销活动" />
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