package com.rummble.api.examples.scribe;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.scribe.http.Request;
import org.scribe.http.Response;
import org.scribe.http.Request.Verb;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;

public class ScribeExample {

	
	 static Scribe createFrom(String properties){
		    try{
		      Properties props = new Properties();
		      FileInputStream fis = new FileInputStream("src/com/rummble/api/examples/scribe/" + properties);
		      props.load(fis);
		      return new Scribe(props);
		    }catch(IOException ioe){
		      throw new RuntimeException("Error while creating the Scribe instance", ioe);
		    }
		  }
	 
	 static void br() {
		    System.out.println();
		  }
	
	 
	 private static final String NETWORK_NAME = "Rummble";
	 private static final String PROPERTIES_FILE = "rummble.properties";
	 private static final String AUTHORIZE_URL = "http://www.rummble.com/oauth/authorize?oauth_token=";
	 private static final String PROTECTED_RESOURCE_URL = "http://api.rummble.com/?method=user.getLoggedInUser";

	  Scribe scribe;

	  public static void main(String[] args) {

	    Scribe scribe = createFrom(PROPERTIES_FILE);
	    Scanner in = new Scanner(System.in);

	    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
	    br();

	    // Obtain the Request Token
	    System.out.println("Fetching the Request Token...");
	    Token requestToken = scribe.getRequestToken();
	    System.out.println("Got the Request Token!");
	    br();

	    System.out.println("Now go and authorize Scribe here:");
	    System.out.println(AUTHORIZE_URL + requestToken.getToken());
	    System.out.println("And paste the verifier here");
	    System.out.print(">>");
	    String verifier = in.nextLine();
	    br();

	    // Trade the Request Token and Verfier for the Access Token
	    System.out.println("Trading the Request Token for an Access Token...");
	    Token accessToken = scribe.getAccessToken(requestToken, verifier);
	    System.out.println("Got the Access Token!");
	    System.out.println("(if your curious it looks like this: " + accessToken + " )");
	    br();

	    // Now let's go and ask for a protected resource!
	    System.out.println("Now we're going to access a protected resource...");
	    Request request = new Request(Verb.GET, PROTECTED_RESOURCE_URL);
	    scribe.signRequest(request, accessToken);
	    Response response = request.send();
	    System.out.println("Got it! Lets see what we found...");
	    br();
	    System.out.println(response.getBody());

	    br();
	    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	  }
	
}
