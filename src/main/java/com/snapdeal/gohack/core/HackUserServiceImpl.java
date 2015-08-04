package com.snapdeal.gohack.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;


@Component
public class HackUserServiceImpl implements HackUserService{


	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;



	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;


	public String doUserRegistration(HackUser user) {
		String status;
		try{
			jdbcTemplate.update("insert into hack_user VALUES(?,?,?,?,?,?,?,?) ",
					user.getEmployeeId(),user.getName(),user.getEmail(),
					user.getSme(),user.getOtherExpertise(),user.getDesignation()
					,user.getTeam(),user.getPassword());
			status="USER REGISTERED SUCCESSFULLY";
		}
		catch(Exception exception){
			System.out.println(exception);
			status="FAILED IN USER REGISTRATION";
		}
		return status;
	}



	@Override
	public String forgotPassword(String emailId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("emailId",emailId );
		String response = namedJdbcTemplate.query("select password from hack_user where hack_email=:emailId",namedParameters,
				new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet resultset)
					throws SQLException, DataAccessException {
				List<String> password= new ArrayList<String>();
				while(resultset.next()){
					password.add(resultset.getString("password"));
				}
				return password.get(0);
			}
		});
		shootForgotPassEmail(emailId);
		return response;
	}


	public void shootForgotPassEmail(final String email){
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {

				mimeMessage.setRecipient(Message.RecipientType.TO,
						new InternetAddress("saloni.jain@snapdeal.com"));
				mimeMessage.setFrom(new InternetAddress("saloni.jain@snapdeal.com"));
				mimeMessage.setSubject("Snap hack password reset mail-no reply");
				mimeMessage.setText("Hola we are happy to help you");
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
	public AuthenticateResponse doUserAuthentication(HackUser user) {
		AuthenticateResponse authenticateResponse=new AuthenticateResponse();
		SqlParameterSource namedParameters = new MapSqlParameterSource("emailId",user.getEmail() );
		String response = namedJdbcTemplate.query("select password from hack_user where hack_email=:emailId",namedParameters,
				new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet resultset)
					throws SQLException, DataAccessException {
				List<String> password= new ArrayList<String>();
				while(resultset.next()){
					password.add(resultset.getString("password"));
				}
				return password.get(0);
			}
		});
		if(response.equals(user.getPassword())){
			authenticateResponse.setEmailId(user.getEmail());
			authenticateResponse.setName(user.getName());
			authenticateResponse.setAuthenticated(Boolean.TRUE);;
		}else{
			authenticateResponse.setEmailId(user.getEmail());
			authenticateResponse.setName(user.getName());
			authenticateResponse.setAuthenticated(Boolean.FALSE);;
		}
		return authenticateResponse;
	}


}
