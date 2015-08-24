package com.snapdeal.gohack.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;


@Component
@PropertySource("classpath:ideas.properties")
public class IdeaServiceImpl implements IdeaService{


	private final static String DEFAULT_IDEA_FEATURE="idea";
	@Resource
	private Environment environment;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;


	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public String doSubmit(final Idea idea,final String hostName) {
		final String ideaNumber=UUID.randomUUID().toString();
		jdbcTemplate.update("insert into user_ideas (ideaNumber,email,ideaOverview,section,objective,description,url,category)"
				+ "VALUES (?,?,?,?,?,?,?,?) ",ideaNumber,idea.getEmail(),idea.getIdeaOverview(),idea.getSection(),idea.getObjective(),
				idea.getDescription(),idea.getUrl(),idea.getCategory());
		jdbcTemplate.update("insert into idea_status(ideaNumber) VALUES (?)" ,ideaNumber);
		jdbcTemplate.update("insert into idea_vote (ideaNumber,user_email) "
				+ "values (?,?)",new Object[]{ideaNumber,idea.getEmail()} );
		jdbcTemplate.update("insert into idea_team(ideaNumber,ideaTeamEmailId) VALUES (?,?)" ,ideaNumber,idea.getEmail());
		new Thread(new Runnable() {

			@Override
			public void run() {
				shootIdeaSubmissionEmail(idea.getEmail().trim(),hostName,ideaNumber);

			}
		}).start();

		return ideaNumber;

	}


	@Override
	public List<Idea> getListOfIdeas(String ideaOrFeature) {

		List<Idea> listofIdeas= jdbcTemplate.query("SELECT  t1.*,t2.ideaStatus,t2.ideaUpVote,t2.ideaDownVote ,count(distinct t3.ideaTeamEmailId) As count FROM user_ideas AS t1 "+
				"INNER JOIN idea_status AS t2 ON t1.ideaNumber = t2.ideaNumber join idea_team as t3 on t1.ideaNumber = t3.ideaNumber "+
				" where section = ? group by 1 order by submittedOn desc",new Object[]{ideaOrFeature==null
				?DEFAULT_IDEA_FEATURE :ideaOrFeature},
				new BeanPropertyRowMapper<Idea>(Idea.class));
		return listofIdeas;
	}


	@Override
	public Idea getIdeaDetail(String ideaNumber) {
		List<Idea> ideas = jdbcTemplate.query("SELECT t1.objective,t1.ideaOverview,t1.section,t1.email,t1.description,t1.url,t2.ideaStatus,t2.ideaUpVote,t2.ideaDownVote,t3.ideaTeamEmailId"
				+" FROM user_ideas AS t1 INNER JOIN idea_status AS t2 ON t1.ideaNumber = t2.ideaNumber join idea_team as t3 on t1.ideaNumber = t3.ideaNumber"+
				" where t1.ideaNumber= ?",new Object[]{ideaNumber},
				new BeanPropertyRowMapper<Idea>(Idea.class));
		List<String> collabarators= new ArrayList<String>();
		for(Idea eachIdea: ideas){
			collabarators.add(eachIdea.getIdeaTeamEmailId());
		}
		Idea finalIdea= ideas.get(0);
		finalIdea.setCollabarators(collabarators);
		return finalIdea;
	}


	@Override
	public Status upVote(String ideaNumber,String email) {
		Status status= new Status();
		try{
			jdbcTemplate.update("insert into idea_vote (ideaNumber,user_email) "
					+ "values (?,?)",new Object[]{ideaNumber,email} );
			jdbcTemplate.update("UPDATE idea_status SET ideaUpVote=ideaUpVote+1 where ideaNumber =?",new Object[]{ideaNumber} );
		}
		catch(Exception e){
			status.setStatus(false);
		}
		return status;

	}


	@Override
	public Status downVote(String ideaNumber,String email) {
		Status status= new Status();
		try{
			jdbcTemplate.update("insert into idea_vote (ideaNumber,user_email) "
					+ "values (?,?)",new Object[]{ideaNumber,email} );
			jdbcTemplate.update("UPDATE idea_status SET ideaDownVote=ideaDownVote+1 where ideaNumber =?",new Object[]{ideaNumber} );
		}
		catch(Exception e){
			status.setStatus(false);
		}
		return status;
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
			// simply log it and go on...
			System.err.println(ex.getMessage());
		}
	}


	@Override
	public List<Idea> exportExcel() {
		List<Idea> listofIdeas= jdbcTemplate.query("SELECT *  FROM user_ideas AS t1 INNER JOIN idea_status "
				+ "AS t2 ON t1.ideaNumber = t2.ideaNumber ",
				new BeanPropertyRowMapper<Idea>(Idea.class));
		return listofIdeas;
	}


	@Override
	public int collabarateIdea(String email, String ideaNumber) {
		int status=1;
		try{
			int countCollaborators=jdbcTemplate.queryForObject("select count(*) from idea_team where ideaNUmber =?", 
					new Object[]{ideaNumber},Integer.class);
			if(countCollaborators<=6){
				jdbcTemplate.update("insert into idea_team (ideaNumber,ideaTeamEmailId) "
						+ "values (?,?)",new Object[]{ideaNumber,email} );
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


			jdbcTemplate.update("update user_ideas SET section=?,objective=?,description=?,url=? "
					+" where ideaNumber= ?",new Object[]{idea.getSection(),idea.getObjective(),idea.getDescription(),
							idea.getUrl(),idea.getIdeaNumber()});
		}
		catch(Exception e){
			updateStatus=false;
		}
		return updateStatus;
	}


	@Override
	public List<Idea> getListOfTrendingIdeas() {
		List<Idea> listOfIdeas= new ArrayList<Idea>();
		try{
			listOfIdeas=jdbcTemplate.query("SELECT  t1.*,t2.ideaStatus,t2.ideaUpVote,t2.ideaDownVote ,t2.ideaUpVote+t2.ideaDownVote as totalVote, "
					+ "count(distinct t3.ideaTeamEmailId) As count FROM user_ideas AS t1 "
					+ "INNER JOIN idea_status AS t2 ON t1.ideaNumber = t2.ideaNumber join idea_team as "+
					"t3 on t1.ideaNumber = t3.ideaNumber group by 1 order by totalVote desc",new BeanPropertyRowMapper<Idea>(Idea.class));
		}
		catch(Exception e){
		}
		return listOfIdeas;
	}


}
