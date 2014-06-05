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

<adk:form name="frmSelectInfo"
	action="doSelect">
	<adk:func name="doSelect" submit="yes" />
</adk:form> 
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmReplace" action="doReplace">
	<adk:func name="doReplace" submit="yes" />
</adk:form>
<adk:form name="frmDownLoad" action="doDownLoadTemp">
	<adk:func name="doDownLoadTemp" submit="yes"/>
</adk:form>
<adk:form name="frmSelectSegment" action="selectSegment">
	<adk:func name="selectSegment" submit="yes" />
</adk:form>
<adk:form name="frmSaveFile" action="saveUpLoadFile">
	<adk:func name="saveUpLoadFile" submit="yes"/>
</adk:form> 
<adk:form name="frmSelectVoucherPool" action="selectVoucherPool">
	<adk:func name="selectVoucherPool" submit="yes" />
</adk:form>
<adk:form name="frmNextStep" action="nextStep" vaction="nextStep" resetref="${m.rule}">
	<adk:func name="nextStep" submit="yes" />
		<div style="min-width: 650px">
		<div class="adk_tab2">
			<ul id="tab" style="cursor: pointer;">
				<li name="base" id="current">
					<a id="tab_base"><span>券发放信息</span></a>
				</li>
			</ul>
		</div>
		<div id="div_base" style="min-width: 300px" class="adk_tab2box">
				<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right" nowrap="nowrap">编码:</td>
							<td align="left" nowrap="nowrap">
								${adk:ifelse(empty m.rule.code, '系统自动生成', m.rule.code)}
							</td>
							<td align="right"  nowrap="nowrap">名称:</td>
							<td align="left" nowrap="nowrap">
								<input:text class="txtinput_wid80" name="name" object="${m.rule}" property="name"  validate="validate" mandatory="yes"/>
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" align="right">开始时间:</td>
							<td nowrap="nowrap" align="left">
								<input:text id="startDtime" name="strStartDtime" object="${m.rule}" property="strStartDtime" readOnly="${true}" validate="validate" mandatory="yes" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\\\'endDtime\\\')}'})"/>
							</td>
							<td nowrap="nowrap" align="right">结束时间:</td>
							<td nowrap="nowrap" align="left">
								<input:text id="endDtime" name="strEndDtime" object="${m.rule}" property="strEndDtime" readOnly="${true}" validate="validate" mandatory="yes" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'#F{$dp.$D(\\\'startDtime\\\')}'})"/>
						  </td>
						</tr>
						<tr>
							<td align="right" nowrap="nowrap">指定方式:</td>
							<td nowrap="nowrap" align="left">
								<input:select name="sourceType" object="${m.rule }" property="sourceType" validate="validate" mandatory="yes">
										<input:option value="">-----请选择-----</input:option>
										<input:option value="${m.VOUCHER_TYPE_RULE}">${DIMS[m.DIMTYPE_VOUCHER_RULE_TYPE][m.VOUCHER_TYPE_RULE]}</input:option>
										<input:option value="${m.VOUCHER_TYPE_FILE}">${DIMS[m.DIMTYPE_VOUCHER_RULE_TYPE][m.VOUCHER_TYPE_FILE]}</input:option>
								</input:select>
							</td>
						</tr>
					</tbody>		
				</table>	
			</div>
	</div>
	</adk:form>
	<div align="center" style="margin:10px 0 0 0">
		<button type="button" id="nextBtn" class="btn save" onclick="${adk:func('nextStep')}()">下一步</button>
		<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">取消</button>
	</div>
<script language="javascript">
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmNextStep')}"].ajaxValidate($("#"+this.id)[0]);
}
</script>
