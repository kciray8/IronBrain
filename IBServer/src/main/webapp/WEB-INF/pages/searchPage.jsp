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
                    <div class="bg">
                        <input autofocus type="text" id="query"/>
                        <button onclick="onSearch();">Поиск</button>
                        <div id="searchResult">

                        </div>

                        <script>
                            $("#query").keypress(function (e) {
                                if (e.which == 13) {
                                    onSearch();
                                }
                            });
                        </script>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>