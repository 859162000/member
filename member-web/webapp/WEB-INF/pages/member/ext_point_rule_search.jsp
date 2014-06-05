<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmSearch" action="search">
	<table width="100%" height="18" border="0">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap" >编码:</td>
				<td align="left" nowrap="nowrap">
					<input:text name="code"></input:text>
				</td>
				<td align="right" nowrap="nowrap">名称:</td>
				<td align="left" nowrap="nowrap">
					<input:text name="name"></input:text>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">开始时间:</td>
				<td nowrap="nowrap" align="left">
					<input:date id="sStartDate" name="sStartDate" end="eStartDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					~
					<input:date id="eStartDate" name="eStartDate" start="sStartDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					<button type="button"class="btn clear" id="clearExtStartDate">清除</button>
				</td>
				<td nowrap="nowrap" align="right">结束时间:</td>
				<td nowrap="nowrap" align="left">
					<input:date id="sEndDate" name="sEndDate" end="eEndDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					~
					<input:date id="eEndDate" name="eEndDate" start="sEndDate"  format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" readOnly="true" />
					<button type="button"class="btn clear" id="clearExtEndDate">清除</button>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap" >积分状态:</td>
				<td align="left" nowrap="nowrap" >
				<input:select name="status" value="${query.status}" class="select_wid">
             		<input:option value="">--请选择--</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_PLAN}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_PLAN]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_PUBLISH}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_PUBLISH]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_EXECUTE}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_EXECUTE]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_INACTIVE}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_INACTIVE]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_FINISH}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_FINISH]}</input:option>
             	</input:select>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<button class="btn search" value="查询" type="submit">查询</button>
				</td>
			</tr>
		</tbody>
	</table>
</adk:form>
<script language="javascript">

$("#clearExtStartDate").click(function(){
	$("#sStartDate").val("");
	$("#eStartDate").val("");
});
$("#clearExtEndDate").click(function(){
	$("#sEndDate").val("");
	$("#eEndDate").val("");
});
</script>