$(function(){

    /*$(window).scroll(function () {
        var $body = $("body");
        console.log($(document).height()+" "+$(this).scrollTop()+" "+$("#fh5co-board").height());
        /!*判断窗体高度与竖向滚动位移大小相加 是否 超过内容页高度*!/
        if ($(document).height() - $(this).scrollTop() - $("#fh5co-board").height()<100) {
            console.log($(document).height()+" "+$(this).scrollTop()+" "+$("#fh5co-board").height());
            getImgs();
        }
    });*/

    var page = 0;
    var url = "api/imgs";
    var fboard = document.querySelector('#fh5co-board');
    var loadend = $("#load-end");

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
               dtitle.innerHTML = val.title;

               var newNode = document.createElement("div");
               newNode.setAttribute("class","item");
               newNode.appendChild(abox);
               newNode.appendChild(dtitle);

               salvattore.appendElements(fboard,[newNode]);

               magnifPopup();
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