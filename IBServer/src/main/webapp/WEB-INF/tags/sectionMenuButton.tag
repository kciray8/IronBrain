<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag pageEncoding="UTF-8" %>

<img onload="addSectionMenu(${section.id}, event);" class="inlineImgButton" src="./res/png/tune.png"/>