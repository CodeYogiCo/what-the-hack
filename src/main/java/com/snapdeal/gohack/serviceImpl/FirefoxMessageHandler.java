/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 31-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.snapdeal.gohack.service.BrowserMessageHandlerService;
@Component
public class FirefoxMessageHandler implements BrowserMessageHandlerService {

    private final String URL = "https://updates.push.services.mozilla.com/push/v1/";

    @Autowired
    RestTemplate         restTemplate;

    @Override
    public void pushMessage(String registrationId) {
        ResponseEntity<String> response = null;
        try {
            String restURl = URL + registrationId;
            //TODO TTL field should be added
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json;charset=UTF-8");

            HttpEntity<?> request = new HttpEntity<Object>(headers);
            response = restTemplate.exchange(restURl, HttpMethod.POST, request, String.class);

            if (response.getBody() != null) {

            }
        } catch (Exception e) {

        }
    }
}
