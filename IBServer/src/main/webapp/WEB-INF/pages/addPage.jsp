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
                    <ib:bg>
                        <ib:path list="${path}"/>
                    </ib:bg>
                </td>
            </tr>

            <tr>
                <c:if test="${ticket == null}">
                    <td valign="top" style="width: 310px;vertical-align: top; padding-right: 10px">
                        <c:if test="${data.user != null}">
                            <ib:panel title="Навигация">
                                <c:if test="${section.parent != null}">
                                    <a class="menu" href="add?sec=${section.parent}">← Назад</a>
                                    <br>
                                </c:if>

                                <ib:sections list="${sections}"/>

                                <ib:gap px="5"/>
                                <ib:input placeholder="Имя раздела" id="newSectionName"/>

                                <ib:gap px="5"/>
                                <ib:button id="addSectionButton" onClick="onAddSection(${section.id});">Добавить раздел
                                </ib:button>
                                <ib:gap px="5"/>
                                <ib:button id="addTicketButton" onClick="onAddNewTicket(${section.id});">Добавить билет
                                </ib:button>
                                <c:if test="${bufferSectionId != null}">
                                    <ib:gap px="5"/>
                                    <ib:button id="addTicketButton" onClick="thisSection.paste()">Вставить
                                        раздел
                                    </ib:button>
                                </c:if>
                            </ib:panel>
                        </c:if>
                    </td>
                </c:if>

                <c:if test="${ticket == null}">
                    <td valign="top">
                        <ib:panel title="Редактор раздела">
                            Название раздела: ${section.label}<br>
                            <ib:gap px="5"/>
                            <ib:button id="renameButton"
                                       onClick="thisSection.showRenameGui();">Переименовать</ib:button>
                            <div hidden id="renameSectionGUI">
                                <ib:input attr="autofocus" style="width:200px;" id="renameSectionName" size="20"/>
                                <ib:gap px="5"/>
                                <ib:button style="margin-right:5px" onClick="thisSection.rename()">OK</ib:button>
                                <ib:button onClick="thisSection.hideRenameGui()">Отмена</ib:button>
                            </div>

                            <ib:gap px="5"/>
                            <ib:fieldEditor target="section" fieldMappers="${secToFlds}" targetId="${section.id}"
                                            unusedFields="${unusedFields}"/>
                        </ib:panel>
                    </td>
                </c:if>

                <c:if test="${ticket != null}">
                    <td style="width: 210px;vertical-align: top; padding-right: 10px">
                        <ib:panel title="Команды">
                            Вспомнить: <br>
                                ${ticketSection.remindDateStr}<br>
                            <ib:gap px="5"/>
                            <ib:button
                                    onClick="onAddNewTicketAfterSave(${section.id},${ticket.id},${ticketSection.id});">
                                Добавить билет
                            </ib:button>
                            <ib:gap px="5"/>
                            <div id="loadingDiv" hidden>
                                <div>Файл - <span id="loadingFileName"></span></div>
                                <div>
                                    Загрузка... <span id="loadingPercent"></span>%
                                </div>
                            </div>
                        </ib:panel>
                    </td>

                    <td valign="top">
                        <ib:panel title="Редактор билета">
                            <script>
                                setSaveOnBlur(${ticket.id}, ${ticketSection.id});
                                setSaveOnUnload(${ticket.id}, ${ticketSection.id});
                                setRegularSave(${ticket.id}, ${ticketSection.id});
                            </script>
                            <ib:ticketEditor data="${data}" ticket="${ticket}" section="${ticketSection}"
                                             secToFields="${secToFlds}" unusedFields="${unusedFields}"/>
                        </ib:panel>
                    </td>
                </c:if>
            </tr>
        </table>
    </body>
</html>