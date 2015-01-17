<%@ attribute name="html" required="true" type="java.lang.String" %>
<%@ attribute name="divID" required="true" type="java.lang.String" %>
<%@ attribute name="section" required="true" type="org.ironbrain.core.Section" %>
<%@ attribute name="ticket" required="true" type="org.ironbrain.core.Ticket" %>
<%@ attribute name="editorName" required="true" type="java.lang.String" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ib" tagdir="/WEB-INF/tags" %>
<%@ tag pageEncoding="UTF-8" %>

<button onclick="document.execCommand('bold',false,null);"><b>B</b></button>
<button onclick="document.execCommand('italic',false,null);"><i>I</i></button>
<button onclick="document.execCommand('underline',false,null);"><span style="text-decoration: underline;">U</span>
</button>

<button onclick="document.execCommand('fontName',false,'monospace');"><span
        style="font-family: monospace">CODE</span>
</button>

<button onclick="document.execCommand('insertImage',false,'http://upload.wikimedia.org/wikipedia/commons/8/8c/JPEG_example_JPG_RIP_025.jpg');">
</button>

<button onclick="document.execCommand('removeFormat',false,null);" title="Очистить формат">X</button>
<button onclick="document.execCommand('backColor',false,'#9CEBFF');"><ib:marker color="#9CEBFF"/></button>
<button onclick="document.execCommand('backColor',false,'#FFFFFF');"><ib:marker color="#FFFFFF"/></button>

<button onclick="document.execCommand('strikeThrough',false,null);"><s>abc</s></button>


<!-- ТЕКСТ НАЗВАНИЕ ИТОГ -->
<button onclick="document.execCommand('createLink',false,'vk.com');"><span
        style="text-decoration: underline;color: #0000EE">link</span>
</button>
<button onclick="document.execCommand('insertUnorderedList',false,null);">list</button>

<button onclick="onEditorModeChange(${editorName}Editor);">HTML</button>

<img accesskey="p" onclick="takeScreenShot(${editorName}Editor);" src="res/png/camera.png">


<button onclick="newLineDown(${editorName}Editor);">newline Down</button>
<button onclick="newLineUp(${editorName}Editor);">newline Up</button>

<div onblur="onEditorBlur(event)" onfocus="onEditorFocus(event)"
     class="richTextEditor" id="${divID}" onload="alert('ddd')"
     contenteditable="true">${html}</div>


<script>
    var ${editorName}Editor = {
        htmlMode: false,
        div: $("#${divID}")
    };
    initEditor(${editorName}Editor.div);

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
        var webSocket = new WebSocket("ws://localhost:9993/websockets/ib");
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

    function onEditorFocus(event) {
        //activeEditor = $(event.srcElement);
    }

    function onEditorBlur(event) {
        //activeEditor = null;
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