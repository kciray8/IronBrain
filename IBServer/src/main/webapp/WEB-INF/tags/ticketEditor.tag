<%@ tag import="org.ironbrain.utils.DateUtils" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ tag pageEncoding="UTF-8" %>

<div class="ticketNameSpan">
    <label>
        Билет: <input type="text" id="ticketLabel" value="${section.label}">
    </label>
    Создан <i><%= DateUtils.getNiceDate(ticket.getCreateDate()) %></i>
    &nbsp;&nbsp;
    Обновлён <i><%= DateUtils.getNiceDate(ticket.getEditDate()) %></i>

    <!-- Вспомнить после <i><%= DateUtils.getNiceDate(ticket.getRemind()) %></i> -->
</div>

<ib:editorTools/><br>

Ответы:<br>
<ib:richEditor ticket="${ticket}" html="${ticket.answers}" section="${section}" divID="answersDiv"/>
Вопросы:<br>
<ib:richEditor ticket="${ticket}" html="${ticket.questions}" section="${section}" divID="questionsDiv"/>

<button onclick="saveTicket(${ticket.id},${section.id});">Сохранить</button>
<span id="saveProgress"></span>