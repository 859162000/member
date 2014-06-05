<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmTicketPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dcontent">
	<div class="table" id="listTicketTable">
		<display:table name="pageResult" id="row" htmlId="idTable" cellspacing="0" cellpadding="0" style="width:100%" export="false" requestURI="">
			<display:column title="No" style="width:20px;" headerClass="ar" class="ar" value="">${pageResult.startIndex+row_rowNum}</display:column>
			<display:column title="销售单号">${row.ORDER_NUM }</display:column>
			<display:column title="券类型">${row.TYPE_NAME }</display:column>
			<display:column title="使用方式">${row.USE_TYPE }</display:column>
			<display:column title="券序列号">${row.SEQUENCE_IN_ORDER }</display:column>
			<display:column title="到期日期" format="{0,date,yyyy-MM-dd HH:mm:ss }" value="${row.EXPIRY_DATE }"></display:column>
			<display:column title="已发放"></display:column>
			<display:column title="会员ID">${row.MEMBER_ID }</display:column>
			<display:column title="会员名称">${row.NAME }</display:column>
			<display:column title="手机">${row.MOBILE }</display:column>
		</display:table>
	</div>
</div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
</script>
