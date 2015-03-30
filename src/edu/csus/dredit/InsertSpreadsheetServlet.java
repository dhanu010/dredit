package edu.csus.dredit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class InsertSpreadsheetServlet extends DrEditServlet {

   	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  	    HttpTransport httpTransport = new NetHttpTransport();
		    JsonFactory jsonFactory = new JacksonFactory();
		    System.out.println("-----------------0");
		    String creditCardName = req.getParameter("creditCardName");
		    System.out.println("-----------------Credit card name = " + creditCardName.toString());
		   
		    System.out.println("-----------------1");
		    Drive service = getDriveService(getCredential(req, resp));

		    //Insert a file  
		    File body = new File();
		    body.setTitle(creditCardName);
		    body.setDescription("A Spreadsheet stores the credit card summary");
		    body.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		    System.out.println("-----------------2");
		    
		    java.io.File fileContent = new java.io.File("WEB-INF/Book1.xlsx");
		    System.out.println("-----------------3");
		    FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileContent);
		    System.out.println("-----------------4");
		    try
		    {
		    	File file = service.files().insert(body, mediaContent).execute();
		    	System.out.println("-----------------5");
		    	System.out.println("File ID: " + file.getId());
		    	sendJson(resp, "File is created");
		    	//return file;
	        } catch (IOException e) {
	        	System.out.println("An error occured: " + e);
	        	e.printStackTrace();
	        	// return null;
	        }
		}

	}


	  

