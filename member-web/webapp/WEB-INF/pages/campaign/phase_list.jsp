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
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT) and not campaignIsEnd and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>
<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
	  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
	  	阶段列表
	  </div>
	</div>
	<c:if test="${editable}">
		<div>
			<button type="button" onclick="${adk:func('doCreate')}();" class="btn add">添加阶段</button>
		</div>
	</c:if>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="编码" sortProperty="c.code" sortable="true" property="code" class="ac" />
		<display:column title="名称" sortProperty="c.name" sortable="true" property="name" class="ac" />
		<display:column title="开始时间" sortProperty="c.startDate" sortable="true" property="startDate" class="ac" format="{0,date,yyyy-MM-dd}" />
		<display:column title="结束时间" sortProperty="c.endDate"  sortable="true" property="endDate" class="ac" format="{0,date,yyyy-MM-dd}" />
		<display:column title="状态" sortProperty="c.status" sortable="true" value="${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][row.status]}" class="ac" />
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">
			<c:choose>
				<c:when test="${editable}">
					<button type="button" class="btn edit"
						onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
					<c:if test="${empty row.tCmnActivities}">
					<button type="button" class="btn del"
						onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
					</c:if>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn csearch"
						onclick="${adk:func('doView')}(${row.id});">查看</button>
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
</script>
