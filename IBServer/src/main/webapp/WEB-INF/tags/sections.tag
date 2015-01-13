<%@ attribute name="list" required="true" type="java.util.List<org.ironbrain.core.Section>" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<script>
    function addSectionMenu(id, event) {
        var menu = [
            {
                name: 'Вспомнить',
                fun: function () {
                    exam.remindSection(id);
                }
            },

            {
                name: 'Вырезать',
                fun: function () {
                    $.get('cut_section', {
                                id: id
                            },
                            function (data) {
                                if (data.res == Result.OK) {
                                    location.reload();
                                }
                            });
                }
            },

            {
                name: 'Удалить',
                fun: function () {
                    deleteSectionDialog(id);
                }
            }
        ];

        $(event.srcElement).contextMenu(menu);
    }
</script>

<table>
    <c:forEach var="sec" items="${list}">
        <div>
            <ib:section sec="${sec}"/>
            <span style="margin-left: 2px"></span>
            <ib:sectionMenuButton section="${sec}"/>
        </div>
    </c:forEach>
</table>