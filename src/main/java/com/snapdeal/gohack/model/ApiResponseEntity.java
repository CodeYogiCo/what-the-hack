/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 20-Feb-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

public class ApiResponseEntity<T> {
    
    ResponseMetaDataV1 responseMetadata;
    
    T result;
    
    public ApiResponseEntity() {
        // TODO Auto-generated constructor stub
    }
    
  
    public ResponseMetaDataV1 getResponseMetadata() {
        return responseMetadata;
    }


    public void setResponseMetadata(ResponseMetaDataV1 responseMetadata) {
        this.responseMetadata = responseMetadata;
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "ApiResponseEntity [responseMetadata=" + responseMetadata + ", result=" + result + "]";
    }

    
    

}
