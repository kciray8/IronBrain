<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<div class="bg" style="width:200px;">
    <div>
        Вход<br>
        <div class="form-element">
            <input style="width: 100%; box-sizing: border-box;" type="text" placeholder="Логин" name="login" id="login" value="">
            <br>
        </div>
        <div class="form-element">
            <input style="width: 100%; box-sizing: border-box;" width="2" type="password" id="password" placeholder="Пароль"
                   name="password"
                   value="">
        </div>
        <div class="form-element">
            <button onclick="enter();">Войти</button>  <button>Регистрация</button>
        </div>
        <div id = "login_error" class="login_error">

        </div>
    </div>
</div>
<div style="margin-top: 6px"></div>