<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<div class="dcontent">
<adk:form name="frmCreateInfo" action="doCreate">
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
<adk:form name="frmEditTarget" action="editTarget">
	<input type="hidden" name="id">
	<adk:func name="editTarget" param="id" submit="yes" />
</adk:form>
<adk:form name="frmEditResult" action="editResult">
	<input type="hidden" name="id">
	<adk:func name="editResult" param="id" submit="yes" />
</adk:form>
<adk:form name="frmPublishActivity" action="publishActivity">
	<input type="hidden" name="id">
	<adk:func name="publishActivity" param="id" submit="yes" />
</adk:form>
<adk:form name="frmCancelPublishActivity" action="cancelPublishActivity">
	<input type="hidden" name="id">
	<adk:func name="cancelPublishActivity" param="id" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT) and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>
<c:set var="publish" value="${adk:exec1(u, 'hasRight', m.ACTIVITY_PUBLISH) and not phaseIsEnd and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>

<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
	  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
	  	波次列表
	  </div>
	</div>
	<div>
		<c:if test="${editable and not phaseIsEnd}">
		<button type="button" onclick="${adk:func('doCreate')}();" class="btn add">添加波次</button>
		</c:if>
		<button type="button" class="btn download hastext" id="exportTmpl">下载文件模板</button>
	</div>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="编码" sortProperty="c.code" sortable="true" property="code" class="ac" />
		<display:column title="名称" sortProperty="c.name" sortable="true" property="name" class="ac" />
		<display:column title="开始时间" sortProperty="c.startDtime" sortable="true" property="startDtime" class="ac" format="{0,date,yyyy-MM-dd HH:mm:ss}" />
		<display:column title="结束时间" sortProperty="c.endDtime"  sortable="true" property="endDtime" class="ac" format="{0,date,yyyy-MM-dd HH:mm:ss}" />
		<display:column title="状态" sortProperty="c.status" sortable="true" value="${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][row.status]}" class="ac" />
		<display:column title="操作" headerClass="ac" class="ac"
			style="width:150px;">
			<c:choose>
				<c:when test="${editable and row.status eq m.CAMPAINGN_STATUS_PLAN and not phaseIsEnd}">
					<button type="button" class="btn edit"
						onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn csearch"
						onclick="${adk:func('doView')}(${row.id});">查看</button>
				</c:otherwise>
			</c:choose>
			<c:if test="${editable and not phaseIsEnd}">
				<c:if test="${row.status eq m.CAMPAINGN_STATUS_PLAN}">
					<button type="button" class="btn del"
						onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
				</c:if>
				<c:choose>
					<c:when test="${empty row.tActTarget}">
						<button type="button" class="btn person notext"
							onclick="${adk:func('editTarget')}(${row.id})">添加波次目标</button>
					</c:when>
					<c:when test="${row.status eq m.CAMPAINGN_STATUS_PLAN}">
						<button type="button" class="btn person notext"
							onclick="${adk:func('editTarget')}(${row.id})">编辑波次目标</button>
					</c:when>
				</c:choose>
			</c:if>
			<c:choose>
				<c:when test="${row.status eq m.CAMPAINGN_STATUS_PLAN and not empty row.tActTarget and row.tActTarget.status eq m.ACTIVITY_ACT_TARGET_LOCKED and publish}">
					<button type="button" class="btn enabled"
						onclick="${adk:func('publishActivity')}(${row.id})">发布</button>
				</c:when>
				<c:when test="${row.status eq m.CAMPAINGN_STATUS_PUBLISH and row.canCancelPublish and publish}">
					<button type="button" class="btn disabled"
						onclick="${adk:func('cancelPublishActivity')}(${row.id})">取消发布</button>
				</c:when>
				<%-- <c:when test="${row.status eq m.CAMPAINGN_STATUS_FINISH and editable}"> --%>
				<c:when test="${editable}">
					<c:choose>
						<c:when test="${empty row.tActResults}">
							<button type="button" class="btn add notext"
								onclick="${adk:func('editResult')}(${row.id})">添加波次响应统计方式</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn search notext"
								onclick="${adk:func('editResult')}(${row.id})">查看波次响应统计结果</button>
						</c:otherwise>
					</c:choose>
				</c:when>
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
$(".btn.download").button( {
	icons : {
		primary : "ui-icon-arrowthickstop-1-s"
	},
	text:true
});
</script>
