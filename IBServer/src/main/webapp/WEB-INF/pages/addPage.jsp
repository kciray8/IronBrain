<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<%
    String baseName = FilenameUtils.getBaseName(request.getRequestURI());
%>

<html>
    <head>
        <ib:headBlock title="-Билеты-" />
    </head>
    <body>
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
                        <c:if test="${data.user == null}">
                            <ib:login/>
                        </c:if>

                        <div class="bg">
                            <ib:sections list="${sections}"/>

                            <c:if test="${section.parent != null}">
                                <a href="add?sec=${section.parent}">&lt;&lt; Назад</a>
                                <br>
                            </c:if>
                            <br>
                            <input placeholder="Имя раздела" style="width:100%;" type="text" size="20"
                                   id="newSectionName">
                            <br>
                            <button id="addSectionButton" onClick="onAddSection(${section.id});">Добавить раздел
                            </button>
                            <br>
                            <button id="addTicketButton" onClick="onAddNewTicket(${section.id});">Добавить билет
                            </button>
                        </div>
                    </td>
                </c:if>

                <c:if test="${ticket != null}">
                    <td valign="top">
                        <div class="bg">
                            <c:if test="${ticket != null}">
                                <script>
                                    setSaveOnBlur(${ticket.id}, ${ticketSection.id});
                                    setSaveOnUnload(${ticket.id}, ${ticketSection.id});
                                    setRegularSave(${ticket.id}, ${ticketSection.id});
                                </script>
                                <ib:ticketEditor ticket="${ticket}" section="${ticketSection}"/>
                            </c:if>
                            <c:if test="${ticket == null}">
                                О программе
                            </c:if>
                        </div>
                    </td>
                </c:if>
            </tr>
            <tr>
                <td>
                    ms == ${ms}
                    <br>
                    var = <span id="var"></span>
                    <br>${debug_data}
                </td>
            </tr>
        </table>
    </body>
</html>