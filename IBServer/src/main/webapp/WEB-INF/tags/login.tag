<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<ib:panel title="Вход">
    <ib:input placeholder="Логин" id="login"/>
    <ib:gap px="5" />
    <ib:input placeholder="Пароль" id="password" attr="type='password''"/>
    <div class="form-element">
        <ib:button onClick="enter();">Войти</ib:button>
        <ib:space px="5"/>
        <ib:button onClick="register();">Регистрация</ib:button>
    </div>
    <div id="login_error" class="msg_error">

    </div>
</ib:panel>

<script>
    function register() {
        location.href = "./register";
    }
</script>