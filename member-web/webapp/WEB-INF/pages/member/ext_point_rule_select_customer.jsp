<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit" resetref="${m.id}">
	<adk:func name="saveEdit" submit="yes" />
<div style="min-width: 650px">
	<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			<li name="base" id="current">
				<a id="tab_base"><span>基本信息</span></a>
			</li>
		</ul>
	</div>
				<!-- 基本信息 -->
			<div id="div_base" style="min-width: 300px" class="adk_tab2box">
				<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right" nowrap="nowrap">编码:</td>
							<td align="left" nowrap="nowrap">
								${adk:ifelse(empty m.tModel.memberNo, '系统自动生成',m.tModel.memberNo)}
							</td>
							<td align="right"  nowrap="nowrap">名称:</td>
							<td align="left" nowrap="nowrap">
								<c:choose>
									<c:when test="${model.editing}">
										<input:text class="txtinput_wid80" name="name" property="tModel.name"  validate="validate" mandatory="yes"/>
									</c:when>
									<c:otherwise>
										${ model.name}
									</c:otherwise>
								</c:choose>
								
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" align="right">开始时间:</td>
							<td nowrap="nowrap" align="left">
								<input type="text" id="cal_startDate15091509" name="startDate" value="" size="20" id="srh_startDate"></input>
									<span id="validate_res_startDate"></span>
									<script language="javascript">
									$(function() {
										$("#cal_startDate15091509").datepicker({changeMonth: true, changeYear: true, showAnim: 'slideDown', 
											beforeShow:function(input, inst){
											try {
												var dt = $("#cal_srh_endDate15091509").datepicker("getDate");
												dt.setDate(dt.getDate());
												return {maxDate: dt};
											} catch (e) {
											}
										}});
									});
									</script>
							</td>
							<td nowrap="nowrap" align="right">结束时间:</td>
							<td nowrap="nowrap" align="left">
							<input type="text" id="cal_endDate15091509" name="endDate" value="" size="20" id="srh_endDate"></input><span id="validate_res_endDate"></span><script language="javascript">
									$(function() {
										$("#cal_endDate15091509").datepicker({changeMonth: true, changeYear: true, showAnim: 'slideDown', 
											beforeShow:function(input, inst){
											try {
												var dt = $("#cal_srh_startDate15091509").datepicker("getDate");
												dt.setDate(dt.getDate());
												return {minDate: dt};
											} catch (e) {
											}
										}});
									});
							</script>
						  </td>
						</tr>						
						<tr>
							<td align="right"  nowrap="nowrap">性别:</td>
							<td >
							<c:choose>
									<c:when test="${model.editing }">
										<input:select name="gender" class="txtinput_wid80" validate="validate" property="tModel.gender">
											<c:forEach items="${DIMS['121']}" var="item">
												<input:option value="${item.key}">${item.value}</input:option>
											</c:forEach>
										</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['121']}" var="item">
										<c:if test="${item.key eq model.gender }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
						<td align="right"  nowrap="nowrap">生日日期:</td>
							<td>
								<c:choose>
									<c:when test="${model.editing }">
										<input:date name="birthday" property="tModel.birthday" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。"  class="txtinput_wid80"/>
									</c:when>
									<c:otherwise>
										${model.birthday }
									</c:otherwise>
								</c:choose>
								 
							</td>
						</tr>
						<tr>
							<td align="right" >注册影城:</td>
							<td align="left">
								<c:choose>
									<c:when test="${model.editing }">
										<input:select name="registCinemaId" class="txtinput_wid80" property="tModel.registCinemaId" validate="validate" mandatory="yes">
											<input:option value="">--请选择注册影城--</input:option>
											<c:forEach items="${cinemaMap}" var="item">
												<input:option value="${item.key}">${item.value}</input:option>
											</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${cinemaMap}" var="item">
											<c:if test="${item.key eq model.registCinemaId }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</td>
							<td align="right" >管理影城:</td>
							<td align="left">
							<c:choose>
								<c:when test="${model.editing }">
									<input:select  name="mgHistoryId" class="txtinput_wid80" property="tModel.mgHistoryId" validate="validate" mandatory="yes">
									<input:option value="">--请选择管理影城--</input:option>
									<c:forEach items="${cinemaMap}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
									</c:forEach>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${cinemaMap}" var="item">
										<c:if test="${item.key eq model.mgHistoryId}">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
								
							</td>							
						</tr>
						<tr>
							<td align="right" >招募员工号:</td>
							<td align="left">
									${USERS }
							</td>
							<td align="right"  nowrap="nowrap">招募渠道:</td>
							<td>
								<c:choose>
									<c:when test="${model.editing }">
										<input:select  name="channelId" class="txtinput_wid80" property="tModel.channelId" validate="validate" mandatory="yes">
											<input:option value="">--请选择渠道--</input:option>
										<c:forEach items="${channelMap}" var="item">
											<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${channelMap}" var="item">
											<c:if test="${item.key eq model.channelId }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>	
						<tr>
							<td align="right"  nowrap="nowrap">会员注册类型:</td>
							<td >
							<c:choose>
								<c:when test="${model.editing }">
									<input:select name="registType" class="txtinput_wid80" property="tModel.registType" validate="validate">
										<c:forEach items="${DIMS['201']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['201']}" var="item">
										<c:if test="${item.key eq model.registType }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>					
				</tbody>
				</table>
		</div>
		</div>
		</adk:form>
		<div align="center" style="margin:10px 0 0 0">
			<c:if test="${model.editing }">
				<button type="button" class="btn save" onclick="${adk:func('saveEdit')}()">保存</button>
			</c:if>
			<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">取消</button>
		</div>

<script language="javascript">
$("#cityId").linkSelect("provinceId",
		"${adk:resproxy('doGetCity')}", false, "id", "${m.tModel.tMemberAddrs.cityId}");
</script>
