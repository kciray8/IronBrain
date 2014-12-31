<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Направления-"/>
    </head>
    <body>
        <script>
            function addDirection() {
                $.get("add_direction", {
                    name: $("#directionName").val()
                }, function (data) {
                    if (data.res == Result.OK) {
                        location.href = ("./direction?id=" + data.data);
                    }
                });
            }

            function recalculateDirection() {
                $.get("recalculate_direction", {
                    id: ${direction.id}
                }, function (data) {
                    if (data.res == Result.OK) {
                        location.reload();
                    }
                });
            }
        </script>

        <table width="100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top;">
                    <div class="bg">
                        <c:forEach var="direction" items="${directions}">
                            <a href="./direction?id=${direction.id}">${direction.name}</a>
                            <br>
                        </c:forEach>
                        <ib:gap px="5"/>

                        <input id="directionName" type="text" placeholder="Имя направления"/>
                        <ib:gap px="5"/>
                        <button onclick="addDirection()">Добавить</button>
                    </div>
                </td>

                <td valign="top">
                    <div class="bg">
                        <c:if test="${direction == null}">
                            Выберите существующее направление или добавьте новое
                        </c:if>
                        <c:if test="${direction != null}">
                            Имя направления - ${direction.name}
                            <ib:gap px="5"/>

                            <ib:fieldEditor target="direction" fieldMappers="${directionToFields}"
                                            targetId="${direction.id}" unusedFields="${unusedFields}"/>

                            <ib:gap px="5"/>
                            Количество билетов - ${direction.ticketsCount}
                            <ib:gap px="5"/>
                            <button onclick="recalculateDirection()">Пересчитать</button>
                        </c:if>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>