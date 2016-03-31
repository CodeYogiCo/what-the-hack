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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.snapdeal.gohack.service.BrowserMessageHandlerService;

@Component
public class ChromeMessageHandler implements BrowserMessageHandlerService {

    private final static String API_KEY = "AIzaSyACrcZjgI-Ohj8IHNH5PxbZ44x9iHRQISs";

    private final static String URL     = "https://android.googleapis.com/gcm/send";

    @Autowired
    RestTemplate                restTemplate;

    @Override
    public void pushMessage(String registrationId) {
        ResponseEntity<String> response = null;
        // TODO Auto-generated method stub  ResponseEntity<String> response = null;
        try {
            JsonObject payloadData = new JsonObject();
            JsonArray regIds = new JsonArray();
            regIds.add(new JsonPrimitive(registrationId));
            payloadData.add("registration_ids", regIds);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json;charset=UTF-8");
            headers.add("Authorization", "key=" + API_KEY);

            HttpEntity<String> request = new HttpEntity<String>(payloadData.toString(), headers);

            response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
            if (response.getBody() != null) {

            }
        }

        catch (Exception e) {

        }

    }
}
