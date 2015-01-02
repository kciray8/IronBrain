<%@ tag import="org.ironbrain.utils.DateUtils" pageEncoding="UTF-8" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="secToFields" required="true" type="java.util.List<org.ironbrain.core.SectionToField>" %>
<%@ attribute name="unusedFields" required="true" type="java.util.List<org.ironbrain.core.Field>" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<div class="ticketNameSpan">
    <label>
        Билет: <input type="text" id="ticketLabel" value="${section.label}">
    </label>
    Создан <i><%= DateUtils.getNiceDate(ticket.getCreateDate()) %>
</i>
    &nbsp;&nbsp;
    Обновлён <i><%= DateUtils.getNiceDate(ticket.getEditDate()) %>
</i>

</div>

Ответы:<br>
<ib:richEditor editorName="answers" ticket="${ticket}"
               html="${ticket.answers}" section="${section}" divID="answersDiv"/>
Вопросы:<br>
<ib:richEditor  editorName="questions" ticket="${ticket}"
                html="${ticket.questions}" section="${section}" divID="questionsDiv"/>

<div style="margin-top: 5px; margin-bottom: 3px">
    <ib:fieldEditor target="section" fieldMappers="${secToFields}" targetId="${section.id}" unusedFields="${unusedFields}"/>
</div>

<div style="margin-top: 5px; margin-bottom: 3px">
    <nobr>

    </nobr>
</div>

<button id="saveButton" onclick="saveTicket(${ticket.id},${section.id});">Сохранить</button>
<span id="saveProgress"></span>
