/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 07-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.service;

import org.quartz.SchedulerException;

public interface ISchedulerService {

    
    public void schedule(String scheduledtime, String jobname) throws SchedulerException ;

}
