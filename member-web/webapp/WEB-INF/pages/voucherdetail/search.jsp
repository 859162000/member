<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>

<adk:form name="frmSearch" vaction="ajaxValidate" action="doSearch">
					<input type="hidden" name="memberId" value="${memberId }">
					<button type="submit" class="btn search" value="会员等级历史">会员等级历史</button>
</adk:form>