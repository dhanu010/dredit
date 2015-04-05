package edu.csus.dredit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class InsertSpreadsheetServlet extends DrEditServlet {

   	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		    System.out.println("-----------------0");
		    String creditCardName = req.getParameter("creditCardName");
		    System.out.println("-----------------Credit card name = " + creditCardName.toString());
		   
		    System.out.println("-----------------1");
		    Drive service = getDriveService(getCredential(req, resp));

		    //Insert a file  
		    File body = createFileBody("CreditCard_" + creditCardName);
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

	protected File createFileBody(String creditCardName) {
		File body = new File();
		body.setTitle(creditCardName);
		body.setDescription("A Spreadsheet stores the credit card summary");
		//body.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		body.setMimeType("application/vnd.google-apps.spreadsheet");
		return body;
	}

	}


	  

