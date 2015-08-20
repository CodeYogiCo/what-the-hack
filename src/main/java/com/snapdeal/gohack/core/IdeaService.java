package com.snapdeal.gohack.core;

import java.util.List;

public interface IdeaService {
	
	public String doSubmit(Idea idea, String hostName);

	public List<Idea> getListOfIdeas();

	public Idea getIdeaDetail(String ideaNumber);

	public Status upVote(String ideaNumber,String email);
	
	public Status downVote(String ideaNumber,String emam);

	public List<Idea> exportExcel();

	public boolean collabarateIdea(String email, String ideaNumber);

}
