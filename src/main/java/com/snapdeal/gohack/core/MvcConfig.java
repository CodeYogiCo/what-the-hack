package com.snapdeal.gohack.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/ideaPage").setViewName("ideaPage");
        registry.addViewController("/hacktube").setViewName("hacktube");
        registry.addViewController("/ideaDetail").setViewName("ideaDetail");
        registry.addViewController("/2048").setViewName("2048");
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/hackDetails").setViewName("hackDetails");
        registry.addViewController("/hackDetails").setViewName("hackDetails");
        registry.addViewController("/hackFeature").setViewName("hackFeature");
        
       }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// TODO Auto-generated method stub
    	registry.addResourceHandler("/resources/**").setCachePeriod(3600);
    }
    
    
    
    



}