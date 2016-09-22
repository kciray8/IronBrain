function Exam() {

}

//http://localhost:8080/add_remind?ticket=2
Exam.prototype.remindSection = function (id) {
    $.get('add_remind', {
            section: id
        },
        function (data) {
            addToTicketsExamCount(1);
        });
};

Exam.prototype.remindTicket = function (id) {
    $.get('add_remind', {
            ticket: id
        },
        function (data) {
            addToTicketsExamCount(1);
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
            correct: correct,
            remindTo: remind
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
            event.preventDefault();

            if (!exam.tempTicketOpen) {
                $("#answerButton").click();
            } else {
                //$("input:radio[value='rLater']").click();//!!!
                $("#nextTicketButton").click();
            }
        }
        if (event.which == 16) {//Shift
            event.preventDefault();
            if (!exam.tempTicketOpen) {
                $("#answerButton").click();
            } else {
                $("input:radio[value='rNow']").click();
                $("#nextTicketButton").click();
            }
        }

        if (event.which == 49) {
            $("input:radio[value='rNow']").click();
        }
        if (event.which == 50) {
            $("input:radio[value='rLater']").click();
        }
        if (event.which == 51) {
            $("input:radio[value='rDay']").click();
        }
        if (event.which == 52) {
            $("input:radio[value='rWeek']").click();
        }
        if (event.which == 53) {
            $("input:radio[value='rMonth']").click();
        }
        if (event.which == 54) {
            $("input:radio[value='rHalfYear']").click();
        }
        if (event.which == 55) {
            $("input:radio[value='rYear']").click();
        }
    });
};

Exam.prototype.openAnswers = function (open) {
    exam.tempTicketOpen = open;
    if (open) {
        $('#answers').show();
        $('#answerButton').hide();
    } else {
        $('#answers').hide();
        $('#answerButton').show();
    }
};

Exam.prototype.tempTicketOpen = false;

var exam = new Exam();
