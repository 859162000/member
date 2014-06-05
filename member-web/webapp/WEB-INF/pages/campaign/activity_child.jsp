<jsp:root version="2.1" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:adk="http://www.aggrepoint.com/adk"
	xmlns:adkhtml="urn:jsptagdir:/WEB-INF/tags/adk/html">
	<jsp:directive.page language="java"
		contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />
	[
	<c:forEach var="activity" items="${activityList}" varStatus="status">
		<c:if test="${not status.first}">,</c:if>
		{"attr" : {"id" : "${activity.id}Activity",
		           "cid" : "${activity.id}",
				   "type":"${m.editActivityType}"
				   },
		 "data" : {"title" : "${activity.name}",
				   "attr" : {"href" : "#"}
				  },
		 "state" : ""
		}
	</c:forEach>]
</jsp:root>