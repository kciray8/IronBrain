<%@ tag pageEncoding="UTF-8" %>

<button onclick="document.execCommand('bold',false,null);"><b>Bold</b></button>
<button onclick="document.execCommand('italic',false,null);"><i>Italic</i></button>
<button onclick="document.execCommand('underline',false,null);">underline</button>
<button onclick="document.execCommand('insertImage',false,'http://upload.wikimedia.org/wikipedia/commons/8/8c/JPEG_example_JPG_RIP_025.jpg');">
    IMG
</button>
<button onclick="document.execCommand('removeFormat',false,null);">removeFormat</button>
<button onclick="document.execCommand('backColor',false,'9CEBFF');clearSelection();">backColor</button>
<button onclick="document.execCommand('createLink',false,'vk.com');">createLink</button>
<button onclick="document.execCommand('insertUnorderedList',false,null);">list</button>

<button onclick="pasteHtmlFromBuffer();">past code</button>