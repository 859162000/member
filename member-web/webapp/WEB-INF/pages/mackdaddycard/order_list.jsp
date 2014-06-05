<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<div class="dcontent">
<adk:form name="frmCreateInfo"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmEditInfo" action="doEdit">
	<input type="hidden" name="id">
	<adk:func name="doEdit" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmView" action="doView">
	<input type="hidden" name="id">
	<adk:func name="doView" param="id" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.MACK_DADDY_CARD_ORDER_EDIT)}"/>
<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<c:if test="${editable}">
	<div>
		<button type="button" onclick="${adk:func('doCreate')}();" class="btn add">新增申请单</button>
	</div>
	</c:if>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="申请影城" sortProperty="tCinema.innerName" sortable="true" property="tCinema.innerName" class="ac" />
		<display:column title="申请数量" sortProperty="numberOfCards" sortable="true" property="numberOfCards" class="ac" />
		<c:if test="${m.status eq m.DIMTYPE_CARD_ORDER_STATUS_P}">
		<display:column title="开始卡号" sortProperty="startNo" sortable="true" property="startNo" class="ac" />
		<display:column title="结束卡号" sortProperty="endNo" sortable="true" property="endNo" class="ac" />
		</c:if>
		<display:column title="申请时间" sortProperty="submitTime"  sortable="true" property="submitTime" class="ac" format="{0,date,yyyy-MM-dd HH:mm:ss}" />
		<display:column title="申请人" sortProperty="submitBy" sortable="true" property="submitBy" class="ac" />
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">
			<c:choose>
				<c:when test="${(row.status eq m.DIMTYPE_CARD_ORDER_STATUS_F or row.status eq m.DIMTYPE_CARD_ORDER_STATUS_X) and row.createdBy eq u.id and editable}">
					<button type="button" class="btn edit" onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
					<button type="button" class="btn del"
						onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn csearch" onclick="${adk:func('doView')}(${row.id});">查看</button>
				</c:otherwise>
			</c:choose>
		</display:column>
	</display:table></div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
$("#listTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
$("#exportTmpl").click(function(){
	var url = "${adk:resproxy('exptExcl')}";
	window.location = url;
});
</script>
