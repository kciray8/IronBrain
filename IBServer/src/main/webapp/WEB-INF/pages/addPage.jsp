<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <c:if test="${ticket == null}">
            <ib:headBlock title="[${section.label}]"/>
        </c:if>
        <c:if test="${ticket != null}">
            <ib:headBlock title="${ticketSection.label}"/>
        </c:if>
        <ib:scripts/>
    </head>
    <body>
        <script>
            updateTime = ${pageGenerateDate};
            var thisSection = new Section("${section.id}", "${section.label}");
        </script>

        <table width="100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td colspan="2">
                    <div class="bg path">
                        <ib:path list="${path}"/>
                    </div>
                </td>
            </tr>

            <tr>
                <c:if test="${ticket == null}">
                    <td valign="top">
                        <c:if test="${data.user != null}">
                            <div class="bg" style="width: 300px">
                                <c:if test="${section.parent != null}">
                                    <a class="menu" href="add?sec=${section.parent}">← Назад</a>
                                    <br>
                                </c:if>

                                <ib:sections list="${sections}"/>

                                <ib:gap px="5"/>
                                <input placeholder="Имя раздела" style="width:100%;" type="text" size="20"
                                       id="newSectionName">
                                <ib:gap px="5"/>
                                <button id="addSectionButton" onClick="onAddSection(${section.id});">Добавить раздел
                                </button>
                                <ib:gap px="5"/>
                                <button id="addTicketButton" onClick="onAddNewTicket(${section.id});">Добавить билет
                                </button>
                                <c:if test="${bufferSectionId != null}">
                                    <ib:gap px="5"/>
                                    <button id="addTicketButton" onClick="thisSection.paste()">Вставить
                                        раздел
                                    </button>
                                </c:if>
                            </div>
                        </c:if>
                    </td>
                </c:if>

                <c:if test="${ticket == null}">
                    <td valign="top" style="width: 100%">
                        <div class="bg">
                            Название раздела: ${section.label}<br>
                            <ib:gap px="5"/>
                            <button id="renameButton" onclick="thisSection.showRenameGui();">Переименовать</button>
                            <div hidden id="renameSectionGUI">
                                <input autofocus type="text" size="20" id="renameSectionName">
                                <button onClick="thisSection.rename()">OK</button>
                                <button onClick="thisSection.hideRenameGui()">Отмена</button>
                            </div>

                            <ib:gap px="5"/>
                            <ib:fieldEditor target="section" fieldMappers="${secToFlds}" targetId="${section.id}"
                                            unusedFields="${unusedFields}"/>
                        </div>
                    </td>
                </c:if>

                <c:if test="${ticket != null}">
                    <td style="width: 200px;vertical-align: top;">
                        <div class="bg">
                            Вспомнить: <br>
                                ${ticketSection.remindDateStr}<br>
                            <ib:gap px="5"/>
                            <button accesskey="a"
                                    onclick="onAddNewTicketAfterSave(${section.id},${ticket.id},${ticketSection.id});">
                                Добавить билет
                            </button>
                            <ib:gap px="5"/>
                            <div id="loadingDiv" hidden>
                                <div>Файл - <span id="loadingFileName"></span></div>
                                <div>
                                    Загрузка... <span id="loadingPercent"></span>%
                                </div>
                            </div>
                        </div>
                    </td>

                    <td valign="top">
                        <div class="bg">
                            <script>
                                setSaveOnBlur(${ticket.id}, ${ticketSection.id});
                                setSaveOnUnload(${ticket.id}, ${ticketSection.id});
                                setRegularSave(${ticket.id}, ${ticketSection.id});
                            </script>
                            <ib:ticketEditor data="${data}" ticket="${ticket}" section="${ticketSection}"
                                             secToFields="${secToFlds}" unusedFields="${unusedFields}"/>
                        </div>
                    </td>
                </c:if>
            </tr>
        </table>
    </body>
</html>