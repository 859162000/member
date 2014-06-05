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
<adk:form name="frmPagingLevel" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员等级历史</div>

<div class="table" id="listTableLevel"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		
		<display:column title="操作时间" value="${row.setTime }" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac"/>
		<display:column title="操作类型" class="ac" >
			${DIMS['217'][row.resonType]}
		</display:column>
		<display:column title="当前级别" class="ac">
			${row.memLevel }星
		</display:column>
		<display:column title="当前级别有效期" class="ac">
			<c:choose>
				<c:when test="${ row.memLevel==1 }">永久有效</c:when>
				<c:otherwise>
					<fmt:formatDate value="${row.expireDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="上一级别" class="ac">
			${row.orgMemLevel }星
		</display:column>
		<display:column title="上一级别有效期" class="ac">
			<c:choose>
				<c:when test="${ row.orgMemLevel==0 or row.orgMemLevel==1 }">永久有效</c:when>
				<c:otherwise>
					${row.orgExpireDate}
				</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="定级积分" class="ac">
			${row.levelPoint }
		</display:column>
	</display:table>
</div>
<script language="javascript">
$("#listTableLevel").displayTagAjax({
	form : '${adk:encodens("frmPagingLevel")}'
});

</script>
