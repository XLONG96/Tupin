$(function(){

    var on = true;

    $("#music").click(function(){
        if(on == true){
            $(".audio-box").hide();
            on = false;
        }
        else{
            $(".audio-box").show();
            on = true;
        }

    });

    $("body").on('click','#del',function(){
        var isdel = confirm("确定要删除该图文？");
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


    //click to like
    $("body").on('click','#like',function(){
        alert("sd");
    });

    var page = 0;
    var url = "api/tupin";
    var fboard = document.querySelector('#fh5co-board');
    var loadend = $("#load-end");
    var isLogin;

    //is login?
    $.get("api/isLogin",function(data){
        isLogin = data;
    });

    // first do
    loadend.hide();
    getImgs();

    $("#load").click(function(){
        getImgs();
    });

    function getImgs(){
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
               abox.setAttribute("class","animate-box");

               var aimg =  document.createElement("a");
               aimg.setAttribute("href",val.img);
               aimg.setAttribute("class","image-popup fh5co-board-img");

               var img = document.createElement("img");
               img.setAttribute("src",val.img);
               aimg.appendChild(img);
               abox.appendChild(aimg);

               var dtitle = document.createElement("div");
               dtitle.setAttribute("class","fh5co-desc");
               dtitle.setAttribute("style","color:black");
               dtitle.innerHTML = val.title;

               console.log(val.title);

               var newNode = document.createElement("div");
               newNode.setAttribute("class","item");
               newNode.appendChild(abox);
               newNode.appendChild(dtitle);

               if(isLogin == true){
                   var adel = document.createElement("a");
                   adel.setAttribute("id","del");
                   adel.setAttribute("class","col-md-offset-10");
                   adel.setAttribute("href","api/tupin?id="+val.id);
                   adel.innerHTML = "删除";

                   var asmall = document.createElement("small");
                   asmall.appendChild(adel);
                   newNode.appendChild(asmall);
               }

               //动态增加瀑布流元素
               salvattore.appendElements(fboard,[newNode]);

               //对新元素再次应用插件
               magnifPopup();
               //若无再次应用此插件会造成图片不显示
               animateBoxWayPoint();
            });
        }
    }

    function magnifPopup(){
        $('.image-popup').magnificPopup({
            type: 'image',
            removalDelay: 300,
            mainClass: 'mfp-with-zoom',
            titleSrc: 'title',
            gallery:{
                enabled:false
            },
            zoom: {
                enabled: true, // By default it's false, so don't forget to enable it

                duration: 300, // duration of the effect, in milliseconds
                easing: 'ease-in-out', // CSS transition easing function

                // The "opener" function should return the element from which popup will be zoomed in
                // and to which popup will be scaled down
                // By defailt it looks for an image tag:
                opener: function(openerElement) {
                    // openerElement is the element on which popup was initialized, in this case its <a> tag
                    // you don't need to add "opener" option if this code matches your needs, it's defailt one.
                    return openerElement.is('img') ? openerElement : openerElement.find('img');
                }
            }
        });
    }

    function animateBoxWayPoint(){

        if ($('.animate-box').length > 0) {
            $('.animate-box').waypoint( function( direction ) {

                if( direction === 'down' && !$(this).hasClass('animated') ) {
                    $(this.element).addClass('bounceIn animated');
                }

            } , { offset: '75%' } );
        }

    }
});