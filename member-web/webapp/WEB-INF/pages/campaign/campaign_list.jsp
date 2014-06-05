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
<adk:form name="frmDisabledActivity" action="disabledActivity">
	<input type="hidden" name="id">
	<adk:func name="disabledActivity" param="id" submit="yes" />
</adk:form>
<adk:form name="frmEnabledActivity" action="enabledActivity">
	<input type="hidden" name="id">
	<adk:func name="enabledActivity" param="id" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT)}"/>
<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<c:if test="${editable}">
		<div>
			<button type="button" onclick="${adk:func('doCreate')}();" class="btn add">添加活动</button>
		</div>
	</c:if>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="编码" sortProperty="c.code" sortable="true" property="code" class="ac" style="width:50px;"/>
		<display:column title="名称" sortProperty="c.name" sortable="true" property="name" class="ac" style="width:150px;"/>
		<display:column title="统计渠道" sortProperty="c.channel" sortable="true" value= "${DIMS[m.DIMTYPE_CAMPAIGN_CHANNEL][row.channel]}" class="ac" style="width:80px;"/>
		<display:column title="开始时间" sortProperty="c.startDate"  sortable="true" property="startDate" class="ac" format="{0,date,yyyy-MM-dd}" style="width:80px;"/>
		<display:column title="结束时间" sortProperty="c.endDate"  sortable="true" property="endDate" class="ac" format="{0,date,yyyy-MM-dd}" style="width:80px;"/>
		<display:column title="状态" sortProperty="c.status" sortable="true" value="${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][row.status]}" class="ac" style="width:50px;"/>
		<display:column title="活动类型" sortProperty="c.type" sortable="true" value="${DIMS[m.DIMTYPE_CAMPAIGN_TYPE][row.type]}" class="ac" style="width:80px;"/>
		<display:column title="活动范围" class="ac" maxLength="20" style="width:120px;">
			<%-- <c:choose>
				<c:when test="${row.allCinema}">所有影城</c:when>
				<c:otherwise>
				<c:set var="cinemas" value="" />
					<c:forEach items="${row.tCampaignCinemas}" var="campaignCinema" varStatus="step">
						${campaignCinema.tCinema.shortName}
						<c:if test="${not step.last}">、</c:if>
					</c:forEach>
				</c:otherwise>
			</c:choose> --%>
			${row.cinemas}
		</display:column>
		<display:column title="阶段及波次" class="ac" maxLength="30">
			<%-- <c:forEach items="${row.tCmnPhases}" var="phase">
				${phase.code}|${phase.name}|${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][phase.status]}
				<c:if test="${not empty phase.tCmnActivities}"> : </c:if>
				<c:forEach items="${phase.tCmnActivities}" var="activity" varStatus="step" >
					${activity.code}|${activity.name}|${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][activity.status]}
					<c:if test="${not step.last}">、</c:if> 
				</c:forEach>
				<br/>
			</c:forEach> --%>
			${row.phaseAndActivitys}
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">
			<c:choose>
				<%-- <c:when test="${row.createdBy eq u.id or row.allowModifier eq u.id}"> --%>
				<c:when test="${editable and row.creationLevel eq u.level and row.creationAreaId eq u.regionCode and row.creationCinemaId eq u.cinemaId}">
					<button type="button" class="btn edit"
						onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
					<c:if test="${empty row.tCmnPhases}">
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
