<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmSearchLevel" action="doSearchLevel">
	<input type="hidden" name="id">
	<adk:func name="doSearchLevel" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmSearchPoint" action="doSearchPoint">
	<input type="hidden" name="id">
	<adk:func name="doSearchPoint" param="id" submit="yes" />
</adk:form>
<adk:form name="frmSearchVoucher" action="doSearchVoucher">
	<input type="hidden" name="id">
	<adk:func name="doSearchVoucher" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmSearchCardRel" action="doSearchCardRel">
	<input type="hidden" name="id">
	<adk:func name="doSearchCardRel" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmSearchTrans" action="doSearchTrans">
	<input type="hidden" name="id">
	<adk:func name="doSearchTrans" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmSearchMemberLog" action="doSearchMemberLog">
	<input type="hidden" name="id">
	<adk:func name="doSearchMemberLog" param="id" submit="yes" />
</adk:form> 
<adk:include var="memberdatas" view="edit" >t</adk:include>
<adk:include var="memPointHistorylist" view="listPoint" winlet="${m.memPointHistoryLet}">t</adk:include>
<adk:include var="memLevelHistorylist" view="listLevel" winlet="${m.memLevelHistoryLet}">t</adk:include>
<adk:include var="memvoucherlist" view="listvoucher" winlet="${m.voucherPoolDetailLet}">t</adk:include>
<adk:include var="memcardrellist" view="listcardrel" winlet="${m.memCardRelLet}">t</adk:include>
<adk:include var="memloglist" view="listMemberLog" winlet="${m.memberLogLet}">t</adk:include>
<adk:include var="translist" view="translist" >t</adk:include>
	${ memberdatas}
<div>
	<table border="0">
		<tbody>
			<tr>
				<td align="left" nowrap="nowrap">
					<button type="button" id="member_point" onclick="${adk:func('doSearchPoint')}(${MEMBERMODEL.id });"
					class="btn search">会员积分历史</button>
				</td>
				<td align="left" nowrap="nowrap">
					<button type="button" id="member_level" onclick="${adk:func('doSearchLevel')}(${MEMBERMODEL.id });"
					class="btn search">会员等级历史</button>
				</td>
				<td align="left" nowrap="nowrap"> 
				<button type="button" id="member_vocuher" onclick="${adk:func('doSearchVoucher')}(${MEMBERMODEL.id });"
					class="btn search">会员券包</button>
				</td>
				<td align="left" nowrap="nowrap">
				<button type="button" id="member_card" onclick="${adk:func('doSearchCardRel')}(${MEMBERMODEL.id });"
					class="btn search">会员卡包</button>
				</td>
				<td align="left" nowrap="nowrap">
				<button type="button" id="member_trans" onclick="${adk:func('doSearchTrans')}(${MEMBERMODEL.id });"
					class="btn search">会员交易历史</button>
				</td>
				<td align="left" nowrap="nowrap">
				<button type="button" id="member_log" onclick="${adk:func('doSearchMemberLog')}(${MEMBERMODEL.id });"
					class="btn search">会员变更记录</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div>
	${memPointHistorylist }
	${memLevelHistorylist}
	${memvoucherlist }
	${memcardrellist }
	${translist }
	${memloglist }
</div>
