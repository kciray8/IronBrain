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

/*
Doc.insertHtml = function (html) {
    alert("111");
    var sel, range;
    if (window.getSelection) {
        // IE9 and non-IE
        sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            // Range.createContextualFragment() would be useful here but is
            // non-standard and not supported in all browsers (IE9, for one)
            var el = document.createElement("div");
            el.innerHTML = html;
            var frag = document.createDocumentFragment(), node, lastNode;
            while ( (node = el.firstChild) ) {
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
};*/

function HTML() {
}

HTML.linkAndImage = function (url, imgProperties) {
    return "<br><a target='_blank' href='" + url + "'><img " + imgProperties + " src='" + url + "'></a><br>";
};