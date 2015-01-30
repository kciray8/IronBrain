<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Профиль-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top; padding-right: 10px">
                    <ib:panel title="Разделы">
                        <a class="menu" href="/profile">Основные настройки</a>
                    </ib:panel>
                </td>

                <td style="vertical-align: top;">
                    <ib:panel title="Основные настройки">
                        <div class="form-element">
                            Имя пользователя: ${user.login} <br>
                        </div>

                        <div class="form-element">
                            <input type="password" placeholder="Новый пароль" id="newPassword">
                        </div>

                        <div class="form-element">
                            <input type="password" placeholder="Подтверждение" id="newPasswordConfirm">
                        </div>

                        <div class="form-element">
                            Режим "расширенный" - <input type="checkbox" placeholder="Новый пароль"
                                                         id="extendedProfile">
                        </div>

                        <div class="form-element">
                            Почта - <input type="text" id="email" value="${user.email}">
                        </div>

                        <div class="form-element">
                            Порт для связи с IBClient - <input type="text" value="${user.port}" id="userPort" size="4">
                        </div>

                        <div class="form-element">
                            <ib:button id="updateProfileButton">Сохранить</ib:button>
                        </div>

                        <div id="update_error" class="msg_error"></div>
                        <div id="update_info" class="msg_info"></div>

                        <c:if test="${user.extended}">
                            <script>
                                $("#extendedProfile").prop('checked', true);
                            </script>
                        </c:if>
                    </ib:panel>
                </td>
            </tr>
        </table>
        <script>
            $("#updateProfileButton").click(function () {
                $.get('update_profile', {
                            newPassword: $("#newPassword").val(),
                            newPasswordConfirm: $("#newPasswordConfirm").val(),
                            extendedProfile: $("#extendedProfile").is(':checked'),
                            email: $("#email").val(),
                            port: $("#userPort").val()
                        },
                        function (data) {
                            var infoDiv = $("#update_info");
                            var errorDiv = $("#update_error");
                            infoDiv.hide();
                            errorDiv.hide();

                            if (data.res == Result.OK) {
                                infoDiv.text("Данные успешно обновлены!");
                                infoDiv.show();
                            } else {
                                errorDiv.text("Ошибка - " + data.message);
                                errorDiv.show();
                            }
                        });
            });
        </script>
    </body>
</html>