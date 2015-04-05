package edu.csus.dredit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GenerateReportByCreditCardServlet extends DrEditServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		    
		    System.out.println("-----------------0");
		    String creditCardName = req.getParameter("creditCardName");
		    System.out.println("-----------------Credit card name = " + creditCardName.toString());
		   
		    Drive service = getDriveService(getCredential(req, resp));
		    
		    //Generating Report DOC
		    File body = createFileBody("GenerateReportByCreditCard_" + creditCardName);
		    System.out.println("-----------------2");
		    java.io.File fileContent = new java.io.File("WEB-INF/Doc1.docx");
		    System.out.println("-----------------3");
		    FileContent mediaContent = new FileContent("application/msword", fileContent);
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

	protected File createFileBody(String creditCardNames) {
		File body = new File();
		body.setTitle(creditCardNames);
		body.setDescription("A Document that shows Report by the credit card");
		body.setMimeType("application/msword");
		return body;
	}

}

