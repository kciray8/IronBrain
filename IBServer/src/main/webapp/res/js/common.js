String.format = function () {
    var theString = arguments[0];

    for (var i = 1; i < arguments.length; i++) {
        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
        theString = theString.replace(regEx, arguments[i]);
    }

    return theString;
};

Image.load = function (file, onload) {
    var url = URL.createObjectURL(file);
    var img = new Image;

    img.onload = function () {
        onload(img);
    };

    img.src = url;
};

function Doc() {
}

Doc.insertHtml = function (html) {
    document.execCommand("insertHTML", false, html);
};


function HTML() {
}

HTML.linkAndImage = function (url, imgProperties) {
    return "<br><a target='_blank' href='" + url + "'><img " + imgProperties + " src='" + url + "'></a><br>";
};