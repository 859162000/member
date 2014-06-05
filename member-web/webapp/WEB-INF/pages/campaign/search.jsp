<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmSearch" action="search">
	<table width="100%" height="18" border="0">
         <tr>
             <td align="right" valign="top" nowrap="nowrap"> 编码:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input type="text" name="code" value="${query.code}"  class="txtinput_wid80" />
           	 </td>
           	 <td align="right" valign="top" nowrap="nowrap"> 统计渠道:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input:select name="channel" value="${query.channel}" class="select_wid" options="${DIMS[m.DIMTYPE_CAMPAIGN_CHANNEL]}" emptyOption="${true}" emptyOptionLabel="- 请选择 -" />
           	 </td>
         </tr>
         <tr>
             <td align="right" valign="top" nowrap="nowrap"> 名称:</td>
             <td align="left" valign="top" nowrap="nowrap" colspan="${adk:ifelse(u.level eq 'CINEMA','3','1')}">
             	<input type="text" name="name" value="${query.name}"  class="txtinput_wid80" />
           	 </td>
           	 <c:if test="${u.level ne 'CINEMA'}">
           	 <td align="right" valign="top" nowrap="nowrap"> 活动范围:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<c:if test="${u.level eq 'GROUP'}">
					<select class="multiple" multiple="multiple" name="area" id="area" size="5" style="width: 100px">
						<c:forEach items="${DIMS[m.DIMTYPE_AREA]}" var="item">
							<option value="${item.key}"
								<c:forEach items="${query.areas}" var="areaId">
								<c:if test="${item.key eq areaId}">
										selected="selected"
									</c:if>
							</c:forEach>>${item.value}</option>
						</c:forEach>
					</select>
				</c:if>
				<select class="multiple" multiple="multiple" id="cinema" name="cinema" size="5" style="width: 100px">
					<c:if test="${u.level eq 'REGION'}">
						<c:forEach items="${cinemaList}" var="cinema">
							<option value="${cinema.id}"
								<c:forEach items="${query.cinemas}" var="cinemaId">
								<c:if test="${cinema.id eq cinemaId}">
										selected="selected"
									</c:if>
							</c:forEach>>${cinema.innerName}</option>
						</c:forEach>
					</c:if>
				</select>
           	 </td>
           	 </c:if>
         </tr>
         <tr>
             <td align="right" valign="top" nowrap="nowrap">开始时间:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input:date name="sStartDate" value="${query.sStartDate}" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" end="eStartDate" class="txtinput_wid80" readOnly="${true}"/>
             	~<input:date name="eStartDate" value="${query.eStartDate}" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" start="sStartDate" class="txtinput_wid80" readOnly="${true}"/>
           	 	<button type="button"class="btn clear" id="clearStartDate">清除</button>
           	 </td>
           	 <td align="right" valign="top" nowrap="nowrap">结束时间:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input:date name="sEndDate" value="${query.sEndDate}" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" end="eEndDate" class="txtinput_wid80" readOnly="${true}"/>
             	~<input:date name="eEndDate" value="${query.eEndDate}" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。" start="eStartDate" class="txtinput_wid80" readOnly="${true}"/>
             	<button type="button"class="btn clear" id="clearEndDate">清除</button>
           	 </td>
         </tr>
         <tr>
             <td align="right" valign="top" nowrap="nowrap">状态:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input:select name="status" value="${query.status}" class="select_wid">
             		<input:option value="">--请选择--</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_PLAN}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_PLAN]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_PUBLISH}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_PUBLISH]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_EXECUTE}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_EXECUTE]}</input:option>
             		<input:option value="${m.CAMPAINGN_STATUS_FINISH}">${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.CAMPAINGN_STATUS_FINISH]}</input:option>
             	</input:select>
           	 </td>
           	 <td align="right" valign="top" nowrap="nowrap">活动类型:</td>
             <td align="left" valign="top" nowrap="nowrap">
             	<input:select name="type" value="${query.type}" class="select_wid" options="${DIMS[m.DIMTYPE_CAMPAIGN_TYPE]}" emptyOption="${true}" emptyOptionLabel="- 请选择 -" />
           	 </td>
         </tr>
         <tr>
           	 <td align="right" colspan="4">
           	 <button type="submit" class="btn search" >查询</button>
           	 </td>
         </tr>
    </table>
    
</adk:form>
<script language="javascript">

$(".multiple").multiselect({
	selectedList : 3
});

$("#cinema").linkSelects("area", "${adk:resproxy('doGetCinema')}",
		true, "id", '${query.strCinemas}', "");

$("#clearStartDate").click(function(){
	$("input[name='sStartDate']").val("");
	$("input[name='eStartDate']").val("");
});
$("#clearEndDate").click(function(){
	$("input[name='sEndDate']").val("");
	$("input[name='eEndDate']").val("");
});
</script>