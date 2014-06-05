<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<adk:include var="search" view="search">x</adk:include>
<adk:include var="list" view="list">x</adk:include>
<adk:form name="frmselStatus" action="selStatus">
	<input type="hidden" name="status" />
	<adk:func name="selStatus" param="status" submit="yes" />
</adk:form>
	<div class="adk_tab2"><ul>  
		<c:choose>
			<c:when test="${empty m.status or m.status eq m.DIMTYPE_CARD_ORDER_STATUS_A}">
				<li id="current"><a><span>待审核</span></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:${adk:func('selStatus')}('${m.DIMTYPE_CARD_ORDER_STATUS_A}')"><span>待审核</span></a></li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${m.status eq m.DIMTYPE_CARD_ORDER_STATUS_P}">
				<li id="current"><a><span>审核通过</span></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:${adk:func('selStatus')}('${m.DIMTYPE_CARD_ORDER_STATUS_P}')"><span>审核通过</span></a></li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${m.status eq m.DIMTYPE_CARD_ORDER_STATUS_F}">
				<li id="current"><a><span>审批拒绝</span></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:${adk:func('selStatus')}('${m.DIMTYPE_CARD_ORDER_STATUS_F}')"><span>审批拒绝</span></a></li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${m.status eq m.DIMTYPE_CARD_ORDER_STATUS_X}">
				<li id="current"><a><span>已取消</span></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:${adk:func('selStatus')}('${m.DIMTYPE_CARD_ORDER_STATUS_X}')"><span>已取消</span></a></li>
			</c:otherwise>
		</c:choose>
	</ul></div>
	<div class="adk_tab2box">
			${search} ${list}
	</div>