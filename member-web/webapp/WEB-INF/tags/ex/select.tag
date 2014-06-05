<jsp:root version="2.1" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core">
	<jsp:directive.attribute name="id" description="html id attribute" />
	<jsp:directive.attribute name="name" required="true"
		description="html name attribute" />
	<jsp:directive.attribute name="list" type="java.util.Map"
		description="html name attribute" />
	<jsp:directive.attribute name="emptyOption"
		description="add an empty option or not" />
	<jsp:directive.attribute name="emptyOptionLabel"
		description="Label for empty option" />
	<jsp:directive.attribute name="cssClass" />
	<jsp:directive.attribute name="style" />
	<jsp:directive.attribute name="readOnly" />
	<jsp:directive.attribute name="onchange" />
	<jsp:directive.attribute name="onclick" />
	<jsp:directive.attribute name="value" description="selected value" />
	<c:choose>
		<c:when test="${readOnly}">
			<input type="hidden" id="${id}" name="${name}" value="${value}"/>
			<c:choose>
				<c:when test="${not empty value}">
					<c:forEach items="${list}" var="item">
						<c:if test="${item.key eq value}">
							<span title="${item.key} - ${item.value}" class="${cssClass}"
								style="${style}">${item.value}</span>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<span class="${cssClass}" style="${style}">${emptyOptionLabel}</span>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<select id="${id}" name="${name}" class="${cssClass}"
				style="${style}" onchange="${onchange}" onclick="${onclick}">
				<c:if test="${emptyOption}">
					<option value="">${emptyOptionLabel}</option>
				</c:if>
				<c:forEach items="${list}" var="item">
					<c:choose>
						<c:when test="${item.key eq value}">
							<option value="${item.key}" selected="selected">${item.value}</option>
						</c:when>
						<c:otherwise>
							<option value="${item.key}">${item.value}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</c:otherwise>
	</c:choose>
</jsp:root>