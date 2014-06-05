<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<script language="javascript" src="${adk:resurl('/js/My97DatePicker/WdatePicker.js')}">
	function a() {
	}
</script>

<adk:form name="frmSelectInfo" action="doSelect">
	<adk:func name="doSelect" submit="yes" />
</adk:form>
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmSelExtPointCriteria" action="selExtPointCriteria">
	<adk:func name="selExtPointCriteria" submit="yes" />
</adk:form>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.rule}">
	<input type="hidden" name="status">
	<adk:func name="saveEdit" param="status" submit="yes" />
	<div style="min-width: 650px">
	<div class="adk_tab2">
	<ul id="tab" style="cursor: pointer;">
		<li name="base" id="current"><a id="tab_base"><span>基本信息</span></a>
		</li>
	</ul>
	</div>
	<!-- 基本信息 -->
	<div id="div_base" style="min-width: 300px" class="adk_tab2box">
	<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1"
		style="background-color: #fbfbfb">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap">编码:</td>
				<td align="left" nowrap="nowrap">
						${adk:ifelse(empty m.rule.id, '系统自动生成', m.rule.code)}
				</td>
				<td align="right" nowrap="nowrap">名称:</td>
				<td align="left" nowrap="nowrap">
					<c:choose>
						<c:when test="${m.rule.editing}">
							<input:text class="txtinput_wid80" name="name" property="rule.name"
								validate="validate" mandatory="yes" />
						</c:when>
						<c:otherwise>
							${m.rule.name}
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">开始时间:</td>
				<td nowrap="nowrap" align="left">
					<c:choose>
						<c:when test="${m.rule.editing}">
							<input:text id="startDtime" name="strStartDtime" object="${m.rule}" property="strStartDtime" readOnly="${true}" validate="validate" mandatory="yes" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\\\'endDtime\\\')}'})"/>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${m.rule.startDtime}" pattern="yyyy-MM-dd"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td nowrap="nowrap" align="right">结束时间:</td>
				<td nowrap="nowrap" align="left">
					<c:choose>
						<c:when test="${m.rule.editing}">
							<input:text id="endDtime" name="strEndDtime" object="${m.rule }" property="strEndDtime" readOnly="${true}" validate="validate" mandatory="yes" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'#F{$dp.$D(\\\'startDtime\\\')}'})"/>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${m.rule.endDtime}" pattern="yyyy-MM-dd"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td width="100px" nowrap="nowrap" colspan="4"><strong>积分账户：非定级积分</strong></td>
			</tr>
			<c:choose>
				<c:when test="${m.rule.editing}">
				<tr>
				<c:if test="${empty m.rule.tExtPointCriteria.type }">
					<td width="50%" colspan="2" align="center" nowrap="nowrap">&nbsp;&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;</td>
				</c:if>
				<c:if test="${m.rule.tExtPointCriteria.type=='1'}">
					<td width="50%" colspan="2" align="center" nowrap="nowrap">
						&nbsp;&nbsp; 
					<td colspan="2">
					<input:radio name="additionType" object="${m.rule}" property="additionType" validate="validate" value="C" />
					额外积分(积分值)
					<input:number name="additionCode"
									object="${m.rule }" property="additionCode" error="请输入正确的数值"
									validate="validate" class="txtinput_wid80"
									disabled="${m.rule.additionType ne 'C'}" mandatory="yes"/>
					</td>
				</c:if>
				<c:if test="${m.rule.tExtPointCriteria.type=='2'}">
					<td width="50%" colspan="2" align="center" nowrap="nowrap">
					<input:radio name="additionType"  object="${m.rule}" property="additionType" validate="validate" value="P" />
					额外积分(百分比)
					<input:number name="additionPercent" object="${m.rule }"
									property="additionPercent" error="请输入正确的数值" validate="validate"
									class="txtinput_wid80"
									disabled="${m.rule.additionType ne 'P'}" mandatory="yes" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					</td>
					<td colspan="2">
					<input:radio name="additionType"  object="${m.rule}" property="additionType" validate="validate" value="C" />
					额外积分(积分值)
					<input:number name="additionCode"
									object="${m.rule }" property="additionCode" error="请输入正确的数值"
									validate="validate" class="txtinput_wid80"
									disabled="${m.rule.additionType ne 'C'}" mandatory="yes"/>
					</td>
				</c:if>
				</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td width="50%" colspan="4" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.rule.additionType eq 'P'}">额外积分(百分比) ${m.rule.additionPercent}</c:when>
							<c:when test="${m.rule.additionType eq 'C'}">额外积分(积分值) ${m.rule.additionCode}</c:when>
						</c:choose>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<tr>
				<td align="left" width="100px" nowrap="nowrap" colspan="4">
				<strong>积分条件:</strong>
				${m.rule.tExtPointCriteria.name}
					<c:if test="${m.rule.editing}">
					<button class="btn search" type="button"
						onclick="${adk:func('selExtPointCriteria')}();">选择积分条件</button>
					</c:if>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	</div>
</adk:form>
<div align="center" style="margin: 10px 0 0 0">
<c:if test="${m.rule.editing}">
<button type="button" class="btn save"
	onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN}')">保存</button>
</c:if>
<c:if test="${m.rule.status eq m.CAMPAINGN_STATUS_PLAN and m.rule.createdBy eq u.id}">
<button type="button" class="btn save"
	onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PUBLISH}')">发布</button>
</c:if>
<button type="button" class="btn close"
	onclick="${adk:func('cancelEdit')}()">${adk:ifelse(m.rule.editing,'取消','关闭')}</button>
</div>
<script type="text/javascript">
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#"+this.id)[0]);
}
</script>
