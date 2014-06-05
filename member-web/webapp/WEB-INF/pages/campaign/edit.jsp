<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<c:choose>
	<c:when test="${m.type eq m.editCampaignType}">
		<adk:include view="campaign_edit" var="campaign_edit">x</adk:include>
		${campaign_edit}
	</c:when>
	<c:when test="${m.type eq m.editPhaseType}">
		<adk:include view="phase_edit" var="phase_edit" winlet="${m.phaseManLet}">x</adk:include>
		${phase_edit}
	</c:when>
	<c:when test="${m.type eq m.editActivityType}">
		<adk:include view="activity_edit" var="activity_edit" winlet="${m.phaseManLet.activityManLet}">x</adk:include>
		${activity_edit}
	</c:when>
</c:choose>
