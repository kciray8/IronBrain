﻿<%@ attribute name="data" required="true" type="org.ironbrain.SessionData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<td colspan="2">
    <div class="bg header" style="position: relative">
        <a class="main-menu" href=".">Главная</a>
        <a class="main-menu" href="exam">Экзамен</a>
        <a class="main-menu" href="test">Профиль</a>
        <a class="main-menu" href="test">О сайте</a>
        <a class="main-menu" href="search">Поиск</a>
        <c:if test="${data.user != null}">
            <div style="float:right;">
                    ${data.user.login}
                <button onclick="window.location.href = 'logout'">Выход</button>
            </div>
        </c:if>
    </div>
</td>
