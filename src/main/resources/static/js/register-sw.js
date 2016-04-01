(function() {
	
	WTH = window.WTH || {};
    
	WTH.pushAPI = function() {
        getChromeVersion = function () {
            var raw = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
            return raw ? parseInt(raw[2], 10) : !1;
        };

        function initialiseState( serviceWorkerRegistration ) {
            // Are Notifications supported in the service worker?  
            if (!('showNotification' in ServiceWorkerRegistration.prototype)) {  
                console.warn('Notifications aren\'t supported.');  
                WTH.pushAPI.canPushBeEnabled = !1;
                return !1;  
            }

            // Check the current Notification permission.  
            // If its denied, it's a permanent block until the user changes the permission  
            if (Notification.permission === 'denied') {  
                WTH.pushAPI.canPushBeEnabled = !1;
                WTH.pushAPI.isUserSubscriptionDenied = !0;
                console.warn('The user has blocked notifications.');  
                return !1;  
            }

            // Check if push messaging is supported  
            if (!('PushManager' in window)) {  
                WTH.pushAPI.canPushBeEnabled = !1;
                console.warn('Push messaging isn\'t supported.');  
                return !1;  
            }
            WTH.pushAPI.canPushBeEnabled = !0;
            //WTH.pushAPI.serviceWorkerRegistrationObj = serviceWorkerRegistration;
           
            return !0;
        }

        function isHttpSite() {
            return "https:" !== location.protocol && 0 !== location.host.indexOf("localhost") && 0 !== location.host.indexOf("127.0.0.1");
        }

        return {
            canPushBeEnabled: !1,
            isPushEnabled: !1,
            //serviceWorkerRegistrationObj: '',
            isChromeSupported : "chrome" in window && "serviceWorker" in navigator && getChromeVersion() >= 42,
            isFirefoxSupported : /Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent) && "serviceWorker" in navigator,
            isSafariSupported : "safari" in window && "pushNotification" in window.safari,
            serviceWorkerPath: "/service-worker.js",
            isUserSubscriptionDenied: !1,
            browserType: "",
            browserVersion: "",
            userAgentInfo : "",
            subscriptionId : "",
            
            registerUser: function(subscriptionId){
                //console.log("Registering user to server for subscriptionId : ",subscriptionId);
                
                var registerInfo = {
                    "browserType" : WTH.pushAPI.browserType,
                    "browserVersion" : WTH.pushAPI.browserVersion,
                    "userAgentInfo" : WTH.pushAPI.userAgentInfo,
                    "registrationId" : subscriptionId
                };

                try{
                    jQuery.ajax({
                        method: 'POST',
                        headers: {
                            'Content-type': 'application/json;charset=utf-8',
                        }, 
                        url: '/web/user/register',
                        data: JSON.stringify(registerInfo) ,
                        dataType: 'json',
                        success: function(data, textStatus, jqXHR){
                            //console.log("SUCCESS USER REGISTER");
                        },
                        error: function (jqXHR, textStatus, errorThrown)
                        {   
                            console.error("FAILED TO REGISTER USER FOR WEB NOTIFICATION: ",errorThrown);
                        }
                    });
                    
                }catch(err){
                    console.error("ERROR:",err);
                }
            },

            splitSubscriptionObject: function ( subscriptionDetails ) {
                if ( "error" !== subscriptionDetails && "denied" !== subscriptionDetails ) {
                    var endpoint = subscriptionDetails.endpoint;
                    id = endpoint.substr(endpoint.lastIndexOf("/")+1);
                    subscriptionDetails.subscriptionId = id;
                }   
                return subscriptionDetails;
            },
            
            registerServiceWorker: function() {
                //console.log("registerServiceWorker method called");
                var promise = jQuery.Deferred();
                if(isHttpSite()){
                    WTH.pushAPI.canPushBeEnabled = !1;
                    promise.reject("Cannot register as this is not an secure site.");
                    return promise;
                }
                if("serviceWorker" in navigator){
                    navigator.serviceWorker.register( this.serviceWorkerPath ).then ( 
                        function ( serviceWorkerRegistration ) {
                            if(initialiseState ( serviceWorkerRegistration )){
                                promise.resolve();    
                            }else{
                                promise.reject("Validation failed");
                            }
                        }
                    );
                }
                return promise;
            },
            
            subscribe: function() {
                var promise = jQuery.Deferred();
                
                return navigator.serviceWorker.ready.then(function( serviceWorkerRegistration ) {
                	
                	serviceWorkerRegistration.pushManager.subscribe({
                        userVisibleOnly: !0
                    }).then(function(subscription) {
                        WTH.pushAPI.isPushEnabled = !0; 
                        promise.resolve(subscription);
                        
                    }).catch(function(err) {
                        if ("denied" !== Notification.permission) {
                            promise.resolve("error");
                        }
                        else {
                            promise.resolve("denied");
                        }
                    });
                }), promise ;
            },
            
            unsubscribe: function() {
                var promise = jQuery.Deferred();
                return navigator.serviceWorker.ready.then(function( serviceWorkerRegistration ) {
                	
                	serviceWorkerRegistration.pushManager.getSubscription().then( function(subscription ) {
                        if ( !subscription ) {
                             WTH.pushAPI.isPushEnabled = !1; promise.resolve("error");
                        }
                        else {
                            subscription.unsubscribe().then( function() {
                                WTH.pushAPI.isPushEnabled = !1; promise.resolve(subscription);                                
                            } )["catch"]( function() {
                                promise.resolve("error");
                            });
                        }
                    } )["catch"]( function() {
                        promise.resolve("error");
                    } );
                }), promise ;    
            }
        };
    }();

    function detectBrowser() {
        //console.log("Detecting browser info");
        var ua= navigator.userAgent, tem, 
        M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
        if(/trident/i.test(M[1])){
            tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
            return 'IE '+(tem[1] || '');
        }
        if(M[1]=== 'Chrome'){
            tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
            if(tem!== null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
        }
        M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
        if((tem= ua.match(/version\/(\d+)/i))!== null) M.splice(1, 1, tem[1]);
        
        WTH.pushAPI.userAgentInfo = navigator.userAgent;
        WTH.pushAPI.browserType = M[0];
        WTH.pushAPI.browserVersion = M[1];
        return M.join(' ');
    }

    detectBrowser();

    if ( WTH.pushAPI.isChromeSupported || WTH.pushAPI.isFirefoxSupported) { 
        var registerPromise = WTH.pushAPI.registerServiceWorker();
        
        registerPromise.then ( function () {
            var subscribePromise = WTH.pushAPI.subscribe();
            subscribePromise.then( function ( newSubscription ) {
                console.log("Subscription obj:",newSubscription);
            	var response = WTH.pushAPI.splitSubscriptionObject( newSubscription );
                
                if ("error" !== response && "denied" !== response ) {
                    WTH.pushAPI.registerUser(response.subscriptionId);
                    WTH.pushAPI.subscriptionId = response.subscriptionId;
                    console.log( response.subscriptionId, {
                        status: "SUBSCRIBED",
                        message: "User has subscribed to push notifications."
                    });
                } 
                else if ( "error" === response ) {
                   console.log({
                        status: "CANCELLED",
                        message: "User has closed the notifications opt-in."
                    });
                }
                else { 
                    console.log({
                        status: "BLOCKED",
                        message: "User has blocked push notifications."
                    });
                }            
            });
        
        },function(error){
            console.error("Can not support notification : ",error);
        });
        

    } else {
        console.warn('Service Worker Unsupported');
    }



})();  