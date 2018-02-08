$(function(){

    var page = 0;
    var url = "api/summary";
    var row = document.getElementById("row");
    var loadend = $("#load-end");

    // first do
    loadend.hide();
    getSummary();

    $("#load").click(function(){
        getSummary();
    });

    function getSummary(){
        $.ajax({
            url:url,
            type:"GET",
            data:{"page":page},
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
            var content = data.content;

            if(content == null || content ==""){
                loadend.show();
                $("#load").hide();
                return false;
            }

            $.each(content,function (index, val) {

                // 构造html元素
                var abox =  document.createElement("div");
                abox.setAttribute("class","col-md-8 col-md-offset-2");

                var ablog =  document.createElement("a");
                ablog.setAttribute("href","/blog?id="+val.id);
                ablog.innerHTML = val.title;

                var ah2 = document.createElement("h2");
                ah2.appendChild(ablog);

                var ap1 = document.createElement("p");
                ap1.innerHTML = "Post on "+val.publicTime+" | In "+val.theme+" | Visitors "+val.visitNum;

                var ap2 = document.createElement("p");
                ap2.innerHTML = val.summary;

                abox.appendChild(ah2);
                abox.appendChild(ap1);
                abox.appendChild(ap2);
                row.appendChild(abox);
            });
        }
    }
})