<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="Результаты экзамена" />
        <ib:scripts />
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top;">
                    <div class="bg">
                        <ib:examList exams="${exams}"/>
                    </div>
                </td>
                <td valign="top">
                    <div class="bg">
                        Длительность экзамена - ${exam.durationMin} минут(ы)

                        <ib:margin/>

                        <% int i = 0; %>
                        <table class="base_table">
                            <tr class="base_tr">
                                <th class="base_th">
                                    Вопрос
                                </th>
                                <th class="base_th">
                                    Сек
                                </th>
                                <th class="base_th">
                                    Результат
                                </th>
                                <th class="base_th">
                                    Действия
                                </th>
                            </tr>

                            <c:forEach var="tempTry" items="${tries}">
                                <tr class="base_tr"  style="background-color: ${tempTry.color}">
                                    <td class="base_td">
                                            ${tempTry.shortText}
                                    </td>

                                    <td class="base_td">
                                            ${tempTry.durationSec}
                                    </td>

                                    <td class="base_td">
                                            ${tempTry.result}
                                    </td>

                                    <td class="base_td">
                                        <button onclick="window.open('edit_ticket?id=${tempTry.ticket}')">
                                            Редактировать
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>