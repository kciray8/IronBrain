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

                <c:if test="${data.user != null}">
                    <td style="width: 200px;vertical-align: top;">
                        <div class="bg" style="width:200px;">
                            Указанные товарные знаки и логотипы принадлежат соответствующим правообладателям
                        </div>
                    </td>
                </c:if>

                <td style="vertical-align: top;">
                    <div class="bg">
                        <ib:about ib="${ib}"/>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>