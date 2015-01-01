<%@ attribute name="exams" required="true" type="java.util.List<org.ironbrain.core.Exam>" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

Все экзамены:

<ib:gap px="5"/>

<c:forEach var="exam" items="${exams}">
    <a href="exam${exam.id}">${exam.name}</a> <br>
</c:forEach>

<a href="http://localhost:8080/exam">--Сборка--</a>