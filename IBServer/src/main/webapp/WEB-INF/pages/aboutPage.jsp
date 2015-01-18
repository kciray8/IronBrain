<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <ib:headBlock title="-О сайте-"/>
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

                    </div>
                </td>

                <td style="vertical-align: top;">
                    <div class="bg">
                        <ib:about ib="${ib}"/>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>