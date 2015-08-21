package com.snapdeal.gohack.core;

import java.util.Properties;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class EmailConfig {
	
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		//Set gmail email id
		mailSender.setUsername("wth@snapdeal.com");
		//Set gmail email password
		mailSender.setPassword("HOLman@124");
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "true");
		return mailSender;
	}
	
	
	@Bean(name="threadPoolTaskExecutor")
	public Executor getThreadPoolExecutor(){
		ThreadPoolTaskExecutor threadPoolExecutor= new ThreadPoolTaskExecutor();
		threadPoolExecutor.setMaxPoolSize(10);
		return new ThreadPoolTaskExecutor();
	}
}
