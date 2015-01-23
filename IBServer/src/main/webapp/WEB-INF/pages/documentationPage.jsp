<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-Документация-"/>
        <ib:scripts/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 200px;vertical-align: top;">
                    <div class="bg">
                        <button onclick="window.history.back()">Назад</button>
                    </div>
                </td>

                <td valign="top">
                    <div class="bg">
                        <ib:doc />
                    </div>
                </td>
            </tr>
        </table>

        <script>
            function onSpoilerClick(contentDiv, headerSpanSign, div) {
                var animationDelay = 300;

                if (contentDiv.is(":visible")) {
                    contentDiv.hide(animationDelay);
                    headerSpanSign.html("►");
                    div.attr('class', 'spoilerHeader');
                } else {
                    contentDiv.show(animationDelay);
                    headerSpanSign.html("▼");
                    div.attr('class', 'spoilerHeader2');
                }
            }
        </script>
    </body>
</html>