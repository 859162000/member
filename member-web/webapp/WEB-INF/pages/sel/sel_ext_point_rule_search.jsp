<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmSearch" action="search">
	<table border="0">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap" width="80">编码:</td>
				<td align="left" nowrap="nowrap" width="180">
					<input:text name="code"></input:text>
				</td>
				<td align="right" nowrap="nowrap" width="80">名称:</td>
				<td align="left" nowrap="nowrap" width="180">
					<input:text name="name"></input:text>
				</td>
			</tr>
			<%-- <tr>
			<td nowrap="nowrap" align="right">从:</td>
			<td nowrap="nowrap" align="left">
				<input:date name="startDtime" end="endDtime"  format="yyyy-MM-dd" error="" readOnly="true"></input:date>
				</td>
				<td nowrap="nowrap" align="center">到</td>
				<td nowrap="nowrap" align="left">
				<input:date name="endDtime" start="startDtime"  format="yyyy-MM-dd" error="" readOnly="true"></input:date>
			  </td>
			</tr> --%>
			<tr>
				<td nowrap="nowrap" align="right">开始时间:</td>
				<td nowrap="nowrap" align="left">
					<input:date name="sStartDate" end="eStartDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					~
					<input:date name="eStartDate" start="sStartDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					<button type="button"class="btn clear" id="clearStartDate">清除</button>
				</td>
				<td nowrap="nowrap" align="right">结束时间:</td>
				<td nowrap="nowrap" align="left">
					<input:date name="sEndDate" end="eEndDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					~
					<input:date name="eEndDate" start="sEndDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					<button type="button"class="btn clear" id="clearEndDate">清除</button>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="right">
					<button class="btn search" value="查询" type="submit">查询</button>
				</td>
			</tr>
		</tbody>
	</table>
</adk:form>
<adk:include view="list" var="list">x</adk:include>
${list}
<script language="javascript">

$("#clearStartDate").click(function(){
	$("input[name='sStartDate']").val("");
	$("input[name='eStartDate']").val("");
});
$("#clearEndDate").click(function(){
	$("input[name='sEndDate']").val("");
	$("input[name='eEndDate']").val("");
});
</script>