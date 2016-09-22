<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="onClick" type="java.lang.String" %>
<%@ attribute name="style" type="java.lang.String" %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="accesskey" type="java.lang.String" %>

<button class="btn btn-xs btn-default" accesskey="${accesskey}" style="${style}" id="${not empty id?id:""}" onclick="${onClick}">
    <jsp:doBody/>
</button>