<%@ page import="org.ironbrain.core.Exam" %>
<%@ page import="org.ironbrain.utils.DateUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Экзаменовка-"/>
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
                                <div class="examTextOutput">
                                        ${ticket.questions}
                                </div>
                                <ib:margin/>
                                <button id="answerButton" onclick="
                                exam.tempTicketOpen = true;
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
                                    <button id="nextTicketButton" onclick="exam.nextTicket(${tempTry.id})">Следующий
                                        билет
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