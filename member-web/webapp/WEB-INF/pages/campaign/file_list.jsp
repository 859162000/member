<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<link rel="stylesheet" id="css" type="text/css"
		href="${adk:resurl('/style/warning.ccs')}" />
<adk:form name="frmCreateAbatchmem" action="doCreatUpLoad">
	<input type="hidden" name="status">
	<adk:func name="doCreatUpLoad" param="status" submit="yes" />
</adk:form> 
<adk:form name="frmdoView" action="doView">
	<input type="hidden" name="id">
	<adk:func name="doView" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmUpLoadTemp"
	action="doUpLoadTemp">
	<adk:func name="doUpLoadTemp" submit="yes" />
</adk:form>

<adk:form name="frmCreateAbatchmem"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form>
<adk:form name="frmPagingAbatchMem" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="table" id="listTableAbatchMem"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="波次名称" class="ac">
			${row.NAME }
		</display:column>
		<display:column title="波次编码" class="ac">
			${row.CODE }
		</display:column>
		<display:column title="文件名称" class="ac">
			${row.FILE_NAME }
		</display:column>
		<display:column title="操作人" class="ac">
			${row.CREATE_BY}
		</display:column>
		<display:column title="操作时间" class="ac">
			<fmt:formatDate value="${row.CREATE_DATE}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</display:column>
		<display:column title="文件状态" class="al">
			${DIMS['1017'][row.FILE_STATUS]}
			<c:if test="${row.FILE_STATUS eq 'S' and row.WARNING gt 0}">
				<span class="warning" title="有警告信息">&nbsp;</span>
			</c:if>
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">&nbsp;
			<button type="button" class="btn csearch"
				onclick="${adk:func('doView')}(${row.FILE_ATTACH_ID});">查看</button>&nbsp;&nbsp;
		</display:column>
	</display:table>
</div>

<adk:include var="fileDatalist" view="list" winlet="${m.abatchErreLogLet}">t</adk:include>
<div>
	${ fileDatalist}
</div>
<script language="javascript">
$("#listTableAbatchMem").displayTagAjax({
	form : '${adk:encodens("frmPagingAbatchMem")}'
});
</script>
