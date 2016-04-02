/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 02-Apr-2016
 *  @author vishal
 */
package com.snapdeal.gohack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

@Configuration
@EnableSolrRepositories("com.snapdeal.gohack.repository")
public class Solrconfig {
    
    @Value("${solr.url:http://30.0.233.10:8983/solr/idea}")
    private String solrUrl;

    @Bean
    protected HttpSolrServerFactoryBean solrServerFactory() {

        HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

        factory.setUrl(solrUrl);

        return factory;
    }

    @Bean
    public SolrOperations solrTemplate() throws Exception {

        return new SolrTemplate(solrServerFactory().getObject());
    }
}
