<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Регистрация-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top; padding-right: 10px">
                    <ib:panel title="Команды">
                        <ib:button onClick="location.href='.'">Назад</ib:button>
                    </ib:panel>
                </td>

                <td style="vertical-align: top;">
                    <ib:panel title="Регистрация">
                        <div class="form-element">
                            <input type="text" id="login" placeholder="Логин"/>
                        </div>

                        <div class="form-element">
                            <input type="text" id="password" placeholder="Пароль"/>
                        </div>

                        <div class="form-element">
                            <input type="text" id="email" placeholder="Почта"/>
                        </div>

                        <div class="form-element">
                            Режим <br>
                            <input checked value="simple" type="radio" name="accountMode">Простой</input>
                            <input value="extended" type="radio" name="accountMode">Расширенный</input>
                        </div>
                        Если вы программист, выбирайте "Расширенный" режим - больше функций. Но с ними надо быть
                        аккуратнее. <br> В любом случае, в
                        настройках всегда можно его изменить.

                        <div class="form-element">
                            <ib:button onClick="submitRegister()">Регистрация</ib:button>
                        </div>

                        <div id="reg_error" class="msg_error">

                        </div>
                    </ib:panel>

                    <script>
                        function submitRegister() {
                            var accountMode = $("input:radio[name='accountMode']:checked").val();
                            var extended = (accountMode == "extended");

                            $.get("register_user", {
                                        login: $("#login").val(),
                                        password: $("#password").val(),
                                        email: $("#email").val(),
                                        extended: extended
                                    },
                                    function (data) {
                                        if (data.res == Result.OK) {
                                            location.href = "add";
                                        }
                                        if (data.res == Result.ERROR) {
                                            $("#reg_error").html(data.message).show();
                                        }
                                    });
                        }
                    </script>
                </td>
            </tr>
        </table>
    </body>
</html>