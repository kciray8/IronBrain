<%@ attribute name="sec" required="true" type="org.ironbrain.core.Section" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<c:url var="openSectionUrl" value="add">
    <c:if test="${sec.ticket == null}">
        <c:param name="sec" value="${sec.id}"/>
    </c:if>
    <c:if test="${sec.ticket != null}">
        <c:param name="sec" value="${sec.parent}"/>
        <c:param name="tic" value="${sec.ticket}"/>
    </c:if>
</c:url>

<a class="menu" href="${openSectionUrl}" id="<%= "navSection" + sec.getId()%>">${sec.framedLabel}</a>