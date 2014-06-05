<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmcancelRuleView" action="cancelRuleView">
	<adk:func name="cancelRuleView" submit="yes" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">特殊积分规则</div>
<table border="0" style="width: 100%;" >
		<tbody>
			<tr><td align="left">
				<button type="button" onclick="${adk:func('cancelRuleView')}();"class="btn cancel">关闭</button>
				</td>
			</tr>
		</tbody>
	</table>
<table border="0" style="width: 100%;">
		<tbody>
			<tr>
				<td width="20%" align="right">规则编码:</td>
				<td width="30%" align="left">${RULE.code }</td>
				<td width="20%" align="right">规则名称:</td>
				<td width="30%" align="left">${RULE.name }</td>
			</tr>
			<tr>
				<td width="20%" align="right">开始时间:</td>
				<td width="30%" align="left"> 
				<fmt:formatDate value="${RULE.startDtime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				<td width="20%" align="right">结束时间:</td>
				<td width="30%" align="left">
					<fmt:formatDate value="${RULE.endDtime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right">状态:</td>
				<td width="30%" align="left">
					${DIMS['1002'][RULE.status]}
				</td>
				<td width="20%" align="right"></td>
				<td width="30%" align="left"></td>
			</tr>
		</tbody>
</table>