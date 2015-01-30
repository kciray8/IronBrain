<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:scripts/>
        <ib:headBlock title="-Документация-"/>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <ib:mainMenuBlock data="${data}"/>
            </tr>

            <tr>
                <td style="width: 210px;vertical-align: top; padding-right: 10px">
                    <ib:panel title="Команды">
                        <ib:button style="margin-top: 5px" onClick="$('.spoilerHeader').click()">
                            Развернуть все разделы
                        </ib:button>
                        <ib:space px="5"/>
                        <ib:button style="margin-top: 5px" onClick="$('.spoilerHeader2').click()">
                            Свернуть все разделы
                        </ib:button>
                    </ib:panel>
                </td>

                <td valign="top">
                    <ib:panel title="Документация">
                        <ib:doc/>
                    </ib:panel>
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