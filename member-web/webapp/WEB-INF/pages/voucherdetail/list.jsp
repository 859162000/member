<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCreateLevel"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmPagingVoucher" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员券包</div>

<div class="table" id="listTableVoucher"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="券类型" class="ac">
			<c:choose>
				<c:when test="${row.USE_TYPE eq 'M' }">代金</c:when>
				<c:when test="${row.USE_TYPE eq 'T' }">兑换</c:when>
				<c:otherwise>折扣</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="券名称" class="ac">
			${row.NAME }
		</display:column>				
		<display:column title="券号" class="ac" >
			${row.VOUCHER_NUMBER }
		</display:column>
		<display:column title="券发放日期" value="${row.CREATE_DATE}" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac">
		</display:column>
		<display:column title="券到期日期" value="${row.EXPIRY_DATE}" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac">
		</display:column>
	</display:table>
</div>
<script language="javascript">
$("#listTableVoucher").displayTagAjax({
	form : '${adk:encodens("frmPagingVoucher")}'
});
$("#selectAllVoucher").selectAll({parentId:'listTableVoucher'});

</script>
