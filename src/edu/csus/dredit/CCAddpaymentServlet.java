package edu.csus.dredit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
//import com.google.gdata.util.*;

import edu.csus.dredit.model.SpreadsheetHandler;

public class CCAddpaymentServlet extends DrEditServlet 
{
	private static String JSON_SPREADSHEET_NAME_LABEL = "spreadsheetName";
	private static String JSON_WORKSHEET_NAME_LABEL = "worksheetName";
	private static String JSON_MONTH_LABEL = "month";
	private static String JSON_YEAR_LABEL = "year";
	private static String JSON_AMOUNT_LABEL = "amount";
	private static String JSON_BALANCE_LABEL = "balance";
	private static String JSON_APR_LABEL = "apr";
	
  /**
   * Writes the data to the spreadsheet 
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException 
  {
	  System.out.println("Called the doPut method in CCAddpaymentServlet");
	  String spreadsheetName = req.getParameter(JSON_SPREADSHEET_NAME_LABEL);
	  String worksheetName = req.getParameter(JSON_WORKSHEET_NAME_LABEL);
	  String month = req.getParameter(JSON_MONTH_LABEL);
	  String year = req.getParameter(JSON_YEAR_LABEL);
	  String amount = req.getParameter(JSON_AMOUNT_LABEL);
	  String balance = req.getParameter(JSON_BALANCE_LABEL);
	  String apr = req.getParameter(JSON_APR_LABEL);
	  
	  System.out.println("Name:" + spreadsheetName + " ws:" + worksheetName + " date:" + month + " " + year + " amount:" +  
			  amount + " balance:" + balance + " apr: " + apr);
	  
	  SpreadsheetService service = new SpreadsheetService("CCDebtViewerService");
	  
	  
	  service.setOAuth2Credentials(getCredential(req, resp));
	  
	  //create a new object to handle the spreadsheet requests
	  System.out.println("CCAddpaymentServlet after OAuth call");
	  
	  
	  SpreadsheetHandler mySpreadsheet = new SpreadsheetHandler(service, spreadsheetName);
	  try
	  {
		  mySpreadsheet.addNewPaymentInfo(month + " " + year, amount, balance, apr);
		  System.out.println("CCAddpaymentServlet: Finished adding the payment info");
	  }
	  catch (ServiceException e)
	  {
		  System.out.println("CCAddpaymentServlet: There was an exception: " + e);
	  }
	   
  }
}