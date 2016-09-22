<%@ attribute name="data" required="true" type="org.ironbrain.SessionData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<%
    String uri = request.getRequestURI();
    String pname = uri.substring(uri.lastIndexOf("/") + 1);
%>

<td colspan="2" style="max-width: 800px">
    <nav class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="main">IronBrain</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li <%= (pname.equals("mainPage.jsp") ? "class='active'" : "") %> ><a href="main">Главная</a></li>
                    <li <%= (pname.equals("documentationPage.jsp") ? "class='active'" : "") %> ><a href="documentation">Документация</a>
                        <c:if test="${data.user != null}">
                    <li <%= (pname.equals("addPage.jsp") ? "class='active'" : "") %> ><a href="add">Билеты</a>
                    <li <%= (pname.equals("examPage.jsp") ? "class='active'" : "") %> ><a href="exam">Экзамен</a>
                    <li <%= (pname.equals("searchPage.jsp") ? "class='active'" : "") %> ><a href="search">Поиск</a>
                    <li <%= (pname.equals("directionPage.jsp") ? "class='active'" : "") %> ><a href="direction">Направления</a>
                    <li><a href="add_ticket_to_time">+Билет</a>
                    <li <%= (pname.equals("aboutPage.jsp") ? "class='active'" : "") %> ><a href="about">О системе</a>

                        </c:if>

                        <c:if test="${data.user != null}">
                    <li <%= (pname.equals("profilePage.jsp") ? "class='active'" : "") %> class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                           aria-expanded="false">${data.user.login}
                            <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="profile">Профиль</a></li>
                            <li><a href="logout">Выход</a></li>
                        </ul>
                    </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
</td>

<script>
    var examCount = 0;

    function addToTicketsExamCount(count) {
        examCount += count;
        $("#examAnchor").html("Экзамен " + "(+" + examCount + ")");
    }
</script>