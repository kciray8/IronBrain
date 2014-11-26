var Result = {
    OK: "OK",
    ERROR: "ERROR"
};

function handleResult(data) {
    if (data.res = Result.OK) {
        location.reload();
    } else {
        alert(data.message);
    }
}

function onAddSection(parentSectionId) {
    $.get('add_section', {
            parent: parentSectionId,
            label: $('#newSectionName').val()
        },
        function (data) {
            handleResult(data);
        }
    );
}

function onDeleteSection(id) {
    $.get('delete_section', {
            id: id
        },
        function (data) {
            handleResult(data);
        }
    );
}

//On press enter
function initAddPage() {
    $("#newSectionName").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#addSectionButton").click();
        }
    });
}

function saveTicket(id) {
    $("#saveProgress").html("Сохранение...");

    $.get('update_ticket', {
            answers: $('#answersDiv').html(),
            questions: $('#questionsDiv').html(),
            id: id
        },
        function (data) {
            //handleResult(data);
            var d = new Date();
            $("#saveProgress").html("OK! " + d.getMinutes() + ":" + d.getSeconds());
        });
}

function onAddNewTicket(section) {
    $.get('add_ticket', {
            section: section
        },
        function (section) {
            location.href = "add?sec=" + section.parent + "&tic=" + section.ticket;
        }
    );
}

function setSaveOnBlur(id) {
    $(window).blur(function () {
        saveTicket(id);
    });
}

function setSaveOnUnload(id) {
    window.onbeforeunload = function () {
        saveTicket(id);
    };
}

function setRegularSave(id) {
    function autoSave() {
        saveTicket(id);
    }

    setInterval(autoSave, 60 * 1000);
}

function clearSelection() {
    if (window.getSelection) {
        if (window.getSelection().empty) {
            window.getSelection().empty();
        } else if (window.getSelection().removeAllRanges) {
            window.getSelection().removeAllRanges();
        }
    } else if (document.selection) {
        document.selection.empty();
    }
}

function pasteHtmlAtCaret(html) {
    var sel, range;
    if (window.getSelection) {
        // IE9 and non-IE
        sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            var el = document.createElement("div");
            el.innerHTML = html;
            var frag = document.createDocumentFragment(), node, lastNode;
            while ((node = el.firstChild)) {
                lastNode = frag.appendChild(node);
            }
            range.insertNode(frag);

            // Preserve the selection
            if (lastNode) {
                range = range.cloneRange();
                range.setStartAfter(lastNode);
                range.collapse(true);
                sel.removeAllRanges();
                sel.addRange(range);
            }
        }
    } else if (document.selection && document.selection.type != "Control") {
        // IE < 9
        document.selection.createRange().pasteHTML(html);
    }
}

function pasteHtmlFromBuffer() {
    pasteHtmlAtCaret("<code> fd gdf gdf     fdgdfg   fdgfdgdfgfd</code>");
}

function deleteSectionDialog(id) {
    dialogYesNo("Вы действительно хотите удалить раздел?", function () {
        onDeleteSection(id );
    });
    $('#menu').remove();
}

function openSettings(id) {
    $(document.documentElement).append($('<ul>').attr('id', "menu")
            .append($('<li>').append("Вспомнить").attr("onClick", "deleteSectionDialog();"))
            .append($('<li>').append(" "))
            .append($('<li>').append("Удалить").attr("onClick", 'deleteSectionDialog(' + id + ");"))
            .append($('<li>').append(" "))
            .append($('<li>').append("Свойства"))
            .append($('<li>').append(" "))
            .append($('<li>').append("Закрыть").attr("onClick", "$('#menu').remove();"))
    );

    $("#menu").menu();
    $("#menu").position({
        my: "left top",
        at: "right+3 top",
        of: "#openMenuSectionDiv" + id
    });
}

function dialogYesNo(body, onYes, onNo) {
    $('<div></div>').appendTo('body')
        .html(body)
        .dialog({
            resizable: false,
            modal: true,
            title: "Подтверждение",
            height: 250,
            width: 400,
            buttons: {
                "Yes": function () {
                    $(this).dialog('close');
                    onYes();
                },
                "No": function () {
                    $(this).dialog('close');
                    onNo();
                }
            }
        });
}