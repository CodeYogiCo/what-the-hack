package com.snapdeal.gohack.core;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;



@RestController
public class IdeaController {


	@Autowired
	private IdeaService ideaService;
	
	 private static final HttpTransport TRANSPORT = new NetHttpTransport();
	 
	 private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
	
	GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
    .setAudience(Arrays.asList("720978887997-5tvk3foplvbv42qpa652josapujtthjj.apps.googleusercontent.com"))
    .build();
	


	@RequestMapping(value="/tokensignin", method=RequestMethod.POST,headers = 
			"content-type=application/x-www-form-urlencoded;charset=UTF-8" ,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public String googleConnect(HttpServletRequest request, @RequestParam("idtoken") String tokenId){
		System.out.println(tokenId);
		GoogleIdToken idToken = null;
		Object takeme = null;
		try {
			idToken = verifier.verify(tokenId);
			request.getSession().setAttribute("gtoken", true);
			takeme = request.getSession().getAttribute("takeme");
			request.getSession().removeAttribute("takeme");
			System.out.println("----takeme removed from session----");
			return (takeme==null)?"/":takeme.toString();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "/login";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "/login";
		}

	}
	
	@RequestMapping(value="/tokensignout", method=RequestMethod.POST,headers = 
			"content-type=application/x-www-form-urlencoded;charset=UTF-8" ,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public String googleDisconnect(HttpServletRequest request){
		request.getSession().removeAttribute("gtoken");
		request.getSession().removeAttribute("takeme");
		return "/";
	}
	
	public  boolean isUserAuthorized(HttpServletRequest request){
		return (request.getSession().getAttribute("gtoken") == null)?false: true;
	}

	public ModelAndView authorizeUser(String takeme, String failureRedirect, HttpServletRequest request ){
		if(!isUserAuthorized(request)){
			request.getSession().setAttribute("takeme", takeme);
			return new ModelAndView("redirect:"+failureRedirect);
		}
		return null;
	}
	
	@RequestMapping(value="/idea", method=RequestMethod.POST,headers = 
			"content-type=application/x-www-form-urlencoded;charset=UTF-8" ,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public ModelAndView submitIdea(@ModelAttribute Idea idea,HttpServletRequest request){
		ModelAndView view = authorizeUser("/ideaPage", "/login", request);
		if(view != null){
			return view;
		}
		String hostName=request.getHeader("Host");
		String ideaNumber=ideaService.doSubmit(idea,hostName);

		return new ModelAndView("redirect:/ideaDetail?idea="+ideaNumber);
	}

	@RequestMapping(value="/update", method=RequestMethod.POST,
			produces={"application/json"},
			consumes={"text/xml","application/json"})
	public boolean updateIdea(@RequestBody Idea idea, HttpServletRequest request){
		if(!isUserAuthorized(request)){
			return false;
		}
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ideastatus/{ideaNumber}/upvote/email/{emailId}" ,method=RequestMethod.POST,produces={"application/json"})
	public @ResponseBody Status upVote(@PathVariable("ideaNumber") String ideaNumber,
			@PathVariable ("emailId") String emailId, HttpServletRequest request)
	{
		if(!isUserAuthorized(request)){
			Status status = new Status();
			status.setMessage("Redirect To Login");
			status.setStatus(false);
			request.getSession().setAttribute("takeme", "/idea/"+ideaNumber);
			return status;
		}
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
	@RequestMapping(value="/ideastatus/{ideaNumber}/downvote/email/{emailId}" ,method=RequestMethod.POST)
	public @ResponseBody Status downVote(@PathVariable("ideaNumber") String ideaNumber ,
			@PathVariable("emailId") String emailId, HttpServletRequest request)
	{
		if(!isUserAuthorized(request)){
			Status status = new Status();
			status.setMessage("Redirect To Login");
			status.setStatus(false);
			request.getSession().setAttribute("takeme", "/idea/"+ideaNumber);
			return status;
		}
		return ideaService.downVote(ideaNumber,emailId);


	}

	@RequestMapping (value="idea/{ideaNumber}/email/{emailId}",method=RequestMethod.POST)
	public @ResponseBody int collabarateIdea(@PathVariable ("emailId") String email,
			@PathVariable ("ideaNumber") String ideaNumber, HttpServletRequest request){
		if(!isUserAuthorized(request)){
			request.getSession().setAttribute("takeme", "/idea/"+ideaNumber);
			return 0;
		}
		return ideaService.collabarateIdea(email,ideaNumber);
	}

}
