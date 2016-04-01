
console.log('Starting...', self);

self.addEventListener('install', function(event) {
  self.skipWaiting();
  console.log('Installed', event);
});
self.addEventListener('activate', function(event) {
	self.skipWaiting();
	console.log('Activated', event);
});

self.addEventListener('notificationclick', function(event) {
    //console.log("Relative url:",event.notification.data);
    event.notification.close();
    var url = event.notification.data;
    event.waitUntil(
        clients.matchAll()
        .then(function(windowClients) {
            for (var i = 0; i < windowClients.length; i++) {
                var client = windowClients[i];
                if (client.url === url && 'focus' in client) {
                    return client.focus();
                }
            }
            if (clients.openWindow) {
                return clients.openWindow(url);
            }
        })
    );
});
self.addEventListener('push', function(event) {
  console.log('Push message', event);
  //Fetch undelived message
  try{
    var subscriptionId = "";
    event.waitUntil(
        self.registration.pushManager.getSubscription()
          .then(function(subscription) {
            var subscriptionId = getSubscriptionId(subscription);
            undeliveredNotificationURL =  "/notification/undelivered?subscriptionId=" + subscriptionId;
            
            fetch(
              "/web/user/undelivered/"+subscriptionId).then(function(response){
                console.log("Status:",response.status);
                if(response.ok){
                  var title, message, url, tagId; 
                  response.json().then(function(json) {
                  var notifications = json.result;
                  console.log(notifications)
                  for (i = 0; i < notifications.length; i++){
                  title = notifications[i].pushTitle;
                  message = notifications[i].pushMessage;
                  url = notifications[i].pushUrl;
                  tagId = notifications[i].tagId;
                  
                  self.registration.showNotification(title, {
                      body: message,
                      icon: 'http://i1.sdlcdn.com/img/storeFrontFeaturedProductAdmin/12/NewSnapdealLogo15045.png',
                      tag: tagId,
                      data: url
                    });
                  
                  }
              }).catch(function(err) {
                  console.log("Error:",JSON.stringify(err));
                  
                });
              
            }

          }).catch(function(err) {
            console.log("Error:",JSON.stringify(err));
          });


        })
    );  

  }catch(err) {
    console.log("Error:",JSON.stringify(err));
  }

});
 
 function getSubscriptionId(subscriptionObj){
  var endpoint = subscriptionObj.endpoint;
  var index = endpoint.lastIndexOf("/");
  return endpoint.substr(index+1);
}