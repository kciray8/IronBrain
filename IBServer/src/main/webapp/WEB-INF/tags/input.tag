<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="value" type="java.lang.String" %>
<%@ attribute name="placeholder" type="java.lang.String" %>
<%@ attribute name="attr" type="java.lang.String" %>
<%@ attribute name="style" type="java.lang.String" %>


<%@ attribute name="size" type="java.lang.Integer" %>

<input class="form-control" type="text" id="${id}" placeholder="${placeholder}" value="${value}" ${attr} size="${size}" style="${style}" />