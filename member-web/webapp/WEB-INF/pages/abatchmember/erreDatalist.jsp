<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCloseList" action="doCloseList">
	<adk:func name="doCloseList" submit="yes" />
</adk:form> 
<adk:form name="frmPagingErreMem" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="pageerr" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCloseList')}();"
					class="btn close">关闭</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="table" id="listTableErreMem"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="文件名称" class="ac">
		${row.erreFileName }
		</display:column>
		<display:column title="上传时间" class="ac">
		<fmt:formatDate value="${row.createdDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</display:column>
		<display:column title="错误内容" class="ac">
		${row.erreData }
		</display:column>
	</display:table>
</div>
<script language="javascript">
$("#listTableErreMem").displayTagAjax({
	form : '${adk:encodens("frmPagingErreMem")}'
});

</script>
