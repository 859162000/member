<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCreateMemberLog"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmPagingMemberLog" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dot-border3" style="margin-top:5px">会员变更记录</div>

<div class="table" id="listTableMemberLog"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		
		<display:column class="ac" title="手机号" value="${row.memberMobile}"/>
		<display:column class="ac" title="注册影城" value="${row.tCinema.innerName}"/>
		
		<display:column class="ac" title="生日" value="${row.birthday}" format="{0,date,yyyy-MM-dd HH:mm:ss }"/>
		<display:column title="会员状态" class="ac">
			<c:choose>
				<c:when test="${row.memberStatus eq 1 }">
					有效
				</c:when>
				<c:when test="${row.memberStatus eq 0 }">
					禁用
				</c:when>
				<c:when test="${row.memberStatus == '-1' }">
					冻结
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
			
		</display:column>
		<display:column title="是否删除" class="ac">
			<c:choose>
				<c:when test="${row.memberDeleted == 1 }">
					已删除
				</c:when>
				<c:when test="${row.memberDeleted == 0 }">
					未删除
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</display:column>
		<display:column  class="ac" title="变更时间" value="${row.createdDate}" format="{0,date,yyyy-MM-dd HH:mm:ss }"/>
		<display:column  class="ac" title="变更人" value="${row.changedBy}" />
	</display:table>
</div>
<script language="javascript">
$("#listTableMemberLog").displayTagAjax({
	form : '${adk:encodens("frmPagingMemberLog")}'
});

</script>
