<%@ attribute name="html" required="true" type="java.lang.String" %>
<%@ attribute name="divID" required="true" type="java.lang.String" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<div class="richTextEditor" id="${divID}" contenteditable="true">${html}</div>

<script>
    initRichEditor('${divID}',${section.id}, ${ticket.id});
</script>