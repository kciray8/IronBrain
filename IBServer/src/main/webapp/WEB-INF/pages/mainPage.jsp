<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-О системе-"/>
        <ib:scripts/>
    </head>
    <body>
        <table width="100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <c:if test="${data.user == null}">
                    <td style="width: 210px; vertical-align: top; padding-right: 10px">
                        <ib:login/>
                    </td>
                </c:if>

                <c:if test="${data.user != null}">
                    <td style="width: 210px;vertical-align: top; padding-right: 10px">
                        <ib:panel title="Информация">
                            Добро пожаловать в систему, ${data.user.login}
                        </ib:panel>
                    </td>
                </c:if>

                <td style="vertical-align: top;">
                    <ib:panel title="О системе">
                        <ib:intro/>
                        <c:if test="${data.user == null}">
                            <ib:button id="guestLogin" >Зайти гостем, без регистрации</ib:button>
                        </c:if>
                    </ib:panel>
                </td>
            </tr>
        </table>
        <script>
            $("#guestLogin").click(function () {
                location.href = "guest_login";
            });
        </script>
    </body>
</html>