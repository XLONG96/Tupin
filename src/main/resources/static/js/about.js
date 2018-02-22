$(function(){

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

    /* 获取评论 */
    var url = "api/comment";
    var arow = document.getElementById("row");
    var commentNum = $("#commentNum");
    var cnum = 0;

    getComment();

    function getComment(){
        $.ajax({
            url:url,
            type:"GET",
            data:{
                blogId:0
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

            $.each(data, function (index, val) {

                cnum ++;

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

            commentNum.text(cnum);
        }
    }

    /* 上传评论 blogId为0 */
    var url = "api/comment";

    $("#submit").click(function(){
        var name = $("input[name=name]").val();
        var content = $("textarea[name=comment]").val();
        if(name == "" || content == ""){
            alert("昵称和评论不能为空！");
            return;
        }

        $.ajax({
            url:url,
            type:"POST",
            data:{
                name : name,
                blogId : 0,
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
});