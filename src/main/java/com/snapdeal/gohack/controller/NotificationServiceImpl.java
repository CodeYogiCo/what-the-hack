/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 30-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.snapdeal.gohack.model.NotificationDataDTO;
import com.snapdeal.gohack.model.NotificationResponseDTO;
import com.snapdeal.gohack.model.UserWebRegistration;
import com.snapdeal.gohack.model.UserWebRegistrationDTO;

@Component
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    JdbcTemplate                             jdbcTemplate;

    @Resource
    org.springframework.core.env.Environment environment;

    @Override
    public UserWebRegistrationDTO doUserWebRegsitration(UserWebRegistration userReg) throws ServiceException {
        UserWebRegistrationDTO userWebRegistrationDTO = new UserWebRegistrationDTO();
        try {
            jdbcTemplate.update(environment.getProperty("sql.userwebRegistration"),
                    new Object[] { userReg.getRegistrationId(), userReg.getBrowserType(), userReg.getBrowserVersion(), userReg.getUserAgentInfo() });
            userWebRegistrationDTO.setMessage("successfully registered");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return userWebRegistrationDTO;

    }

    @Override
    public NotificationResponseDTO push(NotificationDataDTO pushData) throws ServiceException {
        List<UserWebRegistration> listOfRegistrationId = fetchRegistrationId();
        System.out.println(listOfRegistrationId);
        NotificationResponseDTO notificationResponse = new NotificationResponseDTO();
        try {
            for (UserWebRegistration eachRegistrationId : listOfRegistrationId) {
                jdbcTemplate.update("insert into hack_push as hp (registration_id,tag_id,push_title,push_message,push_url) values(?,?,?,?,?) on duplicate key update hp.push_title=push_title,hp.push_message=push_message,hp.push_url=push_url",
                        new Object[] { eachRegistrationId.getRegistrationId(),UUID.randomUUID().toString(), pushData.getPushTitle(), pushData.getPushMessage(), pushData.getPushUrl() });
            }
            notificationResponse.setMessage("successfully pushed");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return notificationResponse;
    }

    private List<UserWebRegistration> fetchRegistrationId() throws ServiceException {
        List<UserWebRegistration> listOfUserRegistration = new ArrayList<>();
        try {
            listOfUserRegistration = jdbcTemplate.query("select * from idea_web_register", new Object[] {}, new BeanPropertyRowMapper(UserWebRegistration.class));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return listOfUserRegistration;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List<NotificationDataDTO> getNotificationDetails(String registrationId) throws ServiceException {
        List<NotificationDataDTO> notificationData = new ArrayList<>();
        try {
            notificationData = jdbcTemplate.query("select * from hack_push where registration_id =?", new Object[] { registrationId },
                    new BeanPropertyRowMapper(NotificationDataDTO.class));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return notificationData;
    }
}