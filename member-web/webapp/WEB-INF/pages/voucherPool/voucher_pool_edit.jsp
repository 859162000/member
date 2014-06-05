<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmSelectInfo"
	action="doSelect">
	<adk:func name="doSelect" submit="yes" />
</adk:form> 
<adk:form name="frmDownLoad" action="doDownLoadTemp">
	<adk:func name="doDownLoadTemp" submit="yes"/>
</adk:form>
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmUpLoadFile" action="saveUpLoadFile">
	<adk:func name="saveUpLoadFile" submit="yes" />
</adk:form>
<adk:form name="frmSelectTicket" action="doSelectTicket">
	<adk:func name="doSelectTicket" submit="yes" />
</adk:form>
<adk:form name="frmNextStep" action="nextStep" vaction="nextStep" resetref="${m.tVoucherPool}">
	<adk:func name="nextStep" submit="yes" />
<div style="min-width: 650px">
	<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			<li name="base" id="current">
				<a id="tab_base"><span>库券信息</span></a>
			</li>
		</ul>
	</div>
				<!-- 券库信息 -->
			<div id="div_base" style="min-width: 300px" class="adk_tab2box">
				<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right" width="25%" nowrap="nowrap">券库名称:</td>
							<td align="left" width="25%" nowrap="nowrap">
								<input:text class="txtinput_wid80" name="name" property="tVoucherPool.name"  validate="validate" mandatory="yes"/>
							</td>
							<td align="right" width="25%" nowrap="nowrap">券库编码:</td>
							<td align="left" width="25%" nowrap="nowrap">
								${adk:ifelse(empty m.tVoucherPool.id, '系统自动生成',m.tVoucherPool.id)}
							</td>
						</tr>
					</tbody>
				</table>
		</div>
		</div>
		</adk:form>
		<div align="center" style="margin:10px 0 0 0">
			<button type="button" id="nextBtn" class="btn next" onclick="${adk:func('nextStep')}()">下一步</button>
			<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">取消</button>
		</div>

<script language="javascript">
</script>
