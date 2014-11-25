<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

Вопросы:<br>
<ib:richEditor html="${ticket.answers}" divID="answersDiv"/>
Ответы:<br>
<ib:richEditor html="${ticket.questions}" divID="questionsDiv"/>

<br>
<button onclick="saveTicket(${ticket.id});">Сохранить</button>
<span id="saveProgress"></span>