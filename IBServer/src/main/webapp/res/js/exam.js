function Exam() {

}

//http://localhost:8080/add_remind?ticket=2
Exam.prototype.remindSection = function (id) {
    $.get('add_remind', {
            ticket: id
        },
        function (data) {
            alert("OK");
        });
};

var exam = new Exam();