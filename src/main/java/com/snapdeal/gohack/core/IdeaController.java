package com.snapdeal.gohack.core;

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

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ideastatus/{ideaNumber}/upvote/email/{emailId}" ,method=RequestMethod.GET,produces={"application/json"})
	public @ResponseBody Status upVote(@PathVariable("ideaNumber") String ideaNumber,
			@PathVariable ("emailId") String emailId)
	{
		return ideaService.upVote(ideaNumber,emailId);


		//		ResponseEntity<HttpStatus> responseEntity = null;
		//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//		if(!(authentication instanceof AnonymousAuthenticationToken)){
		//			if(authentication !=null && authentication.isAuthenticated()){
		//				ideaService.upVote(ideaNumber,emailId);
		//				responseEntity= new ResponseEntity(HttpStatus.OK);
		//			}
		//		}
		//		else{
		//			responseEntity= new ResponseEntity(HttpStatus.UNAUTHORIZED);
		//		}
		//		return responseEntity;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/ideastatus/{ideaNumber}/downvote/email/{emailId}" ,method=RequestMethod.GET)
	public @ResponseBody Status downVote(@PathVariable("ideaNumber") String ideaNumber ,
			@PathVariable("emailId") String emailId)
	{
		return ideaService.downVote(ideaNumber,emailId);


	}

	@RequestMapping (value="idea/{ideaNumber}/email/{emailId}",method=RequestMethod.GET)
	public @ResponseBody boolean collabarateIdea(@PathVariable ("emailId") String email,
			@PathVariable ("ideaNumber") String ideaNumber){
		return ideaService.collabarateIdea(email,ideaNumber);
	}

}
