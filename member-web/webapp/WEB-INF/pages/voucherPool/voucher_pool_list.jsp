<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<div class="dcontent">
<adk:form name="frmCreateInfo"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmEditInfo" action="doEdit">
	<input type="hidden" name="id">
	<adk:func name="doEdit" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">添加券库信息</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar" value="${row.VOUCHER_POOL_ID }">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="编号" sortProperty="" class="ac" value="${row.VOUCHER_POOL_ID }" >
		</display:column>
		<display:column sortProperty="" class="ac" title="名称" value="${row.NAME }"/>
		<display:column sortProperty="" class="ac" title="发放数量" value="${row.GRANT_NUM }"></display:column>
		<display:column sortProperty="" class="ac" title="剩余数量" value="${row.SURPLUS_NUM }"/>
		
		<display:column sortProperty="" class="ac" title="发放锁定" value="${row.SEND_LOCK }">
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac"
			style="width:150px;">&nbsp;
			<c:choose>
				<c:when test="${row.SEND_LOCK eq '0' }">
					<button type="button" class="btn edit" onclick="${adk:func('doEdit')}(${row.VOUCHER_POOL_ID});">编辑</button>&nbsp;&nbsp;
					<button type="button" class="btn del" onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.VOUCHER_POOL_ID });}">删除</button>
				</c:when>	
			</c:choose>
		</display:column>
	</display:table>
	</div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
$("#listTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
$("#selectAll").selectAll({parentId:'listTable'});

</script>
