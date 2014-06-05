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
				<td align="right" nowrap="nowrap" width="80">账号:</td>
				<td align="left" nowrap="nowrap" width="180">
					<input:text name="loginId" />
				</td>
				<td align="right">
					<button class="btn search" value="查询" type="submit">查询</button>
				</td>
			</tr>
		</tbody>
	</table>
</adk:form>
<adk:include view="list" var="list">x</adk:include>
${list}