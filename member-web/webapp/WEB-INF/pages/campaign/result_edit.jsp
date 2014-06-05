<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCancelResultEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmDoResultEdit" action="doEdit">
	<adk:func name="doEdit" submit="yes" />
</adk:form>
<adk:form name="frmSelSegment" action="selSegment">
	<input type="hidden" name="propertyName"/>
	<adk:func name="selSegment" submit="yes" param="propertyName"/>
</adk:form>
<adk:form name="frmResetCountResult" action="resetCountResult">
	<adk:func name="resetCountResult" submit="yes"/>
</adk:form>
<adk:form name="frmSaveResultEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.editResult}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" param="status" submit="yes"/>
	<div class="table" style="max-height: 500px; overflow: auto;">
			<%-- <c:choose>
				<c:when test="${true}">
					<input:radio name="resConfigType" object="${m.editResult}"
						property="resConfigType" validate="validate"
						value="${m.RES_CONFIG_TYPE_INTEGRATE}"
						onclick="$('#integrate').css('display','block');$('#voucher').css('display','none');$('#other').css('display','none')" />${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.RES_CONFIG_TYPE_INTEGRATE]}
				  <input:radio name="resConfigType" object="${m.editResult}"
						property="resConfigType" validate="validate"
						value="${m.RES_CONFIG_TYPE_VOUCHER}"
						onclick="$('#integrate').css('display','none');$('#voucher').css('display','block');$('#other').css('display','none')" />${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.RES_CONFIG_TYPE_VOUCHER]}
				  <input:radio name="resConfigType" object="${m.editResult}"
						property="resConfigType" validate="validate"
						value="${m.RES_CONFIG_TYPE_OTHER}"
						onclick="$('#integrate').css('display','none');$('#voucher').css('display','none');$('#other').css('display','block')" />${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.RES_CONFIG_TYPE_OTHER]}
				</c:when>
				<c:otherwise>
					${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.editResult.resConfigType]}
				</c:otherwise>
			</c:choose> --%>
		<div id="integrate" style="display:${adk:ifelse(m.editResult.resConfigType eq m.RES_CONFIG_TYPE_INTEGRATE, 'block','none')}">
			<table border="0" style="width: 100%;">
				<tbody>
					<tr>
						<td align="right" nowrap="nowrap" width="100px">特殊积分规则:</td>
						<td align="left" nowrap="nowrap">${m.editResult.tExtPointRule.name}
							<c:if test="${false}">
								<button type="button" class="btn search notext" onclick="${adk:func('selExtPointRule')}()">选择特殊积分规则</button>
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="voucher" style="display:${adk:ifelse(m.editResult.resConfigType eq m.RES_CONFIG_TYPE_VOUCHER, 'block','none')}">
			<table border="0" style="width: 100%;">
				<tbody>
					<tr>
						<td align="right" nowrap="nowrap" width="100px">券发放:</td>
						<td align="left" nowrap="nowrap">${m.editResult.tVoucherPool.name}
							<c:if test="${false}">
								<button type="button" class="btn search notext" onclick="${adk:func('selVoucherPool')}()">选择券库</button>
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="other" style="display:${adk:ifelse(m.editResult.resConfigType eq m.RES_CONFIG_TYPE_OTHER, 'block','none')}">
			<table border="0" style="min-width:300px;width: 100%;">
				<tbody>
					<tr>
						<td align="right" nowrap="nowrap" width="100px">推荐响应:</td>
						<td align="left" nowrap="nowrap">${m.editResult.resSegment.name}
							<c:if test="${m.editResult.editing}">
								<button type="button" class="btn search notext" onclick="${adk:func('selSegment')}('${m.resultResSegment}')">选择客群</button>
							</c:if>
						</td>
					</tr>
					<tr>
						<td align="right" nowrap="nowrap" width="100px">关联响应:</td>
						<td align="left" nowrap="nowrap">${m.editResult.alterSegment.name}
							<c:if test="${m.editResult.editing}">
								<button type="button" class="btn search notext" onclick="${adk:func('selSegment')}('${m.resultAlterSegment}')">选择客群</button>
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<c:if test="${not m.editResult.editing}">
			<table>
			  	<thead>
			  		<th>受众发送人数</th>
			  		<th>推荐响应人数</th>
			  		<th>推荐响应率</th>
			  		<th>关联响应人数</th>
			  		<th>关联响应率</th>
			  		<th>控制组人数</th>
			  		<th>控制组响应人数</th>
			  		<th>控制组响应率</th>
			  	</thead>
			  	<c:choose>
					<c:when test="${m.editResult.status eq m.ACTIVITY_ACT_RESULT_EXEXUTE}">
						<tr><td colspan="8">待统计...</td></tr>
					</c:when>
					<c:when test="${m.editResult.status eq m.ACTIVITY_ACT_RESULT_EXECUTE_COUNT}">
						<tr><td colspan="8">统计中...</td></tr>
					</c:when>
					<c:when test="${m.editResult.status eq m.ACTIVITY_ACT_RESULT_EXCEPTION}">
						<tr><td colspan="8">统计异常...</td></tr>
					</c:when>
					<c:otherwise>
						<tr>
							<%-- 受众发送人数 --%>
					  		<td style="word-break:break-all">${m.editResult.contactCount}</td>
							<%-- 推荐响应人数 --%>
					  		<td style="word-break:break-all">${m.editResult.resCount}</td>
							<%-- 推荐响应率 =推荐响应人数/受众发送人数 --%>
					  		<td style="word-break:break-all">
								<c:choose>
									<c:when test="${empty m.editResult.resCount or m.editResult.resCount eq '0' or empty m.editResult.contactCount or m.editResult.contactCount eq '0'}">0</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${m.editResult.resCount*100/m.editResult.contactCount}" pattern="#.##"/>%
									</c:otherwise>
								</c:choose>
					  		</td>
							<%-- 关联响应人数 --%>
					  		<td style="word-break:break-all">${m.editResult.alterResCount}</td>
							<%-- 关联响应率 =关联响应人数/受众发送人数 --%>
					  		<td style="word-break:break-all">
					  			<c:choose>
					  				<c:when test="${empty m.editResult.alterResCount or m.editResult.alterResCount eq '0' or empty m.editResult.contactCount or m.editResult.contactCount eq '0'}">0</c:when>
					  				<c:otherwise>
							  			<fmt:formatNumber value="${m.editResult.alterResCount*100/m.editResult.contactCount}" pattern="#.##"/>%
					  				</c:otherwise>
					  			</c:choose>
					  		</td>
							<%-- 控制组人数 --%>
					  		<td style="word-break:break-all">${m.editResult.controlCount}</td>
							<%-- 控制组响应人数 --%>
					  		<td style="word-break:break-all">${m.editResult.controlResCount}</td>
							<%-- 控制组响应率 =控制组响应人数/控制组人数--%>
					  		<td style="word-break:break-all">
					  			<c:choose>
					  				<c:when test="${empty m.editResult.controlResCount or m.editResult.controlResCount eq '0' or empty m.editResult.controlCount or m.editResult.controlCount eq '0'}">0</c:when>
					  				<c:otherwise>
							  			<fmt:formatNumber value="${m.editResult.controlResCount*100/m.editResult.controlCount}" pattern="#.##"/>%
					  				</c:otherwise>
					  			</c:choose>
					  		</td>
					  	</tr>
					</c:otherwise>		  	
			  	</c:choose>
			  </table>
		  </c:if>
		</div>
</adk:form>

<div align="center" style="margin: 10px 0 0 0">
	<c:choose>
		<c:when test="${m.editResult.editing}">
			<button type="button" class="btn save" onclick="${adk:func('saveEdit')}()">保存</button>
		</c:when>
		<c:when test="${m.editResult.status eq m.ACTIVITY_ACT_RESULT_FINISH or m.editResult.status eq m.ACTIVITY_ACT_RESULT_EXCEPTION}">
			<button type="button" class="btn edit" onclick="${adk:func('doEdit')}()">重置</button>
		</c:when>
	</c:choose>
	<c:if test="${m.editResult.status eq m.ACTIVITY_ACT_RESULT_EXCEPTION}">
		<button type="button" class="btn edit" onclick="${adk:func('resetCountResult')}()">重新统计</button>
	</c:if>
	<button type="button" class="btn cancel" onclick="${adk:func('cancelEdit')}()">${adk:ifelse(m.editResult.editing,'取消','关闭')}</button>
</div>
<script type="text/javascript">
$(function(){
	
});
</script>

