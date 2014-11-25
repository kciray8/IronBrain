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

function saveTicket(id) {
    $("#saveProgress").html("Сохранение...");

    $.get('update_ticket', {
            answers: $('#answersDiv').html(),
            questions: $('#questionsDiv').html(),
            id: id
        },
        function (data) {
            //handleResult(data);
            $("#saveProgress").html("OK!");
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