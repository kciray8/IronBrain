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

            <c:if test="${direction != null}">
            function recalculateDirection() {
                $.get("recalculate_direction", {
                    id: ${direction.id}
                }, function (data) {
                    if (data.res == Result.OK) {
                        location.reload();
                    }
                });
            }

            function sliceAndRemind(directionId, count) {
                var paramObj = {
                    directionId: directionId,
                    count: count
                };
                location.href = "slice_and_remind?" + $.param(paramObj);
            }

            function showSliceMenu() {
                var menuBlock = $('#sliceMenu');
                if (menuBlock.is(':visible')) {
                    menuBlock.hide();
                } else {
                    menuBlock.show()
                }
            }
            </c:if>
        </script>

        <table width="100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top;">
                    <div class="bg">
                        <c:forEach var="direction" items="${directions}">
                            <a class="menu" href="./direction?id=${direction.id}">${direction.name}</a>
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
                            Вы помните ${direction.ticketKnownCount} из ${direction.ticketsCount} (${direction.knowPercentStr}%)
                            <ib:gap px="5"/>
                            <button onclick="recalculateDirection()">Пересчитать</button>
                            <ib:gap px="5"/>
                            <button onclick="showSliceMenu()">Нарезать билеты
                            </button>
                            <span id="sliceMenu" hidden><input value="20" id="sliceCount" type="text"> <button
                                    onclick="sliceAndRemind(${direction.id},$('#sliceCount').val())">OK
                            </button></span>
                        </c:if>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>