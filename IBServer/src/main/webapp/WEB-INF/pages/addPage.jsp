<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<%
    String baseName = FilenameUtils.getBaseName(request.getRequestURI());
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script type="text/javascript" src="res/js/jquery-2.1.1.js"></script>
        <script type="text/javascript" src="res/js/addPage.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                initAddPage();
            });
        </script>
        <link rel="stylesheet" type="text/css" href="res/css/common.css" media="screen"/>
        <meta name="viewport" content="width=320"/>
        <title>Добавление билета</title>
    </head>
    <body>
        <table>
            <tr>
                <td colspan="2" style="width:100%;">
                    <div class="bg">
                        <ib:path list="${path}"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="bg">
                        <ib:sections list="${sections}"/>

                        <br>
                        <a href="add?sec=${parentSection}">&lt;&lt; Назад</a>

                        <br>
                        <label>Имя раздела<input type="text" size="20" id="newSectionName"></label>
                        <br>
                        <button id="addSectionButton" onClick="onAddSection(${thisSection});">Добавить раздел
                        </button>
                        <br>
                        <button id="addTicketButton" onClick="onAddNewTicket(${thisSection});">Добавить билет
                        </button>
                    </div>
                </td>
                <td valign="top">
                    <c:if test="${ticket != null}">
                        <div class="bg" style="width:700px;">
                            <script>
                                setSaveOnBlur(${ticket.id});
                                setSaveOnUnload(${ticket.id});
                                setRegularSave(${ticket.id});
                            </script>
                            <ib:ticketEditor ticket="${ticket}"/>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    ms == ${ms}
                </td>
            </tr>
        </table>
    </body>
</html>
