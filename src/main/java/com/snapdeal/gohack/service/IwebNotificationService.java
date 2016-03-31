/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 31-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.service;

import java.util.List;

import com.snapdeal.gohack.model.UserWebRegistration;

public interface IwebNotificationService {
    
    public void userlookup(List<UserWebRegistration> listoFUserRegistration);

}
