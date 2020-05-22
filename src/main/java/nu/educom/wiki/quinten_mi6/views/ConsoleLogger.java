package nu.educom.wiki.quinten_mi6.views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger {
	/**
	 * Show the exception information in the console.
	 * 
	 * @param String exception The information about the exception.
	 */
	public void debugLog(String exception) {
		LocalDateTime myObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = myObj.format(myFormatObj);
		System.out.println(formattedDate + " --- " +exception);
	}
}
