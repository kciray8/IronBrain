<%@ attribute name="html" required="true" type="java.lang.String" %>
<%@ attribute name="divID" required="true" type="java.lang.String" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="editorName" required="true" type="java.lang.String" %>
<%@ attribute name="data" required="true" type="org.ironbrain.SessionData" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%@ tag pageEncoding="UTF-8" %>

<div>
    <div class="btn-group">
        <ib:button onClick="document.execCommand('bold',false,null);" style="width: 25px;"><b>B</b></ib:button>
        <ib:button onClick="document.execCommand('italic',false,null);" style="width: 25px;"><i>I</i></ib:button>
        <ib:button onClick="document.execCommand('underline',false,null);" style="width: 25px;"><span
                style="text-decoration: underline;">U</span>
        </ib:button>
    </div>

    <ib:button onClick="insertCode()" style="margin-right:3px"><span
            style="font-family: monospace">CODE</span>
    </ib:button>

    <ib:button onClick="document.execCommand('removeFormat',false,null);" style="margin-right:3px">X</ib:button>
    <ib:button onClick="document.execCommand('backColor',false,'#9CEBFF');" style="margin-right:3px"><ib:marker
            color="#9CEBFF"/></ib:button>
    <ib:button onClick="document.execCommand('backColor',false,'#FFFFFF');" style="margin-right:3px"><ib:marker
            color="#FFFFFF"/></ib:button>

    <ib:button onClick="document.execCommand('strikeThrough',false,null);"
               style="margin-right:3px"><s>abc</s></ib:button>

    <ib:button onClick="${divID}onCreateLink()" style="margin-right:3px"><span
            style="text-decoration: underline;color: #0000EE">link</span>
    </ib:button>

    <c:if test="${data.user.extended}">
        <ib:button onClick="onEditorModeChange(${editorName}Editor);" style="margin-right:3px">HTML</ib:button>
    </c:if>

    <ui:imgButton src="res/png/camera.png" srcHover="res/png/camera_blue.png"
                  onClick="takeScreenShot(${editorName}Editor);" accessKey="p"/>
    <ib:space px="5"/>
    <ui:imgButton src="res/png/round_up_arrow.png" srcHover="res/png/round_up_arrow_blue.png"
                  onClick="newLineUp(${editorName}Editor);" accessKey=""/>
    <ib:space px="5"/>
    <ui:imgButton src="res/png/round_down_arrow.png" srcHover="res/png/round_down_arrow_blue.png"
                  onClick="newLineDown(${editorName}Editor);" accessKey=""/>
</div>

<div onblur="${divID}onEditorBlur(event)" onfocus="${divID}onEditorFocus(event)"
     class="richTextEditor" id="${divID}"
     contenteditable="true">${html}</div>

<script>
    var ${editorName}Editor = {
        htmlMode: false,
        div: $("#${divID}")
    };
    initEditor(${editorName}Editor.div);

    function ${divID}onCreateLink(editor) {
        var link = prompt("Введите ссылку", "");
        var text = document.getSelection();

        Doc.insertHtml('<a href="' + link + '" target="_blank">' + text + '</a>');
    }

    function insertCode() {
        var text = document.getSelection();

        Doc.insertHtml('<pre style="font-family: Consolas; font-size: 12px;">' + text + '</pre>');
    }

    function uploadFile(blob, fromScreenShot) {
        var data = new FormData();
        data.append("file", blob);

        $("#loadingDiv").show();

        $.ajax({
            url: 'upload_file',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            xhr: function () {//For progress
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) {
                    myXhr.upload.addEventListener('progress', progress, false);
                }
                return myXhr;
            },
            success: function (data) {
                if (data.res == Result.OK) {
                    var url = data.data;

                    if (fromScreenShot) {
                        Doc.insertHtml("<br><img class='inner' src='" + url + "'><br>");
                    } else {
                        var fileRef = "File: " + blob.name;

                        if (blob.type.match('image.*')) {
                            Image.load(blob, function (image) {
                                if (image.width < 400) {
                                    Doc.insertHtml("<br><img class='inner' src='" + url + "'><br>");
                                } else {
                                    Doc.insertHtml(HTML.linkAndImage(url, "width=100 class='preview'"));
                                }
                            });

                            return;
                        }

                        Doc.insertHtml(String.format("<a href='{0}'>{1}</a>", url, fileRef));
                    }
                }
                $("#loadingDiv").hide();
            }
        });
    }

    function progress(e) {
        if (e.lengthComputable) {
            var max = e.total;
            var current = e.loaded;

            var percentage = Math.round((current * 100) / max);
            $("#loadingPercent").text(percentage);
        }
    }

    function initEditor(editor) {
        var editorPureDiv = editor[0];

        editorPureDiv.addEventListener("paste", function (e) {
            var html = e.clipboardData.getData("text/html");

            if (!jQuery.isEmptyObject(html)) {
                e.preventDefault();

                //Small fix (Windows - copy from Intellij IDEA)
                html = html.replace("font-size:12pt", "font-size:12px") + "<br>";

                document.execCommand("insertHTML", false, html);
            }
        });

        editorPureDiv.ondragover = function () {
            editorPureDiv.className = 'fileDropMode';
            return false;
        };

        editorPureDiv.ondragend = function () {
            editorPureDiv.className = 'richTextEditor';
            return false;
        };

        editorPureDiv.ondragleave = function () {
            editorPureDiv.className = 'richTextEditor';
            return false;
        };

        editorPureDiv.ondrop = function (e) {
            editorPureDiv.className = 'richTextEditor';
            var file = e.dataTransfer.files[0];

            uploadFile(file, false);

            return false;
        };
    }

    function takeScreenShot(editor) {
        var webSocket = new WebSocket("ws://localhost:${data.user.port}/websockets/ib");
        webSocket.onopen = function (event) {
            webSocket.send("screenshot");
        };

        webSocket.onmessage = function (event) {
            uploadFile(event.data, true);
        };

        webSocket.onerror = function (event) {
            alert("IBClient не запущен!");
        }
    }

    function ${divID}onEditorFocus(event) {
        activeEditor = $("#${divID}");
        if (activeEditor != null) {
            lastActiveEditor = activeEditor;
        }
    }

    function ${divID}onEditorBlur(event) {
        activeEditor = null;
    }

    function onEditorModeChange(editorObject) {
        var editorDiv = editorObject.div;
        editorObject.htmlMode = !editorObject.htmlMode;

        if (editorObject.htmlMode) {
            editorDiv.text(editorDiv.html());
            editorDiv.addClass("plainHtmlEditor");
        } else {
            editorDiv.removeClass("plainHtmlEditor");
            editorDiv.html(editorDiv.text());
        }
    }

    function newLineDown(editorObject) {
        var editorDiv = editorObject.div;
        editorDiv.append("<br>");
    }

    function newLineUp(editorObject) {
        var editorDiv = editorObject.div;
        editorDiv.prepend("<br>");
    }

</script>