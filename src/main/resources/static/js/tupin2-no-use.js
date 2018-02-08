$(function(){
    var page = 0;
    var url = "api/imgs";
    var loadend = $("#load-end");

    //first do
    getImgs();

    $("#next-page").click(function(){
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
                $("#next-page").hide();
            }

            page++;
            console.log(page);
            var content = data.content;

            if(content == null || content ==""){
                loadend.show();
                return false;
            }

            var eul = document.createElement("ul");

            var pdiv = document.getElementById("ri-grid");

            $.each(content,function (index, val) {

                // 如果ul没有子元素则初始化，否则清空ul再进行重构
                if ($( "#ri-grid ul:has(li)" ).length==0){
                    $("#ri-grid ul").empty();
                    eul.appendChild(creatConstruction(val));
                    //重构
                    fullHeight();
                    counter();
                    contentWayPoint();
                    counterWayPoint();
                    imgPopup();

                }
                else{
                    $("#ri-grid ul").empty();
                    eul.appendChild(creatConstruction(val));
                    //重构
                    fullHeight();
                    counter();
                    contentWayPoint();
                    counterWayPoint();
                    imgPopup();
                }
             });


            pdiv.appendChild(eul);

            console.log(eul.innerHTML);
        }

        function creatConstruction(val){
            var eli = document.createElement("li");

            var ea = document.createElement("a");
            ea.setAttribute("href","#");

            var eimg = document.createElement("img");
            eimg.setAttribute("src",val.img);

            var ediv = document.createElement("div");
            ediv.setAttribute("class","desc");

            var eh3 = document.createElement("h3");
            eh3.innerHTML = "Album<br>";

            var espan = document.createElement("span");
            espan.innerHTML = val.title;

            eh3.appendChild(espan);
            ediv.appendChild(eh3);
            ea.appendChild(eimg);
            ea.appendChild(ediv);
            eli.appendChild(ea);

            return eli;
        }
    }

    var fullHeight = function() {

        $('.js-fullheight').css('height', $(window).height());
        $(window).resize(function(){
            $('.js-fullheight').css('height', $(window).height());
        });

    };

    // Animations

    var contentWayPoint = function() {
        var i = 0;
        $('.animate-box').waypoint( function( direction ) {

            if( direction === 'down' && !$(this.element).hasClass('animated') ) {

                i++;

                $(this.element).addClass('item-animate');
                setTimeout(function(){

                    $('body .animate-box.item-animate').each(function(k){
                        var el = $(this);
                        setTimeout( function () {
                            var effect = el.data('animate-effect');
                            if ( effect === 'fadeIn') {
                                el.addClass('fadeIn animated');
                            } else {
                                el.addClass('fadeInUp animated');
                            }

                            el.removeClass('item-animate');
                        },  k * 200, 'easeInOutExpo' );
                    });

                }, 100);

            }

        } , { offset: '85%' } );
    };

    var counter = function() {
        $('.js-counter').countTo({
            formatter: function (value, options) {
                return value.toFixed(options.decimals);
            }
        });
    };

    var counterWayPoint = function() {
        if ($('#counter-animate').length > 0 ) {
            $('#counter-animate').waypoint( function( direction ) {

                if( direction === 'down' && !$(this.element).hasClass('animated') ) {
                    setTimeout( counter , 400);
                    $(this.element).addClass('animated');

                }
            } , { offset: '90%' } );
        }
    };


    var imgPopup = function() {


        $('body').on('click', '.img-popup', function(event){
            event.preventDefault();
            var src = $(this).attr('href');
            $.magnificPopup.open({
                items: {
                    src: src
                },
                type: 'image'
            });

        });

    };
});