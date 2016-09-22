<%@ attribute name="exams" required="true" type="java.util.List<org.ironbrain.core.Exam>" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<c:forEach var="exam" items="${exams}">
    <a class="menu" href="exam${exam.id}">${exam.name}</a> <br>
</c:forEach>

<a class="menu" href="exam">--Сборка--</a>