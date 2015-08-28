<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmCcancelStatusEdit" action="cancelStatusEdit">
	<adk:func name="cancelStatusEdit" submit="yes" />
</adk:form>
<div class="dcontent"><adk:form name="frmSaveChangeStatus"
	action="saveChangeStatus" vaction="saveChangeStatus" >
	<table border="0" style="width: 100%;">
		<tbody>
			<tr>
						<td width="80" align="left">
						<button type="submit" class="btn save">保存</button>
						</td>
						<td>
						<button type="button" onclick="${adk:func('cancelStatusEdit')}();"
							class="btn cancel">关闭</button>
						</td>
			</tr>
		</tbody>
	</table>
	<div class="box_border">
	<div>
	<table width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap" width="80px">选择会员状态:</td>
				<td align="left" nowrap="nowrap" width="200px">
					<input type="radio" name="status" value="0"  />禁用
					<input type="radio" name="status" value="-1" />冻结
					<input type="radio" name="status" value="1" />有效(解冻/解禁)
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap" width="80px">调整原因:</td>
				<td align="left"   width="200px">
					<textarea rows="3" cols="40" name="changeResion" ></textarea>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	</div>
</adk:form></div>
<script language="javascript">
	$(document).ready(function() {
		renderButtons();
	});
</script>