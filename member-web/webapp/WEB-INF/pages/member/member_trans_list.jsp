<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmPagingTrans" action="dosearchTransAndGoodsPage">
	<adk:func name="dosearchTransAndGoodsPage" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmTransList" action="searchGoodsOrTrans">
		<input type="hidden" name="transType" />
		<adk:func name="searchGoodsOrTrans" param="transType" submit="yes" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员交易历史</div>
<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			
			<c:choose>
				<c:when test="${'T' eq current }">
				<li name="trans" id="current">
				</c:when>
				<c:otherwise>
				<li name="trans">
				</c:otherwise>
			</c:choose>
				<a id="tab_trans" href="javascript:${adk:func('searchGoodsOrTrans')}('T')"><span>票房历史</span></a>
			</li>
			
			<c:choose>
				<c:when test="${'G' eq current }">
				<li name="goods" id="current">
				</c:when>
				<c:otherwise>
				<li name="goods">
				</c:otherwise>
			</c:choose>
				<a id="tab_goods" href="javascript:${adk:func('searchGoodsOrTrans')}('G')"><span>卖品历史</span></a>
			</li>
		</ul>
	</div>
<div class="table" id="listTableTrans"><display:table
		name="TRANSRESULT" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="交易类型" class="ac">
			<c:choose>
				<c:when test="${row.totalAmount >= 0 }">售卖</c:when>
				<c:otherwise>退货</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="POS订单号" class="ac">
		${row.orderId }
		</display:column>
		<display:column title="消费金额" class="ac">
		${row.totalAmount }
		</display:column>
		<display:column  class="ac" title="购买时间" value="${row.transTime }" format="{0,date,yyyy-MM-dd HH:mm:ss }"/>
		<display:column title="影城" class="ac">
		${CINEMAMAP[row.cinemaInnerCode] }
		</display:column>
	</display:table>
</div>

<script language="javascript">
$("#listTableTrans").displayTagAjax({
	form : '${adk:encodens("frmPagingTrans")}'
});

</script>
