﻿<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmPagingExgPoint" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员积分账户</div>

<div class="table" id="listTableExgPoint">
	<display:table name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="获得年份" class="ac">${row.gainYear}年</display:column>
		<display:column title="过期时间" value="${row.expireTime}" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac"/>
		<display:column title="积分余额" class="ac">${row.pointBalance}</display:column>		
		<display:column title="是否过期" class="ac">
			<c:choose>
				<c:when test="${row.expired eq '0'}">否</c:when>
				<c:otherwise>是</c:otherwise>
			</c:choose>
		</display:column>
	</display:table>
</div>
<script language="javascript">
$("#listTableExgPoint").displayTagAjax({
	form : '${adk:encodens("frmPagingExgPoint")}'
});

</script>
