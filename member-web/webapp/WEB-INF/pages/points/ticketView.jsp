<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmPagingTablePointTicket" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmRuleConList" action="doRuleView">
		<input type="hidden" name="ruleId" />
		<adk:func name="doRuleView" param="ruleId" submit="yes" />
</adk:form>
<div class="table" id="listTablePointTicket"><display:table
		name="TICKETRESULT" id="ticketRow" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${CONRESULT.startIndex+ticketRow_rowNum}</display:column>
		<display:column title="影城" class="ac" value="${ticketRow.INNER_NAME}">
		</display:column>
		<display:column title="订单号" class="ac" value="${ticketRow.ORDER_ID}">
		</display:column>
		<display:column title="影片名称" class="ac" value="${ticketRow.FILM_NAME }">
		</display:column>
		<display:column title="厅号" class="ac" value="${ticketRow.HALL_NUM }">
		</display:column>
		<display:column title="票类名" class="ac" value="${ticketRow.TICKET_TYPE_NAME}">
		</display:column>
		<display:column title="票号" class="ac" value="${ticketRow.TICKET_NO}">
		</display:column>
		<display:column title="金额" class="ac" value="${ticketRow.AMOUNT}">
		</display:column>
		<display:column title="是否退票" class="ac" >
			<c:choose>
				<c:when test="${ticketRow.REFUND_FLAG eq 'N' }">购票</c:when>
				<c:when test="${ticketRow.REFUND_FLAG eq 'Y' }">退票</c:when>
			</c:choose>
		</display:column>
		<display:column title="定级积分" class="ac" value="${ticketRow.LEVEL_POINT}">
		</display:column>
		<display:column title="非定级积分" class="ac" value="${ticketRow.ACTIVITY_POINT}">
		</display:column>
		<display:column title="可兑换积分" class="ac" value="${ticketRow.POINT}">
		</display:column>
		<display:column title="交易时间" class="ac" value="${ticketRow.TRANS_TIME}" format="{0,date,yyyy-MM-dd HH:mm:ss }">
		</display:column>
		<display:column title="营业日" class="ac" value="${ticketRow.BIZ_DATE}" format="{0,date,yyyy-MM-dd}">
		</display:column>
		<display:column title="放映时间" class="ac" value="${ticketRow.SHOW_TIME}" format="{0,date,yyyy-MM-dd HH:mm:ss }">
		</display:column>
		<display:column title="特殊积分规则ID" class="ac" >
			<c:forEach items="${ fn:split( ticketRow.EXT_POINT_RULE_ID, ',') }" var="ruleTICEKTId">
				<c:if test="${not empty ruleTICEKTId}">
					<a href="javascript:${adk:func('doRuleView')}(${ruleTICEKTId});">${ruleTICEKTId }</a> <br/>
				</c:if>
			</c:forEach>
		</display:column>
	</display:table>
</div>
<adk:include var="ruleView" view="ruleView">t</adk:include>
${ruleView }
<script language="javascript">
$("#listTablePointTicket").displayTagAjax({
	form : '${adk:encodens("frmPagingTablePointTicket")}'
});
</script>