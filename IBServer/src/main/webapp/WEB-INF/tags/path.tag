<%@ attribute name="list" required="true" type="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@tag pageEncoding="UTF-8" %>

<c:forEach var="sec" items="${list}">
    <ib:section sec="${sec}"/><c:if test="${list.get(list.size()-1)!= sec}">→</c:if></c:forEach>