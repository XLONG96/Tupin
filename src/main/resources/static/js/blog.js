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

    $("body").on('click','#del',function(){
        var isdel = confirm("确定要删除该评论？");
        if(isdel == true){
            var url = this.href;
            $.ajax({
                url:url,
                type:"DELETE",
                error:function(xhr,info){
                    alert(xhr.status+" "+xhr.statusText+" "+info);
                }
            });
            location.reload();
        }
        return false;
    });

    var isLogin;
    //is login?
    $.get("api/isLogin",function(data){
        isLogin = data;
    });

    /* 获取md */
    var id = getQueryString("id");
    var url = "api/blog";
    var blogId;
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

            if(val == ''){
                location.href="404.html";
            }

            blogId = val.id;

            var ah1 = document.createElement("h1");
            ah1.setAttribute("class","text-center");
            ah1.innerHTML = val.title;

            atitle.innerHTML = val.title;

            var date = new Date(val.publicTime);
            var ap1 = document.createElement("p");
            ap1.setAttribute("class","text-center");
            ap1.innerHTML = "Post on "+date.toDateString()+" | In "+val.theme+" | Visitors ";

            var aspan = document.createElement("span");
            aspan.setAttribute("id","visitor");
            aspan.innerHTML = val.visitNum;

            //webkit
            ap1.appendChild(aspan,ap1);
            //other
            //ap1.append(aspan);

            var abr = document.createElement("br");

            var ap2 = document.createElement("p");
            ap2.setAttribute("style","color:black");
            ap2.innerHTML = marked(val.mdContent);

            abox.appendChild(ah1);
            abox.appendChild(ap1);
            abox.appendChild(abr);
            abox.appendChild(ap2);

            var lastBlog = $("#lastBlog");//document.getElementById("lastBlog");
            if(val.lastId == ""){
                lastBlog.hide();
            }
            lastBlog.attr("href","blog?id="+val.lastId);
            //lastBlog.setAttribute("href","blog?id="+val.lastBlogId);
            lastBlog.append(val.lastBlogTitle);

            var nextBlog = $("#nextBlog");//document.getElementById("nextBlog");
            if(val.nextId == ""){
                nextBlog.hide();
            }
            nextBlog.attr("href","blog?id="+val.nextId);
            //nextBlog.setAttribute("href","blog?id="+val.nextBlogId);
            nextBlog.prepend(val.nextBlogTitle);


            getComment();
            updateVisitor();
        }
    }

    /* 获取评论 */
    var url2 = "api/comment";
    var arow = document.getElementById("row");
    var commentNum = $("#commentNum");
    var page = 0;
    var loadend = $("#load-end");

    loadend.hide();

    $("#load").click(function(){
        getComment();
    });

    function getComment(){
        $.ajax({
            url:url2,
            type:"GET",
            data:{
                "page":page,
                "blogId":blogId
            },
            success: callback,
            error: function (xhr, info) {
                alert(xhr.status + " " + xhr.statusText + " " + info);
            }
        });

        function callback(data) {

            if(data == 'undefine' || data == null){
                return ;
            }

            if(data.last == true){
                loadend.show();
                $("#load").hide();
            }

            page++;

            var content = data.content;

            if(content == null || content ==""){
                loadend.show();
                $("#load").hide();
                return false;
            }

            commentNum.text(data.totalElements);

            $.each(content, function (index, val) {

                var abox = document.createElement("div");
                abox.setAttribute("class","col-md-8 col-md-offset-2");

                var astrong = document.createElement("strong");
                astrong.innerHTML = val.name;

                var date = new Date(val.publicTime);
                var asmall = document.createElement("span");
                asmall.innerHTML = " Post on "+date.toDateString();

                var ap1 = document.createElement("p");
                ap1.appendChild(astrong);
                ap1.appendChild(asmall);

                var ap2 = document.createElement("p");
                ap2.setAttribute("style","color:black");
                ap2.innerHTML = val.content;

                abox.appendChild(ap1);
                abox.appendChild(ap2);

                if(isLogin == true){
                    var adel = document.createElement("a");
                    adel.setAttribute("id","del");
                    adel.setAttribute("class","col-md-offset-11");
                    adel.setAttribute("href","api/comment?id="+val.id);
                    adel.innerHTML = "删除";
                    abox.appendChild(adel);
                }

                abox.appendChild(document.createElement("hr"));
                arow.appendChild(abox);
            });

        }
    }

    /* 上传评论 */
    var url3 = "api/comment";

    $("#submit").click(function(){
        var name = $("input[name=name]").val();
        var content = $("textarea[name=comment]").val();
        if(name == "" || content == ""){
            alert("昵称和评论不能为空！");
            return;
        }

        $.ajax({
            url:url3,
            type:"POST",
            data:{
                name : name,
                blogId : blogId,
                content : content
            },
            success: callback,
            error: function (xhr, info) {
                alert(xhr.status + " " + xhr.statusText + " " + info);
            }
        });

        function callback(){
            alert("感谢你的评论！");
            location.reload();
        }
    });

    /* 更新访问量 */
    var url4 = "api/visitNum"

    function updateVisitor(){
        $.ajax({
            url:url4,
            type:"GET",
            data:{
                blogId:blogId
            },
            success:callback,
            error: function (xhr, info) {
                alert(xhr.status + " " + xhr.statusText + " " + info);
            }
        });

        function callback(data){
            aspan.innerHTML = data;
        }
    }
});
