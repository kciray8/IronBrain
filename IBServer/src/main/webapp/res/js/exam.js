function Exam() {

}

//http://localhost:8080/add_remind?ticket=2
Exam.prototype.remindSection = function (id) {
    $.get('add_remind', {
            section: id
        },
        function (data) {
            alert("OK");
        });
};

Exam.prototype.remindTicket = function (id) {
    $.get('add_remind', {
            ticket: id
        },
        function (data) {
            alert("OK");
        });
};

Exam.prototype.remind = function () {
    var toRemind = [];

    reminds.forEach(function (element) {
        if (element.on) {
            toRemind.push(element.id);
        }
    });

    $.get('remind', {
            ids: toRemind.toString()
        },
        function (data) {
            if (data.res = Result.OK) {
                location.reload();
            }
        }
    );
};

Exam.prototype.nextTicket = function (id) {
    var remind = $("input:radio[name='toRemindRButton']:checked").val();
    var correct = true;

    if (remind == "rNow") {
        correct = false;
    }
    $.get('try_done', {
            id: id,
            correct: correct
        },
        function (data) {
            if (data.res = Result.OK) {
                location.reload();
            }
        }
    );
};

Exam.prototype.endExam = function () {
    var remind = $("input:radio[name='toRemindRButton']:checked").val();
    var correct = true;

    if (remind == "rNow") {
        correct = false;
    }
    $.get('try_done', {
            id: id,
            correct: correct
        },
        function (data) {
            if (data.res = Result.OK) {
                location.reload();
            }
        }
    );
};

Exam.prototype.configure = function () {
    $(document).keydown(function (event) {
        if (event.which == 13) {//Enter
            if (!exam.tempTicketOpen) {
                $("#answerButton").click();
            } else {
                $("input:radio[value='rLater']").click();
                $("#nextTicketButton").click();
            }
        }
        if (event.which == 16) {//Shift
            if (!exam.tempTicketOpen) {
                $("#answerButton").click();
            } else {
                $("#nextTicketButton").click();
            }
        }

        if(event.which == 49){
            $("input:radio[value='rNow']").click();
        }
        if(event.which == 50){
            $("input:radio[value='rLater']").click();
        }
    });
};

Exam.prototype.tempTicketOpen = false;

var exam = new Exam();
