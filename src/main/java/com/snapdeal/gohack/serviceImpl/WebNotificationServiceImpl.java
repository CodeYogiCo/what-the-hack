/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 31-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.snapdeal.gohack.model.UserWebRegistration;
import com.snapdeal.gohack.service.BrowserMessageHandlerService;
import com.snapdeal.gohack.service.IwebNotificationService;


@Component
public class WebNotificationServiceImpl implements IwebNotificationService {

    @Autowired
    private ChromeMessageHandler                      chromeMessageHandler;

    @Autowired
    private FirefoxMessageHandler                     firefoxMessageHandler;

    private Map<String, BrowserMessageHandlerService> browserMessageHandlers;

    @PostConstruct
    private void initialize() {
        browserMessageHandlers = new HashMap<String, BrowserMessageHandlerService>();
        browserMessageHandlers.put("chrome", chromeMessageHandler);
        browserMessageHandlers.put("firefox", firefoxMessageHandler);
    }

    @Override
    @Async("threadPool")
    public void userlookup(List<UserWebRegistration> listoFUserRegistration) {
        for (UserWebRegistration eachUserRegistration : listoFUserRegistration) {
            BrowserMessageHandlerService browserMessageService = browserMessageHandlers.get(eachUserRegistration.getBrowserType().toLowerCase());
            browserMessageService.pushMessage(eachUserRegistration.getRegistrationId());
        }
    }

}
