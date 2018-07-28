$(function(){

    $("body").on('click','#del',function(){
        var isdel = confirm("确定要删除该文章？");
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


    var keyword;
    var page = 0;
    var url = "api/search";
    var row = document.getElementById("row");
    var loadend = $("#load-end");
    var isLogin;

    //is login?
    $.get("api/isLogin",function(data){
        isLogin = data;
    });

    // first do
    $("#load").hide();
    // loadend.hide();

    // 搜索按钮
    $("#sb").click(function(){
        keyword = $("#st").val();
        getSummary();
    });

    // 加载按钮
    $("#load").click(function(){
        keyword = $("#st").val();
        getSummary();
    });

    function getSummary(){
        $.ajax({
            url:url,
            type:"GET",
            data:{
                "page":page,
                "keyword":keyword
            },
            success:callback,
            error:function(xhr,info){
                alert(xhr.status+" "+xhr.statusText+" "+info);
            }
        });

        function callback(data){

            if(data.last == true){
                loadend.show();
                $("#load").hide();
            }

            page++;
            console.log(page);
            //var content = data.content;

            /*if(content == null || content ==""){
                loadend.show();
                $("#load").hide();
                return false;
            }
*/
            //content
            $.each(data, function (index, val) {

                // 构造html元素
                var abox =  document.createElement("div");
                abox.setAttribute("class","col-md-8 col-md-offset-2");

                var ablog =  document.createElement("a");
                ablog.setAttribute("style","text-decoration:none");
                ablog.setAttribute("href","/blog?id="+val.blogId);
                ablog.setAttribute("target","_blank");
                ablog.innerHTML = val.title;

                var ah2 = document.createElement("h2");
                ah2.appendChild(ablog);

                var ap1 = document.createElement("p");
                ap1.innerHTML = "Post on "+val.publicTime+" | In "+val.theme;

                var ap2 = document.createElement("p");
                ap2.innerHTML = val.blogSummary;


                abox.appendChild(ah2);
                abox.appendChild(ap1);
                abox.appendChild(ap2);

                if(isLogin == true){
                    var adel = document.createElement("a");
                    adel.setAttribute("id","del");
                    adel.setAttribute("class","col-md-offset-11");
                    adel.setAttribute("href","api/summary?id="+val.id);
                    adel.innerHTML = "删除";
                    abox.appendChild(adel);
                }

                abox.appendChild(document.createElement("br"));
                abox.appendChild(document.createElement("hr"));
                row.appendChild(abox);
            });
        }
    }
});