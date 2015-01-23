<%@ page import="org.ironbrain.core.Exam" %>
<%@ page import="org.ironbrain.utils.DateUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Экзаменовка-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
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
                        </c:if>
                        <c:if test="${exam == null}">
                            <ib:examList exams="${exams}"/>
                        </c:if>
                    </div>
                </td>
                <td valign="top">
                    <div class="bg">
                        <div id="ticketWindow">
                            <c:if test="${exam == null}">
                                <ib:reminds reminds="${reminds}"/>
                            </c:if>
                            <c:if test="${exam != null}">
                                Билет № ${tempTry.num} из ${exam.count}
                                <br>
                                Вопросы: <br>
                                <div class="examTextOutput" id="questionsDiv">
                                        ${ticket.questions}
                                </div>

                                <ib:margin/>
                                <button id="answerButton" onclick="exam.openAnswers(true)">Ответы
                                </button>

                                <div id="answers" style="display: none">
                                    <ib:margin/>
                                    Ответы: <br>
                                    <div class="examTextOutput" id="answersDiv">
                                            ${ticket.answers}
                                    </div>

                                    Повторить:<br>
                                    <input value="rNow" type="radio" name="toRemindRButton">Сейчас</input>
                                    <input checked value="rLater" type="radio" name="toRemindRButton">Потом</input>
                                    <input value="rDay" type="radio" name="toRemindRButton">Через день</input>
                                    <input value="rWeek" type="radio" name="toRemindRButton">Через неделю</input>
                                    <input value="rMonth" type="radio" name="toRemindRButton">Через месяц</input>
                                    <input value="rHalfYear" type="radio" name="toRemindRButton">Через пол года</input>
                                    <input value="rYear" type="radio" name="toRemindRButton">Через год</input>

                                    <br>
                                    <ib:margin/>
                                    <button accesskey="c" id="turnAsideTicketButton" onclick="exam.openAnswers(false)">
                                        Свернуть
                                    </button>

                                    <button id="nextTicketButton" onclick="exam.nextTicket(${tempTry.id})">Следующий
                                        билет
                                    </button>

                                    <button id="nextTicketButton"
                                            onclick="window.open('edit_ticket?id=${tempTry.ticket}')">Редактировать
                                    </button>
                                </div>
                            </c:if>
                        </div>
                        <script>
                            exam.configure();
                        </script>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>