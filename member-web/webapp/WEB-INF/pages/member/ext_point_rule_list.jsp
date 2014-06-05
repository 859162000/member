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
<adk:form name="frmView" action="doView">
	<input type="hidden" name="id">
	<adk:func name="doView" param="id" submit="yes" />
</adk:form>
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmStopInfo" action="doStop">
	<input type="hidden" name="id">
	<adk:func name="doStop" param="id" submit="yes" />
</adk:form>
<adk:form name="frmStartInfo" action="doStart">
	<input type="hidden" name="id">
	<adk:func name="doStart" param="id" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.EXT_POINT_RULE_EDIT)}"/>
<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<c:if test="${editable}">
	<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">添加积分规则</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	</c:if>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar" value="${row.id}">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="编号" sortProperty="code" sortable="true" class="ac" value="${row.code}" >
		</display:column>
		<display:column sortProperty="name" sortable="true"  class="ac" title="名称" value="${row.name }"/>
		<display:column sortProperty="status" sortable="true" class="ac" title="积分状态" value="${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][row.status]}">
		</display:column>
		<display:column sortProperty="tExtPointCriteria.name" class="ac" title="积分规则条件" sortable="true" value="${row.tExtPointCriteria.name }"/>
		<display:column sortProperty="startDtime" class="ac" sortable="true" title="积分规则开始时间" property="startDtime" value="${row.startDtime }">
		</display:column>
		<display:column sortProperty="endDtime" class="ac" sortable="true" title="积分规则结束时间" property="endDtime" value="${row.endDtime }">
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac"
			style="width:150px;">
			<c:choose>
				<c:when test="${row.status eq m.CAMPAINGN_STATUS_PLAN and row.createdBy eq u.id}">
					<button type="button" class="btn edit" onclick="${adk:func('doEdit')}(${row.id});">编辑</button>&nbsp;&nbsp;
					<button type="button" class="btn del" onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>&nbsp;&nbsp;
				</c:when>
				<c:otherwise>
					<button type="button" class="btn csearch" onclick="${adk:func('doView')}(${row.id});">查看</button>&nbsp;&nbsp;
				</c:otherwise>
			</c:choose>
			<c:if test="${row.createdBy eq u.id}">
				<c:choose>
					<c:when test="${row.status eq m.CAMPAINGN_STATUS_PUBLISH or row.status eq m.CAMPAINGN_STATUS_EXECUTE}">
						<button type="button" class="btn disabled" onclick="if(confirm('确定要停用该数据记录吗?')){${adk:func('doStop')}(${row.id});}">停用</button>&nbsp;&nbsp;
					</c:when>
					<c:when test="${row.status eq m.CAMPAINGN_STATUS_INACTIVE }">
						<button type="button" class="btn enabled" onclick="if(confirm('确定要启用该数据记录吗?')){${adk:func('doStart')}(${row.id});}">启用</button>&nbsp;&nbsp;
					</c:when>
				</c:choose>
			</c:if>
		</display:column>
	</display:table>
	</div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
$("#listTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
$("#selectAll").selectAll({parentId:'listTable'});

</script>
