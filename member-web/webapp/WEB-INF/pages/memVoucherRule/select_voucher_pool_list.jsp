<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCloseVoucherPool" action="closeVoucherPool">
	<adk:func name="closeVoucherPool" submit="yes" />
</adk:form> 
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="page" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<div class="dcontent">
<adk:form name="frmList" action="saveVoucherPool">
	<div class="table" id="segmentListTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="" style="width:20px;" headerClass="ar"
			class="ar">
			<input type="radio" name="ids" value="${row.VOUCHER_POOL_ID}"/>
		</display:column>
		<display:column title="券库编码" sortProperty="t.voucher_pool_id" sortable="true"  class="ac"  value="${row.VOUCHER_POOL_ID }"/>
		<display:column title="券库名称" sortProperty="t.name" sortable="true"  class="ac"  value="${row.NAME }"/>
	</display:table>
	</div>
	<div align="center">
	<button type="submit" class="btn save">保存</button>
	<button type="button" class="btn cancel" onclick="${adk:func('closeVoucherPool')}();">取消</button>
</div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">
$("#segmentListTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
</script>
