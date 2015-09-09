package com.snapdeal.gohack.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.snapdeal.gohack.model.CountInsight;
import com.snapdeal.gohack.model.Idea;
import com.snapdeal.gohack.model.Status;
import com.snapdeal.gohack.serviceImpl.Comment;

public interface IdeaService {
	
	public String doSubmit(Idea idea, String hostName);

	public List<Idea> getListOfIdeas(String ideaOrFeature);

	public Idea getIdeaDetail(String ideaNumber);

	public Status upVote(Idea idea);
	
	public Status downVote(Idea idea);

	public List<Idea> exportExcel();

	public int collabarateIdea(String email, String ideaNumber);

	public boolean  updateIdea(Idea idea, String sessionEmail);

	public List<Idea> getListOfTrendingIdeas();

	public CountInsight getCount();
	
	public boolean comment(String ideaNumber,Comment comment);
	
	public boolean updateCollaborators(String ideaNumber,String listofCollaboratorsRemoved);
	
	public boolean love(String ipAddress);

	public int getLoveCount();

	public List<Idea> getIdeaByEmail(String email);

}
