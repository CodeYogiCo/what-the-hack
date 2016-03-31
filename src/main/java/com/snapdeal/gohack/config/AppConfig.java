package com.snapdeal.gohack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {
	
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.setCorePoolSize(5);
		threadPool.setMaxPoolSize(10);
		threadPool.setWaitForTasksToCompleteOnShutdown(true);
		return threadPool;
	}
	
	@Bean
	public RestTemplate getRestTemplate(){
	    return new RestTemplate();
	}
}
