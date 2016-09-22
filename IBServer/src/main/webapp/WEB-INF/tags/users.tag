<%@ attribute name="users" required="true" type="java.util.List<org.ironbrain.core.User>" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<table class="base_table" width="100%">
    <tr class="base_tr">
        <th class="base_th">
            id
        </th>
        <th class="base_th">
            Логин
        </th>
        <th class="base_th">
            Email
        </th>
        <th class="base_th">
            Пароль
        </th>
        <th class="base_th">
            Регистрация
        </th>
        <th class="base_th">
            Действия
        </th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr class="base_tr">
            <td class="base_td">
                    ${user.id}
            </td>
            <td class="base_td">
                    ${user.login}
            </td>
            <td class="base_td">
                    ${user.email}
            </td>
            <td class="base_td">
                    ${user.password}
            </td>
            <td class="base_td">
                    ${user.registerDateStr}
            </td>
            <td class="base_td">
                <a target="_blank" href="login_and_redirect?login=${user.login}&password=${user.password}">login</a>
            </td>
        </tr>
    </c:forEach>
</table>