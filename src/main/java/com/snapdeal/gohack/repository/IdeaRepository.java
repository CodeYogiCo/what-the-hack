/*
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 02-Apr-2016
 *  @author vishal
 */
package com.snapdeal.gohack.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.snapdeal.gohack.model.Idea;


public interface IdeaRepository extends SolrCrudRepository<Idea, String>{
    
    
    @Query("*:*")
    @Facet(fields = "email_id")
    List<Idea> findAllIdeas(Pageable page);
    
    @Query("email_id:*?0* OR idea_overview:*?0*")
    @Facet(fields = "email_id")
    List<Idea> findByTitleOrEmail(String searchTerm,Pageable page);

}
