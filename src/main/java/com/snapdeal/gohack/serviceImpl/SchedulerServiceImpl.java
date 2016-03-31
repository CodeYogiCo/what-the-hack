/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 07-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.serviceImpl;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.snapdeal.gohack.service.ISchedulerService;


@Component
@Configuration
public class SchedulerServiceImpl implements ISchedulerService {

    private Scheduler            scheduler;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @PostConstruct
    private void startScheduler() throws SchedulerException {
        this.scheduler = schedulerFactory.getScheduler();
        schedulerFactory.start();
        Assert.isTrue(this.scheduler.isStarted());
    }

  

    public void schedule(String scheduledTime,String jobName) throws SchedulerException {

        try {

            JobDataMap jobData = new JobDataMap();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(
                    CronScheduleBuilder.cronSchedule(scheduledTime).inTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Calcutta")))).build();

            JobDetail job = JobBuilder.newJob(ScheduledSlackJob.class).withIdentity(jobName).setJobData(jobData).build();
            scheduler.scheduleJob(job, trigger);

        } catch (Exception e) {

        }

    }


}
