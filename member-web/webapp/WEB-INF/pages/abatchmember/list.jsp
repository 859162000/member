<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCreateAbatchmemup" action="doCreatUpLoad">
	<input type="hidden" name="status">
	<adk:func name="doCreatUpLoad" param="status" submit="yes" />
</adk:form> 
<adk:form name="frmdoView" action="doView">
	<input type="hidden" name="id">
	<adk:func name="doView" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmUpLoadTemp"
	action="doUpLoadTemp">
	<adk:func name="doUpLoadTemp" submit="yes" />
</adk:form>

<adk:form name="frmCreateAbatchmem"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form>
<adk:form name="frmPagingAbatchMems" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:include var="fileDatalist" view="list" winlet="${m.abatchErreLogLet}">t</adk:include>
<c:set var="importmemtemp" value="${adk:exec1(u, 'hasRight', 'member.import.membertemp')}" />
<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<button type="button" onclick="${adk:func('doCreatUpLoad')}('W');"
					class="btn upload" title="请上传2003版excle">上传会员</button>
				<c:if test="${importmemtemp }">
				<button type="button" onclick="${adk:func('doUpLoadTemp')}();"
					class="btn upload" title="请上传2003版excle">上传模板</button>
				</c:if>
				<button type="button" id="dowmLoadTemp"
					class="btn download">下载模板</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="table" id="listTableAbatchMems"><display:table
		name="MEMBERABATCHRESULT" id="rowm" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${MEMBERABATCHRESULT.startIndex+rowm_rowNum}</display:column>
		
		<display:column title="文件名称" class="ac">
			${rowm.fileName }
		</display:column>
		<display:column title="上传人" class="ac">
			${rowm.createdBy }
		</display:column>
		<display:column title="上传时间" class="ac">
			<fmt:formatDate value="${rowm.createdDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</display:column>
		<display:column title="文件状态" class="ac">
			${DIMS['1017'][rowm.status]}
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">&nbsp;
			<button type="button" class="btn edit"
				onclick="${adk:func('doView')}(${rowm.id});">查看</button>&nbsp;&nbsp;
		</display:column>
	</display:table>
</div>


<div>
	${ fileDatalist}
</div>
<script language="javascript">
$("#listTableAbatchMems").displayTagAjax({
	form : '${adk:encodens("frmPagingAbatchMems")}'
});


$("#dowmLoadTemp").click(function() {
	var url = "${adk:resproxy('doDownLoadTemp')}";
	//window.open(url);火狐浏览器可以使用
	window.location = url;
});
$(".btn.upload").button( {
	icons : {
		primary : "ui-icon-arrowthickstop-1-n"
	},
	text:true
});
$(".btn.download").button( {
	icons : {
		primary : "ui-icon-arrowthickstop-1-s"
	},
	text:true
});
</script>
