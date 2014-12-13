var Result = {
    OK: "OK",
    ERROR: "ERROR"
};

var updateTime;
var versionConflict = false;

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

function saveTicket(id, sectionId) {
    $("#saveProgress").html("Сохранение...");
    var ticketLabel = $('#ticketLabel').val();

    $.get('update_ticket', {
            answers: $('#answersDiv').html(),
            questions: $('#questionsDiv').html(),
            id: id,
            label: ticketLabel,
            clientVersionDate: updateTime
        },
        function (data) {
            if (data.res == Result.OK) {
                var d = new Date();
                $("#saveProgress").html("OK! " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
                $("#navSection" + sectionId).html(ticketLabel);
                updateTime = data.data;
            } else {
                var subRes = data.subRes;
                if (data.subRes == "versionConflict") {
                    versionConflict = true;
                    location.href = ".";
                    //alert("conf")
                }
            }
        });
}

function checkTicket(id) {
    $.get('check_ticket', {
            id: id,
            clientVersionDate: updateTime
        },
        function (data) {
            if (data.res != Result.OK) {
                var subRes = data.subRes;
                if (data.subRes == "versionConflict") {
                    versionConflict = true;
                    location.href = ".";
                }
            }
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

function setSaveOnBlur(id, sectionId) {
    $(window).blur(function () {
        saveTicket(id, sectionId);
    });
    $(window).focus(function () {
        checkTicket(id);
    });

}

function setSaveOnUnload(id, sectionId) {
    window.onbeforeunload = function () {
        if (!versionConflict) {
            saveTicket(id, sectionId);
        }
    };
}

function setRegularSave(id, sectionId) {
    function autoSave() {
        saveTicket(id, sectionId);
    }

    //setInterval(autoSave, 1000);
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
        onDeleteSection(id);
    });
    $('#menu').remove();
}

function openSettings(id) {
    $('#menu').remove();
    $(document.documentElement).append($('<ul>').attr('id', "menu")
            .append($('<li>').append("Добавить в экзамен").attr("onClick", "exam.remindSection(" + id + ");$('#menu').remove();"))
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
            width: 300,
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

var timerEditId;
function initRichEditor(id, sectionId, ticketId) {
    /*
     $('#' + id).bind('keydown', function (event) {
     clearInterval(timerEditId);
     timerEditId = setInterval(function () {
     saveTicket(ticketId, sectionId);
     clearInterval(timerEditId);
     }, 2000);
     });*/
}

function enter() {
    $.get('login', {
            login: $("#login").val(),
            password: $("#password").val()
        },
        function (data) {
            if (data.res == Result.ERROR) {
                $("#login_error").html(data.message).show();
            }
            if (data.res == Result.OK) {
                location.reload();
            }
        });
}

function deleteRemind(id) {
    $.get('delete_remind', {
            id: id
        },
        function (data) {
            handleResult(data);
        }
    );
}


var reminds;

function changeRemindState(id) {
    reminds.forEach(function (element) {
        if (element.id == id) {
            element.on = $('#stateRemind' + id)[0].checked;
        }
    })
}

function onSearch() {
    $.get('query', {
            query: $("#query").val()
        },
        function (result) {
            var resultHtml = "<table>";

            result.forEach(function (ticket) {
                resultHtml += "<tr>";
                resultHtml += "<td valign='top' width='50%'><div class='searchTextOutput'>" + ticket.questions + "</div></td>";
                resultHtml += "<td valign='top' width='50%'><div class='searchTextOutput'>" + ticket.answers + "</div></td>";
                resultHtml += "</tr>";
            });
            resultHtml += "</table>";

            $("#searchResult").html(resultHtml);
        }
    );
}