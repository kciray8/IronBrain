<%@ tag import="org.ironbrain.utils.DateUtils" pageEncoding="UTF-8" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="secToFields" required="true" type="java.util.List<org.ironbrain.core.SectionToField>" %>
<%@ attribute name="unusedFields" required="true" type="java.util.List<org.ironbrain.core.Field>" %>
<%@ attribute name="data" required="true" type="org.ironbrain.SessionData" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<div class="ticketNameSpan">

    <ib:input id="ticketLabel" value="${section.label}" />

    Создан <i><%= DateUtils.getNiceDate(ticket.getCreateDate()) %>
</i>
    &nbsp;&nbsp;
    Обновлён <i><%= DateUtils.getNiceDate(ticket.getEditDate()) %>
</i>

</div>

Ответы:<br>
<ib:richEditor data="${data}" editorName="answers" ticket="${ticket}"
               html="${ticket.answers}" section="${section}" divID="answersDiv"/>
Вопросы:<br>
<ib:richEditor data="${data}"   editorName="questions" ticket="${ticket}"
                html="${ticket.questions}" section="${section}" divID="questionsDiv"/>

<ib:gap px="5" />
<ib:button id="showCustomInfo" >Показать дополнительное поле</ib:button>

<div hidden id="customInfoDivBlock">
    <ib:button id="hideCustomInfo" onClick="saveTicket(${ticket.id},${section.id});">Скрыть дополнительное поле</ib:button>

    <ib:gap px="5" />

    <ib:richEditor data="${data}"  editorName="customInfo" ticket="${ticket}"
                    html="${ticket.customInfo}" section="${section}" divID="customInfoDiv"/>
</div>

<div style="margin-top: 5px; margin-bottom: 3px">
    <ib:fieldEditor target="section" fieldMappers="${secToFields}" targetId="${section.id}" unusedFields="${unusedFields}"/>
</div>

<div style="margin-top: 5px; margin-bottom: 3px">
    <nobr>

    </nobr>
</div>

<ib:button id="saveButton" onClick="saveTicket(${ticket.id},${section.id});" style="margin-right:5px">Сохранить</ib:button>
<span id="saveProgress"></span>

<script>
    $("#answersDiv").focus();

    $("#showCustomInfo").click(function(){
        $("#customInfoDivBlock").show();
        $("#showCustomInfo").hide();
    });

    $("#hideCustomInfo").click(function(){
        $("#customInfoDivBlock").hide();
        $("#showCustomInfo").show();
    });
</script>