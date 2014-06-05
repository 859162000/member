<jsp:root version="2.1" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core">
	<jsp:directive.attribute name="id" description="html id attribute" />
	<jsp:directive.attribute name="name" required="true"
		description="html name attribute" />
	<jsp:directive.attribute name="cssClass" />
	<jsp:directive.attribute name="readOnly" />
	<jsp:directive.attribute name="onchange" />
	<jsp:directive.attribute name="onclick" />
	<jsp:directive.attribute name="list" required="true"
		type="java.util.Map" description="html name attribute" />
	<jsp:directive.attribute name="values"
		description="an array of value to select checkbox" />
	<c:forEach items="${list}" var="item" varStatus="idx">
		<span class="checkbox_list_item">
		<c:set var="checked" value="false" />
		<c:forEach items="${values}" var="it">
			<c:if test="${item.key == it}">
				<c:set var="checked" value="true" />
			</c:if>
		</c:forEach>
		<c:choose>
			<c:when test="${checked=='true'}">
				<c:choose>
					<c:when test="${readOnly}">
						<input type="checkbox" id="${id}_${idx.index}" name="${name}"
							class="${cssClass}" value="${item.key}" checked="checked"
							onclick="${onclick}" onchange="${onchange}" disabled="disabled" />
					</c:when>
					<c:otherwise>
						<input type="checkbox" id="${id}_${idx.index}" name="${name}"
							class="${cssClass}" value="${item.key}" checked="checked"
							onclick="${onclick}" onchange="${onchange}" />
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${readOnly}">
						<input type="checkbox" id="${id}_${idx.index}" name="${name}"
							class="${cssClass}" value="${item.key}" onclick="${onclick}"
							onchange="${onchange}" disabled="disabled" />
					</c:when>
					<c:otherwise>
						<input type="checkbox" id="${id}_${idx.index}" name="${name}"
							class="${cssClass}" value="${item.key}" onclick="${onclick}"
							onchange="${onchange}" />
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
			<label for="${id}_${idx.index}"> ${item.value}</label>
			</span>
	</c:forEach>
</jsp:root>