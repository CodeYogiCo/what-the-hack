/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 25-Feb-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

import java.util.UUID;


public class ResponseMetaGeneratorV1 {
    

    public static ResponseMetaDataV1 getResponseMetadata(long responseTime, boolean success,String description, String uri, String clientId) {
        ResponseMetaDataV1 responseMetaData = new ResponseMetaDataV1();
        responseMetaData.setClient(clientId);
        responseMetaData.setMessage(success ? "success" : "failure");
        responseMetaData.setRequestId(UUID.randomUUID().toString());
        responseMetaData.setUri(uri);
        responseMetaData.setResponseTime(responseTime);
        responseMetaData.setDescription(description);
        return responseMetaData;
    }

    public static ResponseMetaDataV1 getResponseMetadata(long responseTime, boolean success, String description, String uri) {
        ResponseMetaDataV1 responseMetaData = new ResponseMetaDataV1();
        responseMetaData.setMessage(success ? "success" : "failure");
        responseMetaData.setRequestId(UUID.randomUUID().toString());
        responseMetaData.setUri(uri);
        responseMetaData.setResponseTime(responseTime);
        responseMetaData.setDescription(description);
        return responseMetaData;
    }


}
