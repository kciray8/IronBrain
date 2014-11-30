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

    if(remind == "rNow"){
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

var exam = new Exam();