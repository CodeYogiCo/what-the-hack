/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 22-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.serviceImpl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

public class ScheduledSlackJob implements Job {

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        SchedulerContext schedulerContext;
        try {
            schedulerContext = jobContext.getScheduler().getContext();
            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");

            SlackReporterServiceImpl SlackReporterService = applicationContext.getBean(SlackReporterServiceImpl.class);
            SlackReporterService.pushToChannel();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

}
