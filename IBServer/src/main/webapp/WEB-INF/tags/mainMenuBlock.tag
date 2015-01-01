<%@ attribute name="data" required="true" type="org.ironbrain.SessionData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<td colspan="2">
    <div class="bg header" style="position: relative">
        <a class="main-menu" href="main">Главная</a>

        <c:if test="${data.user != null}">
            <a class="main-menu" href="add">Билеты</a>
            <a class="main-menu" href="exam" id="examAnchor">Экзамен</a>
            <a class="main-menu" href="profile">Профиль</a>
            <a class="main-menu" href="search">Поиск</a>
            <a class="main-menu" href="direction">Направления</a>
            <a class="main-menu" href="add_ticket_to_time">+Билет</a>
        </c:if>

        <a class="main-menu" href="about">О системе</a>

        <c:if test="${data.user != null}">
            <div style="float:right;">
                    ${data.user.login}&nbsp;&nbsp;
                <button onclick="window.location.href = 'logout'">Выход</button>
            </div>
        </c:if>
    </div>
</td>

<script>
    var examCount = 0;

    function addToTicketsExamCount(count) {
        examCount += count;
        $("#examAnchor").html("Экзамен " + "(+" + examCount + ")");
    }
</script>