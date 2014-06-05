<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmNewEditInfo" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<div class="dcontent"><adk:form name="frmSaveEdit"
	action="saveEdit" vaction="saveEdit" >
	<table border="0" style="width: 100%;">
		<tbody>
			<tr>
						<td width="80" align="left">
						<button type="submit" class="btn save">保存</button>
						</td>
						<td>
						<button type="button" onclick="${adk:func('cancelEdit')}();"
							class="btn cancel">关闭</button>
						</td>
			</tr>
		</tbody>
	</table>
	<div class="box_border">
	<div>
	<table border="0" style="width: 100%;">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap">有效期:</td>
				<td align="left" nowrap="nowrap">
					<input:date name="expireDate" format="yyyy-MM-dd" error="请输入正确的日期 如:2010-01-01。"  class="txtinput_wid80"/>
				</td>
				<td align="right" nowrap="nowrap">调整前级别:</td>
				<td align="left" nowrap="nowrap">
					<input:text name="memLevel" class="txtinput_wid80" />
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">调整后级别:</td>
				<td align="left" nowrap="nowrap">
					<input:text name="memLevel" class="txtinput_wid80" />
				</td>
				<td align="right" nowrap="nowrap"><font color=red>调整原因:<font></td>
				<td align="left" nowrap="nowrap">
					<input:select name="adjReasonType" class="txtinput_wid80" validate="validate" >
						<c:forEach items="${DIMS['217']}" var="item">
								<input:option value="${item.key}">${item.value}</input:option>
						</c:forEach>
					</input:select> 
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