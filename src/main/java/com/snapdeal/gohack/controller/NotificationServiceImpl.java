/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 30-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snapdeal.gohack.model.UserWebRegistration;

public class NotificationServiceImpl implements NotificationService{
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Resource
    org.springframework.core.env.Environment environment;
    

    @Override
    public String doUserWebRegsitration(UserWebRegistration userReg) {
        String  status="true";
        try{
        jdbcTemplate.update(environment.getProperty("sql.userwebRegistration"),
                new Object[] {
                        userReg.getUserIdentity(),
                        userReg.getRegistrationId(),
                        userReg.getBrowserType(),
                        userReg.getBrowserVersion(),
                        userReg.getUserAgentInfo() });
        }
        catch(Exception e){
            status="false";
        }
        return status;
    }

}
