<jsp:root version="2.1" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:adk="http://www.aggrepoint.com/adk"
	xmlns:adkhtml="urn:jsptagdir:/WEB-INF/tags/adk/html">
	<jsp:directive.page language="java"
		contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />
	[
	<c:forEach var="phase" items="${phaseList}" varStatus="status">
		<c:if test="${not status.first}">,</c:if>
		{"attr" : {"id" : "${phase.id}Phase",
				   "cid" : "${phase.id}",
				   "type":"${m.editPhaseType}"
				   },
		 "data" : {"title" : "${phase.name}",
				   "attr" : {"href" : "#"}
				  },
		 "state" : "closed"
		}
	</c:forEach>]
</jsp:root>