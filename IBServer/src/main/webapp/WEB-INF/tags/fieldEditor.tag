<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>


<%@ attribute name="targetId" required="true" type="java.lang.Integer" %>
<%@ attribute name="target" required="true" type="java.lang.String" %>
<%@ attribute name="fieldMappers" required="true" type="java.util.List<org.ironbrain.core.IFieldMapper>" %>
<%@ attribute name="unusedFields" required="true" type="java.util.List<org.ironbrain.core.Field>" %>


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
                        addFieldToTargetRequest(data.data, ${targetId}, name);
                    }
                }
        );
    }

    function addFieldToTargetRequest(fieldId, targetId, name, onOK) {
        $.get('add_field_to_${target}', {
                    fieldId: fieldId,
                    targetId: targetId
                },
                function (data) {
                    if (data.res = Result.OK) {
                        addField(name, data.data, false, fieldId);
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

    function addField(name, mapperId, inverse, fieldId) {
        var newField = $("#fieldPattern").clone();

        var selectFieldMenu = [{
            name: 'Отрицание',
            fun: function () {
                $.get('invert_${target}_to_field', {
                            id: mapperId
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
                $.get('delete_${target}_to_field', {
                            id: mapperId
                        },
                        function (data) {
                            if (data.res = Result.OK) {
                                newField.remove();
                                addSelectionField(name, fieldId);
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
            addFieldToTargetRequest(id, ${targetId}, label, function () {
                newFieldSelection.remove();
            });
        });

        $("#fieldsSelectionContainer").append(newFieldSelection);
        newFieldSelection.show();
    }

    $(document).ready(function () {
        <c:forEach var="fieldMapper" items="${fieldMappers}">
        addField("${fieldMapper.field.label}", ${fieldMapper.id},
        ${fieldMapper.inverse}, "${fieldMapper.field.id}");
        </c:forEach>

        unusedFields.forEach(function (field) {
            addSelectionField(field.label, field.id);
        });

        $('#fieldName').keydown(function(event) {
            if (event.keyCode == 13) {
                onCreateFieldOK();
                return false;
            }
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