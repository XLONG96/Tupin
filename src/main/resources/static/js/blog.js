$(function(){
    //获取id参数
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); // 匹配目标参数
        var result = window.location.search.substr(1).match(reg); // 对querystring匹配目标参数
        if (result != null) {
            return decodeURIComponent(result[2]);
        } else {
            return null;
        }
    }

    var id = getQueryString("id");
    var url = "api/blog";
    var abox = document.getElementById("box");
    var rendererMD = new marked.Renderer();
    marked.setOptions({
        renderer: rendererMD,
        gfm: true,
        tables: true,
        breaks: false,
        pedantic: false,
        sanitize: false,
        smartLists: true,
        smartypants: false
    });//基本设置

    getBlog();

    function getBlog() {
        $.ajax({
            url: url,
            type: "GET",
            data: {"id": id},
            success: callback,
            error: function (xhr, info) {
                alert(xhr.status + " " + xhr.statusText + " " + info);
            }
        });

        function callback(data) {
            var val = data;

            var ah2 = document.createElement("h2");
            ah2.innerHTML = val.title;

            var ap1 = document.createElement("p");
            ap1.innerHTML = "Post on "+val.publicTime+" | In "+val.theme+" | Visitors "+val.visitNum;

            var ap2 = document.createElement("p");
            ap2.innerHTML = marked(val.mdContent);

            abox.appendChild(ah2);
            abox.appendChild(ap1);
            abox.appendChild(ap2);
        }
    }
});
