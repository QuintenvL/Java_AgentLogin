package nu.educom.wiki.quinten_mi6.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import nu.educom.wiki.quinten_mi6.ValidationException;
import nu.educom.wiki.quinten_mi6.objects.Agent;

public class AppModel {
	public short inputNumber;
	public Agent dbAgent = null;
	public long remainingCoolOffMinutes = 0;
	public long remainingCoolOffSeconds = 0;
	public boolean saveAttempt = false;
	public List<LocalDateTime> latestAttempts;
	public String licenseMessage;
	public long newCoolOff;
	public JTextField numberField = new JTextField();
	public JPasswordField codeField = new JPasswordField();
		
	
	public void resetCoolOffVariables() {
		remainingCoolOffMinutes = 0;
		remainingCoolOffSeconds = 0;
	}
	/**
	 * Verify the input of the agentNumber.
	 * Throw an exception if the number is invalid.
	 * @throws ValidationException 
	 */
	public void verifyNumber() throws ValidationException {
		String inputNumberString = numberField.getText();
		inputNumberString = testString(inputNumberString);
		
		try {
			inputNumber = Short.parseShort(inputNumberString);
		}
		catch (NumberFormatException e) {
			throw new ValidationException("Invalid input for :\"" + inputNumberString + "\".");
		}
		if (inputNumberString.length() > 3 || inputNumber < 0 || inputNumber > 956) {
			throw new ValidationException("Invalid agent number for :\"" + inputNumberString + "\".");
		}
	}
	
	/**
	 * Compare the input code with the correct code.
	 * @throws ValidationException 
	 */
	public void verifyCode() throws ValidationException {
		char [] inputCode = codeField.getPassword();
		if (inputCode.length != dbAgent.code.length || !Arrays.equals(inputCode, dbAgent.code)) {
			saveAttempt = true;
			throw new ValidationException("Invalid code for :\"" + new String(inputCode) + "\".");
		}
	}
	/**
	 * Test a string.
	 * 
	 * @param String string the string that needs to be tested.
	 * @return the tested string.
	 */
	public String testString(String string) {
		return string.trim();
	}
	/**
	 * Reset the inputs fields.
	 * Also resets the saveAttempt value.
	 */
	public void resetFields() {
		numberField.setText("");
		codeField.setText("");
		saveAttempt = false;
	}
	/**
	 * Check if an agent has been found.
	 * 
	 * @throws ValidationException
	 */
	public void agentExist() throws ValidationException{
		if (dbAgent == null) {
			throw new ValidationException("Non existing agent.");
		}
	}
	
	/**
	 * Verify if the found agent is active.
	 * 
	 * @throws ValidationException
	 */
	public void verifyActive() throws ValidationException{
		if (!dbAgent.active) {
			saveAttempt = true;
			throw new ValidationException("Inactive agent.");
		}
	}
	
	/**
	 * Find the second difference between then and now
	 * 
	 * @param LocalDateTime then date and time. 
	 * @param LocalDateTime now date and time of current time.
	 * @return long seconds difference which can be negative.
	 */
	private long getSecondsDifference (LocalDateTime then, LocalDateTime now) {
		LocalDateTime currentTime = LocalDateTime.from(now);
		return currentTime.until(then, ChronoUnit.SECONDS);
	}
	
	/**
	 * Verify if the cool off time is still active.
	 * 
	 * @param LocalDateTime now the date and time of the current time. (Used to set a preferred time for testing)
	 * @throws ValidationException
	 */
	public void verifyCoolDown(LocalDateTime now) throws ValidationException {
		if (dbAgent.coolOff > 0) {
			LocalDateTime until = dbAgent.latestCoolOff;
			until = until.plusMinutes(dbAgent.coolOff);
			long secondsDifference = getSecondsDifference(until, now);
			if (secondsDifference > 0) {
				remainingCoolOffMinutes = secondsDifference/60;
				remainingCoolOffSeconds = secondsDifference - 60 * remainingCoolOffMinutes;
				throw new ValidationException("Cool off still active for "+ secondsDifference + " second(s).");
			}
		}
	}
	
	/**
	 * Calculate the new cool off time.
	 * If the cool off time is zero, set it to 1.
	 * Also sets the remainingCoolOffMinutes.
	 */
	public void calculateNewCoolOff() {
		if (dbAgent.coolOff == 0) {
			newCoolOff = (long) 1;
		}
		else {
			newCoolOff = dbAgent.coolOff * 2;
		}
		remainingCoolOffMinutes = newCoolOff;
	}
	
	/**
	 * Set the license message which differs on having a license or not.
	 */
	public void setLicenseMessage() {
		if (dbAgent.license) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String licenseExpire = "<b><font color=navy>" + dbAgent.expireDate.format(formatter) + "</font></b>";
			licenseMessage = "<html>You have a license to kill which expires on " + licenseExpire + "</html>";
		}
		else {
			licenseMessage = "You don't have a license to kill";
		}
	}
}
