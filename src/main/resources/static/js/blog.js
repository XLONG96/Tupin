$(function(){

    //返回顶部
    $(window).scroll(function(){
        var sc=$(window).scrollTop();
        if(sc>500){
            $("#goTop").fadeIn();
        }else{
            $("#goTop").fadeOut();
        }
    });

    $("#goTop").hover(
        function(){
            $("#img").attr("src","images/topb.gif");
        },
        function(){
            $("#img").attr("src","images/topw.gif");
        }
    );

    $("#goTop").click(function(){
        $('body,html').animate({scrollTop:0},300);
    });

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
    var atitle = document.getElementById("tab-title");
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

            var ah1 = document.createElement("h1");
            ah1.setAttribute("class","text-center");
            ah1.innerHTML = val.title;

            atitle.innerHTML = val.title;

            var date = new Date(val.publicTime);
            var ap1 = document.createElement("p");
            ap1.setAttribute("class","text-center");
            ap1.innerHTML = "Post on "+date.toDateString()+" | In "+val.theme+" | Visitors "+val.visitNum;

            var abr = document.createElement("br");

            var ap2 = document.createElement("p");
            ap2.setAttribute("style","color:black");
            ap2.innerHTML = marked(val.mdContent);

            abox.appendChild(ah1);
            abox.appendChild(ap1);
            abox.appendChild(abr);
            abox.appendChild(ap2);
        }
    }
});
