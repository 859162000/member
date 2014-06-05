<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<script language="javascript" src="${adk:resurl('/js/My97DatePicker/WdatePicker.js')}">
	function a() {
	}
</script>
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmDoEdit" action="doEdit">
	<adk:func name="doEdit" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT) and not campaignIsEnd and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.editPhase}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" submit="yes" param="status"/>
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	<c:choose>
		  		<c:when test="${empty m.editPhase.id}">新建阶段</c:when>
		  		<c:otherwise>
		  			${m.editPhase.tCampaign.name}>>${m.editPhase.name}
		  		</c:otherwise>
		  	</c:choose>
		  </div>
		</div>
		<div class="box_border" style="max-height: 500px;overflow: auto;">
		<table border="0" style="width: 100%;">
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">编码:</td>
					<td align="left" nowrap="nowrap" colspan="3" width="500px">
						${adk:ifelse(empty m.editPhase.id,'系统自动生成',m.editPhase.code)}
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">阶段名称:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editPhase.editing}">
								<input:text name="name" object="${m.editPhase}" property="name" validate="validate" class="txtinput_wid80" mandatory="yes"/>
							</c:when>
							<c:otherwise>
								${m.editPhase.name}
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap">状态:</td>
					<td align="left" nowrap="nowrap">
						${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.editPhase.status]}
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">开始日期:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editPhase.editing}">
								<input:text id="startDate" name="startDate"
									object="${m.editPhase}" property="strStartDate"
									readOnly="${true}" validate="validate" mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'${startDate}',maxDate:'#F{$dp.$D(\\\'endDate\\\')||\\\'${endDate}\\\'}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editPhase.startDate}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
						</c:choose>	
					</td>
					<td align="right" nowrap="nowrap">结束日期:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editPhase.editing}">
								<input:text id="endDate" name="endDate" 
									object="${m.editPhase}" property="strEndDate" readOnly="${true}" 
									validate="validate"	mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'#F{$dp.$D(\\\'startDate\\\')||\\\'${startDate}\\\'}',maxDate:'${endDate}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editPhase.endDate}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
						</c:choose>	
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">描述:</td>
					<td align="left" colspan="3" style="word-break:break-all">
						<c:choose>
							<c:when test="${m.editPhase.editing}">
								<input:textarea name="description" object="${m.editPhase}" property="description" class="txtarea_wid2" validate="validate">x</input:textarea>
							</c:when>
							<c:otherwise>
							${m.editPhase.description}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
		<div align="center" style="margin: 10px 0 0 0">
			<c:choose>
				<c:when test="${m.editPhase.editing}">
					<button type="button" class="btn save"
						onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN }')">保存</button>
					<button type="button" class="btn cancel"
						onclick="${adk:func('cancelEdit')}()">取消</button>
				</c:when>
				<c:when test="${editable}">
					<button type="button" class="btn edit"
						onclick="${adk:func('doEdit')}()">编辑</button>
				</c:when>
			</c:choose>
		</div>
		</div>
</adk:form>

<c:if test="${not empty m.editPhase.id}">
	<adk:include view="activity_list" var="activity_list" winlet="${m.activityManLet}">x</adk:include>
	${activity_list}
</c:if>
<script type="text/javascript">
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#"+this.id)[0]);
}
</script>