$(document).ready(function() {
    
     $("#inputSection").change(function() {
         //e.preventDefault();
         var value = $(this).val();
         console.log("value : " + value);
         if (value == -1) {
             $("#inputSection").css({
                 "border": "solid 1px red"
             });
             $("#inputIdea").hide();
            // $("#inputFeature").hide();
         } else if (value == "idea") {
             $("#inputIdea").show();
           //  $("#inputFeature").hide();
         } else if (value == "feature") {
           //  $("#inputFeature").show();
             $("#inputIdea").hide();
         }
     });
     
     function paginateTable(){
    	  $(".easyPaginateNav").remove();
    	  
    	   var head='';
    	     head += '<thead><tr><th class=\"num\">#<\/th>';
    	     head += '                     <th>Topic\/Submitted By<\/th>';
    	     head += '                     <th>Objective<\/th><th>Section<\/th> <th>votes<\/th> <th>Status<\/th> <th>Submitted On<\/th><\/tr><\/thead>';
    	     
		 $('table.table').easyPaginate({
			    paginateElement: 'tbody.link',
			    elementsPerPage: 6,
			    effect: 'fade'
		 });
		 
		 $('table.table').prepend(head);
	 }
 
 
     $.ajax({
         url: "/ideas",
         cache: false,
         async: false,
         success: function(result) {
        	  
             $.each(result, function(i, val) {
                 i = i + 1;
                 var votes = val.ideaUpVote - val.ideaDownVote;
                 
                 var html = '<tbody class="link"><tr>';
                 html += '<td class="num">' + i + '</td>';
                 html += '<td colspan="6"><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.ideaOverview +
                     '</a></td></tr>';
                 html += '<tr><td></td><td><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.email +
                     '</a></td><td><a>' + val.objective + '</a></td>' +
                     '<td><a>' + val.section + '</a></td>' +
                     '<td class="num"><a>' + votes + '</a></td>' +
                     '<td class="num"><a>' + val.ideaStatus + '</a></td>' +
                     /*    '<td class="num"><a>0</a></td>'+ */
                     '<td><a>' + moment(val.submittedOn).format("MMMM Do YYYY, h:mm:ss a") + '</a></td></tr></tbody>';
                 // console.log(html);
                 $('table.table').append(html);
             
             });
             
         //    $(".easyPaginateNav").remove();
             paginateTable();
           
         }
     });

     $("#feature").on("click", function() {
         $("#feature").parent().addClass("active");
         $("#idea").parent().removeClass("active");

         $.ajax({
             url: "/ideas?iof=feature",
             cache: false,
             async: false,
             success: function(result) {
            	 $(".table-striped.table-bordered tbody,.table-striped.table-bordered thead").remove();
                 
                 $.each(result, function(i, val) {
                     i = i + 1;
                     var votes = val.ideaUpVote - val.ideaDownVote;
                     var html='';
               
                     html += '<tbody class="link"><tr>';
                     html += '<td class="num">' + i + '</td>';
                     html += '<td colspan="6"><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.ideaOverview +
                         '</a></td></tr>';
                     html += '<tr><td></td><td><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.email +
                         '</a></td><td><a>' + val.objective + '</a></td>' +
                         '<td><a>' + val.section + '</a></td>' +
                         '<td class="num"><a>' + votes + '</a></td>' +
                         '<td class="num"><a>' + val.ideaStatus + '</a></td>' +
                         /*    '<td class="num"><a>0</a></td>'+ */
                         '<td><a>' + moment(val.submittedOn).format("MMMM Do YYYY, h:mm:ss a") + '</a></td></tr></tbody>';
                     // console.log(html);
                     $('table.table').append(html);
                    
                 });
             //    $(".easyPaginateNav").remove();
                 paginateTable();
                 

             }
         });
     });

     $("#idea").on("click", function() {
         $("#idea").parent().addClass("active");
         $("#feature").parent().removeClass("active");

         $.ajax({
             url: "/ideas?iof=idea",
             cache: false,
             async: false,
             success: function(result) {
                 $(".table-striped.table-bordered tbody,.table-striped.table-bordered thead").remove();
     
                 $.each(result, function(i, val) {
                     i = i + 1;
                     var votes = val.ideaUpVote - val.ideaDownVote;
                     var html='';
                 
                     html += '<tbody class="link"><tr>';
                     html += '<td class="num">' + i + '</td>';
                     html += '<td colspan="6"><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.ideaOverview +
                         '</a></td></tr>';
                     html += '<tr><td></td><td><a href="/ideaDetail?idea=' + val.ideaNumber + '">' + val.email +
                         '</a></td><td><a>' + val.objective + '</a></td>' +
                         '<td><a>' + val.section + '</a></td>' +
                         '<td class="num"><a>' + votes + '</a></td>' +
                         '<td class="num"><a>' + val.ideaStatus + '</a></td>' +
                         /*    '<td class="num"><a>0</a></td>'+ */
                         '<td><a>' + moment(val.submittedOn).format("MMMM Do YYYY, h:mm:ss a") + '</a></td></tr></tbody>';
                     // console.log(html);
                     $('table.table').append(html);
                     
                 });
               
                 paginateTable();
             //    $('table.table').prepend(head);
             }
         });
     });

 });