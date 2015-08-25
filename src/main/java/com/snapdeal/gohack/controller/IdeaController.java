package com.snapdeal.gohack.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.snapdeal.gohack.model.CountInsight;
import com.snapdeal.gohack.model.Idea;
import com.snapdeal.gohack.model.Status;
import com.snapdeal.gohack.service.IdeaService;
import com.snapdeal.gohack.serviceImpl.Comment;



@RestController
public class IdeaController {


	@Autowired
	private IdeaService ideaService;


	@RequestMapping(value="/idea", method=RequestMethod.POST,headers = 
			"content-type=application/x-www-form-urlencoded;charset=UTF-8" ,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public ModelAndView submitIdea(@ModelAttribute Idea idea,HttpServletRequest request){
		String hostName=request.getHeader("Host");
		String ideaNumber=ideaService.doSubmit(idea,hostName);
		return new ModelAndView("redirect:/ideaDetail?idea="+ideaNumber);
	}

	@RequestMapping(value="/update", method=RequestMethod.POST,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public boolean updateIdea(@RequestBody Idea idea){
		boolean  updateStatus=ideaService.updateIdea(idea);
		return updateStatus;
	}


	@RequestMapping(value="/ideas" ,method=RequestMethod.GET)
	public @ResponseBody List<Idea> getListofIdeasOrFeatures(@RequestParam (value="iof",required=false) String ideaOrFeature)
	{
		return ideaService.getListOfIdeas(ideaOrFeature);
	}
	

	@RequestMapping(value="/ideas/trend" ,method=RequestMethod.GET)
	public @ResponseBody List<Idea> getListofTrendingIdeas()
	{
		return ideaService.getListOfTrendingIdeas();
	}


	@RequestMapping(value="/ideas/exportExcel" ,method=RequestMethod.GET)
	public @ResponseBody List<Idea> exportToExcel()
	{
		return ideaService.exportExcel();
	}

	@RequestMapping(value="/idea/{ideaNumber}" ,method=RequestMethod.GET)
	public @ResponseBody Idea getIdeaDetail(@PathVariable("ideaNumber") String ideaNumber)
	{
		return ideaService.getIdeaDetail(ideaNumber);
	}

	@RequestMapping(value="/ideastatus/upvote" ,method=RequestMethod.POST,produces={"application/json"})
	public @ResponseBody Status upVote(@RequestBody Idea idea)
	{
		return ideaService.upVote(idea);

	}

	@RequestMapping(value="/ideastatus/downvote" ,method=RequestMethod.POST)
	public @ResponseBody Status downVote(@RequestBody Idea idea)
	{
		return ideaService.downVote(idea);


	}

	@RequestMapping (value="idea/{ideaNumber}/email/{emailId}",method=RequestMethod.GET)
	public @ResponseBody int collabarateIdea(@PathVariable ("emailId") String email,
			@PathVariable ("ideaNumber") String ideaNumber){
		return ideaService.collabarateIdea(email,ideaNumber);
	}
	

	@RequestMapping (value="/insightcount",method=RequestMethod.GET)
	public @ResponseBody CountInsight getCount(){
		return ideaService.getCount();
	}

	
	@RequestMapping (value="/idea/{ideaNumber}/comment",method=RequestMethod.POST)
	public @ResponseBody boolean ideaComment(@PathVariable("ideaNumber") String ideaNumber,@RequestBody Comment comment){
		return ideaService.comment(ideaNumber,comment);
	}
}
