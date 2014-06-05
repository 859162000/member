<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmshowselcinema" action="showList">
	<adk:func name="showList" submit="yes" />
</adk:form>

<adk:form name="frmAddMyCinema" action="doAddMyCinema">
	<input type="hidden" name="flag" />
	<adk:func name="doAddMyCinema" param="flag" submit="yes" />
</adk:form>


<div class="dtitle" style="font-weight:bold">适用影城列表</div>
<div id="div_btn">
	<c:choose>
		<c:when test="${u.levelName eq 'GROUP'}">
			<c:if test="${m.editing}">
				<input type="checkbox" id="chk_isAll" 
					name="selcinemas" value="suitAllCinema" <c:if test="${m.selectedAll}">checked="checked"</c:if> />所有影城</td>
				<button type="button" id="btn_addcinema" class="btn add">选择适用影城</button>
			</c:if>
			<c:if test="${not m.editing and m.selectedAll}">
				适用于所有影城
			</c:if>
			<c:if test="${not m.editing and not m.selectedAll and fn:length(m.selMap) == 0}">
				无适用影城
			</c:if>
		</c:when>
		<c:when test="${u.levelName eq 'CINEMA' and not includeMe}">
			<c:if test="${m.editing}">
				<button type="button" class="btn add" onclick="${adk:func('doAddMyCinema')}(true);">添加本影城</button>
			</c:if>
			<c:if test="${not m.editing and fn:length(m.selMap) == 0}">
				无适用影城
			</c:if>
		</c:when>
		<c:when test="${u.levelName eq 'CINEMA' and includeMe}">
			<c:if test="${m.selectedAll}">
				适用于所有影城
			</c:if>
			<c:if test="${m.editing}">
				<button type="button" class="btn del" onclick="${adk:func('doAddMyCinema')}(false);">移除本影城</button>
			</c:if>
		</c:when>
	</c:choose>
</div>
	
	<div id="sc_cinemalist" style="max-height: 200px;overflow: auto;">
		<table style="background-color:#fbfbfb;width: 100%" border="1" bordercolor="#CCCCCC">
			<tbody>
				<c:forEach items="${m.selMap}" var="sel" varStatus="stat">
						<tr>
							<td width="10%" nowrap="nowrap">${DIMS[m.DIMTYPE_AREA][sel.key]}:</td>
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
	

<script language="javascript">
	$("#btn_addcinema").click(function(){
		${adk:func('showList')}();
	});
	
	$("#chk_isAll").click(function(){
		var isall = $(this).is(":checked");
		if(isall){
			$("#sc_cinemalist").slideUp();
		}else{
			$("#sc_cinemalist").slideDown();
		}
		$.getJSON('${adk:resproxy("doSetSelectedAll")}', {
			selectedAll : isall,
			ajax : 'true'
		}, function(data) {
		});
	});
	if(${u.levelName eq 'CINEMA'}){
		$('#'+${u.cinemaId}).css('color','red');
	}

	
</script>