<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Все билеты-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width: 100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td valign="top">
                    <ib:panel title="Все билеты сайта">
                        <table style='width: 100%'>
                            <c:forEach var="ticket" items="${tickets}">
                                <tr>
                                    <td colspan="2">
                                            ${ticket.path} <br>
                                        Пользователь - ${ticket.owner}
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%">
                                        <div class='searchTextOutput'>
                                                ${ticket.questions}
                                        </div>
                                    </td>
                                    <td>
                                        <div class='searchTextOutput'>
                                                ${ticket.answers}
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <hr/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </ib:panel>
                </td>
            </tr>
        </table>
    </body>
</html>