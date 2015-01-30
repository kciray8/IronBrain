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
                <td style="width: 200px;vertical-align: top; padding-right: 10px">
                    <c:if test="${exam != null}">
                        <ib:panel title="Экзамен">
                            Запущен <br>
                            ${exam.strStart}
                            <br>
                            Заход № ${tempTry.attemptNum}
                        </ib:panel>
                    </c:if>
                    <c:if test="${exam == null}">
                        <ib:panel title="Все экзмены">
                            <ib:examList exams="${exams}"/>
                        </ib:panel>
                    </c:if>
                </td>
                <td valign="top">

                    <div id="ticketWindow">
                        <c:if test="${exam == null}">
                            <ib:panel title="Сборка">
                                <ib:reminds reminds="${reminds}"/>
                            </ib:panel>
                        </c:if>
                        <c:if test="${exam != null}">
                            <ib:panel title="Билет № ${tempTry.num} из ${exam.count}">
                                Вопросы: <br>
                                <div class="examTextOutput" id="questionsDiv">
                                        ${ticket.questions}
                                </div>

                                <ib:margin/>
                                <ib:button id="answerButton" onClick="exam.openAnswers(true)">Ответы</ib:button>

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

                                    <ib:button accesskey="c" id="turnAsideTicketButton"
                                               onClick="exam.openAnswers(false)">Свернуть</ib:button>
                                    <ib:space px="5"/>
                                    <ib:button id="nextTicketButton"
                                               onClick="exam.nextTicket(${tempTry.id})">Следующий билет</ib:button>
                                    <ib:space px="5"/>
                                    <ib:button id="nextTicketButton"
                                               onClick="window.open('edit_ticket?id=${tempTry.ticket}')">Редактировать</ib:button>
                                </div>
                            </ib:panel>
                        </c:if>
                    </div>
                    <script>
                        exam.configure();
                    </script>

                </td>
            </tr>
        </table>
    </body>
</html>