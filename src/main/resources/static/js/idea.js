 $(document).ready(function(){
	 $('[data-toggle="popover"]').popover({placement: "bottom"}); 
	 
	
	   $.urlParam = function(name, url) {
		    if (!url) {
		     url = window.location.href;
		    }
		    var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(url);
		    if (!results) { 
		        return undefined;
		    }
		    return results[1] || undefined;
		}
	   
	   var idea = $.urlParam('idea');
	   console.log("idea no : "+idea);
	
	   $.ajax({
	          url: "/idea/"+idea, 
	          async:false,
	          cache:false,
	          success: function(result){
	        	  	 if(result.collabarators.length >= 6)
	        	  	 {
	        	  		 $("#btnJoinIdea").val("looks like we are full").css({"background-color": "gray","color":"darkgray","pointer-events":"none"});
	        	  		 
	        	  	 }
	        	  	 
	                 console.log(result);
	                 $(".objective").text(result.objective);
	                 $(".sectionIdea").text(result.section);
	                 $(".description").html(result.description);
	                 
	                 var urls = result.url.split(",");
	                 console.log(urls);
	                 var htmlVal = "";
	                 $.each(urls,function(i,val){
	                	 htmlVal += "<a>"+val+"</a>";
	                	 htmlVal += "<br/>";
	                 });
                	 $(".url").html(htmlVal);
	                 
	                 $(".collabarators").html(result.collabarators.toString());
	                 var votes = result.ideaUpVote - result.ideaDownVote;
	                 $("#upvotes").text(result.ideaUpVote);
	                 $("#downvotes").text(result.ideaDownVote);
	                 
	                 $(".score").text(votes);
	                 $("#ideaStatus").text("Status :"+result.ideaStatus);
	                 $(".idea-title").text(result.ideaOverview);
	                 $(".idea-section").text(result.section);
	                 
	                 $(".idea-email").text(result.email);
	          }
	    });
      
       $("#voteup,#votedown").on("click",function(e){
    	     e.preventDefault();
    	  	 $(".form-group.voting-group").removeClass("hide");
    	  	 $("#upordown").val(e.currentTarget.id);
       });
	   
       $("#btnIdeaCancel").on("click",function(e){
    	   e.preventDefault();
    	   $(".form-group.voting-group").addClass("hide");
       });
       
       $("#btnJoinCancel").on("click",function(e){
    	   e.preventDefault();
    	   $(".form-group.join-group").addClass("hide");
       });
       
       $("#btnJoinIdea").on("click",function(e){
    	   e.preventDefault();
    	   $(".join-group").removeClass("hide");
       });
       
       $("#btnJoinSubmit").on("click",function(e){
    	   e.preventDefault();
    	   var email = $("#inputJoinEmail").val();
    	   $.ajax({
 	          url: "/idea/"+idea+"/email/"+email+"/", 
 	          async:false,
 	          cache:false,
 	          success: function(result){
 	        	  if(result == 1)
 	        		  $(".join-label").text("your collab has been recorded.");
 	        	  else if(result == 0)
 	        		 $(".join-label").text("buddy,you are already on!");
 	        	 else if(result == 2)
 	        		 $(".join-label").text("This team is already full, Please join some other team!");
 	          }
    	   });
    	 
    	   $(".join-group input,.join-label").text("").val("");
    	   
       });
       
       $(document).on("click","#editDescription",function(e){
    	   e.preventDefault();
    	   var data = $(".description").text();
    	   var input = $('<textarea />', { 'name': 'desc', 'id': 'desc', 'class': 'form-control','maxlength':100,'height':$(".description").height() });
    	   $(".description").replaceWith(input);
    	   $("#desc").text(data);
    	   input.focus();
       });
       
       $(document).on("blur","#desc",function(e){
    	   var p = $('<p/>',{'class':'description','text': $("#desc").val()});
    	   $("#desc").replaceWith(p);
    	   submitEditedIdea();
       });
       
       $(document).on("click","#editLinks",function(e){
    	   e.preventDefault();
    	   $('[data-toggle="popover"]').popover(); 
    	   var links = '';
    	   var len =  $('.url a').length;
    	   $('.url a').each(function(idx, item) {
    		   if(idx == len-1)
				   links += $(item).text();
    		   else
    			   links += $(item).text()+",";   
			});
    	  
    	   var textarea = $('<textarea />', { 'name': 'links', 'id': 'links', 'class': 'form-control', 'height':$(".url").height() });
    	   $(".url").replaceWith(textarea);
    	   $("#links").text(links);
    	   textarea.focus();
       });
       
       $(document).on("blur","#links",function(e){
    	   var urls = $("#links").val().split(",");
           console.log(urls);
           var htmlVal = "";
           $.each(urls,function(i,val){
          	 htmlVal += "<a>"+val+"</a>";
          	 htmlVal += "<br/>";
           });
           
    	   var p = $('<p/>',{'class':'url','html': htmlVal});
    	   $("#links").replaceWith(p);
    	   submitEditedIdea();
       });
       
	   $("#btnIdeaSubmit").on("click",function(e){
		   var email = $("#inputEmail").val();
		   if(validateEmail(email))
		  {
			   $("#inputEmail").removeClass("error").addClass("correct");
			   var upordown = $("#upordown").val();
			   if(upordown == "voteup"){
				   var upURL = "/ideastatus/"+idea+"/upvote"+"/email/"+email+"/";
					$.ajax({
				   		url:upURL,
				   		success:function(data){	
				   			if(data.Status){
					   			$(".form-group.voting-group").addClass("hide");
					   			var curVotes = parseInt($(".score").text());
					   			$(".score").text(++curVotes);
					   			
					   			var upvotes = parseInt($("#upvotes").text());
					   		    $("#upvotes").text(++upvotes);
			                
					   			$(".voting-label").text("Your vote has been recorded.")
				   			}
				   			else{
				   				$(".voting-label").text("Oops buddy ! You have already voted.")
				   			}
				   		}
				   	});
			   }
			   
			   else if(upordown == "votedown"){
				   var downURL =  "/ideastatus/"+idea+"/downvote"+"/email/"+email+"/";
				    console.log("downurl :"+downURL);
				    $.ajax({
		    			url:downURL,
				   		success:function(data){
				   			if(data.Status){
					   			$(".form-group.voting-group").addClass("hide");
					   			var curVotes = parseInt($(".score").text());
					   			$(".score").text(--curVotes);
					   			
					   			var downvotes =  parseInt($("#downvotes").text());
					   			$("#downvotes").text(--downvotes);
					   			
					   			$(".voting-label").text("Your vote has been recorded.")
				   			}
				   			else{
				   				$(".voting-label").text("Oops buddy ! You have already voted.")
				   			}
				   		}
				   	});
			   }
	     }
		 else{
			 	$("#inputEmail").addClass("error");
		  }
	   });
	   
	   
	   function validateEmail(email){
		    return email.match(/^\"?[\w-_\.]*\"?@snapdeal\.com$/);        
	   }
	   
	   function submitEditedIdea(){
		   var links = '';
		   var len = $('.url a').length;
		   $('.url a').each(function(idx, item) {
			   if(idx == len - 1)
				   links += $(item).text(); 
			   else
				   links += $(item).text()+",";  
			});
		   
		   var ideaObj = {ideaNumber:idea,description:$(".description").text(),url:links,section: $(".idea-section").text(),objective:$(".objective").text()};
		   
		   $.ajax({
   			url:"/update",
   			type:"POST",
   			cache:false,
   		    beforeSend: function(xhr){xhr.setRequestHeader('content-type', 'application/json');},
   			data:JSON.stringify(ideaObj),
		   	success:function(response){
		   		console.log(response);
		   	}
		   });
	   }
	   
	   
   });
