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
<adk:form name="frmSelCinema" action="selCinema">
	<adk:func name="selCinema" submit="yes" />
</adk:form>
<adk:form name="frmSelUser" action="selUser">
	<adk:func name="selUser" submit="yes" />
</adk:form>
<adk:form name="frmDelUser" action="delUser">
	<adk:func name="delUser" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT)}"/>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.editCampaign}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" submit="yes" param="status"/>
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	<c:choose>
		  		<c:when test="${empty m.editCampaign.id}">新建活动</c:when>
		  		<c:otherwise>
	  			${m.editCampaign.name}
		  		</c:otherwise>
		  	</c:choose>
		  </div>
		</div>
		<div class="box_border" style="overflow: auto;">
		<table border="0" style="width: 100%;">
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">编码:</td>
					<td align="left" nowrap="nowrap">
						${adk:ifelse(empty m.editCampaign.id,'系统自动生成',m.editCampaign.code)}
					</td>
					<td align="right" nowrap="nowrap" width="100px">活动名称:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:text name="name" object="${m.editCampaign}" property="name" validate="validate" class="txtinput_wid80" mandatory="yes"/>
							</c:when>
							<c:otherwise>
								${m.editCampaign.name}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">活动范围:</td>
					<td align="left" nowrap="nowrap" colspan="3">
						<c:choose>
							<c:when test="${empty m.editCampaign.id}">
								<c:if test="${u.levelName eq 'GROUP'}">
									<input:checkbox id="chk_isAll" name="allCinema" object="${m.editCampaign}" property="allCinema" validate="validate" value="all"/>所有影城
								</c:if>
								<c:if test="${u.levelName ne 'CINEMA'}">
									<button type="button" class="btn add" onclick="${adk:func('selCinema')}()">选择影城</button>
								</c:if>
							</c:when>
							<c:when test="${m.editCampaign.allCinema}">所有影城</c:when>
						</c:choose>
						<c:if test="${not m.editCampaign.allCinema}">
						<div id="sc_cinemalist" style="max-height: 200px;overflow: auto;">
							<table style="background-color:#fbfbfb;width: 100%" border="1" bordercolor="#CCCCCC">
								<tbody>
									<c:forEach items="${m.selMap}" var="sel" varStatus="stat">
											<tr>
												<td width="10%" nowrap="nowrap">${DIMS[m.DIMTYPE_AREA][sel.key]}</td>
												<td width="40%" nowrap="nowrap">
													<c:forEach items="${sel.value}" var="cinema" varStatus="sts">
														<span id="${cinema.id}">
															<c:if test="${sts.index ne 0}">,</c:if>${cinema.shortName}
														</span>
													</c:forEach>
												</td>
											</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">结算方式:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing and u.levelName eq 'GROUP'}">
								<input:select name="settlementType" object="${m.editCampaign}" property="settlementType" validate="validate" class="select_wid" mandatory="yes" >
									<input:option value="">--请选择--</input:option>
									<input:option value="${m.CAMPAINGN_SETTLEMENT_TYPE_GROUP}">院线</input:option>
									<input:option value="${m.CAMPAINGN_SETTLEMENT_TYPE_CINEMA}">影城</input:option>
								</input:select>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${m.editCampaign.settlementType eq m.CAMPAINGN_SETTLEMENT_TYPE_GROUP}">院线</c:when>
									<c:when test="${m.editCampaign.settlementType eq m.CAMPAINGN_SETTLEMENT_TYPE_CINEMA}">影城</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">活动统计渠道:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:select name="channel" object="${m.editCampaign}" property="channel" validate="validate" class="select_wid" mandatory="yes" options="${DIMS[m.DIMTYPE_CAMPAIGN_CHANNEL]}" emptyOption="${true}" emptyOptionLabel="- 请选择 -" />
							</c:when>
							<c:otherwise>
								${DIMS[m.DIMTYPE_CAMPAIGN_CHANNEL][m.editCampaign.channel]}
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap" width="100px">活动类型:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:select name="type" object="${m.editCampaign}" property="type" validate="validate" class="select_wid" mandatory="yes" options="${DIMS[m.DIMTYPE_CAMPAIGN_TYPE]}" emptyOption="${true}" emptyOptionLabel="- 请选择 -" />
							</c:when>
							<c:otherwise>
								${DIMS[m.DIMTYPE_CAMPAIGN_TYPE][m.editCampaign.type]}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">开始日期:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:text id="startDate" name="startDate"
									object="${m.editCampaign}" property="strStartDate"
									readOnly="${true}" validate="validate" mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'${date}',maxDate:'#F{$dp.$D(\\\'endDate\\\')}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editCampaign.startDate}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap">结束日期:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:text id="endDate" name="endDate" 
									object="${m.editCampaign}" property="strEndDate" readOnly="${true}" 
									validate="validate"	mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'#F{$dp.$D(\\\'startDate\\\')}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editCampaign.endDate}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
						</c:choose>	
					</td>
				</tr>
				<tr>
					<%-- <td align="right" nowrap="nowrap">授权修改人:</td>
					<td align="left" nowrap="nowrap">
						${m.editCampaign.allowModifier}
						<c:if test="${m.editCampaign.editing and m.editCampaign.createdBy eq u.id}">
							<c:choose>
								<c:when test="${empty m.editCampaign.allowModifier}">
									<button type="button" class="btn search"
										onclick="${adk:func('selUser')}()">查询授权人</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn edit"
										onclick="${adk:func('selUser')}()">编辑授权人</button>
									<button type="button" class="btn clear"
										onclick="${adk:func('delUser')}()">清除授权人</button>
								</c:otherwise>
							</c:choose>
						</c:if>
					</td> --%>
					<td align="right" nowrap="nowrap">状态:</td>
					<td align="left" nowrap="nowrap" colspan="3">
						${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.editCampaign.status]}
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">描述:</td>
					<td align="left" colspan="3" style="word-break:break-all">
						<c:choose>
							<c:when test="${m.editCampaign.editing}">
								<input:textarea name="description" object="${m.editCampaign}" property="description" class="txtarea_wid2" validate="validate">x</input:textarea>
							</c:when>
							<c:otherwise>
							${m.editCampaign.description}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">创建方:</td>
					<td align="left" nowrap="nowrap" colspan="3">
						<c:choose>
							<c:when test="${m.editCampaign.creationLevel eq 'GROUP'}">院线</c:when>
							<c:when test="${m.editCampaign.creationLevel eq 'REGION'}">
							区域:${DIMS[m.DIMTYPE_AREA][m.editCampaign.creationAreaId]}
							</c:when>
							<c:when test="${m.editCampaign.creationLevel eq 'CINEMA'}">
							影城:${DIMS[m.DIMTYPE_AREA][m.editCampaign.creationAreaId]}/${m.editCampaign.tCinema.shortName}
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">创建信息:</td>
					<td align="left" nowrap="nowrap">
						${m.editCampaign.createdBy}&nbsp;&nbsp;<fmt:formatDate value="${m.editCampaign.createdDate}" pattern="yyyy-MM-dd HH:mm"/>
					</td>
					<td align="right" nowrap="nowrap">更新信息:</td>
					<td align="left" nowrap="nowrap">
						${m.editCampaign.updatedBy}&nbsp;&nbsp;<fmt:formatDate value="${m.editCampaign.updatedDate}" pattern="yyyy-MM-dd HH:mm"/>
					</td>
				</tr>
			</tbody>
		</table>
		<div align="center" style="margin: 10px 0 0 0">
			<c:choose>
				<c:when test="${m.editCampaign.editing}">
					<button type="button" class="btn save"
						onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN }')">保存</button>
					<button type="button" class="btn cancel"
						onclick="${adk:func('cancelEdit')}()">取消</button>
				</c:when>
				<%-- <c:when test="${m.editCampaign.createdBy eq u.id or m.editCampaign.allowModifier eq u.id}"> --%>
				<c:when test="${editable and m.editCampaign.creationLevel eq u.level and m.editCampaign.creationAreaId eq u.regionCode and m.editCampaign.creationCinemaId eq u.cinemaId}">
					<button type="button" class="btn edit"
						onclick="${adk:func('doEdit')}()">编辑</button>
				</c:when>
			</c:choose>
		</div>
				
		</div>
</adk:form>

<c:if test="${not empty m.editCampaign.id}">
	<adk:include view="phase_list" var="phase_list" winlet="${m.phaseManLet}">x</adk:include>
	${phase_list}
</c:if>

<script type="text/javascript">
$("#allowModifier").autocompl({
	url:"${adk:resproxy('doGetUser')}",
	minLenght : 2,
	id : 'edit_allowModifier',
	label : 'allowModifier'
});
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#"+this.id)[0]);
}

$("#chk_isAll").click(function(){
	var isall = $(this).is(":checked");
	if(isall){
		$("#sc_cinemalist").slideUp();
	}else{
		$("#sc_cinemalist").slideDown();
	}
});
if(${u.levelName eq 'CINEMA'}){
	$('#'+${u.cinemaId}).css('color','red');
}
</script>

