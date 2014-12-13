<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="res/css/jquery-ui.css"/>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="res/js/jquery.js"></script>
<script type="text/javascript" src="res/js/jquery-ui.js"></script>

<script type="text/javascript" src="res/js/exam.js"></script>
<script type="text/javascript" src="res/js/addPage.js"></script>

<script type="text/javascript">
    updateTime = ${pageGenerateDate};

    $(document).ready(function () {
        initAddPage();
    });
</script>
<link rel="stylesheet" type="text/css" href="res/css/common.css" media="screen"/>
<meta name="viewport" content="width=320"/>
<title>${title}</title>