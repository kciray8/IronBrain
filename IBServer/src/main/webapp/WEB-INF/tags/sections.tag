<%@ attribute name="list" required="true" type="java.util.List<org.ironbrain.core.Section>" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>
<table>
    <c:forEach var="sec" items="${list}">
        <tr class="sectionRow">
            <td>
                <ib:section sec="${sec}"/>
            </td>
            <td style="padding-left: 15px">
                <a target="_blank" onClick="onDeleteSection(${sec.id}); return false ;">X</a>
            </td>
        </tr>
    </c:forEach>
</table>