package com.snapdeal.gohack.core;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;


@EnableAutoConfiguration
@ComponentScan("com.snapdeal")
@EnableAsync
public class Application {
	
	  private static final HttpTransport TRANSPORT = new NetHttpTransport();

	  /*
	   * Default JSON factory to use to deserialize JSON.
	   */
	  private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
	
	private static GoogleClientSecrets clientSecrets;

	  static {
	    try {
	      Reader reader = new FileReader("client_secrets.json.example");
	      clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
	    } catch (IOException e) {
	      throw new Error("No client_secrets.json found", e);
	    }
	  }

	  /*
	   * This is the Client ID that you generated in the API Console.
	   */
	  private static final String CLIENT_ID = clientSecrets.getWeb().getClientId();

	  /*
	   * This is the Client Secret that you generated in the API Console.
	   */
	  private static final String CLIENT_SECRET = clientSecrets.getWeb().getClientSecret();

	  /*
	   * Optionally replace this with your application's name.
	   */
	  private static final String APPLICATION_NAME = "Google+ Java Quickstart";


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);	
	}

}