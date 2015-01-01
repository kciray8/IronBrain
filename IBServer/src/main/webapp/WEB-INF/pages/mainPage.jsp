<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-О сайте-"/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <c:if test="${data.user == null}">
                    <td style="width: 200px;vertical-align: top;">
                        <ib:login/>
                    </td>
                </c:if>

                <td style="width: 100%;vertical-align: top;">
                    <div class="bg">
                        <ib:intro/>
                        <c:if test="${data.user == null}">
                            <button>Зайти гостем, без регистрации</button>
                        </c:if>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>