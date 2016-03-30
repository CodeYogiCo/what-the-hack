/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 25-Feb-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

public class ResponseMetaDataV1 {
    
    @Override
    public String toString() {
        return "ResponseMetaDataV1 [requestId=" + requestId + ", message=" + message + ", client=" + client + ", responseTime=" + responseTime + ", uri=" + uri + ", description="
                + description + "]";
    }
    private  String requestId;
    private  String message;
    private  String client;
    private  Long responseTime;
    private  String uri;
    private String description;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public  String getRequestId() {
        return requestId;
    }
    public void setRequestId( String requestId) {
        this.requestId = requestId;
    }
    public  String getMessage() {
        return message;
    }
    public void setMessage( String message) {
        this.message = message;
    }

    public  Long getResponseTime() {
        return responseTime;
    }
    public void setResponseTime( Long responseTime) {
        this.responseTime = responseTime;
    }
    public  String getUri() {
        return uri;
    }
    public void setUri( String uri) {
        this.uri = uri;
    }
    public  String getClient() {
        return client;
    }
    public void setClient( String client) {
        this.client = client;
    }

}
