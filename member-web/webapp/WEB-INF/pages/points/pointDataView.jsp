<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmTicketTransList" action="doSearchTicketData">
		<input type="hidden" name="ticketType" />
		<adk:func name="doSearchTicketData" param="ticketType" submit="yes" />
</adk:form>
<adk:form name="frmConTransList" action="doSearchConData">
		<input type="hidden" name="conType" />
		<adk:func name="doSearchConData" param="conType" submit="yes" />
</adk:form>
<adk:form name="frmCancelPointData" action="cancelPointSaleView">
	<adk:func name="cancelPointSaleView" submit="yes" />
</adk:form>

<div class="dot-border3" style="margin-top:5px">会员积分订单详细</div>
<table border="0" style="width: 100%;" >
		<tbody>
			<tr><td align="left">
				<button type="button" onclick="${adk:func('cancelPointSaleView')}();"class="btn cancel">关闭</button>
				</td>
			</tr>
		</tbody>
	</table>
<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			
			<c:choose>
				<c:when test="${'T' eq currentSel }">
				<li name="trans" id="current">
				</c:when>
				<c:otherwise>
				<li name="trans">
				</c:otherwise>
			</c:choose>
				<a id="tab_trans" href="javascript:${adk:func('doSearchTicketData')}('T')"><span>购票详细</span></a>
			</li>
			
			<c:choose>
				<c:when test="${'G' eq currentSel }">
				<li name="goods" id="current">
				</c:when>
				<c:otherwise>
				<li name="goods">
				</c:otherwise>
			</c:choose>
				<a id="tab_goods" href="javascript:${adk:func('doSearchConData')}('G')"><span>卖品详细</span></a>
			</li>
		</ul>
	</div>
<adk:include var="ticketView" view="ticketView">t</adk:include>
${ticketView }
<adk:include var="conView" view="conView">t</adk:include>
${conView }