<%@ page import="org.ironbrain.core.Exam" %>
<%@ page import="org.ironbrain.utils.DateUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <body>
        <head>
            <link rel="stylesheet" type="text/css" href="res/css/jquery-ui.css"/>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <script type="text/javascript" src="res/js/jquery.js"></script>
            <script type="text/javascript" src="res/js/jquery-ui.js"></script>

            <script type="text/javascript" src="res/js/exam.js"></script>
            <script type="text/javascript" src="res/js/addPage.js"></script>

            <link rel="stylesheet" type="text/css" href="res/css/common.css" media="screen"/>
            <meta name="viewport" content="width=320"/>

            <title>Экзаменовка</title>
        </head>
        <table style="width:100%;">
            <tr>
                <ib:headerBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top;">
                    <div class="bg">
                        <c:if test="${exam != null}">
                            Экзамен
                            <br>
                            Запущен <br>
                            <%=
                        DateUtils.getNiceDate(((Exam)
                                pageContext.findAttribute("exam")).getStartMs()) %>
                            <br>
                            Заход № ${tempTry.attemptNum}
                            <br>
                            Билет № ${tempTry.num} из ${exam.count}
                        </c:if>
                    </div>
                </td>
                <td valign="top">
                    <div class="bg">
                        <c:if test="${exam == null}">
                            <ib:reminds reminds="${reminds}"/>
                        </c:if>
                        <c:if test="${exam != null}">
                            Вопросы: <br>
                            <div class="examTextOutput">
                                    ${ticket.questions}
                            </div>
                            <ib:margin/>
                            <button id="answerButton" onclick="
                            $('#answers').show();
                            $('#answerButton').hide();
                            ">Ответить
                            </button>
                            <div id="answers" style="display: none">
                                <ib:margin/>
                                Ответы: <br>
                                <div class="examTextOutput">
                                        ${ticket.answers}
                                </div>
                                Повторить:<br>
                                <input checked value="rNow" type="radio" name="toRemindRButton">Сейчас</input>
                                <input value="rLater" type="radio" name="toRemindRButton">Потом</input>

                                <br>
                                <ib:margin/>
                                <button onclick="exam.nextTicket(${tempTry.id})">Следующий билет</button>
                            </div>
                        </c:if>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>