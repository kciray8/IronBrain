<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Поиск-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width: 100%">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td valign="top">
                    <ib:panel title="Поиск">
                        <input size="50" autofocus type="text" id="query"/>
                        <ib:button onClick="onSearch();">Запрос</ib:button>
                        <div id="searchResult">

                        </div>

                        <script>
                            $("#query").keypress(function (e) {
                                if (e.which == 13) {
                                    onSearch();
                                }
                            });
                        </script>
                    </ib:panel>
                </td>
            </tr>
        </table>
    </body>
</html>