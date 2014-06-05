<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCloseWinlet" action="closeWinlet">
	<adk:func name="closeWinlet" submit="yes" />
</adk:form> 
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dcontent">
<adk:form name="frmList" action="saveSelUser">
	<div class="table" id="segmentListTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="" style="width:20px;" headerClass="ar"
			class="ar">
			<input type="radio" name="ids" value="${row.id}"/>
		</display:column>
		<display:column title="账号" class="ac" >
			<c:choose>
				<c:when test="${row.fromNc eq 'N'}">${row.loginId}</c:when>
				<c:when test="${row.fromNc eq 'Y'}">${row.rtx}</c:when>
			</c:choose>
		</display:column>
		<display:column title="EHR用户" class="ac" sortable="true" sortProperty="fromNc">
			<c:choose>
				<c:when test="${row.fromNc eq 'N'}">否</c:when>
				<c:when test="${row.fromNc eq 'Y'}">是</c:when>
			</c:choose>
		</display:column>
		<display:column title="姓名" class="ac" >
			<c:choose>
				<c:when test="${row.fromNc eq 'N'}">${row.userName}</c:when>
				<c:when test="${row.fromNc eq 'Y'}">${row.userName}</c:when>
			</c:choose>
		</display:column>
		<display:column title="级别" class="ac" >
			<c:choose>
				<c:when test="${row.userLevel eq 'G'}">院线</c:when>
				<c:when test="${row.userLevel eq 'R'}">区域</c:when>
				<c:when test="${row.userLevel eq 'C'}">影城</c:when>
				<c:when test="${row.userLevel eq 'T'}">中心店</c:when>
			</c:choose>
		</display:column>
		<display:column title="区域" class="ac" >
			${DIMS[m.DIMTYPE_AREA][row.region]}
		</display:column>
		<display:column title="影城" class="ac" >
			${row.tCinema.innerName}
		</display:column>
	</display:table>
	</div>
	<div align="center">
	<button type="submit" class="btn save">保存</button>
	<button type="button" class="btn cancel" onclick="${adk:func('closeWinlet')}();">取消</button>
	</div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
$("#segmentListTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
</script>
