/*!
 * Start Bootstrap - Creative Bootstrap Theme (http://startbootstrap.com)
 * Code licensed under the Apache License v2.0.
 * For details, see http://www.apache.org/licenses/LICENSE-2.0.
 */

(function($) {
    "use strict"; // Start of use strict

    // jQuery for page scrolling feature - requires jQuery Easing plugin
    $('a.page-scroll').bind('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top - 50)
        }, 1250, 'easeInOutExpo');
        
     // get target div to scroll to
//        var target = $($anchor.attr('href'));
//        // if target is valid, scroll to
//        if(target && target.offset()){
//            $('html, body').stop().animate({
//                scrollTop: target.offset().top - 50
//            }, 1250,'easeInOutExpo');
//        }

        event.preventDefault();
    });


    // Highlight the top nav as scrolling occurs
    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 51
    })

    // Closes the Responsive Menu on Menu Item Click
    $('.navbar-collapse ul li a').click(function() {
        $('.navbar-toggle:visible').click();
    });

    // Fit Text Plugin for Main Header
    $("h1").fitText(
        1.2, {
            minFontSize: '35px',
            maxFontSize: '65px'
        }
    );

    // Offset for Main Navigation
    $('#mainNav').affix({
        offset: {
            top: 100
        }
    })

   

    // Initialize WOW.js Scrolling Animations
    new WOW().init();
    
    
    
//    function updateLove(){
//    $.ajax({
//		url : "/loves",
//		cache:false,
//		success:function(data){
//			 $("#btnLove div").text(data);
//		}
//	});
//    }
//   
//    if($(".heartbeat").length > 0)
//    	updateLove();
//    
//    $("#btnLove").on("click",function(){
//    	$.ajax({
//    		url : "/register/love",
//    		cache:false,
//    		success:function(){
//    			$("#btnLove").css({"color":"#EE0000"}).removeClass("fa-heart-o").addClass("fa-heart");
//    			$("#btnLove").css({"animation":"beat 0.5s infinite alternate"});
//    			 updateLove();
//    		}
//    	});
//    });
  
})(jQuery); // End of use strict


