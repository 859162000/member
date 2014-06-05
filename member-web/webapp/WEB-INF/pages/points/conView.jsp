<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmPagingTablePointCont" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmRuleTiecketList" action="doRuleView">
		<input type="hidden" name="ruleId" />
		<adk:func name="doRuleView" param="ruleId" submit="yes" />
</adk:form>
<div class="table" id="listTablePointCont"><display:table
		name="CONRESULT" id="conRow" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${CONRESULT.startIndex+conRow_rowNum}</display:column>
		<display:column title="影城" class="ac" value="${conRow.INNER_NAME}">
		</display:column>
		<display:column title="营业日" class="ac" value="${conRow.BIZ_DATE}" format="{0,date,yyyy-MM-dd}">
		</display:column>
		<display:column title="订单号" class="ac" value="${conRow.ORDER_ID}">
		</display:column>
		<display:column title="卖品名" class="ac" value="${conRow.GOODS_NAME}">
		</display:column>
		<display:column title="购买金额" class="ac" value="${conRow.AMONT}">
		</display:column>
		<display:column title="购买数量" class="ac" value="${conRow.GOODS_COUNT}">
		</display:column>
		<display:column title="定级积分" class="ac" value="${conRow.LEVEL_POINT}">
		</display:column>
		<display:column title="是否退货" class="ac" >
			<c:choose>
				<c:when test="${conRow.TRANS_TYPE eq 'N' }">购货</c:when>
				<c:otherwise>退货</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="非定级积分" class="ac" value="${conRow.ACTIVITY_POINT}">
		</display:column>
		<display:column title="可兑换积分" class="ac" value="${conRow.POINT}">
		</display:column>
		<display:column title="交易时间" class="ac" value="${conRow.TRANS_TIME}" format="{0,date,yyyy-MM-dd HH:mm:ss }">
		</display:column>
		<display:column title="特殊积分规则ID" class="ac" >
			<c:forEach items="${ fn:split(conRow.EXT_POINT_RULE_ID, ',') }" var="ruleId">
				<a href="javascript:${adk:func('doRuleView')}(${ruleId});">${ruleId }</a> <br/>
			</c:forEach>
		</display:column>
		
	</display:table>
</div>
<adk:include var="ruleView" view="ruleView">t</adk:include>
${ruleView }
<script language="javascript">
$("#listTablePointCont").displayTagAjax({
	form : '${adk:encodens("frmPagingTablePointCont")}'
});
</script>