<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<%
    String baseName = FilenameUtils.getBaseName(request.getRequestURI());
%>

<html>
    <head>

        <link rel="stylesheet" type="text/css" href="res/css/jquery-ui.css"/>
        <!--
        <link rel="stylesheet" type="text/css" href="res/css/jquery-ui.structure.css"/>
        <link rel="stylesheet" type="text/css" href="res/css/jquery-ui.theme.css"/>
        -->

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script type="text/javascript" src="res/js/jquery.js"></script>
        <script type="text/javascript" src="res/js/jquery-ui.js"></script>

        <script type="text/javascript" src="res/js/exam.js"></script>
        <script type="text/javascript" src="res/js/addPage.js"></script>

        <script type="text/javascript">
            updateTime = ${pageGenerateDate};
            
            $(document).ready(function () {
                initAddPage();
            });
        </script>
        <link rel="stylesheet" type="text/css" href="res/css/common.css" media="screen"/>
        <meta name="viewport" content="width=320"/>
        <title>Добавление билета</title>
    </head>
    <body>
        <table width="100%">
            <tr>
                <ib:headerBlock data="${data}"/>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="bg path">
                        <ib:path list="${path}"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td valign="top" style="width:200px;">
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