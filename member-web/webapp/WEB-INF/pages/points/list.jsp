<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCreatePoint"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmViewPointData"
	action="doPointDataView">
	<input type="hidden" name="pointOrderId">
	<adk:func name="doPointDataView" param="pointOrderId" submit="yes" />
</adk:form> 
<adk:form name="frmPagingPoint" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="pagePoint" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:include var="pointSaleView" view="pointSaleView" winlet="${m.memberPointHistoryLet}">t</adk:include>
<c:set var="changepoint" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.changepoint')}" />
<div class="dot-border3" style="margin-top:5px">会员积分历史</div>
<adk:include var="editPoint" view="editPoint">t</adk:include>
${editPoint }
<c:if test="${ changepoint and MEMBERSTATUS == 1}">
<c:choose>
	<c:when test="${u.levelName eq 'CINEMA' and u.cinemaId == MEMBERCINEMA.id}">
		<div align="left">
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">调整积分</button>
				</td>
		 	</tr>
		</div>
	</c:when>
	<c:when test="${u.levelName eq 'REGION' and u.regionCode eq MEMBERCINEMA.area}">
		<div align="left">
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">调整积分</button>
				</td>
		 	</tr>
		</div>
	</c:when>
	<c:when test="${u.levelName eq 'GROUP' }">
		<div align="left">
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">调整积分</button>
				</td>
		 	</tr>
		</div>
	</c:when>
</c:choose>
</c:if>
<div class="table" id="listTablePoint"><display:table
		name="POINTRESULT" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${POINTRESULT.startIndex+row_rowNum}</display:column>
		<display:column title="时间" value="${row.setTime }" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac">
			
		</display:column>
		<display:column title="影城" class="ac">
			${POINTCINAME[row.cinemaInnerCode] }
		</display:column>
		<display:column title="操作类型" class="ac">
			${DIMS['212'][row.pointType]}
		</display:column>
		<display:column title="定级积分变化" class="ac">
			${row.levelPoint }
		</display:column>
		<display:column title="非定级积分变化" class="ac">
			${ row.activityPoint}
		</display:column>
		<display:column title="可兑换积分变化" class="ac">
			${row.exchangePoint }
		</display:column>
		<display:column title="可兑换积分余额"class="ac" >
			${row.pointBalance }
		</display:column>
		<display:column title="可兑换积分有效期" class="ac">
			<c:if test="${ row.exchangePoint>=0 }">
				<fmt:formatDate value="${row.exchangePointExpireTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</c:if>
		</display:column>
		<display:column title="积分调整原因类型" class="ac">
			${DIMS['218'][row.adjReasonType]}
		</display:column>
		<display:column title="积分调整原因" class="ac" >
			<div title="${ row.adjResion}">${fn:substring(row.adjResion, 0, 20)}</div>
		</display:column>
		<display:column title="操作人" class="ac" value="${row.createdBy}" />
	</display:table>
</div>

	${pointSaleView}

<script language="javascript">
$("#listTablePoint").displayTagAjax({
	form : '${adk:encodens("frmPagingPoint")}'
});

</script>
