<%@ attribute name="html" required="true" type="java.lang.String" %>
<%@ attribute name="divID" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<div id="${divID}" contenteditable="true" style="padding: 5px; border: 1px solid #000000;">${html}</div>