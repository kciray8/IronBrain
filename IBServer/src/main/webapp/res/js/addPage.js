var Result = {
    OK: "OK",
    ERROR: "ERROR"
};

var updateTime;
var versionConflict = false;

var saveInProcess = false;

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

function saveTicket(id, sectionId, onSaveOk) {
    if (saveInProcess) {
        return;
    }
    saveInProcess = true;

    $("#saveProgress").html("Сохранение...");
    var ticketLabel = $('#ticketLabel').val();

    console.log("save...");

    $.post('update_ticket', {
            answers: $('#answersDiv').html(),
            questions: $('#questionsDiv').html(),
            id: id,
            label: ticketLabel,
            clientVersionDate: updateTime
        },
        function (data) {
            if (data.res == Result.OK) {
                console.log("OK");
                var d = new Date();
                $("#saveProgress").html("OK! " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
                $("#navSection" + sectionId).html(ticketLabel);
                updateTime = data.data;
                if (onSaveOk != null) {
                    onSaveOk();
                }
            } else {
                var subRes = data.subRes;
                if (subRes == "versionConflict") {
                    versionConflict = true;
                    location.href = ".";

                    console.log("versionConflict!!!");
                }
            }
            saveInProcess = false;
        }
    ).fail(
        function () {
            saveInProcess = false;
        });
}

function checkTicket(id) {
    if (saveInProcess) {
        return;
    }
    saveInProcess = true;

    console.log("checking....");
    $.get('check_ticket', {
            id: id,
            clientVersionDate: updateTime
        },
        function (data) {
            console.log("checking DONE");

            if (data.res != Result.OK) {
                var subRes = data.subRes;
                if (data.subRes == "versionConflict") {
                    versionConflict = true;
                    location.href = ".";

                    console.log("versionConflict check....");
                }
            }
        }).always(
        function () {
            saveInProcess = false;
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

function onAddNewTicketAfterSave(sectionId, ticketId, ticketSectionId) {
    saveTicket(ticketId, ticketSectionId, function () {
        onAddNewTicket(sectionId);
    });
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

    //setInterval(autoSave, 300);//for debug
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

function deleteSectionDialog(id) {
    dialogYesNo("Вы действительно хотите удалить раздел?", function () {
        onDeleteSection(id);
    });
    $('#menu').remove();
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
                location.href = "./add";
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

function editTicket(id) {
    window.open('edit_ticket?id=' + id);
}

function onSearch() {
    var query = $("#query").val();

    $.get('query', {
            query: query
        },
        function (result) {

            var resultHtml = "<table style='width: 100%' >";

            for (var i = 0; i < result.length; i++) {
                var ticket = result[i];

                resultHtml += "<tr><td colspan='2'>";
                resultHtml += ticket.path;
                resultHtml += "</td></tr>";

                resultHtml += "<tr><td colspan='2'>";
                resultHtml += "<button onclick='editTicket(" + ticket.id + ");'>Редактировать</button>";
                resultHtml += " <button onclick='exam.remindTicket(" + ticket.id + ");'>Вспомнить</button>";
                resultHtml += "</td></tr>";

                resultHtml += "<tr>";
                resultHtml += "<td valign='top' style='width: 50%'><div class='searchTextOutput'>" + ticket.questions + "</div></td>";
                resultHtml += "<td valign='top' style='width: 50%'><div class='searchTextOutput'>" + ticket.answers + "</div></td>";
                resultHtml += "</tr>";

                resultHtml += "<tr><td colspan='2'>";
                if (i != result.length - 1) {
                    resultHtml += "<hr>";
                }
                resultHtml += "</td></tr>";
            }

            resultHtml += "</table>";

            $("#searchResult").html(resultHtml);

            var words = query.split(" ");
            words.forEach(function (word) {
                $('#searchResult').highlight(word);
            })
        }
    );
}