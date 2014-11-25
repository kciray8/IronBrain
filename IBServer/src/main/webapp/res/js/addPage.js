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

function initAddPage() {
    $("#newSectionName").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#addSectionButton").click();
        }
    });
}

function saveTicket(id, async) {
    //async = async || true;
    $("#saveProgress").html("Сохранение...");

    /*
     $.ajax({
     async: async,
     type: 'GET',
     url: 'update_ticket?answers='+$('#answersDiv').html()+
     "&questions=" + $('#questionsDiv').html() + "&id="+id,
     success: function(data) {
     var d = new Date();
     $("#saveProgress").html("OK! " + d.getMinutes() + ":" + d.getSeconds());
     }
     });
     */

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