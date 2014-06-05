<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCreateCardRel"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmPagingCardRel" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员卡包</div>

<div class="table" id="listTableCardRel"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="卡类型" class="ac">
			<c:choose>
				<c:when test="${row.tCard.cardValueType eq 'M' }">储值卡</c:when>
				<c:when test="${row.tCard.cardValueType eq 'T' }">计次卡</c:when>
				<c:otherwise>权益卡</c:otherwise>
			</c:choose>
		</display:column>	
		<display:column title="卡号" class="ac">
			${row.tCard.cardNumber }
		</display:column>
		<display:column title="发卡影城" class="ac">
			${CARDCINEMA[row.tCard.issueCinema ]}
		</display:column>
		<display:column title="卡名称" class="ac">
			${row.tCard.cardTypeName }
		</display:column>
		<display:column title="卡状态" class="ac">
			${DIMS['146'][row.tCard.cardStatus]}
		</display:column>
		<display:column title="卡有效期" class="ac">
		<fmt:formatDate value="${row.tCard.expiryDate }"  pattern="yyyy-MM-dd"/>
		</display:column>
		<display:column title="绑定时间" class="ac">
			<fmt:formatDate value="${row.bindTime }"  pattern="yyyy-MM-dd HH:mm:ss"/>
		</display:column>
		<display:column title="绑定方式" class="ac">
			${DIMS['219'][row.bindType]}
		</display:column>
	</display:table>
</div>
<script language="javascript">
$("#listTableCardRel").displayTagAjax({
	form : '${adk:encodens("frmPagingCardRel")}'
});
$("#selectAllCardRel").selectAll({parentId:'listTableCardRel'});

</script>
