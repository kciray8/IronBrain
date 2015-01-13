<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>
<link rel="stylesheet" type="text/css" href="res/css/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="res/css/contextMenu.css"/>

<link rel="icon" type="img/ico" href="favicon.ico">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="res/js/common.js"></script>
<script type="text/javascript" src="res/js/jquery.js"></script>
<script type="text/javascript" src="res/js/jquery-ui.js"></script>
<script type="text/javascript" src="res/js/jquery.highlight-4.js"></script>

<script type="text/javascript" src="res/js/contextMenu.js"></script>

<script type="text/javascript" src="res/js/exam.js"></script>
<script type="text/javascript" src="res/js/addPage.js"></script>
<script type="text/javascript" src="res/js/section.js"></script>


<script type="text/javascript">
    $(document).ready(function () {
        initAddPage();
    });
</script>
<link rel="stylesheet" type="text/css" href="res/css/common.css" />
<meta name="viewport" content="width=320"/>
<title>${title}</title>