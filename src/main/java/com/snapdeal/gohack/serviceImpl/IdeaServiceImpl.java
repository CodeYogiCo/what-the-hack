package com.snapdeal.gohack.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.snapdeal.gohack.model.CountInsight;
import com.snapdeal.gohack.model.Idea;
import com.snapdeal.gohack.model.Status;
import com.snapdeal.gohack.service.IdeaService;


@Component
@PropertySource({"classpath:ideas.properties","classpath:sql.properties"})

public class IdeaServiceImpl implements IdeaService{


	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private final static String DEFAULT_IDEA_FEATURE="idea";
	@Resource
	private Environment environment;

	@Autowired()
	@Qualifier("insert")
	private SimpleJdbcCall simpleJdbcCallForInsert;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired()
	@Qualifier("count")
	private SimpleJdbcCall simpleJdbcCallForCount;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	@Value("${app.teamsize:6}")
	private int maxTeamSize;


	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public String doSubmit(final Idea idea,final String hostName) {
		final String ideaNumber=UUID.randomUUID().toString();
		try{
			SqlParameterSource in= new MapSqlParameterSource()
			.addValue("ideaNumber",ideaNumber).
			addValue("email", idea.getEmail()).
			addValue("ideaOverview",idea.getIdeaOverview()).
			addValue("section",idea.getSection()).
			addValue("objective", idea.getObjective()).
			addValue("description", idea.getDescription()).
			addValue("url", idea.getUrl()).
			addValue("category", idea.getCategory());
			simpleJdbcCallForInsert.execute(in);
			threadPoolTaskExecutor.execute(new Thread(new Runnable() {
				@Override
				public void run() {
					shootIdeaSubmissionEmail(idea.getEmail().trim(),hostName,ideaNumber);
				}
			}));
		}
		catch(Exception exception){
		}
		return ideaNumber;
	}


	@Override
	public List<Idea> getListOfIdeas(String ideaOrFeature) {

		List<Idea> listofIdeas= jdbcTemplate.query(environment.getProperty("sql.listofideas"),new Object[]{ideaOrFeature==null
				?DEFAULT_IDEA_FEATURE :ideaOrFeature},
				new BeanPropertyRowMapper<Idea>(Idea.class));
		return listofIdeas;
	}


	@Override
	public Idea getIdeaDetail(String ideaNumber) {
		List<Idea> ideas = jdbcTemplate.query(environment.getProperty("sql.getideadetail"),
				new Object[]{ideaNumber},new BeanPropertyRowMapper<Idea>(Idea.class));
		List<String> collabarators= new ArrayList<String>();
		for(Idea eachIdea: ideas){
			collabarators.add(eachIdea.getIdeaTeamEmailId());
		}
		Idea finalIdea= ideas.get(0);
		finalIdea.setCollabarators(collabarators);
		List<Comment> comments= jdbcTemplate.query(environment.getProperty("sql.getComments"),new Object[]{ideaNumber},
				new BeanPropertyRowMapper(Comment.class));
		finalIdea.setComments(comments);
		return finalIdea;
	}

	@Override
	public Status upVote(Idea idea) {
		Status status= new Status();
		try{
			jdbcTemplate.update(environment.getProperty("sql.checkifuseralreadyvoted"),new Object[]{idea.getIdeaNumber(),
				idea.getEmail()} );
			jdbcTemplate.update(environment.getProperty("sql.upvote"),new Object[]{idea.getIdeaNumber()} );
		}
		catch(Exception e){
			status.setStatus(false);
		}
		return status;

	}


	@Override
	public Status downVote(Idea idea) {
		Status status= new Status();
		try{
			jdbcTemplate.update(environment.getProperty("sql.checkifuseralreadyvoted"),new Object[]{idea.getIdeaNumber(),idea.getEmail()
			} );
			jdbcTemplate.update(environment.getProperty("sql.downvote"),new Object[]{idea.getIdeaNumber()} );
		}
		catch(Exception e){
			status.setStatus(false);
		}
		return status;
	}

	@Override
	public List<Idea> exportExcel() {
		List<Idea> listofIdeas= jdbcTemplate.query(environment.getProperty("sql.exportexcel"),
				new BeanPropertyRowMapper<Idea>(Idea.class));
		return listofIdeas;
	}


	@Override
	public int collabarateIdea(String email, String ideaNumber) {
		int status=1;
		try{
			int currentTeamSize=jdbcTemplate.queryForObject(environment.getProperty("sql.countteamsize"),
					new Object[]{ideaNumber},Integer.class);
			if(currentTeamSize<maxTeamSize){
				jdbcTemplate.update(environment.getProperty("sql.jointeam"),new Object[]{ideaNumber,email} );
			}else{
				status=2;
			}
		}
		catch(Exception e){
			status=0;
		}
		return status;
	}


	@Override
	public boolean updateIdea(Idea idea) {
		boolean updateStatus=true;
		try{
			jdbcTemplate.update(environment.getProperty("sql.updateidea"),
					new Object[]{idea.getSection(),idea.getObjective(),idea.getDescription(),
				idea.getUrl(),idea.getIdeaNumber()});
		}
		catch(Exception e){
			updateStatus=false;
		}
		return updateStatus;
	}


	@Override
	public boolean updateCollaborators(String ideaNumber,String listofCollaboratorsRemoved) {
		boolean status=true;
		List<String> listofEmails =Arrays.asList(listofCollaboratorsRemoved.split(","));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("emails", listofEmails);
		params.put("ideaNumber", ideaNumber);
		String  queryToBeExceuted=environment.getProperty("sql.updateCollaborators");
        try{
			namedParameterJdbcTemplate.update(queryToBeExceuted,params);
			}
		catch(Exception e){
			status=false;
		}
		return status;
	}



	@Override
	public List<Idea> getListOfTrendingIdeas() {
		List<Idea> listOfIdeas= new ArrayList<Idea>();
		try{
			listOfIdeas=jdbcTemplate.query(environment.getProperty("sql.gettrendingideas"),
					new BeanPropertyRowMapper<Idea>(Idea.class));
		}
		catch(Exception e){
		}
		return listOfIdeas;
	}





	public void shootIdeaSubmissionEmail(final String email,final String hostName,final String ideaNumber){
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				int randomMessage = (int) (Math.random() * (9 - 1)) + 1;
				String ideaMessage=environment.getProperty("message"+"."+randomMessage,"message.4");

				String ideaPageLink="http://"+hostName+"/"+"ideaDetail"+"?idea="+ideaNumber;
				HashMap<String,String> templateValues= new HashMap<String, String>();
				templateValues.put("ideaPageLink", ideaPageLink);
				templateValues.put("message",ideaMessage);
				mimeMessage.setRecipient(Message.RecipientType.TO,
						new InternetAddress(email));
				mimeMessage.setFrom(new InternetAddress(email));
				mimeMessage.setSubject("Thank you for your submission");
				TemplateLoader loader = new ClassPathTemplateLoader();
				loader.setPrefix("/templates");
				loader.setSuffix(".html");
				Handlebars handlebars = new Handlebars(loader);

				Template template = handlebars.compile("submission");
				String text = template.apply(templateValues);
				mimeMessage.setText(text, "utf-8", "html");
			}
		};

		try {
			this.javaMailSenderImpl.send(preparator);
		}
		catch (MailException ex) {

		}
	}

	@Override
	public CountInsight getCount() {
		CountInsight counts= new CountInsight();
		try{
			Map<String, Object> result = simpleJdbcCallForCount.execute();
			counts.setIdeaCount((Integer)result.get("idea_count"));
			counts.setUpVoteCount((Integer) result.get("upvote_count"));
			counts.setDownVoteCount((Integer) result.get("downvote_count"));
			counts.setTotalVoteCount((Integer) result.get("totalvote_count"));
			counts.setFeatureCount((Integer) result.get("feature_count"));
			counts.setCommentCount((Integer) result.get("comment_count"));
			return counts;
		}
		catch(Exception e){
			System.out.println(e);
		}
		return counts;
	}

	@Override
	public boolean comment(String ideaNumber,Comment comment) {
		boolean status=true;
		try{
			jdbcTemplate.update(environment.getProperty("sql.comment"),new Object[]{ideaNumber,comment.getEmail(),comment.getComment()} );
		}
		catch(Exception e){
			status=false;
		}
		return status;
	}


}