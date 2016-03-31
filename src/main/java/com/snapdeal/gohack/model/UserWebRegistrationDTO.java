/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 28-Feb-2016
 *  @author vishal
 */
package com.snapdeal.gohack.model;

public class UserWebRegistrationDTO {
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserWebRegistrationDTO [message=" + message + "]";
    }

}
