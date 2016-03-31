/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 30-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.controller;

import java.util.List;

import com.snapdeal.gohack.model.NotificationDataDTO;
import com.snapdeal.gohack.model.NotificationResponseDTO;
import com.snapdeal.gohack.model.UserWebRegistration;
import com.snapdeal.gohack.model.UserWebRegistrationDTO;

public interface NotificationService {

    UserWebRegistrationDTO doUserWebRegsitration(UserWebRegistration userReg) throws ServiceException;

    NotificationResponseDTO push(NotificationDataDTO pushData) throws ServiceException;

    List<NotificationDataDTO> getNotificationDetails(String registrationId) throws ServiceException;

}
