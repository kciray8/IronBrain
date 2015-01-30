<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<div class="spoilerHeader" onclick="onSpoilerClick($(this).next(),$(this).children().first(), $(this));">
    <span>►</span> ${title}
</div>
<div hidden style="border: #ddd 1px solid; padding: 3px">
    <jsp:doBody/>
</div>