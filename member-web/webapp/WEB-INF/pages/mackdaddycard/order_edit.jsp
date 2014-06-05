<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:include var="log_list" view="list" winlet="${m.logLet}">x</adk:include>
 <!-- and adk:exec1(u, 'hasRight', m.MACK_DADDY_CARD_ORDER_APPROVE) -->
<c:set var="canApprove" value="${u.cinemaId eq m.editOrder.cinemaId and m.editOrder.status eq m.DIMTYPE_CARD_ORDER_STATUS_A and adk:exec1(u, 'hasRight', m.MACK_DADDY_CARD_ORDER_APPROVE)}"/>
<c:set var="canCancelRequest" value="${u.id eq m.editOrder.createdBy and m.editOrder.status eq m.DIMTYPE_CARD_ORDER_STATUS_A}"/>

<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.editOrder}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" submit="yes" param="status"/>
		<div class="box_border" style="max-height: 500px;overflow: auto;">
		<table border="0" style="width: 100%;">
			<tbody>
			<tr>
				<td align="right" nowrap="nowrap" width="100px">编码:</td>
				<td align="left" nowrap="nowrap">
					${adk:ifelse(empty m.editOrder.id,'系统自动生成',m.editOrder.id)}
				</td>
				<td align="right" nowrap="nowrap" width="100px">数量:</td>
				<td align="left" nowrap="nowrap">
					<c:choose>
						<c:when test="${m.editOrder.editing}">
							<input:number name="numberOfCards" object="${m.editOrder}" property="numberOfCards" format="0"
								class="input" error="请输入正确的数量" mandatory="yes" validate="validate"/>
						</c:when>
						<c:otherwise>
							${m.editOrder.numberOfCards}
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap" width="100px">申请人:</td>
				<td align="left" nowrap="nowrap">
					${m.editOrder.submitBy}
				</td>
				<td align="right" nowrap="nowrap" width="100px">OA流程编号:</td>
				<td align="left" nowrap="nowrap">
					<c:choose>
						<c:when test="${m.editOrder.editing}">
							<input:text name="oaId" object="${m.editOrder}" property="oaId" class="txtinput_wid" validate="validate" />
						</c:when>
						<c:otherwise>
							${m.editOrder.oaId}
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<c:if test="${m.editOrder.status eq m.DIMTYPE_CARD_ORDER_STATUS_P}">
			<tr>
				<td align="right" nowrap="nowrap" width="100px">开始卡号:</td>
				<td align="left" nowrap="nowrap">
					${m.editOrder.startNo}
				</td>
				<td align="right" nowrap="nowrap" width="100px">结束卡号:</td>
				<td align="left" nowrap="nowrap">
					${m.editOrder.endNo}
				</td>
			</tr>
			</c:if>
			<tr>
				<td align="right" nowrap="nowrap">申请说明:</td>
				<td align="left" colspan="3">
				<c:choose>
					<c:when test="${m.editOrder.editing}">
						<input:textarea name="description" object="${m.editOrder}" property="description" class="txtarea_wid2" validate="validate"/>
					</c:when>
					<c:otherwise>
						${m.editOrder.description}
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			</tbody>
		</table>
		</div>
</adk:form>
<c:if test="${not empty m.editOrder.id}">
${log_list}
</c:if>

	<adk:form name="frmDoApp" action="doApp" vaction="doApp"
	resetref="${m.editOrder}">
	<input type="hidden" name="status" />
	<adk:func name="doApp" submit="yes" param="status"/>
	<c:if test="${canApprove}">
		<div class="box_border" style="max-height: 500px;overflow: auto;">
		<table border="0" style="width: 100%;">
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap">审批意见:</td>
					<td align="left" colspan="3">
						<input:textarea name="comments" class="txtarea_wid2" validate="validate"/>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
	</c:if>
	</adk:form>

<div align="center" style="margin: 10px 0 0 0">
	<c:if test="${m.editOrder.editing}">
		<button type="button" class="btn save" onclick="${adk:func('saveEdit')}('${m.DIMTYPE_CARD_ORDER_STATUS_A}')">保存</button>
	</c:if>
	<c:if test="${canApprove}">
		<button type="button" class="btn enabled" onclick="${adk:func('doApp')}('${m.DIMTYPE_CARD_ORDER_STATUS_P}')">审核通过</button>
		<button type="button" class="btn disabled" onclick="${adk:func('doApp')}('${m.DIMTYPE_CARD_ORDER_STATUS_F}')">审核拒绝</button>
	</c:if>
	<c:if test="${canCancelRequest}">
		<button type="button" class="btn disabled" onclick="${adk:func('doApp')}('${m.DIMTYPE_CARD_ORDER_STATUS_X}')">取消审核</button>
	</c:if>
	<button type="button" class="btn close" onclick="${adk:func('cancelEdit')}()">关闭</button>
</div>