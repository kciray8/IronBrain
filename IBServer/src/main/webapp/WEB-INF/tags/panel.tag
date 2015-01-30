<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute required="true" name="title" type="java.lang.String" %>

<div class="panel panel-default">
    <div class="panel-heading">${title}</div>
    <div class="panel-body">
        <jsp:doBody />
    </div>
</div>
