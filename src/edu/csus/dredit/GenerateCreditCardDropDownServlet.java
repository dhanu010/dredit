package edu.csus.dredit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GenerateCreditCardDropDownServlet extends DrEditServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Drive service = getDriveService(getCredential(req, resp));
		List<String> files = retrieveAllFiles(service);
		System.out.println("List of credit cards = " + files.toString());
		
		JsonObject jsonObject = createReturnJSONObject(files);
		System.out.println("Json Object print zali ka " + jsonObject.toString());
		sendJson(resp, jsonObject);
	}

	private JsonObject createReturnJSONObject(List<String> listOfFiles) {
		JsonObject returnObject = new JsonObject();
		int i = 0;
		for (String filename: listOfFiles) {
			String shortFileName = filename.substring(filename.indexOf("_") + 1, filename.length());
			returnObject.addProperty("creditCardName" + i, shortFileName);
			i++;
		}		
		return returnObject;
	}
	private List<String> retrieveAllFiles(Drive service)
			throws IOException {
		List<String> result = new ArrayList<String>();
		Files.List request = service.files().list();
		
		do {
			try {

				System.out.println("in try block");
				FileList files = request.execute();
				List<File> listOfFiles = files.getItems();
				Iterator<File> iter = listOfFiles.iterator();
				while (iter.hasNext()) {
					File file = iter.next();

					if (file.getTitle() != null
							&& file.getTitle().contains("CreditCard_")) {
						result.add(file.getTitle());
						System.out.println("File title is " + file.getTitle());
					}
				}
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);		
		return result;
	}
}
