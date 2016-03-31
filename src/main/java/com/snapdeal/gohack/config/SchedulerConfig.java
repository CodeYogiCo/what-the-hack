/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 31-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.snapdeal.gohack.controller.ServiceException;


@Configuration
public class SchedulerConfig {
    
     @Autowired
     Environment env;
     
     
    @Bean
    public Properties getScheduledPropperties() throws IOException, ServiceException {
        Properties property = new Properties();
        InputStream inputStrem = getClass().getClassLoader().getResourceAsStream("quartz.properties");
        property.load(inputStrem);
        property.put("org.quartz.jobStore.dataSource", "myDS");
        property.put("org.quartz.dataSource.myDS.driver", env.getProperty("mysql.driver"));
        property.put("org.quartz.dataSource.myDS.URL", env.getProperty("mysql.jdbcurl"));
        property.put("org.quartz.dataSource.myDS.user", env.getProperty("mysql.user"));
        property.put("org.quartz.dataSource.myDS.password", env.getProperty("mysql.password"));
        property.put("org.quartz.dataSource.myDS.validateOnCheckout", true);
        property.put("org.quartz.dataSource.myDS.validationQuery", "Select 1");
        return property;
    }

    @Bean
    public SchedulerFactoryBean getSchedulerFactory() throws IOException, ServiceException {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setQuartzProperties(getScheduledPropperties());
        factoryBean.setSchedulerName("SellerNotification_Scheduler");
        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return factoryBean;

    }
}
