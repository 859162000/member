<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<script language="javascript" src="${adk:resurl('/js/My97DatePicker/WdatePicker.js')}">
	function a() {
	}
</script>
<adk:form name="frmCancelPointEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<div class="dcontent"><adk:form name="frmSaveEdit"
	action="saveEdit" vaction="saveEdit" resetref="${m.pointlong}">
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
	<table width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap">生效期:</td>
				<td align="left" nowrap="nowrap">
					<input type="text" name="setTime" class="Wdate"  onclick="WdatePicker({skin:'whyGreen',minDate:'${minDate }',maxDate:'${maxDate }'})" value="${maxDate }"></input>
				</td>
				<td align="right" nowrap="nowrap">非定级积分:</td>
				<td align="left" nowrap="nowrap">
				<input:number name="activityPoint"  format="0.00" property="activityPoint" object="${m.tPointHistory}"
								class="input" error="请输入正确的数量"  validate="validate"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">调整类型:</td>
				<td align="left" nowrap="nowrap">
					<input:select name="adjReasonType" property="adjReasonType" object="${m.tPointHistory}" class="txtinput_wid80" validate="validate" >
						<c:forEach items="${DIMS['218']}" var="item">
								<input:option value="${item.key}">${item.value}</input:option>
						</c:forEach>
					</input:select> 
				</td>
				<td align="right" nowrap="nowrap">定级积分:</td>
				<td align="left" nowrap="nowrap">
				<input:number name="levelPoint"  format="0.00"  property="levelPoint" object="${m.tPointHistory}"
								class="input" error="请输入正确的数量"  validate="validate"/>
				</td>
				
			</tr>
			<tr >
				<td align="right" nowrap="nowrap">积分影城:</td>
				<td align="left" nowrap="nowrap">
					<input:select name="cinemaInnerCode" property="cinemaInnerCode" object="${m.tPointHistory}" class="txtinput_wid80" validate="validate" mandatory="yes" >
						<input:option value="">--请选择积分影城--</input:option>
						<c:forEach items="${CINEMAINNERCODEMAP}" var="item">
								<input:option value="${item.key}">${item.value}</input:option>
						</c:forEach>
					</input:select> 
				</td>
				<td align="right" nowrap="nowrap">调整原因:</td>
				<td align="left"  >
					<input:textarea class="txtinput_wid80" name="adjResion"  property="adjResion" object="${m.tPointHistory}" validate="validate" size="100"></input:textarea>
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