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
<adk:form name="frmPagingPoint" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="pagePoint" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">调整积分</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
<div class="table" id="listTablePoint"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="会员编号" class="ac">
			${row.tMember.memberNo }
		</display:column>
		<display:column title="会员手机号" class="ac">
			${row.tMember.mobile }
		</display:column>
		<display:column title="生效日期" value="${row.setTime }" format="{0,date,yyyy-MM-dd HH:mm:ss }" class="ac">
		</display:column>
		<display:column title="影城" class="ac">
			${POINTCINAME[row.cinemaInnerCode] }
		</display:column>
		<display:column title="定级积分变化" class="ac">
			${row.levelPoint }
		</display:column>
		<display:column title="非定级积分变化" class="ac">
			${ row.activityPoint}
		</display:column>
		<display:column title="审批状态" class="ac">
			<c:choose>
				<c:when test="${row.approve eq 'W' }">
					待审批
				</c:when>
				<c:when test="${row.approve eq 'N' }">
					审批未通过
				</c:when>
				<c:when test="${row.approve eq 'W' }">
					已审批
				</c:when>
			</c:choose>
			
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
<script language="javascript">
$("#listTablePoint").displayTagAjax({
	form : '${adk:encodens("frmPagingPoint")}'
});

</script>
