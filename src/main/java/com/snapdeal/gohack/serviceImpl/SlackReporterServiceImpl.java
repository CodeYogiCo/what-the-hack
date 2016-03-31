/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 21-Mar-2016
 *  @author vishal
 */
package com.snapdeal.gohack.serviceImpl;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.snapdeal.gohack.model.CountInsight;
import com.snapdeal.gohack.model.SlackMessageDTO;
import com.snapdeal.gohack.service.ISchedulerService;
import com.snapdeal.gohack.service.IdeaService;
import com.snapdeal.gohack.service.SlackReoprterService;

@Component
public class SlackReporterServiceImpl implements SlackReoprterService {
    
    @Autowired
    ISchedulerService schedulerService;
    
    
    private final static String scheduledTime="0 0 0/6 * * ?";

    @PostConstruct
    public void init() throws SchedulerException {
        schedulerService.schedule(scheduledTime, "Slack Daily job");

    }

    @Autowired
    private RestTemplate        restTemplate;

    @Autowired
    private IdeaService         ideaService;

    @Autowired
    Gson                        gson;

    private final static String WEBHOOK_URL = "https://hooks.slack.com/services/T02UFKJS8/B0WQ4KFSR/gbJiYPrMDF80DBfjIB90d";

    public CountInsight getData() {
        return ideaService.getCount();
    }

    @Override
    public void pushToChannel() {
        SlackMessageDTO slackMessageDTO = new SlackMessageDTO();
        CountInsight counts = getData();
        if (counts.getIdeaCount() < 50) {
            slackMessageDTO.setText("Dang! Looks like We're still kicking tires.\n "
                    + "Just " + counts.getIdeaCount() + " ideas \n"+"<http://wth.snapdeal.com/wth-numbers>");
        }
        
        if (counts.getIdeaCount() >50 && counts.getIdeaCount()<100) {
            slackMessageDTO.setText("Dang! Looks like We're still kicking tires.\n "
                    + "Just " + counts.getIdeaCount() + " ideas \n"+"<http://wth.snapdeal.com/wth-numbers>");
        }
        if (counts.getIdeaCount() > 100 && counts.getIdeaCount() <150) {
            slackMessageDTO.setText("Oh we just had a century.Wth-bot is loving it. \n " + counts.getIdeaCount() + " ideas.Lots more to come \n"+"<http://wth.snapdeal.com/wth-numbers>");
        }

        if (counts.getIdeaCount() > 150 && counts.getIdeaCount() <200) {
            slackMessageDTO.setText("We are doing good guys. \n" + counts.getIdeaCount() + " ideas.Bring it on \n"+"<http://wth.snapdeal.com/wth-numbers>");
        }

        if (counts.getIdeaCount() > 200) {
            slackMessageDTO.setText("Awesome .\n" + counts.getIdeaCount() + "  ideas.\n"+"<http://wth.snapdeal.com/wth-numbers>");
        }
        slackMessageDTO.setUsername("wth-bot");
        slackMessageDTO.setIcon(":ghost:");
        HttpEntity<String> request = new HttpEntity<String>(gson.toJson(slackMessageDTO));
        restTemplate.exchange(WEBHOOK_URL, HttpMethod.POST, request, String.class);

    }

}
