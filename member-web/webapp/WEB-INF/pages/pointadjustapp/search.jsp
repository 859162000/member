<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmSearch" action="search">
<div style="overflow: auto;">
	<table border="0" width="100%" >
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap"  width="20%">手机号:</td>
				<td align="left" nowrap="nowrap" width="20%">
					<input:text name="mobile" ></input:text>
				</td>
				<td align="right"  >会员编号:</td>
				<td align="left" >
					<input:text name="memberNo" ></input:text>
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">审批状态:</td>
				<td align="left" nowrap="nowrap" >
					<select name="approve">
						  <option value ="">--请选择审批状态--</option>
						  <option value="W">待审批</option>
						  <option value="Y">审批通过</option>
						  <option value="N">审批未通过</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<button class="btn search" value="查询" type="submit">查询</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
</adk:form>
