<%@ tag import="org.ironbrain.utils.DateUtils" pageEncoding="UTF-8" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="secToFields" required="true" type="java.util.List<org.ironbrain.core.SectionToField>" %>
<%@ attribute name="unusedFields" required="true" type="java.util.List<org.ironbrain.core.Field>" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
    var unusedFields = [];

    <c:forEach var="unusedField" items="${unusedFields}">
    unusedFields.push({label: "${unusedField.label}", id:${unusedField.id}});
    </c:forEach>

    function onCreateFieldOpen() {
        var form = $("#createFieldForm");
        if (form.is(':visible')) {
            form.hide();
            $("#selectFieldButton").show();
        } else {
            form.show();
            $("#selectFieldButton").hide();
            $("#fieldName").focus();
        }
    }

    function onCreateFieldOK() {
        var name = $("#fieldName").val();

        $.get('add_field', {
                    name: name
                },
                function (data) {
                    if (data.res = Result.OK) {
                        addFieldToSectionRequest(data.data, ${section.id}, name);
                    }
                }
        );
    }

    function addFieldToSectionRequest(fieldId, sectionId, name, onOK) {
        $.get('add_field_to_section', {
                    fieldId: fieldId,
                    sectionId: sectionId
                },
                function (data) {
                    if (data.res = Result.OK) {
                        addField(name, data.data);
                        $("#createFieldForm").hide();
                        $("#selectFieldButton").show();
                        $("#fieldName").val("");
                        if (onOK != null) {
                            onOK();
                        }
                    }
                }
        );
    }

    function addField(name, secToFieldId, inverse) {
        var newField = $("#fieldPattern").clone();

        var selectFieldMenu = [{
            name: 'Отрицание',
            fun: function () {
                $.get('invert_field', {
                            fieldToSecId: secToFieldId
                        },
                        function (data) {
                            if (data.res = Result.OK) {
                                if (data.data) {
                                    newField.attr("class", "fieldsBGInverse");
                                } else {
                                    newField.attr("class", "fieldsBG");
                                }
                            }
                        }
                );
            }
        }, {
            name: 'Удалить',
            fun: function () {
                $.get('del_secToField', {
                            sectionToFieldId: secToFieldId
                        },
                        function (data) {
                            if (data.res = Result.OK) {
                                newField.remove();
                            }
                        }
                );
            }
        }];

        if (inverse) {
            newField.attr("class", "fieldsBGInverse");
        }
        newField.html("&nbsp;" + name);
        newField.contextMenu(selectFieldMenu);
        $("#fieldsContainer").append(newField);
        newField.show();
    }

    function addSelectionField(label, id) {
        var newFieldSelection = $("#fieldSelectionPattern").clone();
        newFieldSelection.html(label);
        newFieldSelection.click(function () {
            addFieldToSectionRequest(id, ${section.id}, label, function () {
                newFieldSelection.remove();
            });
        });

        $("#fieldsSelectionContainer").append(newFieldSelection);
        newFieldSelection.show();
    }

    $(document).ready(function () {
        <c:forEach var="secToField" items="${secToFields}">
        addField("${secToField.field.label}", ${secToField.id}, ${secToField.inverse});
        </c:forEach>

        unusedFields.forEach(function (field) {
            addSelectionField(field.label, field.id);
        });
    });

    function onSelectField() {
        $("#selectFieldButton").hide();
        $("#createFieldButton").hide();
        $("#createFieldForm").hide();
        $("#fieldsSelection").show();
    }

    function onCreateFieldClose() {
        $("#fieldsSelection").hide();
        $("#createFieldButton").show();
        $("#selectFieldButton").show();
    }
</script>

<div class="ticketNameSpan">
    <label>
        Билет: <input type="text" id="ticketLabel" value="${section.label}">
    </label>
    Создан <i><%= DateUtils.getNiceDate(ticket.getCreateDate()) %>
</i>
    &nbsp;&nbsp;
    Обновлён <i><%= DateUtils.getNiceDate(ticket.getEditDate()) %>
</i>

    <!-- Вспомнить после <i><%= DateUtils.getNiceDate(ticket.getRemind()) %></i> -->
</div>

<ib:editorTools/><br>

Ответы:<br>
<ib:richEditor ticket="${ticket}" html="${ticket.answers}" section="${section}" divID="answersDiv"/>
Вопросы:<br>
<ib:richEditor ticket="${ticket}" html="${ticket.questions}" section="${section}" divID="questionsDiv"/>

<div style="margin-top: 5px; margin-bottom: 3px">
    Области:
    <div id="fieldsContainer" style="margin-top: 3px; margin-bottom: 3px">
        <button class="fieldsBG" id="fieldPattern" hidden>Pattern</button>
    </div>

    <nobr>
        <button id="createFieldButton" onclick="onCreateFieldOpen()">Создать</button>
        <span id="createFieldForm" style="display: none;">
            <input type="text" placeholder="Имя области" id="fieldName"/>
            <button onclick="onCreateFieldOK()">OK</button>
        </span>
    </nobr>

    <button onclick="onSelectField()" id="selectFieldButton">Выбрать</button>
    <div class="fieldSelection" id="fieldsSelection" hidden>
        <div id="fieldsSelectionContainer">
            <button id="fieldSelectionPattern" class="selectedField" hidden></button>
        </div>
        <button onclick="onCreateFieldClose()" style="margin-left: 5px; margin-top: 5px">Закрыть</button>
    </div>
</div>

<div style="margin-top: 5px; margin-bottom: 3px">
    <nobr>

    </nobr>
</div>

<button id="saveButton" onclick="saveTicket(${ticket.id},${section.id});">Сохранить</button>
<span id="saveProgress"></span>
