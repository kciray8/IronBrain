<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="src" required="true" type="java.lang.String" %>
<%@ attribute name="srcHover" required="true" type="java.lang.String" %>
<%@ attribute name="onClick" required="true" type="java.lang.String" %>
<%@ attribute name="accessKey" type="java.lang.String" %>

<img onmouseover="this.src='${srcHover}'" onmouseout="this.src='${src}'"
     style="vertical-align: middle;"  accesskey="${accessKey}" onclick="${onClick}" src="${src}" />