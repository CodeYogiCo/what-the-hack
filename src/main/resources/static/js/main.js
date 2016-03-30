var socket = null;
$(document).on("ready",function(){
	 
	  socket = io.connect('http://localhost:3000');
	 
	  // Socket events
	  // Whenever the server emits 'new message', update the chat body
	  socket.on('new message', function (data) {
		  console.log('got a new notification.');
		  console.log(data);
		  //push this notification to DB
		  var noti = '<li><a href="/ideaDetail?idea="'+data.idea+'">'+data.comment.email+' has commented '+data.comment.comment+'</a></li>';
		  $(".notification").append(noti); 
		  
		  // addChatMessage(data);
	  });

	  // Whenever the server emits 'user joined', log it in the chat body
	  socket.on('user joined', function (data) {
	    log(data.username + ' joined');
	    addParticipantsMessage(data);
	  });

	  // Whenever the server emits 'user left', log it in the chat body
	  socket.on('user left', function (data) {
	    log(data.username + ' left');
	    addParticipantsMessage(data);
	    removeChatTyping(data);
	  });

});