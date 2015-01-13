function Section(id, label) {
    var _id = id;
    var _label = label;

    this.getId = function () {
        return _id;
    };

    this.showRenameGui = function () {
        $("#renameSectionName").val(_label);

        $("#renameButton").hide();
        $("#renameSectionGUI").show();
    };

    this.hideRenameGui = function () {
        $("#renameSectionGUI").hide();
        $("#renameButton").show();
    };

    this.rename = function () {
        var newLabel = $("#renameSectionName").val();

        $.get("update_section",
            {
                id: _id,
                label: newLabel
            },
            function (data) {
                if (data.res = Result.OK) {
                    _label = newLabel;
                    location.reload();
                }
            }
        );
    };

    this.paste = function () {
        $.get("paste_section",
            {
                parent: _id
            },
            function (data) {
                if (data.res == Result.OK) {
                    location.reload();
                }
            }
        );
    }
}