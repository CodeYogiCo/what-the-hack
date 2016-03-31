/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 30-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.controller;

public class ServiceException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -5895833361870543962L;
    
    private String message;

    public ServiceException(String message) {
        this.message=message;
        // TODO Auto-generated constructor stub
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
