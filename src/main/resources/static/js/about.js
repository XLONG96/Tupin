$(function(){
    /* 获取评论 */
    var url = "api/comment";
    var arow = document.getElementById("row");

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
                arow.appendChild(abox);
            });
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